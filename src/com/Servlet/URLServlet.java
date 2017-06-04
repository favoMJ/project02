package com.Servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

import com.crawler.CatchHtml;
import com.server.Request;
import com.server.Response;


public class URLServlet extends Servlet{

	@Override
	protected void doGet(Request req, Response rep) {
		String str = req.getUrl();
		String newUrl = System.getProperty("user.dir") + "\\" + "root" + "\\" + str + ".txt";
		
		check(newUrl,str);
		
		
		System.out.println(newUrl); 
		Scanner scan = null;
		try {
				scan = new Scanner(new FileInputStream(newUrl));
				while( scan.hasNext() ){
					String st = scan.nextLine();
					rep.println(st);
				}
				
		} catch (FileNotFoundException e) {
			System.out.println("URLServlet file error");
		} catch (IOException e) {
		}
		
	}

	private void check(String newUrl,String name){
		File file = new File(newUrl);
		if( file.exists() ){
			return ;
		}
		CatchHtml cat = new CatchHtml();
		cat.start(name);
	}
	
	@Override
	protected void doPost(Request req, Response rep) {
		super.doPost(req, rep);
	}

}
