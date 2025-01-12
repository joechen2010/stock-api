package com.stock.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.stock.api.util.ApiUtils;
import com.stock.api.util.DoubleConverter;
import com.stock.api.util.IntegerConverter;
import com.stock.api.util.Op;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

public class Account {
   @JsonProperty("Status")
   private int status;
   @JsonProperty("Count")
   private int count;
   @JsonProperty("Errcode")
   private int errcode;
   @JsonProperty("Data")
   private List<AccountData> data = new ArrayList();

   public int getStatus() {
      return this.status;
   }

   public void setStatus(int status) {
      this.status = status;
   }

   public int getCount() {
      return this.count;
   }

   public void setcount(int count) {
      this.count = count;
   }

   public int getErrcode() {
      return this.errcode;
   }

   public void setErrcode(int errcode) {
      this.errcode = errcode;
   }

   public List<AccountData> getData() {
      return this.data;
   }

   public void setData(List<AccountData> data) {
      this.data = data;
   }

   public String toString() {
      return "Account{status=" + this.status + ", count=" + this.count + ", errcode=" + this.errcode + ", data=" + this.data + '}';
   }

   public static class Position {

      @Schema(description = "证券代码", example = "300063")
      @JsonProperty("Zqdm")
      private String code;

      @Schema(description = "证券名称", example = "天龙集团")
      @JsonProperty("Zqmc")
      private String name;

      @Schema(description = "成本价格", example = "10.87")
      @JsonProperty("Cbjg")
      private double buyPrice;

      @Schema(description = "参考盈亏", example = "500.00")
      @JsonProperty("Ckyk")
      @JsonDeserialize(
         using = DoubleConverter.class
      )
      private double money;

      @Schema(description = "当前价格", example = "12.47")
      @JsonProperty("Zxjg")
      @JsonDeserialize(
         using = DoubleConverter.class
      )
      private double currentPrice;

      @Schema(description = "持仓数量", example = "1000")
      @JsonProperty("Zqsl")
      @JsonDeserialize(
         using = IntegerConverter.class
      )
      private int amount;

      @Schema(description = "可用数量", example = "1000")
      @JsonProperty("Kysl")
      @JsonDeserialize(
         using = IntegerConverter.class
      )
      private int canSellAmount;

      @JsonProperty("Market")
      @JsonIgnore
      private String market;

      @Schema(description = "股东代码", example = "0161112100")
      @JsonProperty("Gddm")
      private String userCode;

      @Schema(description = "盈亏比例", example = "8")
      @JsonProperty("Ykbl")
      @JsonDeserialize(
         using = DoubleConverter.class
      )
      private double percent;

      @JsonIgnore
      private OrderDetail orderDetail;

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

      @JsonIgnore
      public String getMarket() {
         return this.market;
      }

      public void setMarket(String market) {
         this.market = market;
      }

      public String getUserCode() {
         return this.userCode;
      }

      public void setUserCode(String userCode) {
         this.userCode = userCode;
      }

      public double getBuyPrice() {
         return this.buyPrice;
      }

      public void setBuyPrice(double buyPrice) {
         this.buyPrice = buyPrice;
      }

      public void setCurrentPrice(double currentPrice) {
         this.currentPrice = currentPrice;
      }

      public int getAmount() {
         return this.amount;
      }

      public void setAmount(int amount) {
         this.amount = amount;
      }

      public int getCanSellAmount() {
         return this.canSellAmount;
      }

      public void setCanSellAmount(int canSellAmount) {
         this.canSellAmount = canSellAmount;
      }

      public double getPercent(double currentPrice) {
         return ApiUtils.round((currentPrice - this.buyPrice) / this.buyPrice, 4) * 100.0D;
      }

      public void setPercent(double percent) {
         this.percent = percent;
      }

      public boolean canSell() {
         return this.canSellAmount > 0;
      }

      public long getHoldTime(long buyTime) {
         return System.currentTimeMillis() - buyTime;
      }
      public double getMoney() {
         return this.money;
      }

      public void setMoney(double money) {
         this.money = money;
      }

      public String toString() {
         return Op.str("{}:{}={}", this.code, this.name, this.percent);
      }
   }

   public static class AccountData implements IAccount {
      @JsonProperty("Djzj")
      @JsonIgnore
      private String djzj;

      @Schema(description = "当日盈亏", example = "6000.00")
      @JsonProperty("Dryk")
      private String dryk;

      @Schema(description = "可取资金", example = "100000.00")
      @JsonProperty("Kqzj")
      private String kqzj;

      @Schema(description = "可用资金", example = "100000.00")
      @JsonProperty("Kyzj")
      private String kyzj;

      @Schema(description = "持仓盈亏", example = "10000.00")
      @JsonProperty("Ljyk")
      private String ljyk;

      @Schema(description = "币种", example = "RMB")
      @JsonProperty("Money_type")
      private String moneyType;

      @Schema(description = "RMB总资产", example = "RMB")
      @JsonProperty("RMBZzc")
      private String rmbZzc;

      @Schema(description = "资金余额", example = "123456.78")
      @JsonProperty("Zjye")
      private String zjye;

      @Schema(description = "最新市值", example = "100000.00")
      @JsonProperty("Zxsz")
      private String zxsz;

      @Schema(description = "总资产", example = "100000.00")
      @JsonProperty("Zzc")
      private String zzc;

      @Schema(description = "持仓列表")
      @JsonProperty("positions")
      private List<Position> positions = Op.list();

      @JsonIgnore
      private List<Order> orders = Op.list();
      @JsonIgnore
      private List<OrderDetail> orderHistories = Op.list();
      @JsonIgnore
      private List<OrderDetail> allOrders = Op.list();

      @JsonIgnore
      public String getDjzj() {
         return this.djzj;
      }

      public void setDjzj(String djzj) {
         this.djzj = djzj;
      }

      public String getDryk() {
         return this.dryk;
      }

      public void setDryk(String dryk) {
         this.dryk = dryk;
      }

      public String getKqzj() {
         return this.kqzj;
      }

      public void setKqzj(String kqzj) {
         this.kqzj = kqzj;
      }

      public String getKyzj() {
         return this.kyzj;
      }

      public void setKyzj(String kyzj) {
         this.kyzj = kyzj;
      }

      public String getLjyk() {
         return this.ljyk;
      }

      public void setLjyk(String ljyk) {
         this.ljyk = ljyk;
      }

      public String getMoneyType() {
         return this.moneyType;
      }

      public void setMoneyType(String moneyType) {
         this.moneyType = moneyType;
      }

      public String getRmbZzc() {
         return this.rmbZzc;
      }

      public void setRmbZzc(String rmbZzc) {
         this.rmbZzc = rmbZzc;
      }

      public String getZjye() {
         return this.zjye;
      }

      public void setZjye(String zjye) {
         this.zjye = zjye;
      }

      public String getZxsz() {
         return this.zxsz;
      }

      public void setZxsz(String zxsz) {
         this.zxsz = zxsz;
      }

      public String getZzc() {
         return this.zzc;
      }

      public void setZzc(String zzc) {
         this.zzc = zzc;
      }

      public List<Position> getPositions() {
         return this.positions;
      }

      public void setPositions(List<Position> positions) {
         this.positions = positions;
      }

      @JsonIgnore
      public double getMoney() {
         return this.kyzj == null ? 0.0D : Double.valueOf(this.kyzj);
      }

      public List<Order> getOrders() {
         return this.orders;
      }

      public void setOrders(List<Order> orders) {
         this.orders = orders;
      }

      public List<OrderDetail> getOrderHistories() {
         return this.orderHistories;
      }

      public void setOrderHistories(List<OrderDetail> orderHistories) {
         this.orderHistories = orderHistories;
      }

      public Order getOrder(String code) {
         return (Order)Op.first(this.orders, (Order o) -> {
            return o != null && Op.eq(o.getCode(), code) && o.isNotDone();
         });
      }

      public Order getBuyOrder(String code) {
         return (Order)Op.first(this.orders, (Order o) -> {
            return o != null && Op.eq(o.getCode(), code) && o.isNotDone() && o.isBuyOrder();
         });
      }

      public boolean hasBuyOrder(String code) {
         return this.getBuyOrder(code) != null;
      }

      public List<OrderDetail> getAllOrders() {
         return this.allOrders;
      }

      public List<OrderDetail> getRevokeOrders(String code, long currentTimestamp, int min) {
         return Op.filter(this.allOrders, (OrderDetail o) -> {
            return currentTimestamp - o.getOrderTime() < (long)(min * 60 * 1000) && o.isCancel();
         });
      }

      public void setAllOrders(List<OrderDetail> allOrders) {
         this.allOrders = allOrders;
      }

      public String toString() {
         return Op.str("共{}条持仓：【{}】 共{}条委托：【{}】 共{}条历史委托 共{}条当日委托", this.positions.size(), this.positions, this.orders.size(), this.orders, this.orderHistories.size(), this.allOrders.size());
      }
   }
}
