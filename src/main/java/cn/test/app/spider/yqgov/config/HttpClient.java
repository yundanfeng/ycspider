package cn.test.app.spider.yqgov.config;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLException;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpClient {
	
    private Integer maxTotal = 200;

    private Integer defaultMaxPerRoute = 100;

    private Integer connectTimeout = 2000;

    private Integer connectionRequestTimeout = 500;

    private Integer socketTimeout = 5000;
    
    private int retryTime = 2;  
    
    /**
     * 请求重试处理
     * @return
     */
    @Bean
    public HttpRequestRetryHandler httpRequestRetryHandler() {  
        // 请求重试  
        final int retryTime = this.retryTime;  
        return new HttpRequestRetryHandler() {  
            public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {  
                // Do not retry if over max retry count,如果重试次数超过了retryTime,则不再重试请求  
                if (executionCount >= retryTime) {  
                    return false;  
                }  
                // 服务端断掉客户端的连接异常  
                if (exception instanceof NoHttpResponseException) {  
                    return true;  
                }  
                // time out 超时重试  
                if (exception instanceof InterruptedIOException) {  
                    return true;  
                }  
                // Unknown host  
                if (exception instanceof UnknownHostException) {  
                    return false;  
                }  
                // Connection refused  
                if (exception instanceof ConnectTimeoutException) {  
                    return false;  
                }  
                // SSL handshake exception  
                if (exception instanceof SSLException) {  
                    return false;  
                }  
                HttpClientContext clientContext = HttpClientContext.adapt(context);  
                HttpRequest request = clientContext.getRequest();  
                if (!(request instanceof HttpEntityEnclosingRequest)) {  
                    return true;  
                }  
                return false;  
            }  
        };  
    }  
      
    /**
     * 首先实例化一个连接池管理器，设置最大连接数、并发连接数
     * @return
     */
    @Bean(name = "httpClientConnectionManager")
    public PoolingHttpClientConnectionManager getHttpClientConnectionManager(){
        PoolingHttpClientConnectionManager httpClientConnectionManager = new PoolingHttpClientConnectionManager();
        //最大连接数
        httpClientConnectionManager.setMaxTotal(maxTotal);
        //并发数
        httpClientConnectionManager.setDefaultMaxPerRoute(defaultMaxPerRoute);
        return httpClientConnectionManager;
    }

    /**
     * 实例化连接池，设置连接池管理器。
     * 这里需要以参数形式注入上面实例化的连接池管理器
     * @param httpClientConnectionManager
     * @return
     */
    @Bean(name = "httpClientBuilder")
    public HttpClientBuilder getHttpClientBuilder(@Qualifier("httpClientConnectionManager")PoolingHttpClientConnectionManager httpClientConnectionManager){

        //HttpClientBuilder中的构造方法被protected修饰，所以这里不能直接使用new来实例化一个HttpClientBuilder，可以使用HttpClientBuilder提供的静态方法create()来获取HttpClientBuilder对象
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();

        httpClientBuilder.setConnectionManager(httpClientConnectionManager).setConnectionManagerShared(true);
        // 设置重试handler
        httpClientBuilder.setRetryHandler(httpRequestRetryHandler());

        return httpClientBuilder;
    }

    /**
     * 注入连接池，用于获取httpClient
     * @param httpClientBuilder
     * @return
     */
    @Bean
    public CloseableHttpClient getCloseableHttpClient(@Qualifier("httpClientBuilder") HttpClientBuilder httpClientBuilder){
        return httpClientBuilder.build();
    }

    /**
     * Builder是RequestConfig的一个内部类
     * 通过RequestConfig的custom方法来获取到一个Builder对象
     * 设置builder的连接信息
     * 这里还可以设置proxy，cookieSpec等属性。有需要的话可以在此设置
     * @return
     */
    @Bean(name = "builder")
    public RequestConfig.Builder getBuilder(){
        RequestConfig.Builder builder = RequestConfig.custom();
        return builder.setConnectTimeout(connectTimeout)
                .setConnectionRequestTimeout(connectionRequestTimeout)
                .setSocketTimeout(socketTimeout);
    }

    /**
     * 使用builder构建一个RequestConfig对象
     * @param builder
     * @return
     */
    @Bean
    public RequestConfig getRequestConfig(@Qualifier("builder") RequestConfig.Builder builder){
        return builder.build();
    }
}