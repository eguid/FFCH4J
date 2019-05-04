package cc.eguid.commandManager.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * http请求工具
 * @author eguid
 */
public final class HttpUtil {
	
	// 一个网络库，用于快速发送http消息
	private static final int DEFAULLTIMEOUT = 3000;// 默认3秒
	
	/**
	 * 发送请求
	 */
	public final static String request(String method,String requrl) {
		return request(method,requrl,null,false);
	}
	
	/**
	 * 发送请求
	 */
	public final static String request(String method,String requrl,Map<String,String> data) {
		String param=null;
		if(data!=null) {
			param=JsonUtil.toJson(data);
		}
		return request(method,requrl,param,true);
	}
	
	/**
	 * 发送请求
	 */
	public final static String request(String method,String requrl,String data,boolean json) {
		URL url=null;
		HttpURLConnection connection=null;
		BufferedReader reader=null;
		PrintWriter writer=null;
		StringBuilder result;
		try {
			url = new URL(requrl);
			// 打开和URL之间的连接
			connection =(HttpURLConnection) url.openConnection();
			// 设置通用的请求属性
			connection.setRequestMethod(method.toUpperCase());
			//connection.setFollowRedirects(true);//默认自动重定向
			connection.setUseCaches(false);//不适用缓存
			connection.setConnectTimeout(DEFAULLTIMEOUT);//建立连接超时时间
			connection.setReadTimeout(DEFAULLTIMEOUT);//读取超时时间
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			connection.setDoOutput(true);
			connection.setDoInput(true);
			//设置内容格式
			if(json) {
				connection.addRequestProperty("Content-type", "application/json; charset=utf-8");
			}
			connection.connect();//建立连接
			if(data!=null) {
				OutputStream output=connection.getOutputStream();
				writer=new PrintWriter(output, true);
				writer.print(data);
				writer.flush();
			}
			int code=connection.getResponseCode();
//			String msg=connection.getResponseMessage();
			if(code==HttpURLConnection.HTTP_OK) {
				InputStream input=connection.getInputStream();
				reader=new BufferedReader(new InputStreamReader(input,"utf-8"));
				reader.ready();
				result=new StringBuilder();
				String temp=null;
				for(;(temp=reader.readLine())!=null;) {
					result.append(temp);
				}
				return result.toString();
			}
			return null;
		} catch (IOException e) {
			//请求错误，返回空
			return null;
		}finally {
			try {
				if(writer!=null)
					writer.close();
				if(reader!=null) 
					reader.close();
			} catch (IOException e) {}
			
			if(connection!=null)
				connection.disconnect();
		}
	}
	
	/**
	 * 发送get请求
	 */
	public final static String get(String url) {
		return request("GET",url);
	}
	
	/**
	 * 发送post请求
	 */
	public static final String post(String url,Map<String,String>data) {
		return request("POST",url,data) ;
	}
	
	/**
	 * 发送post请求
	 */
	public static final String post(String url) {
		return post(url,null) ;
	}

	
	public static void main(String[] args) {
		System.err.println(post("http://eguid.cc:1985/api/v1/versions"));
	}
}
