package com.cmcc.report.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

/**
 * <b>类名称：</b>FileUtil
 * <b>类描述：</b>文件Util
 * <b>创建人：</b>ChenSong
 * <b>修改人：</b>ChenSong
 * <b>修改时间：</b>2016-8-2 上午11:07:50
 * <b>修改备注：</b>
 * @version v1.0<br/>
 */
public class FileUtil {
	
	private static String path = AppPropertiesUtil.getValue("storagedir");

    // 重命名文件；
    public static File[] renameToFiles(String[] fileNames, File[] files) {
        File[] retFiles = null;
        if (fileNames != null && fileNames.length > 0) {
            retFiles = new File[fileNames.length];

            for (int i = 0, n = fileNames.length; i < n; i++) {
                File dist = new File(fileNames[i]);
                files[i].renameTo(dist);
                retFiles[i] = dist;
            }
        }
        return retFiles;
    }

    // save文件
    public static long saveFile(File file, String fileName, String filePath)
            throws Exception {
        if (file == null) {
            return 0;
        }

        File filepath = new File(filePath);
        if (!filepath.isDirectory())
            filepath.mkdirs();
        File filedesc = new File(filepath, fileName);

        return copyFile(file, filedesc);
    }

    // copy文件
    public static long copyFile(File fromFile, File toFile) {
        long len = 0;

        InputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(fromFile);
            out = new FileOutputStream(toFile);
            byte[] t = new byte[1024];
            int ii = 0;
            while ((ii = in.read(t)) > 0) {
                out.write(t, 0, ii);
                len += ii;
            }

        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

        return len;
    }

    // 验证文件正确;
    public static boolean verifyFile(String fileFix, String[] exts) throws Exception {
        boolean flag = false;
        for(String ext : exts){
            if(fileFix.equals(ext)){
                flag = true;
                break;
            }
        }
        return flag;
    }

    // 取得文件扩展;
    public static String getExtension(String fileName) {

        int newEnd = fileName.length();
        int i = fileName.lastIndexOf('.', newEnd);
        if (i != -1) {
            return fileName.substring(i + 1, newEnd).toLowerCase();
        } else {
            return null;
        }
    }


    /**
     * 传文件名称，返回一个输出流
     * @param fileName 需要上传的文件目录
     */
    public static PrintWriter uploadBossCountFile(String fileName){
        File file = null;
        file = new File(fileName);
        if(file.exists()==false){//如果文件不存在，则创建文件
            try{
                file.createNewFile();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(fileName);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        PrintWriter writer = null;
        writer = new PrintWriter(new OutputStreamWriter(fos));
        return writer;
    }

    public static boolean deleteFile(String filePath){
       boolean flag = false;
       File file = new File(filePath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }
    
  //文件路径+名称
    private static String filenameTemp;
    /**
     * 创建文件
     * @param fileName  文件名称
     * @param filecontent   文件内容
     * @return  是否创建成功，成功则返回true
     */
    public static boolean createFile(String fileName,String filecontent){
        Boolean bool = false;
        filenameTemp = path+File.separator+fileName+".txt";//文件路径+名称+文件类型
        File file = new File(filenameTemp);
        try {
            //如果文件不存在，则创建新的文件
            if(!file.exists()){
                file.createNewFile();
                bool = true;
                System.out.println("success create file,the file is "+filenameTemp);
                //创建文件成功后，写入内容到文件里
                writeFileContent(filenameTemp, filecontent);
            }else{//如果存在先删除再创建
            	file.delete();
            	file.createNewFile();
                bool = true;
                System.out.println("success create file,the file is "+filenameTemp);
                //创建文件成功后，写入内容到文件里
                writeFileContent(filenameTemp, filecontent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return bool;
    }
    
    /**
     * 向文件中写入内容
     * @param filepath 文件路径与名称
     * @param newstr  写入的内容
     * @return
     * @throws IOException
     */
    public static boolean writeFileContent(String filepath,String newstr) throws IOException{
        Boolean bool = false;
        String filein = newstr+"\r\n";//新写入的行，换行
        String temp  = "";
        
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        FileOutputStream fos  = null;
        PrintWriter pw = null;
        try {
            File file = new File(filepath);//文件路径(包括文件名称)
            //将文件读入输入流
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
            StringBuffer buffer = new StringBuffer();
            
            //文件原有内容
            for(int i=0;(temp =br.readLine())!=null;i++){
                buffer.append(temp);
                // 行与行之间的分隔符 相当于“\n”
                buffer = buffer.append(System.getProperty("line.separator"));
            }
            buffer.append(filein);
            
            fos = new FileOutputStream(file);
            pw = new PrintWriter(fos);
            pw.write(buffer.toString().toCharArray());
            pw.flush();
            bool = true;
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //不要忘记关闭
            if (pw != null) {
                pw.close();
            }
            if (fos != null) {
                fos.close();
            }
            if (br != null) {
                br.close();
            }
            if (isr != null) {
                isr.close();
            }
            if (fis != null) {
                fis.close();
            }
        }
        return bool;
    }
    
    /**
     * 功能：Java读取txt文件的内容
     * 步骤：1：先获得文件句柄
     * 2：获得文件句柄当做是输入一个字节码流，需要对这个输入流进行读取
     * 3：读取到输入流后，需要读取生成字节流
     * 4：一行一行的输出。readline()。
     * 备注：需要考虑的是异常情况
     * @param filePath
     */
    public static String readTxtFile(String fileName){
    	String content = "";
        try {
        	String filePath = path+File.separator+fileName+".txt";//文件路径+名称+文件类型
		    String encoding="GBK";
		    File file=new File(filePath);
		    if(file.exists() && file.isFile()){ //判断文件是否存在
		        InputStreamReader read = new InputStreamReader(
		        new FileInputStream(file),encoding);//考虑到编码格式
		        BufferedReader bufferedReader = new BufferedReader(read);
		        String lineTxt = null;
		        while((lineTxt = bufferedReader.readLine()) != null){
		        	content += lineTxt;
		            System.out.println(lineTxt);
		        }
		     read.close();
		    }else{
		    	System.out.println("找不到指定的文件");
		    }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
        return content;
    }
    
    /**
     * 导出下载
     * @param path
     * @param response
     */
    public static void download(String path, HttpServletResponse response) {
    	try {
            // path是指欲下载的文件的路径。  
            File file = new File(path);  
            // 取得文件名。  
            String filename = file.getName();  
            // 以流的形式下载文件。  
            InputStream fis = new BufferedInputStream(new FileInputStream(path));  
            byte[] buffer = new byte[fis.available()];  
            fis.read(buffer);  
            fis.close();  
            // 清空response  
            response.reset();  
            // 设置response的Header  
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));  
            response.addHeader("Content-Length", "" + file.length());  
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());  
            response.setContentType("application/vnd.ms-excel;charset=gb2312");  
            toClient.write(buffer);  
            toClient.flush();  
            toClient.close();  
        } catch (IOException ex) {  
            ex.printStackTrace();  
        }  
    }  

    public static void main(String[] args){
        String filePath = "c:/22.log";
        String uploadPath = "C:/down/20101203/100000000066503/";
        String fileName = "100000000066503-0.jpg";
        File aaa=new File(filePath);
        boolean ttt = aaa.exists();
        try {
            saveFile(new File(filePath),fileName,uploadPath);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
