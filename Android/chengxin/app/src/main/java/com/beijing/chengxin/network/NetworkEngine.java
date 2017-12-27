package com.beijing.chengxin.network;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.net.ParseException;
import android.net.Uri;

import com.beijing.chengxin.config.Constants;
import com.beijing.chengxin.debug.Logger;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.mime.HttpMultipartMode;
import cz.msebera.android.httpclient.entity.mime.MIME;
import cz.msebera.android.httpclient.entity.mime.MultipartEntityBuilder;
import cz.msebera.android.httpclient.entity.mime.content.ByteArrayBody;
import cz.msebera.android.httpclient.entity.mime.content.ContentBody;
import cz.msebera.android.httpclient.entity.mime.content.FileBody;
import cz.msebera.android.httpclient.entity.mime.content.StringBody;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import cz.msebera.android.httpclient.protocol.HTTP;

/**
 * Helper for making http request using HttpUrlConnection class. It's light
 * weight and optimized for android
 *
 * Created by star on 11/2/2017.
 * 
 */
public class NetworkEngine {

	public static final String TAG = "NetworkEngine";

	public static final String USER_AGENT = "HiBOX Android HTTP Client";

	public static final char PARAMETER_DELIMITER = '&';
	public static final char PARAMETER_EQUALS_CHAR = '=';
	public static final int HTTP_CONNECT_TIMEOUT = 10000;
	public static final int HTTP_READ_TIMEOUT = 30000;
	public static final int BUFF_SIZE = 1 * 1024 * 1024;

	public static ContentResolver 	mContentResolver;
	
	
/**
 * send post request with form data and retrun string
 * @param urlTo Server address
 * @param params key & value pair for request parameters.
 * @return response String.
 */
	public static String post(String urlTo, Map<String, String> params) {

		HttpURLConnection connection = null;

		String responseString = null;

		try {

			URL urlToRequest = new URL(urlTo);
			connection = (HttpURLConnection) urlToRequest.openConnection();
//			connection.setRequestProperty("User-Agent", USER_AGENT);
			connection.setConnectTimeout(HTTP_CONNECT_TIMEOUT);
			connection.setReadTimeout(HTTP_READ_TIMEOUT);

			if (params != null && params.size() > 0) {

				connection.setDoOutput(true);
				connection.setUseCaches(false);
				connection.setRequestMethod("POST");
				connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				connection.setRequestProperty("charset", "utf-8");

				String queryString = createQueryStringForParameters(params);
				PrintWriter out = new PrintWriter(connection.getOutputStream());
				out.print(queryString);
				out.flush();
				out.close();
			}

			int status = connection.getResponseCode();
			if (status == HttpURLConnection.HTTP_OK) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

				StringBuilder builder = new StringBuilder();
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}

				reader.close();
				responseString = builder.toString();
			} else {
				Logger.e(TAG, "status code:" + status + " message:" + connection.getResponseMessage());
			}

		} catch (Exception e) {
			Logger.ex(TAG, e);
		} finally {
			connection.disconnect();
		}

		return responseString;
	}

	public static String post(String urlTo, String callData) {

		HttpURLConnection connection = null;

		String responseString = null;

		try {

			URL urlToRequest = new URL(urlTo);
			connection = (HttpURLConnection) urlToRequest.openConnection();
//			connection.setRequestProperty("User-Agent", USER_AGENT);
			connection.setConnectTimeout(HTTP_CONNECT_TIMEOUT);
			connection.setReadTimeout(HTTP_READ_TIMEOUT);

			if (callData != null && callData != "") {

				connection.setDoOutput(true);
				connection.setUseCaches(false);
				connection.setRequestMethod("POST");
				connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				connection.setRequestProperty("charset", "utf-8");

				String queryString = callData;
				PrintWriter out = new PrintWriter(connection.getOutputStream());
				out.print(queryString);
				out.flush();
				out.close();
			}

			int status = connection.getResponseCode();
			if (status == HttpURLConnection.HTTP_OK) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

				StringBuilder builder = new StringBuilder();
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}

				reader.close();
				responseString = builder.toString();
			} else {
				Logger.e(TAG, "status code:" + status + " message:" + connection.getResponseMessage());
			}

		} catch (Exception e) {
			Logger.ex(TAG, e);
		} finally {
			connection.disconnect();
		}

		return responseString;
	}

	public static InputStreamReader postReturnReader(String urlTo, Map<String, String> params) {

		HttpURLConnection connection = null;

		InputStreamReader reader = null;

		try {
			URL urlToRequest = new URL(urlTo);
			connection = (HttpURLConnection) urlToRequest.openConnection();
//			connection.setRequestProperty("User-Agent", USER_AGENT);
			connection.setConnectTimeout(HTTP_CONNECT_TIMEOUT);
			connection.setReadTimeout(HTTP_READ_TIMEOUT);

			if (params != null && params.size() > 0) {

				connection.setDoOutput(true);
				connection.setUseCaches(false);
				connection.setRequestMethod("POST");
				connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				connection.setRequestProperty("charset", "utf-8");

				String queryString = createQueryStringForParameters(params);
				PrintWriter out = new PrintWriter(connection.getOutputStream());
				out.print(queryString);
				out.flush();
				out.close();
			}

			int status = connection.getResponseCode();
			if (status == HttpURLConnection.HTTP_OK) {
				reader = new InputStreamReader(connection.getInputStream());
			} else {
				Logger.e(TAG, "status code:" + status + " message:" + connection.getResponseMessage());
			}

		} catch (Exception e) {
			Logger.ex(TAG, e);
		} finally {
			connection.disconnect();
		}

		return reader;
	}

    /**
     * send get request with form data and return string
     * @param urlTo Server address
     * @param params key & value pair for request parameters.
     * @return response String.
     */
    public static String get(String urlTo, Map<String, String> params) {
        HttpURLConnection connection = null;

        String responseString = null;
        String urlWithParam = urlTo;

        try {

            if (params != null && params.size() > 0) {
                urlWithParam += "?" + createQueryStringForParameters(params);
            }

            URL urlToRequest = new URL(urlWithParam);
            connection = (HttpURLConnection) urlToRequest.openConnection();
//            connection.setRequestProperty("User-Agent", USER_AGENT);
            connection.setConnectTimeout(HTTP_CONNECT_TIMEOUT);
            connection.setReadTimeout(HTTP_READ_TIMEOUT);

            connection.setUseCaches(false);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setRequestProperty("charset", "utf-8");

            int status = connection.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }

                reader.close();
                responseString = builder.toString();
            } else {
                Logger.e(TAG, "status code:" + status + " message:" + connection.getResponseMessage());
            }

        } catch (Exception e) {
            Logger.ex(TAG, e);
        } finally {
            connection.disconnect();
        }

        return responseString;
    }

	/**
	 * Create Query String
	 *
	 * @author ssk
	 * @param parameters
	 * @return http request query string
	 * @throws UnsupportedEncodingException
	 */
	public static String createQueryStringForParameters(Map<String, String> parameters) throws UnsupportedEncodingException {
		StringBuilder parametersAsQueryString = new StringBuilder();
		if (parameters != null) {
			boolean firstParameter = true;

			for (String parameterName : parameters.keySet()) {
                if (parameters.get(parameterName) != null) {
                    if (!firstParameter) {
                        parametersAsQueryString.append(PARAMETER_DELIMITER);
                    }

                    parametersAsQueryString.append(parameterName).append(PARAMETER_EQUALS_CHAR).append(URLEncoder.encode(parameters.get(parameterName), "UTF-8"));

                    firstParameter = false;
                }
			}
		}
		return parametersAsQueryString.toString();
	}

	/**
	 * Send multipart post request including one file
	 *
	 * @author ssk
	 * @param urlTo
	 *            Server Url
	 * @param params
	 *            post parameters
	 * @param fileParamName
	 *            file name parameters
	 * @param filepath
	 *            file path
	 * @return Resonse String
	 */
	public static String postMultipart(String urlTo, Map<String, String> params, String fileParamName, String filepath) {

		HttpURLConnection connection = null;
		DataOutputStream outputStream = null;

		String responseString = null;

		String twoHyphens = "--";
		String boundary = "*****" + Long.toString(System.currentTimeMillis()) + "*****";
		String lineEnd = "\r\n";

		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;

		String[] q = filepath.split("/");
		int idx = q.length - 1;

		try {
			File file = new File(filepath);
			FileInputStream fileInputStream = new FileInputStream(file);

			URL url = new URL(urlTo);
			connection = (HttpURLConnection) url.openConnection();

			connection.setConnectTimeout(HTTP_CONNECT_TIMEOUT);
			connection.setReadTimeout(HTTP_READ_TIMEOUT);

			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);

			connection.setRequestMethod("POST");
			connection.setRequestProperty("Connection", "Keep-Alive");
//			connection.setRequestProperty("User-Agent", USER_AGENT);
			connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

			outputStream = new DataOutputStream(connection.getOutputStream());
			outputStream.writeBytes(twoHyphens + boundary + lineEnd);
			outputStream.writeBytes("Content-Disposition: form-data; name=\"" + fileParamName + "\"; filename=\"" + q[idx] + "\"" + lineEnd);
			outputStream.writeBytes("Content-Type: image/jpeg" + lineEnd);
			outputStream.writeBytes("Content-Transfer-Encoding: binary" + lineEnd);
			outputStream.writeBytes(lineEnd);

			bytesAvailable = fileInputStream.available();
			bufferSize = Math.min(bytesAvailable, BUFF_SIZE);
			buffer = new byte[bufferSize];

			bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			while (bytesRead > 0) {
				outputStream.write(buffer, 0, bufferSize);
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, BUFF_SIZE);
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			}

			outputStream.writeBytes(lineEnd);

			// Upload POST Data
			for (String key : params.keySet()) {
				outputStream.writeBytes(twoHyphens + boundary + lineEnd);
				outputStream.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"" + lineEnd);
				outputStream.writeBytes("Content-Type: text/plain" + lineEnd);
				outputStream.writeBytes(lineEnd);
				outputStream.writeBytes(params.get(key));
				outputStream.writeBytes(lineEnd);
			}

			outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
			outputStream.flush();
			outputStream.close();
			fileInputStream.close();

			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

			StringBuilder builder = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}

			reader.close();
			responseString = builder.toString();

		} catch (Exception e) {
			Logger.e(TAG, "Multipart Form Upload Error");
			Logger.ex(TAG, e);
		} finally {
			connection.disconnect();
		}
		return responseString;
	}

	/**
	 * Send multipart post request including one file
	 *
	 * @author ssk
	 * @param urlTo
	 *            Server Url
	 * @param params
	 *            post parameters
	 * @param fileModels
	 *            FileModelList parameters
	 * @return Resonse String
	 */
	public static String postMultipart(String urlTo, Map<String, String> params, ArrayList<Constants.UploadFileModel> fileModels) {

		HttpURLConnection connection = null;
		DataOutputStream outputStream = null;

		String responseString = null;

		String twoHyphens = "--";
		String boundary = "----" + Long.toString(System.currentTimeMillis()) + "";
		String lineEnd = "\r\n";

		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;

		try {

			URL url = new URL(urlTo);
			connection = (HttpURLConnection) url.openConnection();

			connection.setConnectTimeout(HTTP_CONNECT_TIMEOUT);
			connection.setReadTimeout(HTTP_READ_TIMEOUT);

			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);

			connection.setRequestMethod("POST");
			connection.setRequestProperty("Connection", "keep-alive");
			connection.setRequestProperty("Cache-Control", "max-age=0");
//			connection.setRequestProperty("User-Agent", USER_AGENT);
			connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

			outputStream = new DataOutputStream(connection.getOutputStream());
			outputStream.writeBytes(twoHyphens + boundary + lineEnd);

			for (int i = 0; i < fileModels.size(); i++) {
				Constants.UploadFileModel fileItem = fileModels.get(i);

				outputStream.writeBytes("Content-Disposition: form-data; name=\"" + fileItem.fileTitle + "\"; filename=\"" + fileItem.fileName + "\"" + lineEnd);
				//outputStream.writeBytes("Content-Type: image/jpeg" + lineEnd);
				outputStream.writeBytes("Content-Type: application/octet-stream" + lineEnd);
				outputStream.writeBytes(lineEnd);

				File file = new File(fileItem.filePath);
				FileInputStream fileInputStream = new FileInputStream(file);

				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, BUFF_SIZE);
				buffer = new byte[bufferSize];

				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
				while (bytesRead > 0) {
					outputStream.write(buffer, 0, bufferSize);
					bytesAvailable = fileInputStream.available();
					bufferSize = Math.min(bytesAvailable, BUFF_SIZE);
					bytesRead = fileInputStream.read(buffer, 0, bufferSize);
				}

				outputStream.writeBytes(lineEnd);
				outputStream.writeBytes(twoHyphens + boundary + lineEnd);

				fileInputStream.close();
			}

			// Upload POST Data
			for (String key : params.keySet()) {
				outputStream.writeBytes(twoHyphens + boundary + lineEnd);
				outputStream.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"" + lineEnd);
				outputStream.writeBytes(lineEnd);
				outputStream.writeBytes(params.get(key));
				outputStream.writeBytes(lineEnd);
			}

			outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
			outputStream.flush();
			outputStream.close();

			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

			StringBuilder builder = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}

			reader.close();
			responseString = builder.toString();

		} catch (Exception e) {
			Logger.e(TAG, "Multipart Form Upload Error");
			Logger.ex(TAG, e);
		} finally {
			connection.disconnect();
		}
		return responseString;
	}

	public static String postWithFile(String urlTo, Map<String, String> params, ArrayList<Constants.UploadFileModel> fileModels) {
		try {
			// Create a new HttpClient and Post Header
			HttpClient httpclient = HttpClientBuilder.create().build();
			HttpPost httppost = new HttpPost(urlTo);

			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			builder.setCharset(Charset.forName("UTF-8"));
			int count = fileModels.size();
			for (int i = 0; i < count; i++) {
				Constants.UploadFileModel fileItem = fileModels.get(i);
				File file = new File(fileItem.filePath);
				FileBody cbFile = new FileBody(file);
				builder.addPart(fileItem.fileTitle, cbFile);
			}

			for (String key : params.keySet()) {
                builder.addPart(key, new StringBody(params.get(key), Charset.forName("UTF-8")));
//                builder.addTextBody(key,params.get(key), ContentType.create(HTTP.PLAIN_TEXT_TYPE, MIME.UTF8_CHARSET));
			}

			HttpEntity mpEntity = builder.build();
			httppost.setEntity(mpEntity);

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);

			Reader reader = new InputStreamReader(response.getEntity().getContent(), "UTF-8");
			String result = "";
			char[] buffer = new char[1024];
			int read;
			while((read = reader.read(buffer))>0)
			{
				result += new String(buffer, 0, read);
			}
			return result;
		} catch (Exception e) {
			return "";
		}
	}

	public static String postMultipart(String urlTo, Map<String, String> params, String fileParamName, Uri imgPath) {

		HttpURLConnection connection = null;
		DataOutputStream outputStream = null;

		String responseString = null;

		String twoHyphens = "--";
		String boundary = "*****" + Long.toString(System.currentTimeMillis()) + "*****";
		String lineEnd = "\r\n";

		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;

		String[] q = imgPath.getPath().split("/");
		int idx = q.length - 1;

		try {
			InputStream fileInputStream = mContentResolver.openInputStream(imgPath);
			URL url = new URL(urlTo);
			connection = (HttpURLConnection) url.openConnection();

			connection.setConnectTimeout(HTTP_CONNECT_TIMEOUT);
			connection.setReadTimeout(HTTP_READ_TIMEOUT);

			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);

			connection.setRequestMethod("POST");
			connection.setRequestProperty("Connection", "Keep-Alive");
//			connection.setRequestProperty("User-Agent", USER_AGENT);
			connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

			outputStream = new DataOutputStream(connection.getOutputStream());
			outputStream.writeBytes(twoHyphens + boundary + lineEnd);
			outputStream.writeBytes("Content-Disposition: form-data; name=\"" + fileParamName + "\"; filename=\"temp\"" + lineEnd);
			outputStream.writeBytes("Content-Type: image/jpeg" + lineEnd);
			outputStream.writeBytes("Content-Transfer-Encoding: binary" + lineEnd);
			outputStream.writeBytes(lineEnd);

			bytesAvailable = fileInputStream.available();
			bufferSize = Math.min(bytesAvailable, BUFF_SIZE);
			buffer = new byte[bufferSize];

			bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			while (bytesRead > 0) {
				outputStream.write(buffer, 0, bufferSize);
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, BUFF_SIZE);
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			}

			outputStream.writeBytes(lineEnd);

			// Upload POST Data
			for (String key : params.keySet()) {
				outputStream.writeBytes(twoHyphens + boundary + lineEnd);
				outputStream.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"" + lineEnd);
				outputStream.writeBytes("Content-Type: text/plain" + lineEnd);
				outputStream.writeBytes(lineEnd);
				outputStream.writeBytes(params.get(key));
				outputStream.writeBytes(lineEnd);
			}

			outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
			outputStream.flush();
			outputStream.close();
			fileInputStream.close();

			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

			StringBuilder builder = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}

			reader.close();
			responseString = builder.toString();

		} catch (Exception e) {
			Logger.e(TAG, "Multipart Form Upload Error");
			Logger.ex(TAG, e);
		} finally {
			connection.disconnect();
		}
		return responseString;
	}
	/**
	 * Send multipart post request including one file
	 *
	 * @author ssk
	 * @param urlTo
	 *            Server Url
	 * @param params
	 *            post parameters
	 * @param fileParamName
	 *            file name parameters
	 * @param filepath
	 *            file path
	 * @return Response StreamReader
	 * @throws ParseException
	 * @throws IOException
	 */
	public static String postMultipartRetrunReader(String urlTo, Map<String, String> params, String fileParamName, String filepath) {

		HttpURLConnection connection = null;
		DataOutputStream outputStream = null;

		InputStreamReader response = null;
		String responseString ="";

		String twoHyphens = "--";
		String boundary = "*****" + Long.toString(System.currentTimeMillis()) + "*****";
		String lineEnd = "\r\n";

		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;

		String[] q = filepath.split("/");
		int idx = q.length - 1;

		try {
			File file = new File(filepath);
			FileInputStream fileInputStream = new FileInputStream(file);

			URL url = new URL(urlTo);
			connection = (HttpURLConnection) url.openConnection();

			connection.setConnectTimeout(HTTP_CONNECT_TIMEOUT);
			connection.setReadTimeout(HTTP_READ_TIMEOUT);

			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);

			connection.setRequestMethod("POST");
			connection.setRequestProperty("Connection", "Keep-Alive");
//			connection.setRequestProperty("User-Agent", USER_AGENT);
			connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

			outputStream = new DataOutputStream(connection.getOutputStream());
			outputStream.writeBytes(twoHyphens + boundary + lineEnd);
			outputStream.writeBytes("Content-Disposition: form-data; name=\"" + fileParamName + "\"; filename=\"" + q[idx] + "\"" + lineEnd);
			outputStream.writeBytes("Content-Type: image/jpeg" + lineEnd);
			outputStream.writeBytes("Content-Transfer-Encoding: binary" + lineEnd);
			outputStream.writeBytes(lineEnd);

			bytesAvailable = fileInputStream.available();
			bufferSize = Math.min(bytesAvailable, BUFF_SIZE);
			buffer = new byte[bufferSize];

			bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			while (bytesRead > 0) {
				outputStream.write(buffer, 0, bufferSize);
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, BUFF_SIZE);
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			}

			outputStream.writeBytes(lineEnd);

			// Upload POST Data
			for (String key : params.keySet()) {
				outputStream.writeBytes(twoHyphens + boundary + lineEnd);
				outputStream.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"" + lineEnd);
				outputStream.writeBytes("Content-Type: text/plain" + lineEnd);
				outputStream.writeBytes(lineEnd);
				outputStream.writeBytes(params.get(key));
				outputStream.writeBytes(lineEnd);
			}

			outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

			outputStream.flush();
			outputStream.close();
			fileInputStream.close();

			int status = connection.getResponseCode();
			if (status == HttpURLConnection.HTTP_OK) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

				StringBuilder builder = new StringBuilder();
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}

				reader.close();
				responseString = builder.toString();
			}
//			response = new InputStreamReader(connection.getInputStream());

		} catch (Exception e) {
			Logger.e(TAG, "Multipart Form Upload Error");
			Logger.ex(TAG, e);
		} finally {
			connection.disconnect();
		}
		return responseString;
//		return response;
	}

	public static String uploadFile(String urlTo, String fileParamName, String filepath){

		HttpURLConnection connection = null;
		DataOutputStream outputStream;

		String responseString = null;

		String twoHyphens = "--";
		String boundary = "*****" + Long.toString(System.currentTimeMillis()) + "*****";
		String lineEnd = "\r\n";

		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1024 * 1024;

		String[] q = filepath.split("/");
		int idx = q.length - 1;

		try {
			File file = new File(filepath);
			FileInputStream fileInputStream = new FileInputStream(file);

			URL url = new URL(urlTo);
			connection = (HttpURLConnection) url.openConnection();

			connection.setConnectTimeout(HTTP_CONNECT_TIMEOUT);
			connection.setReadTimeout(HTTP_READ_TIMEOUT);

			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);

			connection.setRequestMethod("POST");
			connection.setRequestProperty("Connection", "Keep-Alive");
//			connection.setRequestProperty("User-Agent", USER_AGENT);
			connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

			outputStream = new DataOutputStream(connection.getOutputStream());
			outputStream.writeBytes(twoHyphens + boundary + lineEnd);
			outputStream.writeBytes("Content-Disposition: form-data; name=\"" + fileParamName + "\"; filename=\"" + q[idx] + "\"" + lineEnd);
			outputStream.writeBytes("Content-Type: image/jpeg" + lineEnd);
			outputStream.writeBytes("Content-Transfer-Encoding: binary" + lineEnd);
			outputStream.writeBytes(lineEnd);

			bytesAvailable = fileInputStream.available();
			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			buffer = new byte[bufferSize];

			bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			while (bytesRead > 0) {
				outputStream.write(buffer, 0, bufferSize);
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			}

			outputStream.writeBytes(lineEnd);

			outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
			outputStream.flush();
			outputStream.close();
			fileInputStream.close();

			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

			StringBuilder builder = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}

			reader.close();
			responseString = builder.toString();

		} catch (Exception e) {
			Logger.e(TAG, "Multipart Form Upload Error");
			Logger.ex(TAG, e);
		} finally {
			connection.disconnect();
		}
		return responseString;
	}

	public static boolean downloadFile(String url, String filePath){

		boolean result = false;

		HttpURLConnection connection = null;

		try {

			URL urlToRequest = new URL(url);
			connection = (HttpURLConnection) urlToRequest.openConnection();
//			connection.setRequestProperty("User-Agent", USER_AGENT);
			connection.setConnectTimeout(HTTP_CONNECT_TIMEOUT);
			connection.setReadTimeout(HTTP_READ_TIMEOUT);
			connection.setUseCaches(false);

			int status = connection.getResponseCode();
			if (status == HttpURLConnection.HTTP_OK) {
				BufferedInputStream inputStream = new BufferedInputStream(connection.getInputStream());

				File outputFile = new File(filePath);
				BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFile));
				byte buff[] = new byte[BUFF_SIZE];

				int count;
				while((count = inputStream.read(buff)) > 0){
					outputStream.write(buff, 0, count);
				}

				inputStream.close();
				outputStream.flush();
				outputStream.close();

				result = true;

			} else {
				Logger.e(TAG, "status code:" + status + " message:" + connection.getResponseMessage());
			}

		} catch (Exception e) {
			Logger.ex(TAG, e);
		} finally {
			connection.disconnect();
		}
		
		return result;
	}
}
