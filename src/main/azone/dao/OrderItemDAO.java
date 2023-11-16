package azone.dao;

import azone.pojo.CartItem;
import azone.pojo.OrderItem;
import azone.pojo.User;

import java.util.List;

public interface OrderItemDAO {
    //添加订单项
    void addOrderItem( OrderItem orderItem);//新增购物车项
    void updateCartItem(OrderItem orderItem );
    List<CartItem> getCartItemList( User user );//获取所有 用户 的购物车项
    void delCartItem( OrderItem orderItem  );//删除特定的 购物车项

}
