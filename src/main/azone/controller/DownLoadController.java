package azone.controller;

import azone.pojo.CartItem;
import azone.pojo.Order;
import azone.pojo.User;
import azone.service.OrderService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

// 这边是：server端 给 前端的响应
public class DownLoadController {
    private final static String utf8 ="utf-8";
    private OrderService orderService;
    //private String path = "/Users/hanchun/IdeaProjects/Practice_Space/pro24/src/main/webapp/download/";
    /**
     * 购物车 下载; 是 下载成 压缩包 版本
     */
    public void downLoadCart(HttpServletRequest request, HttpServletResponse response, List<String> stringList) throws Exception {
        //http://localhost:8080/pro24/download.do?operate=downLoadFile
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=\"multiple_files.zip\"");

        byte[] buffer = new byte[1024];
        ZipOutputStream zipOutputStream = null ;
        OutputStream outputStream = null ;

        try{
            outputStream = response.getOutputStream() ;
            zipOutputStream = new ZipOutputStream(outputStream);// 获取 response 的输出流 往 浏览器 端 写

            for( int i = 0 ; i < stringList.size() ; i++ ){
                String path = "/Users/hanchun/IdeaProjects/Practice_Space/pro24/src/main/webapp/download/"+ stringList.get(i);
                System.out.println( "下载路径 ： " + path );
                File file = new File(path);

                FileInputStream fileInputStream = new FileInputStream(file);
                zipOutputStream.putNextEntry(new ZipEntry(file.getName()));

                int bytesRead;
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    zipOutputStream.write(buffer, 0, bytesRead);
                }

                fileInputStream.close();
                zipOutputStream.closeEntry();
                System.out.println( stringList.get(i) + "下载完成" );
            }
            zipOutputStream.finish();
        } finally {
            zipOutputStream.close();
            outputStream.close();
        }
        //return "cart/cart";
    }

    /**分片下载根本没能实现 读取的就是整个文件的range；
     *  根本没能实现 分片下载
     */
    public String downLoadFile( HttpServletRequest request, HttpServletResponse response,String fileNamePath ) throws Exception {
        //http://localhost:8080/pro24/download.do?operate=downLoadFile
        System.out.println("下载了下载了！！！ fileNamePath : " + fileNamePath);
        String path = "/Users/hanchun/IdeaProjects/Practice_Space/pro24/src/main/webapp/download/"+ fileNamePath ;
        System.out.println(" Path : " + path );
        File file = new File(path);//下载到本地的路径；指定的下载某个文件；也 从数据里面查
        response.setCharacterEncoding(utf8);
        InputStream is = null ; // 读这个数据 往前端去写
        OutputStream os = null ;
        try{
            //分片下载   http  Range bytes=100-1000   bytes=100-【服务端 负责 把文件打散；客户端 起多个线程下载，然后 合并】==》【服务端支持 分片，客户端完成 合并】
            long fSize = file.length();//获取文件长度
            // 对response head 头 做设置
            response.setContentType("application/x-download");// 设置 "下载类型"；这样 前端 才知道你要做下载
            String fileName = URLEncoder.encode(file.getName(),utf8);
            response.addHeader("Content-Disposition","attachment;filename=" + fileName);//告诉 前端 我的文件要 存在 哪里==》前端会弹出一个另存为的对话框
            response.setHeader("Accept-Range","bytes");// 告诉前端 支持 分片 下载， 且反馈 分片单位

            response.setHeader("fSize",String.valueOf(fSize));//返回 java 客户端 文件长度
            response.setHeader("fName",fileName);//java 客户端 需要知道 你的 文件的 名字

            long pos = 0 , last = fSize-1 , sum = 0 ;// sum 是目前总共读取了 多大的文件

            long rangeLengh = last - pos + 1;
            String contentRange = new StringBuffer("bytes").append(pos).append("-").append(last).append("/").append(fSize).toString();
            System.out.println("Flag : "+ contentRange );
            response.setHeader("Content-Range",contentRange);
            response.setHeader("Content-Lenght",String.valueOf(rangeLengh));
            //
            os = new BufferedOutputStream(response.getOutputStream());// 获取 response 的输出流 往 浏览器 端 写
            is = new BufferedInputStream(new FileInputStream(file));//  往 本地文件 写 内容的 流
            is.skip(pos);// 从 pos 位置开始 读；也可以使用randomAccess，更灵活
            byte[] buffer = new byte[1024];
            int length = 0;
            //
            while( sum < rangeLengh ){// 不停的 读 直到 读完
                length = is.read(buffer,0,((rangeLengh-sum) <= buffer.length ? ((int)(rangeLengh-sum)) : buffer.length));//
                sum = sum + length ;
                os.write( buffer,0, length );
            }

            System.out.println("下载完成");

        } finally {
            if( is != null){
                is.close();
            }
            if( os != null){
                os.close();
            }
        }

        return "index";
    }

    public void downLoadFileList( HttpServletRequest request, HttpServletResponse response, HttpSession session ) throws Exception {
        System.out.println("进入到downLoadFileList里面了");
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=\"multiple_files.zip\"");

       /* User user = (User)session.getAttribute("curUser");
        Map<Integer, CartItem> cartMap = user.getCart().getCartItemMap();//Map<Integer,CartItem>
        List<String> stringList = new ArrayList<>();

        for ( Map.Entry<Integer, CartItem> entry : cartMap.entrySet() ) {
            Integer key = entry.getKey();
            CartItem cartItem = entry.getValue();
            String fileName = cartItem.getResource_id().getResourceName();
            stringList.add(fileName);
            System.out.println(" fileName : " +  fileName);
        }*/
        List<String> stringList = (List<String>)session.getAttribute("fileStringList");

        byte[] buffer = new byte[1024];
        ZipOutputStream zipOutputStream = null ;
        OutputStream outputStream = null ;

        try{
            outputStream = response.getOutputStream() ;
            zipOutputStream = new ZipOutputStream(outputStream);// // 获取 response 的输出流 往 浏览器 端 写

            for( int i = 0 ; i < stringList.size() ; i++ ){
                String path = "/Users/hanchun/IdeaProjects/Practice_Space/pro24/src/main/webapp/download/"+ stringList.get(i);
                System.out.println( "下载路径 ： " + path );
                File file = new File(path);

                FileInputStream fileInputStream = new FileInputStream(file);
                zipOutputStream.putNextEntry(new ZipEntry(file.getName()));

                int bytesRead;
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    zipOutputStream.write(buffer, 0, bytesRead);
                }

                fileInputStream.close();
                zipOutputStream.closeEntry();
                System.out.println( stringList.get(i) + "下载完成" );
            }
            zipOutputStream.finish();
        } finally {
            zipOutputStream.close();
            outputStream.close();
        }
        List<String> stringList_null = new ArrayList<>();
        session.setAttribute("fileStringList",stringList_null);

        //checkout( request, response, session);
        //return "index";
    }


}



