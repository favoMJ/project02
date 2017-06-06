package com.xml;
public class Main {

	public static void main(String args[]){
		NovelXml dd=new NovelXml();
		String str="E:/grade.xml";
		dd.init();
		dd.createXml(str);    //创建xml
		dd.parserXml(str);    //读取xml
	}
}