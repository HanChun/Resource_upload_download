/*
package myssm.filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebFilter(urlPatterns = {"*.do","*.html"},
        initParams = {
            @WebInitParam(name ="whiteList",
                    value="/pro24/page.do?operate=page&page=user/login,/pro24/user.do?null")
        }
)
public class SessionFilter implements Filter {// 这个filter就是帮助 如果 你没有 登录 就访问 到其它的controller，那么
    List<String> whiteList = null ;
    public SessionFilter(){

    }

    @Override
    public void init(FilterConfig config) throws ServletException {
        String whiteListStr = config.getInitParameter("whiteList");
        String[] whiteListArr = whiteListStr.split(",");
        whiteList = Arrays.asList(whiteListArr);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        */
/*HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response =(HttpServletResponse) servletRequest;

        System.out.println("request.getRequestURL() = "+ request.getRequestURL());
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();
        String str = uri +"?"+queryString;
        if(whiteList.contains(str)){
            filterChain.doFilter(request,response);
            return;
        }else {
            HttpSession session = request.getSession();
            Object curUserObj = session.getAttribute("curUser");
            if( curUserObj == null ){
                response.sendRedirect("page.do?operate=page&page=user/login");
            }else{
                filterChain.doFilter(request,response);
            }
        }*//*

    }

    @Override
    public void destroy() {

    }
}
*/
