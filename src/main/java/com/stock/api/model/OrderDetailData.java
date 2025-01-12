package com.stock.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailData {
   @JsonProperty("Message")
   private String message;
   @JsonProperty("Status")
   private int status;
   @JsonProperty("Data")
   private List<OrderDetail> orders = new ArrayList();

   public String getMessage() {
      return this.message;
   }

   public void setMessage(String message) {
      this.message = message;
   }

   public int getStatus() {
      return this.status;
   }

   public void setStatus(int status) {
      this.status = status;
   }

   public List<OrderDetail> getOrders() {
      return this.orders;
   }

   public void setOrders(List<OrderDetail> orders) {
      this.orders = orders;
   }
}
