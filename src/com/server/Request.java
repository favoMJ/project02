package com.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class Request {

	private final String URLF = "\r\n";
	private final String BLANK = " ";

	private Socket socket;
	private String url;
	private String requestInfo;
	private InputStream is;

	public String getUrl() {
		return url;
	}

	public Request() {
		url = "";
		requestInfo = "";
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Request(InputStream is) throws IOException {
		this();
			int len ;
			byte[] data = new byte[20480];
			len = is.read(data);
			if( len != -1 ){
			url = new String(data, 0, len);
			System.out.println(url);
			}
	}


	private String decode(String value, String code) {
		try {
			return java.net.URLDecoder.decode(value, code);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}


	public void close() throws IOException
	{
		if( is != null ){
			is.close();
		}
	}
		
}
