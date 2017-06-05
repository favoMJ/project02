package com.crawler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


/**
 *
 * <pre>
* Title: 网页抓取程序
* Description:
* 根据不同的网页结构 需要修改getElement方法实现解析
 * </pre>
 *
 */
public class CatchHtml {
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
	private String[] shiftWord;

	/**
	 * 测试入口
	 * 
	 * @param args
	 */
	private void init() {

		Map<String, String> proMap = readPropertieFile("catchHtml.properties");
		// 抓取站点url
		siteUrl = proMap.get("siteUrl");
		// 文字根url
		proName = proMap.get("proName");
		// 保存路径
		filePath = proMap.get("filePath");
		// 屏蔽关键字
		shiftWord = proMap.get("shiftWord").split("&");

	}

	/**
	 * start Running
	 * 
	 * @param name
	 */
	public void start(String name) {
		System.out.println(name);
		if(name != "" && name != null ){
			new SearchByName().setProName(name);
		}
		long startTime = System.currentTimeMillis();
		// 获取目录所在页面元素
		Document docum = getHtmlDoc(siteUrl + proName);
		// System.out.println(docum);
		// 解析目录所在页面元素 获取所有章节url
		Map<String, String> urlMap = getElement(docum);
		// System.out.println(urlMap.toString());
		// 通过所有章节url 获取每个章节内容并保存
		writeFile(siteUrl, filePath + name + ".txt", urlMap, "UTF-8", shiftWord, proName);
		long endTime = System.currentTimeMillis();
		System.out.println("The End ! totalSum:" + (totalSum - 1) + " failureSum:" + failureSum);
		System.out.println("run time: " + (endTime - startTime) / 6000 + "s");
	}

	/**
	 * 获取Html Document
	 * 
	 * @param url
	 */
	private Document getHtmlDoc(String url) {
		// System.out.println(url);
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

	// Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like
	// Gecko) Chrome/50.0.2661.102 Safari/537.36
	// Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:23.0) Gecko/20100101
	// Firefox/23.0
	// Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0;
	// BIDUBrowser 2.x)
	// Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like
	// Gecko) Chrome/26.0.1410.64 Safari/537.31
	/**
	 * 获取数据元素
	 * 
	 * @param doc
	 * @return Map<String,String> key 章节名 value url
	 */
	private Map<String, String> getElement(Document doc) {
		// System.out.println(doc);
		Element singerListDiv = doc.getElementsByAttributeValue("id", "at").first();
		// System.out.println(singerListDiv);
		// System.out.println();
		Elements links = singerListDiv.getElementsByTag("td");
		Map<String, String> emap = new LinkedHashMap<String, String>();

		for (Element link : links) {
			if (link.childNodeSize() > 0) {
				Element linkcs = link.child(0);
				String linkHref = linkcs.attr("href");
				// System.out.println(linkHref);
				String linkText = link.text().trim();
				// System.out.println(linkText + "##" + linkHref);
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
	private void writeFile(String siteUrl, String filePath, Map<String, String> emap, String encoding,
			String[] shiftWord, String proName) {
		try {
			File file = new java.io.File(filePath);
			if (!file.exists()) {
				file.createNewFile();
			}
			if (file.isFile() && file.exists()) {
				OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(file), encoding);
				BufferedWriter bufferedWriter = new BufferedWriter(write);
				// 遍历Map
				Set<String> eset = emap.keySet();
				Iterator<String> it = eset.iterator();
				String eName = "";
				String content = "";
				while (it.hasNext()) {
					eName = it.next();
					content = this.getContents(eName, siteUrl + proName + emap.get(eName), shiftWord);
					// System.out.println(content);
					bufferedWriter.write(eName);
					bufferedWriter.newLine();
					bufferedWriter.write(content);
					bufferedWriter.newLine();
					bufferedWriter.newLine();
				}
				bufferedWriter.flush();
				write.close();
			} else {
				System.out.println("file error");
			}
		} catch (Exception e) {
			System.out.println("writeFile error");
			e.printStackTrace();
		}
	}

	/**
	 * catch内容
	 * 
	 * @param conUrl
	 * @return
	 */
	private String getContents(String eName, String conUrl, String[] shiftWord) {
		if (conUrl == "" || conUrl == null)
			return "";
		
		String contents = this.getHtmlDoc(conUrl).getElementById("contents").text();
		
		// 屏蔽关键词
		for (String str : shiftWord) {
			contents = contents.replaceAll(str, "");
		}
		contents = contents.replaceAll("     ", "####");
		contents = contents.replaceAll("####", "\n    ");
//		contents = contents.replaceAll(" ","\n");
//		contents = contents.replaceAll("  ","\n");
		System.out.println(contents);
		
		//contents = contents.replaceAll("####","  \n");
		//System.out.println(contents);
		// 抓取内容长度大于100 即成功
		if (contents.length() > 100) {
			System.out.println("Succeed - " + totalSum + eName);
		} else {
			System.out.println("* Failure - " + totalSum + eName);
			failureSum++;
		}
		totalSum++;
		return contents;
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
			File file = new File(CatchHtml.class.getResource(filePath).getFile());
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
	public CatchHtml(){
		init();
	}
	
	public static void main(String[] args) {
		CatchHtml cat = new CatchHtml();
		cat.start("大主宰");
	}
	
	/**
	 * 
	 * @author tan si xiang
	 *
	 *
	 * 通过名字改变url 从而搜取对应书籍
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
			proName = st ;
			//System.out.println(proName);
		}

		/**
		 * 获取thml Document
		 * 
		 * @param name
		 * 			书籍名字
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
		 * 			搜索结果
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
