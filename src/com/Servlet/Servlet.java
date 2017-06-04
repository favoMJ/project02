package com.Servlet;

import com.server.Request;
import com.server.Response;

public abstract class Servlet {
	public void receive(Response rep , Request req)
	{
		doGet(req,rep);
		doPost(req,rep);
	}
	protected void doGet(Request req,Response rep){};
	protected void doPost(Request req,Response rep){};
}
