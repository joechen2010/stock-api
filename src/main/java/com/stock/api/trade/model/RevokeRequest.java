package com.stock.api.trade.model;

import com.stock.api.util.JsonUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.StringUtils;


public class RevokeRequest {

    @Schema(description = "委托单号", example = "20241129_2200363", required = true)
    private String orderId;

    @Schema(description = "委托日期", example = "20241129", required = true)
    private String orderDay;

    @Schema(description = "账号", example = "540600280000", required = true)
    private String username;
    @Schema(description = "密码", example = "2142342", required = true)
    private String password;

    public void validate(){
        if(StringUtils.isBlank(orderId)
                || StringUtils.isBlank(orderDay)
                || StringUtils.isBlank(username)
                || StringUtils.isBlank(password)){
            throw new IllegalArgumentException("all fields are required");
        }
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderDay() {
        return orderDay;
    }

    public void setOrderDay(String orderDay) {
        this.orderDay = orderDay;
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
