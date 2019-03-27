package com.cmcc.report.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cmcc.report.util.RandomUtil;
import com.google.code.kaptcha.Producer;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

@Controller("KeyController")
@RequestMapping(value="/api")
public class KeyController {
	@Autowired  
    private Producer captchaProducer = null;  
	

    //生成验证码
	@SuppressWarnings("restriction")
	@RequestMapping(value="/getKaptchaImage") 
	@ResponseBody
    public Map<String,Object> getKaptchaImage(HttpServletRequest request, HttpServletResponse response) throws Exception {  
        HttpSession session = request.getSession();  
          
        response.setDateHeader("Expires", 0);  
          
        // Set standard HTTP/1.1 no-cache headers.  
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");  
          
        // Set IE extended HTTP/1.1 no-cache headers (use addHeader).  
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");  
          
        // Set standard HTTP/1.0 no-cache header.  
        response.setHeader("Pragma", "no-cache");  
          
        // return a jpeg  
        response.setContentType("image/jpeg");  
          
        // create the text for the image  
        //String capText = captchaProducer.createText();  
        String capText = RandomUtil.generateString(5);
        // store the text in the session  
        session.setAttribute("key", capText);  
          
        // create the image with the text  
        BufferedImage bi = captchaProducer.createImage(capText);  
       // ServletOutputStream out = response.getOutputStream();  
          
        // write the data out  
       // ImageIO.write(bi, "jpg", out);  
        ByteArrayOutputStream out = null; 
        out = new ByteArrayOutputStream(); 
        ImageIO.write(bi, "jpg", out); 
        
        // 对字节数组Base64编码    
        BASE64Encoder encoder = new BASE64Encoder();
        Map<String, Object> map = new HashMap<>();
        map.put("code", 0);
        map.put("img", encoder.encode(out.toByteArray()));  
        try {  
            out.flush();  
        } finally {  
            out.close();  
        }  
        return map;  
    }  
}
