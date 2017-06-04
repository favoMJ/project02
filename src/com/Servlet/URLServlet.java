package com.Servlet;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.server.Request;
import com.server.Response;


public class URLServlet extends Servlet{

	@Override
	protected void doGet(Request req, Response rep) {
		String str = req.getUrl();
		String newUrl = System.getProperty("user.dir") + "\\" + "root" + "\\" + str + ".txt";
		System.out.println(newUrl); 
		DataInputStream dos = null;
		try {
			   dos = new DataInputStream(new FileInputStream(newUrl));
				int len = dos.available();
				if( len > -1 )
				{
					byte[] data = new byte[len];
					dos.read(data);
					rep.println(data,len);
				}
				//rep.print()
				
		} catch (FileNotFoundException e) {
			System.out.println("URLServlet file error");
		} catch (IOException e) {
		}
		
	}

	@Override
	protected void doPost(Request req, Response rep) {
		super.doPost(req, rep);
	}

}
