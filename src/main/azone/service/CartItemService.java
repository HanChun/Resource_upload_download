package azone.service;

import azone.pojo.Cart;
import azone.pojo.CartItem;
import azone.pojo.User;

import java.util.List;

public interface CartItemService {
    void addCartItem(CartItem cartItem);
    void updateCartItem(CartItem cartItem);
    void addOrUpdateCartItem(CartItem cartItem, Cart cart);
    List<CartItem> getCartItemList(User user);
    //获取指定用户的所有购物车项的列表（需要注意的是，这个方法内部查询的时候，会将Resource的详细信息设置进去）
    Cart getCart(User user);
    //加载特定 用户 的购物车信息===》
    //传给我一个 用户信息 我就可以 把它的 购物车信息拿出来：
    // 1.1 未注册；1.2 注册了，没加车；1.3 注册了，加车了
    void delCartItem(Integer cartItem_id);
}
