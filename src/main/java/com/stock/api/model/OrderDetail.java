package com.stock.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.stock.api.util.DoubleConverter;
import com.stock.api.util.IntegerConverter;
import com.stock.api.util.Op;
import io.swagger.v3.oas.annotations.media.Schema;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

//@JsonSerialize(using = LowerCasePropertyNameSerializer.class)
public class OrderDetail {
   @JsonIgnore
   private static final String dateFormat = "yyyyMMddHHmmss";
   @JsonIgnore
   private static final DateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
   @Schema(description = "证券代码", example = "300063")
   @JsonProperty("Zqdm")
   private String code;
   @Schema(description = "证券名称", example = "天龙集团")
   @JsonProperty("Zqmc")
   private String name;

   @Schema(description = "买卖类型", example = "B", allowableValues ={"B", "S"})
   @JsonProperty("Mmlb")
   private String orderType;

   @Schema(description = "委托时间", example = "14560498")
   @JsonProperty("Wtsj")
   private String time;
   @Schema(description = "证券市场", example = "SA", allowableValues ={"SA", "HA"})
   @JsonProperty("Market")
   private String market;
   @Schema(description = "委托编号", example = "2200363")
   @JsonProperty("Wtbh")
   private String id;
   @Schema(description = "股东代码", example = "0161112100")
   @JsonProperty("Gddm")
   private String gddm;

   @JsonIgnore
   //@Schema(description = "股票名称", example = "天龙集团")
   @JsonProperty("Khdm")
   private String khdm;

   @Schema(description = "证券账号", example = "540600280000")
   @JsonProperty("Zjzh")
   private String zjzh;

   @Schema(description = "交割编码", example = "5123")
   @JsonProperty("Jgbm")
   private String jgbm;

   @Schema(description = "委托日期", example = "20241129")
   @JsonProperty("Wtrq")
   private String orderDay;
   @Schema(description = "委托状态", example = "已成")
   @JsonProperty("Wtzt")
   private String status;
   @Schema(description = "委托数量", example = "1000")
   @JsonProperty("Wtsl")
   @JsonDeserialize(
      using = IntegerConverter.class
   )

   private int amount;
   @Schema(description = "成交数量", example = "1000")
   @JsonProperty("Cjsl")
   @JsonDeserialize(
      using = IntegerConverter.class
   )
   private int doneAmount;
   @Schema(description = "委托价格", example = "10.87")
   @JsonProperty("Wtjg")
   @JsonDeserialize(
      using = DoubleConverter.class
   )
   private double orderPrice;
   @Schema(description = "成交价格", example = "10.87")
   @JsonProperty("Cjjg")
   @JsonDeserialize(
      using = DoubleConverter.class
   )
   private double price;

   @Schema(description = "撤单编号", example = "20241129_2200363")
   @JsonProperty("RevokeId")
   private String revokeId;

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

   public String getMarket() {
      return this.market;
   }

   public void setMarket(String market) {
      this.market = market;
   }

   public String getId() {
      return this.id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getGddm() {
      return this.gddm;
   }

   public void setGddm(String gddm) {
      this.gddm = gddm;
   }

   @JsonIgnore
   public String getKhdm() {
      return this.khdm;
   }

   public void setKhdm(String khdm) {
      this.khdm = khdm;
   }

   public String getZjzh() {
      return this.zjzh;
   }

   public void setZjzh(String zjzh) {
      this.zjzh = zjzh;
   }

   public String getJgbm() {
      return this.jgbm;
   }

   public void setJgbm(String jgbm) {
      this.jgbm = jgbm;
   }

   public String getOrderDay() {
      return this.orderDay;
   }

   public void setOrderDay(String orderDay) {
      this.orderDay = orderDay;
   }

   public int getAmount() {
      return this.amount;
   }

   public void setAmount(int amount) {
      this.amount = amount;
   }

   public int getDoneAmount() {
      return this.doneAmount;
   }

   public void setDoneAmount(int doneAmount) {
      this.doneAmount = doneAmount;
   }

   public double getPrice() {
      return this.price;
   }

   public void setPrice(double price) {
      this.price = price;
   }

   //@JsonIgnore
   public String getRevokeId() {
      if(this.revokeId == null){
         String day = this.orderDay == null ? Op.format(System.currentTimeMillis()) : this.orderDay;
         revokeId = day + "_" + this.id;
      }
      return revokeId;
   }

   @JsonIgnore
   public Long getOrderTime() {
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
   public boolean notDone() {
      return !this.isDone() && !this.isCancel() && !this.isInvalid();
   }

   @JsonIgnore
   public boolean isDone() {
      return Op.eq(this.status, "已成");
   }

   @JsonIgnore
   public boolean isInvalid() {
      return Op.eq(this.status, "废单");
   }

   @JsonIgnore
   public boolean isCancel() {
      return Op.eq(this.status, "已撤");
   }

   @JsonIgnore
   public boolean isBuyOrder() {
      return "B".equals(this.orderType);
   }

   @JsonIgnore
   public boolean isSellOrder() {
      return "S".equals(this.orderType);
   }

   @JsonIgnore
   public boolean isZhai() {
      return this.getCode().startsWith("5") || this.getCode().startsWith("1");
   }

   public double getOrderPrice() {
      return this.orderPrice;
   }

   public void setOrderPrice(double orderPrice) {
      this.orderPrice = orderPrice;
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

   public String getStatus() {
      return this.status;
   }

   public void setStatus(String status) {
      this.status = status;
   }
}
