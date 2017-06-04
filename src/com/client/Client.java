package com.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;


import com.noteBook.Notebook;

public class Client {
	public  static void createInfo(String name)
	{
		if( name == "" || name == null ){
			return;
		}
		Socket client = null;
		try {
			client = new Socket("localhost",9527);
			PrintStream ps = new PrintStream(client.getOutputStream());
			ps.print(name);
			
			Scanner scan = new Scanner(client.getInputStream());
			//if(client.getInputStream().available() < 1){};
			new Notebook(client.getInputStream());
		} catch (Exception e) {
			System.out.println("createInfo error");
		}
	}
	
	JFrame frame = new JFrame("begin");
	JButton jb = new JButton("搜索");
	JTextField area = new JTextField();
	public Client() throws Exception
	{
		org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();

		// Set windows border type.
		BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.translucencyAppleLike;
		BeautyEyeLNFHelper.translucencyAtFrameInactive = true;
		// BeautyEyeLNFHelper.activeCaptionTextColor = new Color(15,233,55);

		// Hide setting button.
		UIManager.put("RootPane.setupButtonVisible", false);
		JPanel jp = new JPanel();
		JPanel jPanel = new JPanel();
		JLabel jLable = new JLabel();
		jLable.setText("输入小说名称");
		
		
		jp.add(jb,BorderLayout.CENTER);
		area.setColumns(10);
		
		jPanel.add(jLable);
		jPanel.add(area,BorderLayout.CENTER);
		frame.add(jPanel,BorderLayout.NORTH);
		frame.add(jp,BorderLayout.SOUTH);
		
		jb.addActionListener(new SearchAction());
		
	}
	
	void init(){
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setSize(d.width/4, d.height/5);
		frame.setLocation(200, 300);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension b = frame.getSize();
	}
	
	class SearchAction implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			String name = area.getText();
			area.setText("");
			createInfo(name);
		}
		
	}
	
	public static void main(String[] args) throws Exception {
		new Client().init();
	}
}
