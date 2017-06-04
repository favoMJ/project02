package com.search;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SearchByName {

	private String siteUrl;
	// 文字根url
	private String proName;
	// 保存路径
	private String filePath;
	private String[] shiftWord;

	/**
	 * 获取thml Document
	 * 
	 * @param name
	 * @return
	 */
	private Document getDoc(String name) {
		String url = getSearchURL(name);
		 System.out.println(url);
		Document doc = null;
		try {
			doc = Jsoup.connect(url).timeout(600000)
					.userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36")
					.get();
		} catch (IOException e) {
			System.out.println("search error");
			e.printStackTrace();
		}
		return doc;
	}

	public void getElement(String name) {
		Document doc = getDoc(name);
		String url = getBookURL(doc);

	}

	private String getBookURL(Document doc) {
		String bookUrl = "";
		Element singerListDiv = doc.getElementsByAttributeValue("class", "result-game-item-pic").first();
		//System.out.println(singerListDiv);
		// System.out.println(singerListDiv);
		// System.out.println();
		if (singerListDiv.childNodeSize() > 0) {
			Element linkcs = singerListDiv.child(0);
			System.out.println(linkcs);
			String linkHref = linkcs.attr("href");
			System.out.println(linkHref);
			String linkText = singerListDiv.text().trim();
			// System.out.println(linkText + "##" + linkHref);
		}
		return bookUrl;
	}

	/**
	 * 
	 * get url
	 * 
	 * @param name
	 * @return
	 */
	// http://zhannei.baidu.com/cse/search?q=%E6%8B%A9%E5%A4%A9%E8%AE%B0&click=1&entry=1&s=8253726671271885340&nsid=
	private String getSearchURL(String name) {
		String ENCODE = "iso-8859-1";
		String url = "";
		try {
			String enCode = new String(java.net.URLEncoder.encode(name, "utf-8").getBytes());
			url = "http://zhannei.baidu.com/cse/search?q=" + enCode + "&click=1&entry=1&s=8253726671271885340&nsid=";
			// System.out.println(url);

		} catch (UnsupportedEncodingException e) {
			System.out.println("encode error");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return url;
	}

	public static void main(String[] args) {
		new SearchByName().getElement("择天记");
	}
}
