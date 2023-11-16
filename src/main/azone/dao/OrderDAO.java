package azone.dao;

import azone.pojo.Order;
import azone.pojo.User;

import java.util.List;

public interface OrderDAO {
    void addOrder( Order order);//添加订单
    // 获取指定用户的订单列表
    List<Order> getOrderList(User user);
}
