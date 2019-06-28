package http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.http.HttpEntity;
import json.jsonUtils;

public class HttpUtils {

	public static String responseJSON(String url, int method, String jsonData) {
		return jsonUtils.JSONFormatter(executeCall(url, method, jsonData));
	}

	public static String executeCall(String url, int method, String jsonData) {
		String response = "";
		try {
			// http client
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpEntity httpEntity = null;
			HttpResponse httpResponse = null;
			// Checking http request method type
			if (method == 0) {
				HttpPost httpPost = new HttpPost(url);
				// adding post params
				if (jsonData != null && jsonData.length() > 0) {
					StringEntity encodedEntity = new StringEntity(jsonData, "UTF-8");
					encodedEntity.setContentEncoding("UTF-8");
					encodedEntity.setContentType("application/json");
					httpPost.setEntity(encodedEntity);
				}
				httpResponse = httpClient.execute(httpPost);

			} else if (method == 1) {
				HttpGet httpGet = new HttpGet(url);
				httpResponse = httpClient.execute(httpGet);
			}
			// codigo pruebna
			response = new Scanner(httpResponse.getEntity().getContent(), "UTF-8").useDelimiter("\\A").next();
			/*
			 * httpEntity = httpResponse.getEntity(); response =
			 * EntityUtils.toString(httpEntity,"utf-8");
			 */
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}

	public static void sendPost(String url, String jsonData) {
		InputStream is = null;
		try {

			URL connUrl = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) connUrl.openConnection();

			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			connection.setUseCaches(false);
			connection.setInstanceFollowRedirects(true);
			connection.setRequestProperty("Content-Type", "application/json");

			connection.connect();

			// POST
			DataOutputStream out = new DataOutputStream(connection.getOutputStream());
			out.writeBytes(jsonData);
			out.flush();
			out.close();

			HttpURLConnection urlConnection = connection;
			if (urlConnection.getResponseCode() > 400) {
				is = urlConnection.getErrorStream();
			} else {
				is = urlConnection.getInputStream();
			}

			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader reader = new BufferedReader(isr);
			String lines;
			StringBuffer sb = new StringBuffer("");
			while ((lines = reader.readLine()) != null) {
				lines = new String(lines.getBytes(), "utf-8");
				sb.append(lines);
			}
			System.out.println(sb);
			reader.close();

			connection.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			if (null != is) {
				try {
					is.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}

	}
}
