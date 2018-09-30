package com.aibyd.appsys.utils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HTTP请求工具类
 */
public class HTTPUtils {

	// 添加HTTPS标识
	// private static boolean useSSL;
	// 创建静态代码块获取配置文件中的flag
	// static {
	// PropertiesLoader propertiesLoader = new
	// PropertiesLoader("application.properties");
	//
	// boolean value = propertiesLoader.getBoolean("elasticsearch.http.ssl");
	//
	// useSSL = value;
	//
	// }

	// HTTPS准备工作
	private static class TrustAnyTrustManager implements X509TrustManager {

		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}

		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}

		public X509Certificate[] getAcceptedIssuers() {
			return new X509Certificate[] {};
		}
	}

	private static class TrustAnyHostnameVerifier implements HostnameVerifier {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	}

	private static Logger log = LoggerFactory.getLogger(HTTPUtils.class);

	/**
	 * 发送get请求
	 * 
	 * @param url
	 *            请求url
	 * @return 请求结果。异常或者没拿到返回结果的情况下,请求结果为""
	 */
	public static String doGetQuery(boolean useSSL, String url, int time) throws IOException {
		// 判断是否执行HTTPS请求
		if (useSSL) {
			SSLContext sc = null;
			try {
				sc = SSLContext.getInstance("SSL");
				sc.init(null, new TrustManager[] { new TrustAnyTrustManager() }, new java.security.SecureRandom());
			} catch (Exception e) {
				e.printStackTrace();
			}

			URL console = new URL(null, url, new sun.net.www.protocol.https.Handler());
			HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();
			conn.setSSLSocketFactory(sc.getSocketFactory());
			conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
			conn.setDoOutput(true);
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(time);
			conn.setReadTimeout(time);
			conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
			conn.connect();
			if (conn.getResponseCode() == 200) {
				InputStream is = conn.getInputStream();
				if (is != null) {
					ByteArrayOutputStream outStream = new ByteArrayOutputStream();
					byte[] buffer = new byte[1024];
					int len = 0;
					while ((len = is.read(buffer)) != -1) {
						outStream.write(buffer, 0, len);
					}
					is.close();
					return new String(outStream.toByteArray());
				}
			}
		} else {
			// 创建get请求
			GetMethod method = new GetMethod(url);
			setMethodHeader(method);

			// 发出请求
			String result = "";
			int stateCode = 0;
			try {
				stateCode = createHttpClient(time).executeMethod(method);
			} catch (IOException e) {
				log.error("Http post request failure, reason could be: {}", e.getMessage());
				throw e;
			}
			log.info("Http get method, stateCode: {}, url: {}", stateCode, url);

			// 请求成功
			if (stateCode == HttpStatus.SC_OK) {
				try {
					result = method.getResponseBodyAsString();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			// 释放资源
			releaseHttpConnection(method);

			return result;
		}
		;

		return null;
	}

	/**
	 * 发送post请求
	 * 
	 * @param url
	 *            请求url
	 * @param content
	 *            请求参数
	 * @return 请求结果。异常或者没拿到返回结果的情况下,请求结果为""
	 */
	public static String doPostQuery(boolean useSSL, String url, String content, int time) throws IOException {
		if (useSSL) {
			SSLContext sc = null;
			try {
				sc = SSLContext.getInstance("SSL");
				sc.init(null, new TrustManager[] { new TrustAnyTrustManager() }, new java.security.SecureRandom());
			} catch (Exception e) {
				e.printStackTrace();
			}

			URL console = new URL(null, url, new sun.net.www.protocol.https.Handler());
			HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();
			conn.setSSLSocketFactory(sc.getSocketFactory());
			conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
			conn.setDoOutput(true);
			conn.setConnectTimeout(time);
			conn.setReadTimeout(time);
			conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
			conn.connect();
			DataOutputStream out = new DataOutputStream(conn.getOutputStream());
			out.write(content.getBytes("UTF-8"));
			// 刷新、关闭
			out.flush();
			out.close();
			InputStream is = null;
			ByteArrayOutputStream outStream = null;
			try {
				is = conn.getInputStream();
				if (is != null) {
					outStream = new ByteArrayOutputStream();
					byte[] buffer = new byte[1024];
					int len = 0;
					while ((len = is.read(buffer)) != -1) {
						outStream.write(buffer, 0, len);
					}
					return new String(outStream.toByteArray());
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (outStream != null)
					outStream.close();
				if (is != null)
					is.close();

			}
		} else {
			// 创建post请求
			PostMethod method = new PostMethod(url);
			setMethodHeader(method);

			try {
				RequestEntity requestEntity = new ByteArrayRequestEntity(content.getBytes("UTF-8"), "UTF-8");
				method.setRequestEntity(requestEntity);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			// 发出请求
			String result = "";
			int stateCode = 0;
			try {
				stateCode = createHttpClient(time).executeMethod(method);
			} catch (IOException e) {
				log.error("Http post request failure, reason could be: {}", e.getMessage());
				throw e;
			}
			log.info("Http post method, stateCode: {}, url: {}, body: {}", stateCode, url, content);

			// 请求成功
			if (stateCode == HttpStatus.SC_OK) {
				try {
					result = method.getResponseBodyAsString();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			// 释放资源
			releaseHttpConnection(method);

			return result;
		}
		return null;
	}

	/**
	 * 发送put请求
	 * 
	 * @param url
	 *            请求路径
	 * @param content
	 *            请求参数
	 * @return 请求结果。异常或者没拿到返回结果的情况下,请求结果为""
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 */
	public static String doPutQuery(boolean useSSL, String url, String content, int time) throws IOException {
		if (useSSL) {
			SSLContext sc = null;
			try {
				sc = SSLContext.getInstance("SSL");
				sc.init(null, new TrustManager[] { new TrustAnyTrustManager() }, new java.security.SecureRandom());
			} catch (Exception e) {
				e.printStackTrace();
			}

			URL console = new URL(null, url, new sun.net.www.protocol.https.Handler());
			HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();
			conn.setSSLSocketFactory(sc.getSocketFactory());
			conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
			conn.setDoOutput(true);
			conn.setRequestMethod("PUT");
			conn.setConnectTimeout(time);
			conn.setReadTimeout(time);
			conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
			conn.connect();
			DataOutputStream out = new DataOutputStream(conn.getOutputStream());
			out.write(content.getBytes("UTF-8"));
			// 刷新、关闭
			out.flush();
			out.close();
			InputStream is = null;
			ByteArrayOutputStream outStream = null;
			try {
				is = conn.getInputStream();
				if (is != null) {
					outStream = new ByteArrayOutputStream();
					byte[] buffer = new byte[1024];
					int len = 0;
					while ((len = is.read(buffer)) != -1) {
						outStream.write(buffer, 0, len);
					}
					return new String(outStream.toByteArray());
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (outStream != null)
					outStream.close();
				if (is != null)
					is.close();

			}

		} else {
			// 创建put请求
			PutMethod method = new PutMethod(url);
			setMethodHeader(method);

			try {
				RequestEntity requestEntity = new ByteArrayRequestEntity(content.getBytes("UTF-8"), "UTF-8");
				method.setRequestEntity(requestEntity);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			// 发出请求
			String result = "";
			int stateCode = 0;
			try {
				stateCode = createHttpClient(time).executeMethod(method);
			} catch (IOException e) {
				log.error("Http post request failure, reason could be: {}", e.getMessage());
				throw e;
			}
			log.info("Http put method, stateCode: {}, url: {}, body: {}", stateCode, url, content);

			// 请求成功
			if (stateCode == HttpStatus.SC_OK) {
				try {
					result = method.getResponseBodyAsString();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			// 释放资源
			releaseHttpConnection(method);

			return result;
		}

		return null;

	}

	/**
	 * 发送delete请求
	 * 
	 * @param url
	 *            请求url
	 * @return 请求结果。异常或者没拿到返回结果的情况下,请求结果为""
	 */
	public static String doDeleteQuery(boolean useSSL, String url, int time) throws IOException {
		if (useSSL) {
			SSLContext sc = null;
			try {
				sc = SSLContext.getInstance("SSL");
				sc.init(null, new TrustManager[] { new TrustAnyTrustManager() }, new java.security.SecureRandom());
			} catch (Exception e) {
				e.printStackTrace();
			}
			URL console = new URL(null, url, new sun.net.www.protocol.https.Handler());
			HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();
			conn.setSSLSocketFactory(sc.getSocketFactory());
			conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
			conn.setDoOutput(true);
			conn.setRequestMethod("DELETE");
			conn.setConnectTimeout(time);
			conn.setReadTimeout(time);
			conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
			conn.connect();
			if (conn.getResponseCode() == 200) {
				InputStream is = conn.getInputStream();
				if (is != null) {
					ByteArrayOutputStream outStream = new ByteArrayOutputStream();
					byte[] buffer = new byte[1024];
					int len = 0;
					while ((len = is.read(buffer)) != -1) {
						outStream.write(buffer, 0, len);
					}
					is.close();
					return new String(outStream.toByteArray());
				}
			}
		} else {
			// 创建get请求
			DeleteMethod method = new DeleteMethod(url);
			setMethodHeader(method);

			// 发出请求
			String result = "";
			int stateCode = 0;
			try {
				stateCode = createHttpClient(time).executeMethod(method);
			} catch (IOException e) {
				log.error("Http post request failure, reason could be: {}", e.getMessage());
				throw e;
			}
			log.info("Http delete method, stateCode: {}, url: {}", stateCode, url);

			// 请求成功
			if (stateCode == HttpStatus.SC_OK) {
				try {
					result = method.getResponseBodyAsString();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			// 释放资源
			releaseHttpConnection(method);

			return result;
		}

		return null;
	}

	/**
	 * 创建http客户端
	 * 
	 * @param timeout
	 *            读取数据超时时间
	 */
	private static HttpClient createHttpClient(int timeout) {
		// 创建http请求客户端
		HttpClient client = new HttpClient();
		// 设置超时时间
		HttpConnectionManagerParams managerParams = client.getHttpConnectionManager().getParams();
		// 设置连接超时时间(单位毫秒)
		managerParams.setConnectionTimeout(30000);
		// 设置读数据超时时间(单位毫秒)
		managerParams.setSoTimeout(timeout);
		return client;
	}

	/**
	 * 设置请求头
	 * 
	 * @param method
	 *            请求方法
	 */
	private static void setMethodHeader(HttpMethod method) {
		// 设置请求媒体类型为json格式，编码为 utf-8
		method.setRequestHeader("Content-type", "application/json;charset=UTF-8");
		// 在完成请求后自动关闭链接，不需要手动关闭client
		method.setRequestHeader("Connection", "close");
	}

	/**
	 * 释放method链接
	 * 
	 * @param method
	 *            请求方法
	 */
	private static void releaseHttpConnection(HttpMethod method) {
		// 中断请求
		method.abort();
		// 释放请求连接
		method.releaseConnection();
	}

	/**
	 * 通过正则替换参数，参数格式为：#{value}
	 */
	public StringBuffer parseStatement(String statement, Map<String, String> values) {
		// 参数正则
		String variableRegex = "#\\{\\s*([_\\w]*)\\s*\\}";

		StringBuffer statementBuffer = new StringBuffer();

		// 依次替换语句中的参数
		Matcher matcher = Pattern.compile(variableRegex).matcher(statement);
		while (matcher.find()) {
			StringBuffer temp = new StringBuffer();
			String key = matcher.group(1);
			if (StringUtils.isEmpty(key) || values.get(key) == null) {
				continue;
			}
			matcher.appendReplacement(temp, values.get(key));
			statementBuffer.append(temp);
		}
		matcher.appendTail(statementBuffer);

		return statementBuffer;
	}
}
