package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * http发起请求
 * @author Renhui Lin
 * 2015-7-10
 */
public class RestHttp {

	private static int _timeout = 7200000;
	private static final String CONTENT_CHARSET = "UTF-8";
	private static Log log = LogFactory.getLog(RestHttp.class);


	public static String get(String uri, Map paramMap) throws IOException {
		String data = "";
		HttpClient httpClient = new HttpClient();
		httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, CONTENT_CHARSET);
		GetMethod getMethod = null;
		try {
			getMethod = new GetMethod();
			getMethod.setURI(new URI(uri, false));
			getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
			getMethod.setRequestHeader("Connection", "close");
			// param
			if (paramMap != null) {
				NameValuePair[] NameValuePairs = new NameValuePair[paramMap.size()];
				java.util.Iterator keys = paramMap.keySet().iterator();
				int i = 0;
				while (keys.hasNext()) {
					String key = (String) keys.next();
					NameValuePair param = new NameValuePair(key, (String) paramMap.get(key));
					NameValuePairs[i] = param;
					i++;
				}
				getMethod.setQueryString(NameValuePairs);
			}
			int statusCode = httpClient.executeMethod(getMethod);
			if (statusCode != HttpStatus.SC_OK) {
				log.error("Method failed: " + getMethod.getStatusLine());
				return data;
			}
			byte[] dataResponseBody = getMethod.getResponseBody();
			data = new String(dataResponseBody, "utf-8");
		} catch (HttpException e) {
			log.error("Please check your provided http address!");
		} catch (IOException e) {
			log.error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			if (getMethod != null)
				getMethod.releaseConnection();
		}

		return data;
	}

	public static String post(String uri, Map paramMap) throws IOException {
		String data = "";
		DefaultHttpClient httpClient = new DefaultHttpClient();
		PostMethod method = null;
		String json = JSONObject.fromObject(paramMap).toString();
		System.out.println("http发送的json:"+json);
		try {
			HttpPost post = new HttpPost(uri);
			StringEntity s = new StringEntity(json.toString(),"UTF-8");
			// s.setContentEncoding("UTF-8");
			s.setContentType("application/json");// 发送json数据需要设置contentType
			post.setEntity(s);
			HttpResponse res = httpClient.execute(post);
			if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = res.getEntity();
				data = EntityUtils.toString(res.getEntity());// 返回json格式：
			}
		} catch (HttpException e) {
			log.error("Please check your provided http address!");
		} catch (IOException e) {
			log.error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			if (method != null)
				method.releaseConnection();
		}
		return data;
	}

	public static String put(String uri, Map paramMap) throws IOException {
		String data = "";
		HttpClient httpClient = new HttpClient();
		PutMethod method = null;
		try {
			method = new PutMethod();
			method.setURI(new URI(uri, false));
			// param
			if (paramMap != null) {
				NameValuePair[] NameValuePairs = new NameValuePair[paramMap.size()];
				java.util.Iterator keys = paramMap.keySet().iterator();
				int i = 0;
				while (keys.hasNext()) {
					String key = (String) keys.next();
					NameValuePair param = new NameValuePair(key, (String) paramMap.get(key));
					NameValuePairs[i] = param;
					i++;
				}
				method.setQueryString(NameValuePairs);
			}

			int statusCode = httpClient.executeMethod(method);
			if (statusCode != HttpStatus.SC_OK) {
				log.error("Method failed: " + method.getStatusLine());
			}
			data = new String(method.getResponseBody(), "utf-8");
		} catch (HttpException e) {
			log.error("Please check your provided http address!");
		} catch (IOException e) {
			log.error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			if (method != null)
				method.releaseConnection();
		}
		return data;
	}

	public static String delete(String uri) throws IOException {
		String data = "";
		HttpClient httpClient = new HttpClient();
		httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, CONTENT_CHARSET);
		httpClient.setConnectionTimeout(_timeout);
		httpClient.setTimeout(_timeout);

		DeleteMethod method = null;
		try {
			method = new DeleteMethod();
			method.setURI(new URI(uri, false));
			method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
			int statusCode = httpClient.executeMethod(method);
			if (statusCode != HttpStatus.SC_OK) {
				log.error("Method failed: " + method.getStatusLine());
			}
			data = new String(method.getResponseBody(), "utf-8");
		} catch (HttpException e) {
			log.error("Please check your provided http address!");
		} catch (IOException e) {
			log.error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			if (method != null)
				method.releaseConnection();
		}
		return data;
	}

	public static String httpRest(String uri, String method) throws IOException {
		return httpRest(uri, null, method);
	}

	public static String httpRest(String uri, Map paramMap, String method) throws IOException {
		String threadName = Thread.currentThread().getName();
		log.info("<" + threadName + ">" + method + ": " + uri);
		String result = null;
		if (method.toUpperCase().equals("PUT"))
			result = put(uri, paramMap);
		if (method.toUpperCase().equals("GET"))
			result = get(uri, paramMap);
		if (method.toUpperCase().equals("POST"))
			result = post(uri, paramMap);
		if (method.toUpperCase().equals("DELETE"))
			result = delete(uri);
		if (result == null)
			result = "";

		if (result.length() < 10000) {
			log.info("<" + threadName + ">" + "result: " + result);
		} else {
			log.info("<" + threadName + ">" + "result:Large data... ");
		}

		return result;
	}

	private static String doRest(String uri, String method) throws IOException {
		StringBuffer data = new StringBuffer();
		if (uri == null || "".equals(uri))
			return "";

		URL url = new URL(uri);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setDoOutput(true);

		conn.setRequestMethod(method);
		BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String input = null;
		while ((input = reader.readLine()) != null) {
			data.append(input);
		}
		conn.disconnect();
		return data.toString();
	}

}
