package cn.test.app.spider.yqgov.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.test.app.spider.yqgov.global.Globals;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.HttpClientGenerator;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.utils.HttpConstant;

/**
 * @remark
 * @author luzh
 * @createTime 2017年5月2日 下午10:03:23
 */
public class HttpClientDownloader {
	
	private static Logger logger = LoggerFactory.getLogger(HttpClientDownloader.class);

	private final static Map<String, CloseableHttpClient> httpClients = new HashMap<String, CloseableHttpClient>();

	private static HttpClientGenerator httpClientGenerator = new HttpClientGenerator();

	public static int getHttpUriStatus(String url) throws Exception{
		Task task = Site.me()
				.setCharset("utf-8")
				.addHeader("User-Agent", Globals.USERAGENT)
				.toTask();
		Request request = new Request(url);
		Site site = task.getSite();
        Map<String, String> headers = site.getHeaders();
		CloseableHttpResponse httpResponse = null;
		int statusCode = 0;
		try {
			HttpHost proxyHost = null;
			Proxy proxy = null; 
			if (site.getHttpProxyPool() != null && site.getHttpProxyPool().isEnable()) {
				proxy = site.getHttpProxyFromPool();
				proxyHost = proxy.getHttpHost();
			} else if (site.getHttpProxy() != null) {
				proxyHost = site.getHttpProxy();
			}

			HttpUriRequest httpUriRequest = getHttpUriRequest(request, site, headers, proxyHost);
			httpResponse = getHttpClient(site, proxy).execute(httpUriRequest);
			statusCode = httpResponse.getStatusLine().getStatusCode();
			return statusCode;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			request.putExtra(Request.STATUS_CODE, statusCode);
            if (site.getHttpProxyPool()!=null && site.getHttpProxyPool().isEnable()) {
                site.returnHttpProxyToPool((HttpHost) request.getExtra(Request.PROXY), (Integer) request
                        .getExtra(Request.STATUS_CODE));
            }
            try {
                if (httpResponse != null) {
                    EntityUtils.consume(httpResponse.getEntity());
                }
            } catch (IOException e) {
                logger.warn("close response fail", e);
            }
		}
	}

	protected static HttpUriRequest getHttpUriRequest(Request request, Site site, Map<String, String> headers,
			HttpHost proxy) {
		RequestBuilder requestBuilder = selectRequestMethod(request).setUri(request.getUrl());
		if (headers != null) {
			for (Map.Entry<String, String> headerEntry : headers.entrySet()) {
				requestBuilder.addHeader(headerEntry.getKey(), headerEntry.getValue());
			}
		}
		RequestConfig.Builder requestConfigBuilder = RequestConfig.custom()
				.setConnectionRequestTimeout(site.getTimeOut())
				.setSocketTimeout(site.getTimeOut())
				.setConnectTimeout(site.getTimeOut())
				.setCookieSpec(CookieSpecs.DEFAULT);
		if (proxy != null) {
			requestConfigBuilder.setProxy(proxy);
			request.putExtra(Request.PROXY, proxy);
		}
		requestBuilder.setConfig(requestConfigBuilder.build());
		return requestBuilder.build();
	}

	protected static RequestBuilder selectRequestMethod(Request request) {
		String method = request.getMethod();
		if (method == null || method.equalsIgnoreCase(HttpConstant.Method.GET)) {
			// default get
			return RequestBuilder.get();
		} else if (method.equalsIgnoreCase(HttpConstant.Method.POST)) {
			RequestBuilder requestBuilder = RequestBuilder.post();
			NameValuePair[] nameValuePair = (NameValuePair[]) request.getExtra("nameValuePair");
			if (nameValuePair != null && nameValuePair.length > 0) {
				requestBuilder.addParameters(nameValuePair);
			}
			return requestBuilder;
		} else if (method.equalsIgnoreCase(HttpConstant.Method.HEAD)) {
			return RequestBuilder.head();
		} else if (method.equalsIgnoreCase(HttpConstant.Method.PUT)) {
			return RequestBuilder.put();
		} else if (method.equalsIgnoreCase(HttpConstant.Method.DELETE)) {
			return RequestBuilder.delete();
		} else if (method.equalsIgnoreCase(HttpConstant.Method.TRACE)) {
			return RequestBuilder.trace();
		}
		throw new IllegalArgumentException("Illegal HTTP Method " + method);
	}

	private static CloseableHttpClient getHttpClient(Site site, Proxy proxy) {
		if (site == null) {
			return httpClientGenerator.getClient(null, proxy);
		}
		String domain = site.getDomain();
		CloseableHttpClient httpClient = httpClients.get(domain);
		if (httpClient == null) {
			httpClient = httpClients.get(domain);
			if (httpClient == null) {
				httpClient = httpClientGenerator.getClient(site, proxy);
				httpClients.put(domain, httpClient);
			}
		}
		return httpClient;
	}
}
