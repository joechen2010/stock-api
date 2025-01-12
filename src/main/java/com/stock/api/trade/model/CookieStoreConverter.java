package com.stock.api.trade.model;

import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class CookieStoreConverter implements AttributeConverter<BasicCookieStore, String> {

    private static final String COOKIE_DELIMITER = ";";

    @Override
    public String convertToDatabaseColumn(BasicCookieStore attribute) {
        if (attribute == null || attribute.getCookies().isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Cookie cookie : attribute.getCookies()) {
            sb.append(cookie.getName()).append("=").append(cookie.getValue()).append(COOKIE_DELIMITER);
        }
        return sb.toString();
    }

    @Override
    public BasicCookieStore convertToEntityAttribute(String dbData) {
        BasicCookieStore cookieStore = new BasicCookieStore();
        if (dbData == null || dbData.isEmpty()) {
            return cookieStore;
        }
        String[] cookies = dbData.split(COOKIE_DELIMITER);
        for (String cookieStr : cookies) {
            if (!cookieStr.isEmpty()) {
                String[] keyValue = cookieStr.split("=", 2);
                if (keyValue.length == 2) {
                    BasicClientCookie cookie = new BasicClientCookie(keyValue[0], keyValue[1]);
                    cookieStore.addCookie(cookie);
                }
            }
        }
        return cookieStore;
    }
}
