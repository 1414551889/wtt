package com.cmcc.report.util;



import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;





/**
 * @ClassName: CaptchaUtil
 * @Description: 关于验证码的工具类
 * @author
 * @date
 * @version 1.0
 */
public final class CaptchaUtil {
    private CaptchaUtil() {
    }

    /*
     * 随机字符字典
     */
    private static final char[] CHARS = {'2', '3', '4', '5', '6', '7', '8',
            '9', '0', '1','a','b','c','d','e','f','g','h','k','j','m','n','p','q','r','s','t','u','v','w','x','y','z',};

    /*
     * 随机数
     */
    private static Random random = new Random();

    /*
     * 获取6位随机数
     */
    public static String getRandomString() {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < 4; i++) {
            buffer.append(CHARS[random.nextInt(CHARS.length)]);
        }
        return buffer.toString();
    }

    /*
     * 获取随机数颜色
     */
    private static Color getRandomColor() {
        return new Color(random.nextInt(255), random.nextInt(255),
                random.nextInt(255));
    }

    /*
     * 返回某颜色的反色
     */
    private static Color getReverseColor(Color c) {
        return new Color(255 - c.getRed(), 255 - c.getGreen(),
                255 - c.getBlue());
    }

    public static void outputCaptcha(HttpServletRequest request, HttpServletResponse response,String radomValue)
            throws ServletException, IOException {

        response.setContentType("image/jpeg");

      //  String randomString = getRandomString();
        request.getSession(true).setAttribute("key", radomValue);

        int width = 100;
        int height = 30;

        Color color = getRandomColor();
        Color reverse = getReverseColor(color);

        BufferedImage bi = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bi.createGraphics();
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 23));
        g.setColor(color);
        g.fillRect(0, 0, width, height);
        g.setColor(reverse);
        g.drawString(radomValue, 18, 20);
        for (int i = 0, n = random.nextInt(30); i < n; i++) {
            g.drawRect(random.nextInt(width), random.nextInt(height), 1, 1);
        }


        // 禁止图像缓存。
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        response.setContentType("image/jpeg");

        // 将图像输出到Servlet输出流中。
        ServletOutputStream sos = response.getOutputStream();
        ImageIO.write(bi, "jpeg", sos);
        sos.close();




        // 转成JPEG格式
       /** ServletOutputStream out = response.getOutputStream();
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
        encoder.encode(bi);
        out.flush();**/

    }
    public static void main(String[]args){


    }
}

