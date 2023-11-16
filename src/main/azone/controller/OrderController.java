package azone.controller;

import azone.pojo.Cart;
import azone.pojo.CartItem;
import azone.pojo.Order;
import azone.pojo.User;
import azone.service.CartItemService;
import azone.service.CreditService;
import azone.service.OrderService;
import azone.service.ResourceService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class OrderController {
    private OrderService orderService;
    private CreditService creditService;
    private CartItemService cartItemService;

    private ResourceService resourceService;
    public String checkout( HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        //1_check之前要先把 要下载的 文件列表拿出来
        User user = (User)session.getAttribute("curUser");
        Map<Integer, CartItem> cartMap = user.getCart().getCartItemMap();//Map<Integer,CartItem>
        List<String> stringList = new ArrayList<>();

        for ( Map.Entry<Integer, CartItem> entry : cartMap.entrySet() ) {
            Integer key = entry.getKey();
            CartItem cartItem = entry.getValue();
            String fileName = cartItem.getResource_id().getResourceName();
            creditService.addDownloadCredit(fileName);
            stringList.add(fileName);
            System.out.println(" fileName : " +  fileName);
        }
        session.setAttribute("fileStringList",stringList);


        //2_ 对当前 session 的 credit做更新   //session.curUser.cart.totalResourceCount
        int credit = (int)session.getAttribute("credit") - user.getCart().getTotalResourceCount()*3;
        session.setAttribute("credit",credit);
        creditService.updateCredit(user,credit);

        //3 生成新订单
        Order order = new Order();
        Date now = new Date();

        int year = now.getYear();
        int month = now.getMonth();
        int day = now.getDate();
        int hour = now.getHours();
        int min = now.getMinutes();
        int sec = now.getSeconds();
        order.setOrderNo(UUID.randomUUID().toString() + "_" + year + month + day + hour + min + sec);
        order.setOrderDate(now);

        //User user = (User) session.getAttribute("curUser");
        order.setUser_id(user);
        order.setOrderFileSize(user.getCart().getTotalFileSize());
        order.setOrderStatus(0);

        orderService.addOrder(order);

        //4 重新加载购物车
        Cart cart = cartItemService.getCart(user);
        user.setCart(cart);
        session.setAttribute("curUser",user);

        //5 资源free 页面也该更新了===》因为付过费了 就把付过费的资源 改成 download directly
        List<Integer> resourceIds = resourceService.getFreeResource(user);
        session.setAttribute("resourceIds",resourceIds);
        return "cart/checkout";
    }

    public String getOrderList( HttpSession session ){
        User user = (User)session.getAttribute("curUser");
        List<Order> orderList = orderService.getOrderList(user);
        user.setOrderList(orderList);
        session.setAttribute("curUser",user);
        return "order/order";
    }


}

