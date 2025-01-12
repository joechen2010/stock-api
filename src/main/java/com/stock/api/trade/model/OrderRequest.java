package com.stock.api.trade.model;

import com.stock.api.util.JsonUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.StringUtils;

public class OrderRequest {
    @Schema(description = "证券代码", example = "300063", required = true)
    private String code;
    @Schema(description = "证券名称", example = "贵州茅台", required = true)
    private String name;
    @Schema(description = "委托数量", example = "1000", required = true)
    private int count;
    @Schema(description = "委托价格", example = "10.5", required = true)
    private double price;
    @Schema(description = "B:买入, S:卖出", example = "B", required = true, allowableValues ={"B", "S"})
    private String type;
    @Schema(description = "股东代号, type是S时必填", example = "5700")
    private String gddm;
    @Schema(description = "账号", example = "540600280000", required = true)
    private String username;
    @Schema(description = "密码", example = "2142342", required = true)
    private String password;

    public void validate(){
        if(StringUtils.isBlank(code)){
            throw new IllegalArgumentException("code is required");
        }
        if(StringUtils.isBlank(name)){
            throw new IllegalArgumentException("name is required");
        }
        if(count <= 0){
            throw new IllegalArgumentException("count is required");
        }
        if(price <= 0){
            throw new IllegalArgumentException("price is required");
        }
        if(!type.equalsIgnoreCase("B") || !type.equalsIgnoreCase("S")){
            throw new IllegalArgumentException("type is incorrect, should be B or S");
        }
        if(StringUtils.isBlank(username) || StringUtils.isBlank(password)){
            throw new IllegalArgumentException("username and password is required");
        }
        if(StringUtils.isBlank(gddm) && type.equalsIgnoreCase("S")){
            throw new IllegalArgumentException("gddm is required for sell order");
        }
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGddm() {
        return gddm;
    }

    public void setGddm(String gddm) {
        this.gddm = gddm;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return JsonUtil.objectToJson(this);
    }
}
