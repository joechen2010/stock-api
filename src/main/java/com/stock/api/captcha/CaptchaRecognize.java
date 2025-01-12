package com.stock.api.captcha;

public abstract class CaptchaRecognize {

     public String recognite(byte[] data) {
          String result = reco(data);
          if(result == null || result.isEmpty()){
               return null;
          }
          return result.replaceAll("z", "2")
                  .replaceAll("o", "0")
                  .replaceAll("g", "9")
                  .replaceAll("q", "9")
                  .replaceAll("s", "5")
                  .replaceAll("i", "1");

     }

     // 验证码识别接口
     abstract String reco(byte[] data);
}
