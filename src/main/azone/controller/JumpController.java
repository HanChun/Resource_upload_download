package azone.controller;

import javax.servlet.http.HttpSession;

public class JumpController {
    //加载当前用户的购物车信息
    public String index( HttpSession session ){

        return "upload/upload";// 跳转到购物车里面了
        //return "upload/uploadTest";//
    }
}
