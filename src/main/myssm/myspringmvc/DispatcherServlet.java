package myssm.myspringmvc;

import myssm.ioc.BeanFactory;
import myssm.util.StringUtil;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@WebServlet("*.do")
public class DispatcherServlet extends ViewBaseServlet{

    //private Map<String,Object> beanMap = new HashMap<>();
    private BeanFactory beanFactory;
    public DispatcherServlet(){
    }

    public void init() throws ServletException {
        super.init();
        //beanFactory =  new ClassPathXmlApplicationContext(); 这块优化，the initialization of this part has been put in to "ContextLoaderListener"
        ServletContext application = getServletContext();
        Object beanFactoryObj = application.getAttribute("beanFactory");
        if( beanFactoryObj!=null){
            beanFactory = (BeanFactory) beanFactoryObj ;
        }else {
            throw new RuntimeException("IOC container establishment failure.");
        }
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //设置编码
        //request.setCharacterEncoding("UTF-8"); ===> 这一块 在 dispatcherServlet里面设置了

        String servletPath = request.getServletPath();
        servletPath = servletPath.substring(1);
        int lastDotIndex = servletPath.lastIndexOf(".do") ;
        servletPath = servletPath.substring(0,lastDotIndex);

        System.out.println(" servletPath : " + servletPath);

        Object controllerBeanObj = beanFactory.getBean(servletPath);

        String operate = request.getParameter("operate");
        if( StringUtil.isEmpty(operate) ){
            operate = "index" ;
        }

        try {
            Method[] methods = controllerBeanObj.getClass().getDeclaredMethods();
            for( Method method : methods ){
                if( operate.equals(method.getName()) ){
                    //1.统一获取请求参数

                    //1-1.获取当前方法的参数，返回参数数组
                    Parameter[] parameters = method.getParameters();
                    //1-2.parameterValues 用来承载参数的值
                    Object[] parameterValues = new Object[parameters.length];
                    for ( int i = 0 ; i < parameters.length ; i++ ) {
                        Parameter parameter = parameters[i] ;
                        String parameterName = parameter.getName() ;
                        System.out.println(" Parameter :  " + parameter + " ; ParameterName : " + parameterName  );

                        //如果参数名是request,response,session 那么就不是通过请求中获取参数的方式了
                        if( "request".equals(parameterName) ){
                            parameterValues[i] = request ;
                        }else if("response".equals(parameterName)){
                            parameterValues[i] = response ;
                        }else if("session".equals(parameterName)){
                            parameterValues[i] = request.getSession() ;
                        }else{
                            //从请求中获取参数值
                            String parameterValue = request.getParameter(parameterName);

                            String typeName = parameter.getType().getName();
                            System.out.println(" TypeName : "+ typeName + " ; ParameterValue :  " + parameterValue );
                            Object parameterObj = parameterValue ;

                            if( parameterObj!=null) {
                                if ("java.lang.Integer".equals(typeName)) {
                                    parameterObj = Integer.parseInt(parameterValue);
                                }
                            }
                            parameterValues[i] = parameterObj ;
                        }
                    }
                    //2.controller组件中的方法调用
                    method.setAccessible(true);

                    System.out.println(" method.getName() : " + method.getName() + " ; method.getParameters().length :" + method.getParameters().length);

                     //System.out.println(" controllerBeanObj : " + controllerBeanObj.);
                    Object returnObj = method.invoke(controllerBeanObj,parameterValues);

                    //3.视图处理
                    String methodReturnStr = (String)returnObj ;
                    System.out.println(methodReturnStr);
                    /*System.out.println(returnObj.getClass().getName());
                    System.out.println(returnObj.getClass().getName().equals("java.io.ByteArrayOutputStream"));

                    if( returnObj.getClass().getName().equals("java.io.ByteArrayOutputStream")){
                        methodReturnStr = null;
                    }else {
                        methodReturnStr = (String)returnObj ;
                    }*/

                    if( methodReturnStr != null && methodReturnStr.startsWith("redirect:") ){        //比如：  redirect:fruit.do
                        String redirectStr = methodReturnStr.substring("redirect:".length());
                        response.sendRedirect(redirectStr);
                    }else if(  methodReturnStr != null ){
                            super.processTemplate(methodReturnStr,request,response);    // 比如：  "edit"
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}







