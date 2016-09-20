package com.james.jetty.utils;

import com.octo.captcha.service.captchastore.FastHashMapCaptchaStore;  
import com.octo.captcha.service.image.DefaultManageableImageCaptchaService;  
import com.octo.captcha.service.image.ImageCaptchaService;  
  
  
/**  
 * 验证码图片生成类--->单例类  
 */  
public class CaptchaServiceSingleton {  
  
    private static ImageCaptchaService instance =   
        new DefaultManageableImageCaptchaService(new FastHashMapCaptchaStore(),  
            new CaptchaEngine(),  
            180,  
            100000,  
            75000);  
  
  
    private CaptchaServiceSingleton(){}  
      
    public static ImageCaptchaService getInstance(){  
        return instance;  
    }  
      
}  