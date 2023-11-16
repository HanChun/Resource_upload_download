package myssm.filters;

import myssm.util.StringUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/** 目的 就是 进入到 servlet之前 把 encoding = "UTF-8" 设置了；
 *  Qverride 快捷键 ctrl+o
 *  导包 快捷键 alt+enter
 */
@WebFilter( urlPatterns = {"*.do"}, initParams = { @WebInitParam(name="encoding",value="UTF-8")} )
public class CharacterEncodingFilter implements Filter {
    private String encoding = "UTF-8" ;
    @Override
    public void init( FilterConfig filterConfig ) throws ServletException {
        String encodingStr = filterConfig.getInitParameter("encoding");//如果在 注解 里面 配置了  @WebInitParam(name="encoding",value="GBK") ===》就会读取里面的值
        if( StringUtil.isNotEmpty(encodingStr) ){
            encoding = encodingStr ;
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        ((HttpServletRequest)servletRequest).setCharacterEncoding(encoding);
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {

    }
}
