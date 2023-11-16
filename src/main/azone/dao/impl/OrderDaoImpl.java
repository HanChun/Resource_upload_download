package azone.dao.impl;
import azone.dao.OrderDAO;
import azone.pojo.Order;
import azone.pojo.User;
import myssm.basedao.BaseDAO;

import java.util.List;

public class OrderDaoImpl extends BaseDAO<Order> implements OrderDAO {
    @Override
    public void addOrder(Order order) {
        int order_id = super.executeUpdate("insert into t_order values((select max(\"order_id\")+1 FROM t_order),?,?,?,?,?)",
                order.getOrderNo(),order.getOrderDate(),order.getUser_id().getUser_id(),order.getOrderFileSize(),order.getOrderStatus());
        order.setOrder_id(order_id);
    }

    @Override
    public List<Order> getOrderList(User user) {
        return executeQuery("select * from t_order where user_id = ?",user.getUser_id());
    }


}
