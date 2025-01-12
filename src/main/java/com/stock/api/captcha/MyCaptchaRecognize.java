package com.stock.api.captcha;

import com.stock.api.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

@Component("myCaptchaRecognize")
public class MyCaptchaRecognize extends CaptchaRecognize {

    public static RestTemplate restTemplate = new RestTemplate();
    private static final Logger logger = LoggerFactory.getLogger(MyCaptchaRecognize.class);

    @Value("${captcha.api.url}")
    private String apiUrl;
    @Override
    public String reco(byte[] data) {
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Content-Type", "application/json; charset=UTF-8");
            Map<String, String> form = new LinkedHashMap<>();
            form.put("image", Base64.getEncoder().encodeToString(data));
            HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity(JsonUtil.objectToJson(form), httpHeaders);
            ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, httpEntity, String.class, new Object[0]);
            String result = response.getBody();
            logger.debug("Recognite as {}.", result);
            return result;
        } catch (Exception var7) {
            logger.error("Fail to recognite by {}.", apiUrl, var7.getMessage());
            return null;
        }
    }
}
