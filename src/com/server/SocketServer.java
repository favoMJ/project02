package com.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class SocketServer {
	private ServerSocket server;
	private boolean isdone;
	public static void main(String[] args) {
		SocketServer socketServer  = new SocketServer();
		socketServer.start();
	}
	public  void start()
	{
		isdone = false;
		start(9527);
	}
	private void start(int port)
	{
		try {
			server = new ServerSocket(port);
			receive();
		} catch (IOException e) {
		}
	}

	private void receive()
	{
		try {
			while(!isdone){
			Socket socket = server.accept();
			Thread t = new Thread(new Disaptcher(socket));
			t.setDaemon(true);
			t.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
			isdone = true;
		}
	}
}
