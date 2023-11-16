package azone.controller;

import azone.pojo.Cart;
import azone.pojo.CartItem;
import azone.pojo.Resource;
import azone.pojo.User;
import azone.service.CartItemService;
import javax.servlet.http.HttpSession;

public class CartController {
    private CartItemService cartItemService;

    //加载当前用户的购物车信息
    public String index( HttpSession session ){
        User user = (User)session.getAttribute("curUser");
        Cart cart = cartItemService.getCart(user);
        user.setCart(cart);
        session.setAttribute("curUser",user);
        return "cart/cart";// 跳转到购物车里面了
    }
    public String addCart( Integer resource_id, HttpSession session ){
        //System.out.println(" resource_id : " + resource_id);
        User user = (User) session.getAttribute("curUser");
        //Resource a = new Resource(resource_id);
        CartItem cartItem = new CartItem(new Resource(resource_id),1,user);
        // 将 指定的图书 添加到 当前用户的 购物车中
        cartItemService.addOrUpdateCartItem(cartItem,user.getCart());
        return "redirect:cart.do";// 其实 就默认 cartController 的index（）方法
    }
    public String editCart(Integer cartItem_id, Integer downLoadNum){
        System.out.println("被触发editCart");
        cartItemService.updateCartItem(new CartItem(cartItem_id, downLoadNum));
        return "redirect:cart.do";
    }
    public String deleteCartItem(Integer cartItem_id){
        System.out.println("被触发deleteCartItem");
        cartItemService.delCartItem(cartItem_id);
        return "redirect:cart.do";
    }
}

























