package com.stock.api.trade.model;


import com.stock.api.util.JsonUtil;
import com.stock.api.util.RSAUtils;
import org.apache.http.impl.client.BasicCookieStore;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String password;
    public String sessionId;
    public String lastSessionId;
    public Long loginCounter = 0l;
    public Long orderCounter = 0l;
    public Long revokeCounter = 0l;
    public Long queryCounter = 0l;
    private String encryptedPassword;

    @Column(name = "buy_type", nullable = false, columnDefinition = "int default -1")
    public int buyType = -1;
    public LocalDateTime expireTime;

    @Lob // Large Object, for storing potentially large cookie data
    @Convert(converter = CookieStoreConverter.class)
    private BasicCookieStore cookieStore;

    public User() {
    }

    public User(String name, String password) {
        this.name = name;
        this.password = password;
        this.encryptedPassword = RSAUtils.getMyPass(password);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User tradeUser = (User) o;
        return Objects.equals(name, tradeUser.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        this.encryptedPassword = RSAUtils.getMyPass(password);
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEncryptedPassword() {
        if (encryptedPassword == null) {
            encryptedPassword = RSAUtils.getMyPass(password);
        }
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    public String getLastSessionId() {
        return lastSessionId;
    }

    public void setLastSessionId(String lastSessionId) {
        this.lastSessionId = lastSessionId;
    }

    public Long getLoginCounter() {
        return loginCounter;
    }

    public void setLoginCounter(Long loginCounter) {
        this.loginCounter = loginCounter;
    }

    public Long getOrderCounter() {
        return orderCounter;
    }

    public void setOrderCounter(Long orderCounter) {
        this.orderCounter = orderCounter;
    }

    public Long getRevokeCounter() {
        return revokeCounter;
    }

    public void setRevokeCounter(Long revokeCounter) {
        this.revokeCounter = revokeCounter;
    }

    public Long getQueryCounter() {
        return queryCounter;
    }

    public void setQueryCounter(Long queryCounter) {
        this.queryCounter = queryCounter;
    }

    public synchronized void incrementLoginCounter() {
        this.loginCounter++;
    }
    public synchronized void incrementOrderCounter() {
        this.orderCounter++;
    }
    public synchronized void incrementRevokeCounter() {
        this.revokeCounter++;
    }
    public synchronized void incrementQueryCounter() {
        this.loginCounter++;
    }

    @Override
    public String toString() {
        return JsonUtil.objectToJson(this);
    }

    public boolean isAdmin() {
        return false;
    }

    public BasicCookieStore getCookieStore() {
        return cookieStore;
    }

    public void setCookieStore(BasicCookieStore cookieStore) {
        this.cookieStore = cookieStore;
    }

    public LocalDateTime getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
    }

    public int getBuyType() {
        return buyType;
    }

    public void setBuyType(int buyType) {
        this.buyType = buyType;
    }

    public boolean isVIP(){
        return getBuyType() > 0
                && getExpireTime() != null
                && getExpireTime().isAfter(LocalDateTime.now());
    }


}
