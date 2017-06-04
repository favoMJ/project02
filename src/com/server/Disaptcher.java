package com.server;

import java.io.IOException;
import java.net.Socket;

import com.Servlet.Servlet;
import com.Servlet.URLServlet;

public class Disaptcher implements Runnable{

	private Socket socket;
	private Response rep;
	private Request req;
	private int code;
	
	public Disaptcher()
	{
		code = 200;
	}
	public Disaptcher(Socket socket)
	{
		this();
		this.socket = socket;
		try {
			rep = new Response(socket.getOutputStream());
			req = new Request(socket.getInputStream());
		} catch (IOException e) {
			code = 500;
		}
	}
	
	@Override
	public void run() {
//		System.out.println(req.getUrl());
		try {
			Servlet serv = new URLServlet();
			if( serv != null ){
				serv.receive(rep, req);
				rep.pushtoClient();
				rep.close();
				req.close();
			}
		} catch (Exception e) {
			code = 500;
		}
	}
}
