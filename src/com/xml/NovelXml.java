package com.xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class NovelXml implements XmlInterface {
	private Document document;
	private Element root;
	private Element novel;
	private String xmlPath = "";
	private Map<String, String> hsMap;

	// --------------------------------------init----------------------------------------
	private void getXmlPath() {

		try {
			Properties prop = new Properties();
			InputStream in = getClass().getResourceAsStream("saveHtml.properties");
			prop.load(in);
			xmlPath = prop.getProperty("xmlPath");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void init() {
		try {
			getXmlPath();

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			File file = new File(xmlPath);

			// 没有则创建一个新的doucment
			if (!file.exists()) {
				this.document = builder.newDocument();
				root = this.document.createElement("root");
				this.document.appendChild(root);
			} else {
			
				this.document = builder.parse(xmlPath);
				// 1.获得文档根元素对对象;
				root = this.document.getDocumentElement();
			}

		} catch (Exception e) {
			System.out.println("init error");
		}
	}

	public NovelXml() {
		init();
	}

	// -----------------------------------create----------------------------------------------
	// 创建一个书节点
	private Element createNovel(String novelName, Element root) {
		Element novel = this.document.createElement("novel");
		novel.setAttribute("novelName", novelName);
		root.appendChild(novel);
		return novel;
	}

	public void createNovel(String novelName) {
		this.novel = createNovel(novelName, root);
	}

	// 创建树节点下面的目录节点
	private void createNovelNode(String eName, String content, Element novel) {
		String st = eName.split(" ")[0];//因为汉字和空格等错误，所以取前面
		if (!st.startsWith("第"))
			return;
		System.out.println("success a charpter");
		try {
			Element dir = this.document.createElement("content");
			dir.setAttribute("chapter", st);
			dir.appendChild(this.document.createTextNode(content));
			novel.appendChild(dir);
		} catch (Exception e) {
			System.out.println("createNovelNodel" + st + "error");
		}
	}

	public void createNovelNode(String eName, String content) {
		createNovelNode(eName, content, novel);
	}

	// ---------------------------------------------------------------------------------

	public Element getRoot() {
		return root;
	}
	
	/**
	 * 
	 * @param novelName
	 * @param emap 
	 * 			url 和 章节对应
	 * @param pro
	 * 			url 前缀
	 */
	public void updateXML(String novelName, Map<String, String> emap,String pro) {
		
		hsMap = new HashMap<String, String>();//用来保存已经存在的章节
		NodeList novels = root.getElementsByTagName("novel");
		
		Node novel = null;
		for (int i = 0; i < novels.getLength(); ++i) {
			novel = novels.item(i);
			String name = novel.getAttributes().getNamedItem("novelName").getNodeValue();
			if (name.equals(novelName)) {
				NodeList chapters = novel.getChildNodes();
				for (int j = 0; j < chapters.getLength(); ++j) {
					Node chapte = chapters.item(j);
					NamedNodeMap atr = chapte.getAttributes();
					if( atr != null ){
						String st = atr.getNamedItem("chapter").getNodeValue();
						hsMap.put(st, novelName);
					}
				}
			}
		}
		Set<String> eset = emap.keySet();
		Iterator<String> it = eset.iterator();
		String eName = "";
		while (it.hasNext()) {//该章节不存在
			eName = it.next();
			String st = eName.split(" ")[0].trim();
			if( !hsMap.containsKey(st) ){
				
				createNovelNode(eName,pro+emap.get(eName),novel);
			}
				
		}
	}

	// ----------------------------------write------------------------------------
	public void createXml(String fileName) {
		try {
			
			// 生成 transformer
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();

			// 获得初始节点
			DOMSource source = new DOMSource(document);

			transformer.setOutputProperty(OutputKeys.ENCODING, "utf8");// 字符设置
			// indent = "yes" | "no". indent 指定了当输出结果树时，Transformer 是否可以添加额外的空白；
			// 其值必须为 yes 或 no。
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");

			PrintWriter pw = new PrintWriter(new FileOutputStream(fileName));
			StreamResult result = new StreamResult(pw);// 转换结果的持有者
			
			transformer.transform(source, result);// 将 XML Source 转换为 Result。

		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		} catch (TransformerException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void parserXml(String fileName) {
		
	}

	public static void main(String[] args) {
		SaveHtml cat = new SaveHtml();
		cat.start("教练万岁");
	}

	public void createNovelNode(String eName, String content, Node novel) {
		// TODO Auto-generated method stub
		String st = eName.split(" ")[0];
		if (!st.startsWith("第"))
			return;
		System.out.println("success a charpter");
		try {
			Element dir = this.document.createElement("content");
			dir.setAttribute("chapter", st);
			dir.appendChild(this.document.createTextNode(content));
			novel.appendChild(dir);
		} catch (Exception e) {
			System.out.println("createNovelNodel" + st + "error");
		}
	}
}
