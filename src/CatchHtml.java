import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.jsoup.Connection;
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

	/**
	 * 测试入口
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		CatchHtml ch = new CatchHtml();
		Map<String, String> proMap = ch.readPropertieFile("catchHtml.properties");
		// 抓取站点url
		String siteUrl = proMap.get("siteUrl");
		// 文字根url
		String proName = proMap.get("proName");
		// 保存路径
		String filePath = proMap.get("filePath");
		//System.out.println(proMap.get("shiftWord"));
		// 屏蔽关键字
		String[] shiftWord = proMap.get("shiftWord").split("&");
		// 获取目录所在页面元素
		Document docum = ch.getHtmlDoc(siteUrl + proName);
		//System.out.println(docum);
		// 解析目录所在页面元素 获取所有章节url
		Map<String, String> urlMap = ch.getElement(docum);
		//System.out.println(urlMap.toString());
		// 通过所有章节url 获取每个章节内容并保存
		ch.writeFile(siteUrl, filePath, urlMap, "UTF-8", shiftWord,proName);
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
		System.out.println(url);
		Document doc = null;
		try {
		//	 Connection conn = Jsoup.connect(url);
		//	 conn.header("User-Agent", userAgent);
			doc = Jsoup.connect(url).timeout(6000000)
					.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31")
					.get();
		} catch (IOException e) {
			System.out.println("!!" + url);
			e.printStackTrace();
		}
		return doc;
	}
	//Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:23.0) Gecko/20100101 Firefox/23.0
	//Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0; BIDUBrowser 2.x)
	//Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31
	/**
	 * 获取数据元素
	 * 
	 * @param doc
	 * @return Map<String,String> key 章节名 value url
	 */
	private Map<String, String> getElement(Document doc) {
		//System.out.println(doc);
		Element singerListDiv = doc.getElementsByAttributeValue("id", "at").first();
		//System.out.println(singerListDiv);
		//System.out.println();
		Elements links = singerListDiv.getElementsByTag("td");
		Map<String, String> emap = new LinkedHashMap<String, String>();

		for (Element link : links) {
			if (link.childNodeSize() > 0) {
				Element linkcs = link.child(0);
				String linkHref = linkcs.attr("href");
				//System.out.println(linkHref);
				String linkText = link.text().trim();
				//System.out.println(linkText + "##" + linkHref);
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
			String[] shiftWord,String proName) {
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
					content = this.getContents(eName, siteUrl + proName  + emap.get(eName), shiftWord);
			//		System.out.println(content);
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
		if( conUrl == "" || conUrl == null )return "";
		String contents = this.getHtmlDoc(conUrl).getElementById("contents").text();
		// 屏蔽关键词
		for (String str : shiftWord) {
			contents = contents.replaceAll(str, "");
		}
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
}
