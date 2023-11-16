package myssm.myspringmvc;


import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;

public class ViewBaseServlet extends HttpServlet {

    private TemplateEngine templateEngine;
    private OutputStream outputStream = null;
    @Override
    public void init() throws ServletException {

        // 1.获取ServletContext对象
        ServletContext servletContext = this.getServletContext();

        // 2.创建Thymeleaf解析器对象
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);

        // 3.给解析器对象设置参数
        //  HTML是默认模式，明确设置是为了代码更容易理解
        templateResolver.setTemplateMode(TemplateMode.HTML);

        //  设置前缀
        String viewPrefix = servletContext.getInitParameter("view-prefix");

        templateResolver.setPrefix(viewPrefix);

        //  设置后缀
        String viewSuffix = servletContext.getInitParameter("view-suffix");

        templateResolver.setSuffix(viewSuffix);

        //  设置缓存过期时间（毫秒）
        templateResolver.setCacheTTLMs(60000L);

        //  设置是否缓存
        templateResolver.setCacheable(true);

        // 设置服务器端编码方式
        templateResolver.setCharacterEncoding("utf-8");

        // 4.创建模板引擎对象
        templateEngine = new TemplateEngine();

        // 5.给模板引擎对象设置模板解析器
        templateEngine.setTemplateResolver(templateResolver);

    }

    protected void processTemplate(String templateName, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // 1.设置响应体内容类型和字符集
        resp.setContentType("text/html;charset=UTF-8");

        // 2.创建WebContext对象
        WebContext webContext = new WebContext(req, resp, getServletContext());

        // 3.处理模板数据
        templateEngine.process(templateName, webContext, resp.getWriter());
    }

    protected void processTemplate_DownLoad(String templateName, HttpServletRequest req, HttpServletResponse resp) throws IOException {//, ByteArrayOutputStream tempOutputStream) throws IOException {
       /* // 1. 设置响应体内容类型和字符集
        resp.setContentType("text/html;charset=UTF-8");

        // 2. 创建WebContext对象
        WebContext webContext = new WebContext(req, resp, getServletContext());

        // 3. 将模板内容写入临时缓冲区
        StringWriter templateWriter = new StringWriter();
        templateEngine.process(templateName, webContext, templateWriter);
        String templateOutput = templateWriter.toString();

        // 4. 创建一个临时的 ByteArrayOutputStream，用于存储文件下载内容
        //ByteArrayOutputStream tempOutputStream = new ByteArrayOutputStream();

        // 5. 文件下载操作，将内容写入临时输出流
        // ...

        // 6. 设置响应头和类

        // 7. 获取响应的输出流
        try ( OutputStream outputStream = resp.getOutputStream() ) {
            // 8. 将临时缓冲区中的内容写入响应输出流
            //tempOutputStream.writeTo(outputStream);
            tempOutputStream.writeTo(outputStream);

            // 9. 将模板引擎的输出写入响应输出流
            outputStream.write(templateOutput.getBytes());
        }*/
        resp.setContentType("text/html;charset=UTF-8");

        // 2.创建WebContext对象
        WebContext webContext = new WebContext(req, resp, getServletContext());

        // 3.处理模板数据
        templateEngine.process(templateName, webContext, resp.getWriter());
    }
}