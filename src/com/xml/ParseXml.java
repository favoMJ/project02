package com.xml;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
 
	;

public class ParseXml {
	/**
	 * 抓取总数
	 */
	private static int totalSum = 1;
	/**
	 * 获取失败记录数
	 */
	private static int failureSum = 0;
	// 抓取站点url
	private String siteUrl = "";
	// 文字根url
	private String proName = "";
	// 保存路径
	private String filePath = "";
	private String xmlPath = "";
	private String[] shiftWord;

	// 章节，url 对应
	private Map<String, String> urlMap;
	// 章节，小说名对应
	private Map<String, String> chapterMap;
	NovelXml novelXml;
	
	
	/**
	 * 测试入口
	 * 
	 * @param args
	 */
	private void getUrlMap(String novelName) {

		org.w3c.dom.Element root = novelXml.getRoot();
		NodeList novels = root.getElementsByTagName("novel");
		for (int i = 0; i < novels.getLength(); ++i) {
			Node novel = novels.item(i);
			String st = novel.getAttributes().getNamedItem("novelName").getNodeValue();
			if (st.equals(novelName))
			{
				NodeList contents = novel.getChildNodes();
				for(int k = 0 ; k < contents.getLength() ; ++k)
				{
					Node content =  contents.item(k);
					if( content.hasAttributes() )
					{
						String key = content.getAttributes().getNamedItem("chapter").getNodeValue();
						String value = content.getTextContent();
					//	System.out.println(key + value);
						urlMap.put(key, value);
					}		
				}
				
			}
		}

	}


	Boolean checkNovelExist(String novelName) {
		String pt = filePath + "novel.xml";
		if (!(new File(pt).exists()))
			return false;
		org.w3c.dom.Element root = novelXml.getRoot();
		NodeList novels = root.getElementsByTagName("novel");
		for (int i = 0; i < novels.getLength(); ++i) {
			NamedNodeMap novel = novels.item(i).getAttributes();
			String st = novel.getNamedItem("novelName").getNodeValue();
			if (st.equals(novelName))
				return true;
		}
		return false;
	}

	/**
	 * 通过判断XMl存在 和 包含小说情况去调用函数
	 * 
	 * @param name
	 */
	public Map start(String novelName) {
		try {
			 novelXml = new NovelXml();
			if (!checkNovelExist(novelName)) {
				 new SaveHtml().start(novelName);
			}
			getUrlMap(novelName);
			return urlMap;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}



	/**
	 * read Propertie File by IO
	 * 
	 * @param filePath
	 * @return
	 */

	public ParseXml() {
		urlMap = new HashMap();
	}

	public static void main(String[] args) {
		ParseXml cat = new ParseXml();
		cat.start("教练万岁");
	}


}
