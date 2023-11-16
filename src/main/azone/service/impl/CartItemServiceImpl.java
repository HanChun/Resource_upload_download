package azone.service.impl;

import azone.dao.CartItemDAO;
import azone.pojo.Cart;
import azone.pojo.CartItem;
import azone.pojo.Resource;
import azone.pojo.User;
import azone.service.CartItemService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartItemServiceImpl implements CartItemService {
    private CartItemDAO cartItemDAO ;
    private ResourceServiceImpl resourceService;
    @Override
    public void addCartItem(CartItem cartItem) {
        cartItemDAO.addCartItem(cartItem);
    }
    @Override
    public void updateCartItem(CartItem cartItem) {
        cartItemDAO.updateCartItem(cartItem);
    }
    @Override
    public void addOrUpdateCartItem( CartItem cartItem, Cart cart ) {
        // 判断当前用户的购物车中是否有这本书的 CartItem; 有就 update; 无就 add;
        // 1.如果当前用户的购物车中已经存在这个图书了，那么将购物车中的这本书数量+1
        // 2.否则在我的购物车中新增一个CartItem,数量是1
        if( cart != null ){
             Map<Integer,CartItem> cartItemMap = cart.getCartItemMap();// resource_id, CartItem
             if( cartItemMap == null ){// 如果为空==》说明购物车里面都没有 cartItem，帮助新建一个CarItem
                 cartItemMap = new HashMap<>();
             }//如果不为空，2种情况：1. 有这个resourceFile，则 其+1 就是update； 2.没有这个resourceFile， 就是 add
             if( cartItemMap.containsKey(cartItem.getResource_id().getResource_id()) ){
                 CartItem cartItemTemp = cartItemMap.get(cartItem.getResource_id().getResource_id());//

                 cartItemTemp.setDownLoadNum(cartItemTemp.getDownLoadNum()+1);
                 //cartItemTemp.setDownLoadNum(1); 这样写 是为了 保证无论如何加车 都是一个文件，但不这样；

                 updateCartItem(cartItemTemp);
             }else {
                 addCartItem(cartItem);
             }
        } else {
            addCartItem(cartItem);
        }
    }
    @Override
    public List<CartItem> getCartItemList(User user) {
        List<CartItem> cartItemList = cartItemDAO.getCartItemList(user);//这个CartItem的内部 只有resource_id
        for( CartItem cartItem:cartItemList ){
            Resource resource =resourceService.getResource( cartItem.getResource_id().getResource_id());
            cartItem.setResource_id(resource);
        }
        return cartItemList;
    }
    @Override
    public Cart getCart(User user) {
        List<CartItem> cartItemList = getCartItemList(user);//cartItemDAO.getCartItemList(user);
        Map<Integer,CartItem> cartItemMap = new HashMap<>();
        for( CartItem cartItem : cartItemList ){
            cartItemMap.put(cartItem.getResource_id().getResource_id(),cartItem);
        }
        Cart cart = new Cart();
        cart.setCartItemMap(cartItemMap);
        return cart;
    }

    @Override
    public void delCartItem(Integer cartItem_id) {
        cartItemDAO.delCartItem(cartItem_id);
    }
}












