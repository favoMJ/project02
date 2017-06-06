package com.noteBook;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.Timer;
import javax.swing.UIManager;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

import com.JDBC.PositionSave;

public class Notebook2 {

	private JFrame frame;
	private JTextArea textArea;
	private JMenuBar menuBar;
	private JMenu file, format, search, help,mark;
	private JMenuItem open, exit;
	private JMenuItem font, color, background, speed;
	private JMenuItem find, changeto;
	private JMenuItem helps, about;
	private JMenuItem setMark, openMark;
	private Boolean autoLineWrap = true;
	private String fileName = "未命名";// 文件名

	// private File currentFile ;
	private JScrollPane jsp;
	private JScrollBar jsb;


	int delay = 10;
	
	//private JDBC
	PositionSave ps = new PositionSave();
	
	
	
	//auto increment
	
	//
	public Notebook2(InputStream is) throws Exception
	{
		this();
		frameInit();
	}
	

	
	public Notebook2() throws Exception {
		
		
		frame = new JFrame();
		frame.setTitle("未命名");
		frame.setLayout(new BorderLayout());

		textArea = new JTextArea();
		textArea.setFont(new Font("宋体", Font.PLAIN, 18));// 设置默认字体样式字号

	
		textArea.setText("");
		textArea.setEditable(false);
	

		
	

		//frame.add(jsp, BorderLayout.CENTER);

		// 菜单
		menuBar = new JMenuBar();
		file = new JMenu("文件");
		file.setMnemonic('F');// 设置快捷键
		format = new JMenu("格式");
		search = new JMenu("搜索");
		help = new JMenu("帮助");
		mark = new JMenu("书签"); 
		
		// 文件菜单项
		open = new JMenuItem("打开...", KeyEvent.VK_O);
		exit = new JMenuItem("退出");
		file.add(open);
		file.add(exit);

		
	
		// 设置菜单条

		menuBar.add(file);

		frame.setJMenuBar(menuBar);

		// sped///

		//sped = new Speed(jsb, timer);
		
		// 调用监听方法

		addEventHandler();


		// 监听上下键//

		textArea.addKeyListener(new KeyListener() {

			public void keyPressed(KeyEvent arg0) {
				System.out.println(jsb.getValue());
				if (arg0.getKeyCode() == KeyEvent.VK_LEFT) {
					jsb.setValue(3333);
				}
				if (arg0.getKeyCode() == KeyEvent.VK_UP) {
					jsb.setValue(jsb.getValue() - jsb.getUnitIncrement());
				}

				if (arg0.getKeyCode() == KeyEvent.VK_DOWN) {
					jsb.setValue(jsb.getValue() + jsb.getUnitIncrement());
				}
		
			}
			public void keyReleased(KeyEvent arg0) {
			}

			public void keyTyped(KeyEvent arg0) {

			}
		});

		//

		
	}

	// /////////初始化frame////////////////

	protected void frameInit() {

		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setSize(d.width, d.height);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension b = frame.getSize();
	}

	private void addEventHandler() {

		// 文件项的监听
		open.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fileOpen();
			}
		}
		);

		
	}

	// ////////文件项的方法/////////////////////////

	private void fileOpen() {

		textArea.setText("");
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.showOpenDialog(frame);
		File file = fileChooser.getSelectedFile();

		if (file == null)
			return;
			
		fileName = file.getName();// 获得文件名
		frame.setTitle(file.getAbsolutePath());
		System.out.println(fileName);
		int st = ps.search(fileName);
	
		FileInputStream fis = null;
		BufferedReader br = null;

		try {
			fis = new FileInputStream(file);
			br = new BufferedReader(new InputStreamReader(fis));
			String str = null;
			while ((str = br.readLine()) != null) {
				textArea.append(str + "\n");
			}		
			
			textArea.setCaretPosition(0);
			
			jsp = new JScrollPane(textArea);// set Scroll
			frame.add(jsp, BorderLayout.CENTER);
			frame.invalidate();
			frame.validate();
			frame.repaint();
			jsb = jsp.getVerticalScrollBar();
			System.out.println(jsb);
			jsb.setMaximum(10000);
			jsb.setValue(99);
			System.out.println(jsb.getValue());
			System.out.println(jsb);
			
		} catch (IOException e1) { 
			JOptionPane.showMessageDialog(frame, "文件不存在或已被损坏");
		} finally {
			if (br != null)
				try {
					br.close();
				} catch (IOException e) {
				}

			if (fis != null)
				try {
					fis.close();
				} catch (IOException e) {
				}
		}

	}



	// ////////////////////////////////////////////////////////////

	public static void main(String args[]) throws Exception {

		Notebook2 nt = new Notebook2();
		
		nt.frameInit();

	}

}



	

