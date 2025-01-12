<div align="center">
<h2 align="center">股票交易API</h2>

</div>

**这是一个基于爬虫实现的对东方财富证券的在线网上交易的封装.**

**下单，撤单，查询持仓，资金，历史订单，查询委托，查询成交**

**与手工在东方财富APP操作无区别，小散也能拥有的自动交易系统。**

**没有资金门槛限制！没有资金门槛限制!! 没有资金门槛限制!!!**

**Rest API: 免费试用30次，月卡180元，季卡300元, 年卡800元, API文档: http://124.220.4.54/api**

**JAVA API + 开发部署支持: 1800元**

**接口的实现代码未开源，如需使用请联系作者授权，授权后可提供源码，jar包**

Usage:

```java
    User user = new User("东方财富证券账号", "东方财富证券密码");
    TradeApi tradeApi = new H5TradeApi();
   //获取账号信息
    Account.AccountData accountData = tradeApi.getMyAccount(user);
    
    //下单
    OrderRequest orderRequest = new OrderRequest();
    orderRequest.setCode("600519");
    orderRequest.setName("贵州茅台");
    orderRequest.setPrice(1436.00);
    orderRequest.setCount(1000);
    orderRequest.setOrderType("B"); //B:买入，S:卖出
    orderRequest.setGdk("5700");
    Order order = tradeApi.sendOrder( request,user);

    //撤单
    RevokeRequest revokeRequest = new RevokeRequest();
    revokeRequest.setOrderId("20241129_2200363"); // get from the response of sendOrder
    revokeRequest.setOrderDay("20241129");
    sendRevokeOrder(RevokeRequest request, User user);

    //获取当天委托订单
    List<OrderDetail> todayOrders = tradeApi.getAllOrders( user);

    //查询历史委托
    List<OrderDetail> tradeApi.getOrdersHistory(15,user);
  
```


<table>
  <thead>
    <tr>
  <th>我的微信</th>
  </tr>
</thead>
<tr>
<td align="center"><img  src="wechat.jpg"></img></td>
</tr>
</table>