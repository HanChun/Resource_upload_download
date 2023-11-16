package azone.service.impl;

import azone.dao.CartItemDAO;
import azone.dao.OrderDAO;
import azone.dao.OrderItemDAO;
import azone.pojo.CartItem;
import azone.pojo.Order;
import azone.pojo.OrderItem;
import azone.pojo.User;
import azone.service.OrderService;

import java.util.List;
import java.util.Map;

public class OrderServiceImpl implements OrderService {
    private OrderDAO orderDAO ;
    private OrderItemDAO orderItemDAO ;
    private CartItemDAO cartItemDAO ;
    @Override
    public void addOrder(Order order) {
        //[1]订单表添加一条记录
        //[2]订单详情表添加7条记录
        //[3]购物车项表中需要删除对应的7条记录

        //1_第一步；
        orderDAO.addOrder(order);

        //2_第二步
        // 需要通过 user 的 cart 项 来把 order 中的 orderItemList 填充；
        User curUser = order.getUser_id();
        Map<Integer, CartItem> cartItemMap = curUser.getCart().getCartItemMap();
        for(CartItem cartItem:cartItemMap.values()){
            OrderItem orderItem = new OrderItem();
            orderItem.setResource_id(cartItem.getResource_id());
            orderItem.setDownLoadNum(cartItem.getDownLoadNum());
            orderItem.setOrder_id(order);

            orderItemDAO.addOrderItem(orderItem);
        }

        //3_第三步
        for(CartItem cartItem:cartItemMap.values()){
            cartItemDAO.delCartItem(cartItem.getCartItem_id());
        }
    }

    @Override
    public List<Order> getOrderList(User user) {
        return orderDAO.getOrderList(user);
    }
}







