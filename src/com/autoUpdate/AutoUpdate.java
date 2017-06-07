package com.autoUpdate;

import java.io.File;
import java.util.ArrayList;

import com.crawler.CatchHtml;

public class AutoUpdate {
	
	final static String filePath = "D://java//work//Project02//root//";
	static CatchHtml ch = new CatchHtml();
	public void updateInNight(){
		DateSearch ds = new DateSearch();
		if( ds.hour == 0 ){
			update();
		}
	}

	private static void update(){
		File root = new File(filePath);
		File[] files = root.listFiles();
		
		for(File f : files ){
			String fileName = f.getName();
			if( fileName.endsWith(".txt")){
				String st = fileName.replace(".txt", "");
				ch.start(st);
				System.out.println(st+"更新成功");
			}
		}
	}
	public static void main(String[] args) {
		update();
	}
}
