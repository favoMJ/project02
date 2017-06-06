package com.xml;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlImpl implements XmlInterface{
	private Document document;

	public void init() {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			this.document = builder.newDocument();
			
		} catch (ParserConfigurationException e) {
			System.out.println(e.getMessage());
		}
	}

	public void createXml(String fileName) {
		Element root = this.document.createElement("scores"); //创建一个根节点
		
		this.document.appendChild(root); //创建根节点
		
		System.out.println(this.document);
		
		Element employee = this.document.createElement("employee"); //子节点
		
		//----------------------创建employeee节点---------------------------------//
		Element name = this.document.createElement("name"); //
		name.appendChild(this.document.createTextNode("wangchenyang")); 
		employee.appendChild(name); 
		
		Element sex = this.document.createElement("sex"); 
		sex.appendChild(this.document.createTextNode("m")); 
		employee.appendChild(sex); 
		
		Element age = this.document.createElement("age"); 
		age.appendChild(this.document.createTextNode("26")); 
		employee.appendChild(age); 
		
		//添加至root节点
		root.appendChild(employee); 
		
		
		try {
			//生成 transformer
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			
			//获得初始节点
			DOMSource source = new DOMSource(document);
			transformer.setOutputProperty(OutputKeys.ENCODING, "utf8");//字符设置
			//indent = "yes" | "no". indent 指定了当输出结果树时，Transformer 是否可以添加额外的空白；
			//其值必须为 yes 或 no。
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			
			
			PrintWriter pw = new PrintWriter(new FileOutputStream(fileName));
			StreamResult result = new StreamResult(pw);//转换结果的持有者
			transformer.transform(source, result);//  将 XML Source 转换为 Result。
			
			//System.out.println("生成XML文件成功!");
		} catch (TransformerConfigurationException e) {
			System.out.println(e.getMessage());
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (TransformerException e) {
			System.out.println(e.getMessage());
		}
	}

	public void parserXml(String fileName) {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document document = db.parse(fileName);
			
			NodeList employees = document.getChildNodes();//获得根节点
			for (int i = 0; i < employees.getLength(); i++) {
				Node employee = employees.item(i);//获得employee节点
				NodeList employeeInfo = employee.getChildNodes();//获得他的一系列 content
				for (int j = 0; j < employeeInfo.getLength(); j++) {//遍历contents
					Node node = employeeInfo.item(j);
					NodeList employeeMeta = node.getChildNodes();
					for (int k = 0; k < employeeMeta.getLength(); k++) {
						System.out.println(employeeMeta.item(k).getNodeName()
								+ ":" + employeeMeta.item(k).getTextContent());
					}
				}
			}
			System.out.println("解析完毕");
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (ParserConfigurationException e) {
			System.out.println(e.getMessage());
		} catch (SAXException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	public static void main(String[] args) {
		XmlImpl dd=new XmlImpl();
		String str="D://java//work//Project02//root// ";
		dd.init();
	
		dd.parserXml(str);    //读取xml
	}
}