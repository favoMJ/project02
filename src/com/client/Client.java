package com.client;

import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

import com.noteBook.Notebook;

public class Client {
	public  static void createInfo()
	{
		Socket client = null;
		try {
			client = new Socket("localhost",9527);
			PrintStream ps = new PrintStream(client.getOutputStream());
			ps.print("择天记");
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	public static void main(String[] args) {
		Socket client = null;
		try {
			client = new Socket("localhost",9527);
			PrintStream ps = new PrintStream(client.getOutputStream());
			ps.print("择天记");
			Scanner scan = new Scanner(client.getInputStream());
			new Notebook(client.getInputStream());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
