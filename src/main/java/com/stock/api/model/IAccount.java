package com.stock.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.stock.api.util.Op;

import java.util.List;

public interface IAccount {
   double getMoney();

   List<Account.Position> getPositions();

   default List<Order> getOrders() {
      return Op.list();
   }

   default List<OrderDetail> getAllOrders() {
      return Op.list();
   }

   default List<OrderDetail> getOrderHistories() {
      return Op.list();
   }

   default List<OrderDetail> getRevokeOrders(String code, long timestamp, int min) {
      return Op.list();
   }

   default Order getOrder(String code) {
      return null;
   }

   @JsonIgnore
   default List<String> getPositionCodes() {
      return Op.map(this.getPositions(), Account.Position::getCode);
   }
}
