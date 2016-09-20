package com.test.qianxun.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qianxun.util.RandomUtils;
import com.test.qianxun.service.UserService;

public class ValidateUtils {
    private Random random = new Random();
    private String randString = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";//随机产生的字符串
    
    private int width = 80;//图片宽
    private int height = 26;//图片高
    private static int lineSize = 40;//干扰线数量
    private static int stringNum = 4;//随机产生字符数量
    
    public static final String gRandString = "千寻游戏是隶属于上海雨游网络科技有限公司的游戏网站是公司自主研发的一个休闲网页游戏平台千寻飞行棋正在火热推广中";
    //public static final String gNotifyString = "请点击下方的:";
    public static final int gWidth = 150;//图片宽
    public static final int gHeight = 100;//图片高
    public static final int gFontSize = 32;//字体尺寸
    
    private UserService userService;
    
    public ValidateUtils(UserService userService){
    	this.userService = userService;
    }
    /*
     * 获得字体
     */
    private static Font getFont(){
        return new Font("Fixedsys",Font.CENTER_BASELINE,18);
    }
    /*
     * 获得颜色
     */
    private static Color getRandColor(int fc,int bc){
        if(fc > 255)
            fc = 255;
        if(bc > 255)
            bc = 255;
        int r = fc + RandomUtils.nextInt(bc-fc-16);
        int g = fc + RandomUtils.nextInt(bc-fc-14);
        int b = fc + RandomUtils.nextInt(bc-fc-18);
        return new Color(r,g,b);
    }
    /**
     * 生成随机图片
     */
    public void getRandcode(HttpServletRequest request,
            HttpServletResponse response) {
        //BufferedImage类是具有缓冲区的Image类,Image类是用于描述图像信息的类
        BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_INT_BGR);
        Graphics g = image.getGraphics();//产生Image对象的Graphics对象,改对象可以在图像上进行各种绘制操作
        g.fillRect(0, 0, width, height);
        g.setFont(new Font("Times New Roman",Font.ROMAN_BASELINE,18));
        g.setColor(getRandColor(110, 133));
        //绘制干扰线
        for(int i=0;i<=lineSize;i++){
            drowLine(g);
        }
        //绘制随机字符
        String randomString = "";
        for(int i=0;i<stringNum;i++){
            randomString=drowString(g,randomString,13*i, 16);
        }
        String sid = request.getSession().getId();
        userService.putValidate(randomString, sid);
        g.dispose();
        try {
            ImageIO.write(image, "JPEG", response.getOutputStream());//将内存中的图片通过流动形式输出到客户端
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*
     * 绘制字符串
     */
    private String drowString(Graphics g,String randomString,int x,int y){
        g.setFont(getFont());
        g.setColor(new Color(random.nextInt(101),random.nextInt(111),random.nextInt(121)));
        String rand = String.valueOf(getRandomString(random.nextInt(randString.length())));
        randomString +=rand;
        g.translate(random.nextInt(3), random.nextInt(3));
        g.drawString(rand, x, y);
        return randomString;
    }
    /*
     * 绘制干扰线
     */
    private void drowLine(Graphics g){
        int x = RandomUtils.nextInt(width);
        int y = RandomUtils.nextInt(height);
        int xl = RandomUtils.nextInt(13);
        int yl = RandomUtils.nextInt(15);
        g.drawLine(x, y, x+xl, y+yl);
    }
    /*
     * 获取随机的字符
     */
    public String getRandomString(int num){
        return String.valueOf(randString.charAt(num));
    }
    
    /**
     * 生成触碰验证图片,随机位置
     */
    public static void getTouchImage(HttpServletRequest request, HttpServletResponse response, String str, int strx, int stry) {
        //BufferedImage类是具有缓冲区的Image类,Image类是用于描述图像信息的类
        BufferedImage image = new BufferedImage(gWidth,gHeight,BufferedImage.TYPE_INT_BGR);
        Graphics g = image.getGraphics();//产生Image对象的Graphics对象,改对象可以在图像上进行各种绘制操作
        g.fillRect(0, 0, gWidth, gHeight);
        g.setFont(TTFFont.getFont("cool-chinese.ttf", gFontSize));//new Font("Times New Roman",Font.ROMAN_BASELINE,18)
        g.setColor(new Color(RandomUtils.nextInt(100),RandomUtils.nextInt(100),RandomUtils.nextInt(100)));
        //绘制干扰线
        for(int i=0;i<=lineSize;i++){
        	drawLine(g);
        }
        //绘制指定字符
        g.drawString(str, strx, stry);
        //绘制随机字符
        drawRandomString(g,str);

        
        g.dispose();
        try {
            ImageIO.write(image, "JPEG", response.getOutputStream());//将内存中的图片通过流动形式输出到客户端
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void drawLine(Graphics g){
        int x = RandomUtils.nextInt(gWidth);
        int y = RandomUtils.nextInt(gHeight);
        int xl = RandomUtils.nextInt(13);
        int yl = RandomUtils.nextInt(15);
        g.drawLine(x, y, x+xl, y+yl);
    }
    
    /*
     * 绘制字符串
     */
    private static void drawRandomString(Graphics g, String exclude){
		int rand = RandomUtils.nextInt(ValidateUtils.gRandString.length()-stringNum);
		String str = ValidateUtils.gRandString.substring(rand, rand+stringNum);
		rand = RandomUtils.nextInt(stringNum);
		int x=0,y=0;
		for(int i=0;i<stringNum;i++){
			String s = str.substring(i,i+1);
			if(s.indexOf(exclude)>=0){
				continue;
			}
			
			g.setColor(new Color(RandomUtils.nextInt(100),RandomUtils.nextInt(100),RandomUtils.nextInt(100)));
						
			y = (int)(gFontSize*(1+i/5f*3));//RandomUtils.nextInt(gFontSize*2, ValidateUtils.gHeight);
			x = RandomUtils.nextInt(0, gWidth-gFontSize);

	        //g.translate(RandomUtils.nextInt(3), RandomUtils.nextInt(3));
	        g.drawString(s, x, y);
		}
    }

}    
