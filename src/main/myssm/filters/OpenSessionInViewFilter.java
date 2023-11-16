package myssm.filters;

import myssm.trans.TransactionManager;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.sql.SQLException;

/** 目的 就是 进入到 servlet之前 把 encoding = "UTF-8" 设置了；
 *  Qverride 快捷键 ctrl+o
 *  导包 快捷键 alt+enter
 */
@WebFilter("*.do")
public class OpenSessionInViewFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try{
            TransactionManager.beginTrans();//
            System.out.println("Begin开启事务...");
            filterChain.doFilter(servletRequest,servletResponse);
            TransactionManager.commit();//
            System.out.println("Commit提交事务...");

        } catch (Exception e){
            e.printStackTrace();
            try {
                TransactionManager.rollback();
                System.out.println("TransactionRollback...");
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    @Override
    public void destroy() {

    }
}
