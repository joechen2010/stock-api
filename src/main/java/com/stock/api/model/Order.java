package com.stock.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.stock.api.util.Op;
import io.swagger.v3.oas.annotations.media.Schema;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Order {
   private static final String dateFormat = "yyyyMMddHHmmss";
   private static final DateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
   @Schema(description = "证券代码", example = "300063")
   @JsonProperty("Zqdm")
   private String code;
   @Schema(description = "证券名称", example = "天龙集团")
   @JsonProperty("Zqmc")
   private String name;
   @Schema(description = "委托日期", example = "20241129")
   @JsonProperty("Wtrq")
   private String orderDay;

   @Schema(description = "委托编号", example = "2200363")
   @JsonProperty("Wtbh")
   private String id;

   @Schema(description = "委托数量", example = "1000")
   @JsonProperty("Wtsl")
   private String amount;

   @Schema(description = "委托价格", example = "10.87")
   @JsonProperty("Wtjg")
   private String price;

   @JsonIgnore
   @JsonProperty("Mmsm")
   private String type;

   @Schema(description = "冻结金额", example = "10000.00")
   @JsonProperty("Djje")
   private String money;

   @Schema(description = "成交数量", example = "800")
   @JsonProperty("Cjsl")
   private String done;

   @Schema(description = "委托时间", example = "14560498")
   @JsonProperty("Wtsj")
   private String time;

   @Schema(description = "买卖类型", example = "B", allowableValues ={"B", "S"})
   @JsonProperty("Mmbz")
   private String orderType;

   public String getCode() {
      return this.code;
   }

   public void setCode(String code) {
      this.code = code;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getOrderDay() {
      return this.orderDay;
   }

   public void setOrderDay(String orderDay) {
      this.orderDay = orderDay;
   }

   public String getId() {
      return this.id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getAmount() {
      return this.amount;
   }

   public void setAmount(String amount) {
      this.amount = amount;
   }

   public String getPrice() {
      return this.price;
   }

   public void setPrice(String price) {
      this.price = price;
   }

   public String getType() {
      return this.type;
   }

   public void setType(String type) {
      this.type = type;
   }

   public String getMoney() {
      return this.money;
   }

   public void setMoney(String money) {
      this.money = money;
   }

   public String getDone() {
      return this.done;
   }

   public void setDone(String done) {
      this.done = done;
   }

   public String getOrderType() {
      return this.orderType;
   }

   public void setOrderType(String orderType) {
      this.orderType = orderType;
   }

   public String getTime() {
      return this.time;
   }

   public void setTime(String time) {
      this.time = time;
   }

   @JsonIgnore
   public String getRevokeId() {
      String day = this.orderDay == null ? Op.format(System.currentTimeMillis()) : this.orderDay;
      return day + "_" + this.id;
   }

   @JsonIgnore
   public long getOrderTime() {
      if (Op.anyEmpty(this.orderDay, this.time)) {
         return Long.MAX_VALUE;
      } else {
         try {
            Date dateParse = sdf.parse(this.orderDay + this.time);
            return dateParse.getTime();
         } catch (Exception var2) {
            return Long.MAX_VALUE;
         }
      }
   }

   @JsonIgnore
   public boolean isNotDone() {
      return "0".equals(this.done);
   }

   @JsonIgnore
   public boolean isBuyOrder() {
      return "B".equals(this.orderType);
   }

   @JsonIgnore
   public boolean isZhai() {
      return this.getCode().startsWith("5") || this.getCode().startsWith("1");
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         Order order = (Order)o;
         return Objects.equals(order.getRevokeId(), this.getRevokeId());
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.getRevokeId()});
   }

   public String toString() {
      return Op.str("{} {} {}", this.orderType, this.name, this.getRevokeId());
   }
}
