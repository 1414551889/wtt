package com.cmcc.report.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

/**
 * 上传文件
 *
 * @ClassName: UploadFile
 * @author
 * @date
 *
 */
public class UploadFileUtil {

	private static Logger log = LoggerFactory.getLogger( UploadFileUtil.class );
	private MultipartFile file;

	/**
	 *
	 * uploadFile(上传文件方法)
	 *
	 * @date
	 * @Title: uploadFile
	 * @param @param request 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public static List<Map<String, String>> uploadFile(HttpServletRequest request, String desPath, Boolean absolutePath) {

		PrintStream ps=null;// 创建文件输出流1
		PrintStream out = System.out;// 保存原输出流
		try {
			ps=new PrintStream(request.getSession().getServletContext().getContextPath()+"/log.txt");// 创建文件输出流1
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		List<Map<String, String>> mapList=new ArrayList<>();
		ServletContext servletContext = request.getSession().getServletContext();
		// 创建一个通用的多部分解析器
		CommonsMultipartResolver resolver = new CommonsMultipartResolver( servletContext );
		// 判断 request 是否有文件上传,即多部分请求
		if( resolver.isMultipart( request ) ) {
			// 转换成多部分request
			MultipartHttpServletRequest multiRequest = ( MultipartHttpServletRequest )request;
			// 取得request中的所有文件名
			Iterator<String> iter = multiRequest.getFileNames();
			String os = System.getProperty("os.name");
			while( iter.hasNext() ) {
//注释
				String pathTemp=desPath;
				Map<String, String> fileNameMap = new HashMap<String, String>();
				// 取得上传文件
				MultipartFile file = multiRequest.getFile( iter.next() );
				if( file != null ) {
					// 文件名称
					String fileName = file.getOriginalFilename();
					String fileSize = String.valueOf( file.getSize() );
					String keyname = file.getName();
					// 定义上传路径
					ServletContext sc = request.getSession().getServletContext();
					// 生成文件存放目录
					String ctxDir = sc.getRealPath( String.valueOf( File.separatorChar ) );
					if( !ctxDir.endsWith( String.valueOf( File.separatorChar ) ) ) {
						ctxDir = ctxDir + File.separatorChar;
					}

					if(!os.toLowerCase().startsWith("win")){
						//如果是linux系统
						desPath="image"+String.valueOf( File.separatorChar );
						pathTemp = desPath;
						ctxDir=String.valueOf( File.separatorChar )+"usr"+String.valueOf( File.separatorChar )+"local"+String.valueOf( File.separatorChar )+"tomcat7"+String.valueOf( File.separatorChar )+"webapps"+String.valueOf( File.separatorChar );
					}
					fileNameMap.put( "filePath", ctxDir + desPath );
					if(StringUtil.isEmpty(fileName)){
						mapList.add(fileNameMap);
						continue;
					}
					fileName=String.valueOf(System.currentTimeMillis())+fileName;
					if( desPath.endsWith( String.valueOf( File.separatorChar ) ) ) {
						pathTemp = pathTemp + fileName;
					}
					log.info( pathTemp + "=" + file.getSize() / 1024 );
					File savePath = new File( ctxDir + pathTemp );
					if(absolutePath){
						savePath = new File( pathTemp );
					}else{
						savePath = new File( ctxDir + pathTemp );
					}
					if( !savePath.exists() ) {
						savePath.mkdirs();
					}
					try {
						file.transferTo( savePath );
						System.out.println("文件上传完"+savePath);
						log.error("文件上传完"+savePath);
						fileNameMap.put( "fileSize", fileSize );
						fileNameMap.put( "fileName", fileName );
						fileNameMap.put( "absolutePath", savePath.getPath() );
						fileNameMap.put("originalName", file.getOriginalFilename());
						fileNameMap.put("keyname", keyname);
						mapList.add(fileNameMap);

					} catch( IOException e ) {
						log.error( "文件上传失败", e );
					}
				}
			}
		}
		return mapList;
	}

	public static Map<String,InputStream> uploadFile(HttpServletRequest request){
		ServletContext servletContext = request.getSession().getServletContext();
		// 创建一个通用的多部分解析器
		CommonsMultipartResolver resolver = new CommonsMultipartResolver( servletContext );
		Map<String,InputStream> fileNameMap = new HashMap<>();
		// 判断 request 是否有文件上传,即多部分请求
		if( resolver.isMultipart( request ) ) {
			MultipartHttpServletRequest multiRequest = ( MultipartHttpServletRequest )request;
			Iterator<String> iter = multiRequest.getFileNames();
			// 取得上传文件
			try {
				while( iter.hasNext() ) {
					MultipartFile file = multiRequest.getFile( iter.next() );
					InputStream inputStream = file.getInputStream();
					fileNameMap.put( file.getOriginalFilename(),inputStream );
				}
			} catch( IOException e ) {
				e.printStackTrace();
			}
		}
		return fileNameMap;
	}
}
