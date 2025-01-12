package com.stock.api.trade.api;

import com.stock.api.model.Account;
import com.stock.api.model.Order;
import com.stock.api.model.OrderDetail;
import com.stock.api.trade.model.OrderRequest;
import com.stock.api.trade.model.RevokeRequest;
import com.stock.api.trade.model.User;

import java.util.List;

public interface TradeApi {

    Account.AccountData getMyAccount(User user);

    Order sendOrder(OrderRequest request, User user);

    boolean sendRevokeOrder(RevokeRequest request, User user);

    List<OrderDetail> getAllOrders(User user);

    List<OrderDetail> getOrdersHistory(int day, User user);
}
