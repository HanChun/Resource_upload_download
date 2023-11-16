package azone.dao;

import azone.pojo.CartItem;
import azone.pojo.User;

import java.util.List;

public interface CartItemDAO {
    // 购物车 新增 物品项
    void addCartItem(CartItem cartItem);
    //修改 购物车 中 某个 选项的信息
    void updateCartItem(CartItem cartItem);
    List<CartItem> getCartItemList(User user);
    void delCartItem(Integer cartItem_id);

}
