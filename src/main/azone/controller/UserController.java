package azone.controller;

import azone.pojo.Cart;
import azone.pojo.User;
import azone.service.CartItemService;
import azone.service.CreditService;
import azone.service.UserService;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class UserController {
    private UserService userService;
    private CartItemService cartItemService;
    private CreditService creditService;

    public String login( String uname, String pwd, HttpSession session ) {

        User user = userService.login(uname, pwd);

        if ( user != null ){
            Cart cart = cartItemService.getCart(user);//！！！*** 帮助初始化 Cart
            user.setCart(cart);
            session.setAttribute("curUser",user);

            int credit = creditService.getUserCredit(user);
            System.out.println("UserController 里面的 credit : " + credit );
            session.setAttribute("credit",credit);

            return "redirect:resource.do";// 不写 就是默认 operate=index，就是默认调用 ResourceController.index方法
            // 这块表示 如果 登录验证成功， 就 ResourceController里面 加载 Resource 的资源
        }
       return "user/login";
        //
        /*System.out.println(" user = " + user);
        return "index";*/
    }

    public String logout(HttpSession session, HttpServletResponse response) throws IOException {
        session.removeAttribute("currUser");
        session.removeAttribute("fileStringList");
        session.removeAttribute("resourceList");
        session.removeAttribute("pageNo");
        session.removeAttribute("credit");
        return "user/login";

    }
}







