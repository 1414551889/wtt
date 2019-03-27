package com.cmcc.report.util;

import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.IOException;

/**
 *
 * 用于文件下载
 * @Title:
 * @Package
 */
public class DownloadUtil {

	/**
	 *
	 * @param filepath
	 * @return
	 * @throws IOException
	 */
	public static ResponseEntity<byte[]> download(String filepath) throws IOException{
		ResponseEntity<byte[]> responseEntity = null;
		File downloadFile= new File(filepath);
		if(downloadFile.exists()){
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType( MediaType.APPLICATION_OCTET_STREAM);
			headers.setContentDispositionFormData("attachment", downloadFile.getName());
			responseEntity = new ResponseEntity<>( FileUtils.readFileToByteArray( downloadFile), headers, HttpStatus.CREATED );
		}
		return responseEntity;
	}

	public static ResponseEntity<byte[]> download(String name,byte [] bytes) throws IOException{
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType( MediaType.APPLICATION_OCTET_STREAM);
		headers.setContentDispositionFormData("attachment", name);
		return new ResponseEntity<>( bytes, headers, HttpStatus.CREATED );
	}
}
