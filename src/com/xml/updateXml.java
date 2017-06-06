package com.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;


import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;

public class updateXml {

	public void init(String name)
	{
	    try {  
	    	SAXReader saxReader = new SAXReader();
	        // 2.得到Document
	        Document document = saxReader.read("xmlDemo.xml");
	        // 3.获取根节点
	        Element root = document.getRootElement();
	        // 4.获取anime节点
	        List<Element> elements = root.elements("nolve");
	        for(int i = 0 ; i < elements.size() ; ++i){
	        	Element novel = elements.get(i);
	        	
	        	//novel.add(arg0);
	        }
	        
	        
        } catch(Exception e) {  
            throw new RuntimeException(e);  
        }  
	}
}
