package azone.dao.impl;

import azone.dao.OrderItemDAO;
import azone.pojo.CartItem;
import azone.pojo.OrderItem;
import azone.pojo.User;
import myssm.basedao.BaseDAO;
import java.util.List;

public class OrderItemDAOImpl extends BaseDAO<OrderItem> implements OrderItemDAO {
    @Override
    public void addOrderItem(OrderItem orderItem) {
        super.executeUpdate("insert into t_order_item values((select max(\"orderItem_id\")+1 FROM t_order_item),?,?,?)",
                orderItem.getResource_id().getResource_id(), orderItem.getDownLoadNum(), orderItem.getOrder_id().getOrder_id());
    }

    @Override
    public void updateCartItem(OrderItem orderItem) {

    }


    @Override
    public List<CartItem> getCartItemList(User user) {
        return null;
    }

    @Override
    public void delCartItem(OrderItem orderItem) {

    }
}
