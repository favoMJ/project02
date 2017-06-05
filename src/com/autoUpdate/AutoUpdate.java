package com.autoUpdate;

import java.io.File;
import java.util.ArrayList;

import com.crawler.CatchHtml;

public class AutoUpdate {
	
	final static String filePath = "D://java//work//Project02//root//";
	static CatchHtml ch = new CatchHtml();
	public void updateInNight(){
		DateSearch ds = new DateSearch();
		if( ds.hour == 0 ){
			update();
		}
	}
	
//    public static void main(final String[] args){
//        walkR(Paths.get(pathSt));
//    }
//     
//    private static void walkR(Path dir){
//        walk(dir);
//        try {
//            Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {
//              
//                @Override
//                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
//                    if(attrs.isDirectory())
//                        walkR(file);
//                    return FileVisitResult.CONTINUE;
//                }
//            });
//        } catch(IOException e){
//            e.printStackTrace();
//        }
//    }
//     
//    private static void walk(Path dir){
//        try(DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*.txt")){
//            for(Path entry : stream){
//                System.out.println("---------------------------------------------------------");
//                System.out.println(entry);
//                for(String line : Files.readAllLines(entry, Charset.forName("UTF-8"))){
//                    System.out.println(line);
//                }
//            }
//        }catch(Exception xe){
//            System.err.println(xe);
//        }
//    }
	
//		 private static ArrayList<String> filelist = new ArrayList<String>();
//		 
//		 public static void main(String[] args) throws Exception {
//		    
//		    String filePath = "E://Struts2";
//		    getFiles(filePath);
//		 } 
		 /*
		  * 通过递归得到某一路径下所有的目录及其文件
		  */

	private static void update(){
		File root = new File(filePath);
		File[] files = root.listFiles();
		
		for(File f : files ){
			String fileName = f.getName();
			if( fileName.endsWith(".txt")){
				String st = fileName.replace(".txt", "");
				ch.start(st);
			}
		}
	}
	public static void main(String[] args) {
		update();
	}
}
