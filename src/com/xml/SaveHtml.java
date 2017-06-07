package com.xml;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class SaveHtml {
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
	private void init() {

		Map<String, String> proMap = readPropertieFile("saveHtml.properties");
		// 抓取站点url
		siteUrl = proMap.get("siteUrl");
		// 文字根url
		proName = proMap.get("proName");
		// 保存路径
		filePath = proMap.get("filePath");

		xmlPath = proMap.get("xmlPath");
		// 屏蔽关键字
		shiftWord = proMap.get("shiftWord").split("&");

	}
	//-----------------------------------------update------------------------
	/**
	 * 如果XML 中存在该小说，更新
	 * 
	 * @param novelName
	 *            小说名字
	 * @param node
	 *            小说在在Node
	 */
	private void updateXML(String novelName) {
		novelXml = new NovelXml();
		novelXml.updateXML(novelName, urlMap , siteUrl + proName);
		novelXml.createXml(filePath + "novel.xml");
	}
	//--------------------------------------------create-----------------------
	/**
	 * 如果没有XML，则创建该XML ,把该小说放入 如果存在XML，但该小说不存在，把该小说放入
	 * 
	 * @param novelName
	 */
	private void createXML(String novelName) {
		try {
			novelXml.createNovel(novelName);
			writeXML(siteUrl, urlMap, "UTF-8", proName, novelXml);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * 判断是否在xml 中存在该本小说,如果有返回该小说存在的节点
	 * 
	 * @param novelName
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
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
	public void start(String novelName) {
		if (novelName != "" && novelName != null) {
			new SearchByName().setProName(novelName);
		}
		try {
			 novelXml = new NovelXml();
			 Document docum = getHtmlDoc(siteUrl + proName);
			 urlMap = getElement(docum);

			if (checkNovelExist(novelName)) {
				 updateXML(novelName);
			} else {
				 createXML(novelName);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 获取Html Document
	 * 
	 * @param url
	 */
	private Document getHtmlDoc(String url) {
		Document doc = null;
		try {
			doc = Jsoup.connect(url).timeout(6000000)
					.userAgent(
							"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36")
					.get();
		} catch (IOException e) {
			System.out.println("!!" + url);
			e.printStackTrace();
		}
		return doc;
	}

	/**
	 * 获取数据元素
	 * 
	 * @param doc
	 * @return Map<String,String> key 章节名 value url
	 */
	private Map<String, String> getElement(Document doc) {
		Element singerListDiv = doc.getElementsByAttributeValue("id", "at").first();
		Elements links = singerListDiv.getElementsByTag("td");
		Map<String, String> emap = new LinkedHashMap<String, String>();

		for (Element link : links) {
			if (link.childNodeSize() > 0) {
				Element linkcs = link.child(0);
				String linkHref = linkcs.attr("href");
				String linkText = link.text().trim();
				emap.put(linkText, linkHref);

			}
		}
		System.out.println("url total：" + emap.size());
		return emap;
	}

	/**
	 * 文件缓存
	 * 
	 * @param filePath
	 *            路径
	 * @param emap
	 *            写入内容
	 * @param encoding
	 *            文件编码
	 * @param shiftWord
	 *            屏蔽关键字
	 */
	private void writeXML(String siteUrl, Map<String, String> emap, String encoding, String proName, NovelXml nc) {
		Set<String> eset = emap.keySet();
		Iterator<String> it = eset.iterator();
		String eName = "";
		while (it.hasNext()) {
			eName = it.next();
			nc.createNovelNode(eName.trim(), emap.get(eName));
		}
		novelXml.createXml(filePath + "novel.xml");
	}

	/**
	 * read Propertie File by IO
	 * 
	 * @param filePath
	 * @return
	 */
	private Map<String, String> readPropertieFile(String filePath) {
		Map<String, String> retData = new HashMap<String, String>();
		try {
			String encoding = "UTF-8";
			File file = new File(SaveHtml.class.getResource(filePath).getFile());
			if (file.isFile() && file.exists()) {
				InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);//
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					int _index = lineTxt.indexOf("=");
					if (_index > 0) {
						retData.put(lineTxt.substring(0, _index), lineTxt.substring(_index + 1));
					}
				}
				read.close();
			} else {
				System.out.println("Can't find PropertieFile !");
			}
		} catch (Exception e) {
			System.out.println("readPropertieFile error");
			e.printStackTrace();
		}
		return retData;
	}

	public SaveHtml() {
		init();
	}

	public static void main(String[] args) {
		SaveHtml cat = new SaveHtml();
		cat.start("教练万岁");
	}

	/**
	 * 
	 * @author tan si xiang
	 *
	 *
	 *         通过名字改变url 从而搜取对应书籍
	 */
	class SearchByName {

		/**
		 * set proName
		 * 
		 * @param name
		 */
		public void setProName(String name) {
			Document doc = getDoc(name);
			String url = getBookURL(doc);
			String st = (String) url.subSequence(20, url.length());
			proName = st;
			// System.out.println(proName);
		}

		/**
		 * 获取thml Document
		 * 
		 * @param name
		 *            书籍名字
		 * @return
		 */

		private Document getDoc(String name) {
			String url = getSearchURL(name);
			Document doc = null;
			try {
				doc = Jsoup.connect(url).timeout(6000000)
						.userAgent(
								"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36")
						.get();
			} catch (IOException e) {
				System.out.println(url);
				System.out.println("error code: 503");
			}
			return doc;
		}

		/**
		 * get match book's url
		 * 
		 * @param doc
		 *            搜索结果
		 * @return
		 */
		private String getBookURL(Document doc) {
			String bookUrl = "";
			Element singerListDiv = doc.getElementsByAttributeValue("class", "result-game-item-pic").first();

			if (singerListDiv.childNodeSize() > 0) {
				Element linkcs = singerListDiv.child(0);
				bookUrl = linkcs.attr("href");
			}
			return bookUrl;
		}

		/**
		 * 
		 * encode and fill the information
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
				url = "http://zhannei.baidu.com/cse/search?q=" + enCode
						+ "&click=1&entry=1&s=8253726671271885340&nsid=";

			} catch (UnsupportedEncodingException e) {
				System.out.println("encode error");
				e.printStackTrace();
			}
			return url;
		}
	}
}
