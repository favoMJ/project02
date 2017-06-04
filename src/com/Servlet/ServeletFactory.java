package com.Servlet;

import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServeletFactory {

	private static Map<String,String>urlServletMap;
	private static Map<String,String>urlNameMap;
	
	public ServeletFactory()
	{
		urlServletMap = new HashMap<>();
		urlNameMap = new HashMap<>();
	}
	
	public Map<String, String> getUrlSocketMap() {
		return urlServletMap;
	}

	public void setUrlServletMap(Map<String, String> urlServletMap) {
		this.urlServletMap = urlServletMap;
	}

	public Map<String, String> getUrlNameMap() {
		return urlNameMap;
	}

	public void setUrlNameMap(Map<String,String> urlNameMap) {
		this.urlNameMap = urlNameMap;
	}

}
