package com.server;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Date;

/**
 * 
 * @author tan si xiang 
 */
public class Response {
	
	private StringBuilder context;
	private byte[] data;

	private OutputStream os;
	private PrintStream pStream ;

	private final static String URLF = "\r\n";
	private final static String BLANK = " ";
	private  int contextLen;


	public Response() {
		context = new StringBuilder();
		os = null;
		pStream = null;
	}

	public Response(OutputStream os) {
		this();
		this.os = os;
		pStream = new PrintStream(os);
	}


	public Response print(String msg)
	{
		context.append(msg);
		return this;
	}
	
	public Response println(String msg)
	{
		context.append(msg).append(URLF);
		return this;
	}
	
	public Response println(byte[] msg,int len)
	{
		data = msg;
		return this;
	}
	
	public  void pushtoClient()
	{
		if( data == null ){
			pStream.append(context.toString());
			pStream.flush();
		}
		else 
		{
			try {
				pStream.write(data);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void inTimePush(String msg){
		pStream.append(msg);
		pStream.flush();
	}
	
	public void close() throws IOException
	{
		pStream.close();
		if( os != null ){
			os.close();
		}
	}
}
