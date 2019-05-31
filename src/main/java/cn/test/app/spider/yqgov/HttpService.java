package cn.test.app.spider.yqgov;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.test.app.spider.yqgov.service.bean.HttpResult;

/**
 * @remark
 * @author luzh
 * @createTime 2017年7月3日 下午2:44:46
 */
@Component
public class HttpService {

	@Autowired
	private CloseableHttpClient httpClient;

	@Autowired
	private RequestConfig config;

	private static Logger logger = LoggerFactory.getLogger(HttpService.class);

	/**
	 * 不带参数的get请求，如果状态码为200，则返回body，如果不为200，则返回null
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public String doGet(String url) throws Exception {
		CloseableHttpResponse response = null;
		String result = null;
		try {
			// 声明 http get 请求
			HttpGet httpGet = new HttpGet(url);

			// 装载配置信息
			httpGet.setConfig(config);

			// 发起请求
			response = this.httpClient.execute(httpGet);

			// 判断状态码是否为200
			if (response.getStatusLine().getStatusCode() == 200) {
				// 返回响应体的内容
				result = EntityUtils.toString(response.getEntity(), "UTF-8");
			}
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (response != null) {
					EntityUtils.consume(response.getEntity());
				}
			} catch (IOException e) {
				logger.warn("close response fail", e);
			}
		}
		return result;
	}

	public int getStatus(String url) throws Exception {
		int statusCode = 0;
		CloseableHttpResponse httpResponse = null;
		try {
			// 声明 http get 请求
			HttpGet httpGet = new HttpGet(url);
			httpGet.setHeader("Connection", "close");

			// 装载配置信息
			httpGet.setConfig(config);

			// 发起请求
			httpResponse = this.httpClient.execute(httpGet);
			statusCode = httpResponse.getStatusLine().getStatusCode();
			return statusCode;
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (httpResponse != null) {
					EntityUtils.consume(httpResponse.getEntity());
					//httpResponse.close();
				}
			} catch (IOException e) {
				logger.warn("close response fail", e);
			}
		}
	}

	/**
	 * 带参数的get请求，如果状态码为200，则返回body，如果不为200，则返回null
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public String doGet(String url, Map<String, Object> map) throws Exception {
		URIBuilder uriBuilder = new URIBuilder(url);

		if (map != null) {
			// 遍历map,拼接请求参数
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				uriBuilder.setParameter(entry.getKey(), entry.getValue().toString());
			}
		}

		// 调用不带参数的get请求
		return this.doGet(uriBuilder.build().toString());

	}

	/**
	 * 带参数的post请求
	 * 
	 * @param url
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public HttpResult doPost(String url, Map<String, Object> map) throws Exception {
		CloseableHttpResponse response = null;
		try {
			// 声明httpPost请求
			HttpPost httpPost = new HttpPost(url);
			// 加入配置信息
			httpPost.setConfig(config);

			// 判断map是否为空，不为空则进行遍历，封装from表单对象
			if (map != null) {
				List<NameValuePair> list = new ArrayList<NameValuePair>();
				for (Map.Entry<String, Object> entry : map.entrySet()) {
					list.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
				}
				// 构造from表单对象
				UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(list, "UTF-8");

				// 把表单放到post里
				httpPost.setEntity(urlEncodedFormEntity);
			}
			// 发起请求
			response = this.httpClient.execute(httpPost);
			EntityUtils.consumeQuietly(response.getEntity());
			return new HttpResult(response.getStatusLine().getStatusCode(),
					EntityUtils.toString(response.getEntity(), "UTF-8"));
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (response != null) {
					EntityUtils.consume(response.getEntity());
				}
			} catch (IOException e) {
				logger.warn("close response fail", e);
			}
		}
	}

	/**
	 * 不带参数post请求
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public HttpResult doPost(String url) throws Exception {
		return this.doPost(url, null);
	}

	public HttpResult doPostJson(String url, String json) throws Exception {
		CloseableHttpResponse response = null;
		try {
			// 声明httpPost请求
			HttpPost httpPost = new HttpPost(url);
			// 加入配置信息
			httpPost.setConfig(config);

			// 给httpPost设置JSON格式的参数
			StringEntity requestEntity = new StringEntity(json, "utf-8");
			requestEntity.setContentEncoding("UTF-8");
			httpPost.setHeader("Content-type", "application/json");
			httpPost.setEntity(requestEntity);

			// 发起请求
			response = this.httpClient.execute(httpPost);
			return new HttpResult(response.getStatusLine().getStatusCode(),
					EntityUtils.toString(response.getEntity(), "UTF-8"));
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (response != null) {
					EntityUtils.consume(response.getEntity());
				}
			} catch (IOException e) {
				logger.warn("close response fail", e);
			}
		}
	}

	/**
	 * 下载图片请求
	 * 
	 * @remark
	 * @author luzh
	 * @reateTime 2017年7月3日 下午3:30:29
	 * @param @param
	 *            url
	 * @param @return
	 * @param @throws
	 *            Exception
	 * @return: byte[]
	 */
	public byte[] downloadImage(String url) throws Exception {
		CloseableHttpResponse response = null;
		try {
			HttpGet httpget = new HttpGet(url);
			httpget.setConfig(config);

			response = this.httpClient.execute(httpget);

			// 判断状态码是否为200
			if (response.getStatusLine().getStatusCode() == 200) {
				// 返回响应体的内容
				return EntityUtils.toByteArray(response.getEntity());
			}
			return null;
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (response != null) {
					EntityUtils.consume(response.getEntity());
				}
			} catch (IOException e) {
				logger.warn("close response fail", e);
			}
		}
	}
}