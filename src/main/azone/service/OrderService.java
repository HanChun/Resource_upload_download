package azone.service;

import azone.pojo.Order;
import azone.pojo.User;

import java.util.List;

public interface OrderService {
    void addOrder(Order order);
    List<Order> getOrderList(User user);
}
