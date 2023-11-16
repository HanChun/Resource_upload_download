package azone.controller;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

/**
 * 文件上传原理：
 * 将一个（无论什么类型） 二进制的 数据内容，从本地发送到服务器。
 * 二进制：一长串 由 0和1
 *
 * 文件上传，断点续传，大文件秒传
 * http://fex.baidu.com/webuploader/
 * ？？？ MD5 没传过来
 *
 * 改进点：之前看到有人用redis做缓存，存储文件的md5值，这样做太麻烦了。
 * Webuploader会根据你的文件名，修改时间，生成一个唯一的md5值，可以用这个md5作为文件夹的名称；
 * 然后把这个文件的所有分片都存在这个文件夹下面。然后等所有分片都上传成功了之后。合并这个文件夹下面的所有分片，然后依次把分片删除，然后删除文件夹。
 *
 * https://www.cnblogs.com/zyzzz/p/17119661.html  ====》这块可以是设置请求头 做分片下载 请求头
 *
 * Command + Option + J (Mac)  ===》 "Network"
 */
public class UploadController {
    private final static String utf8 ="utf-8";
    public void upload( HttpServletRequest request, HttpServletResponse response) throws Exception{
        System.out.println("upload，访问进来了，访问进来了");
        //http://localhost:8080/pro24/upload.do
        response.setCharacterEncoding(utf8);
        Integer schunk = null; // 当前分片数
        Integer schunks = null;// 总分片数
        String name = null;
        String uploadPath ="/Users/hanchun/IdeaProjects/Practice_Space/pro24/src/main/webapp/download";// 本地的存储路径; 就是 上传到 server 端的数据
        BufferedOutputStream os = null;

        try{
            DiskFileItemFactory factory = new DiskFileItemFactory();
            factory.setSizeThreshold(1024);// 设置缓冲区【即， 不是直接往硬盘里面写，先读到内存里面，再往文件上面写】
            factory.setRepository(new File(uploadPath));// server 端存储目录
            ServletFileUpload upload = new ServletFileUpload(factory);// 用来帮助解析request
            upload.setFileSizeMax( 3l * 1024l * 1024l * 1024l );//设置单个文件 多大
            upload.setSizeMax( 3l * 1024l * 1024l * 1024l );//设置整体上传队列多大
            List<FileItem> items = upload.parseRequest(request);//解析 出来的文件

            /*
            // 1.解析出来 分片信息
            getInfo(fitems);
            // 2.写分片
            writeChunk(fitems);
            System.out.println("=========================");
            // 3. 文件合并: 当前 来的 分片为最后一个 就启动 合并的流程
            if( totalNum != null && totalNum.intValue() == totalNum.intValue()-1 ){// 合并条件 【1】分片是否为最后一个分片，分片数 不为空//【2】分片是否为最后一个分片
                merge();
            }*/
            for( FileItem item : items ){
                if( item.isFormField()){// 是不是 文件域
                    if( "chunk".equals(item.getFieldName()) ){//获得 当前分片数
                        schunk = Integer.parseInt(item.getString(utf8) );
                    }
                    if( "chunks".equals(item.getFieldName()) ){// 获得总分片数
                        schunks = Integer.parseInt(item.getString(utf8));
                    }
                    if( "name".equals(item.getFieldName()) ){
                        name = item.getString(utf8);
                    }
                }
            }

            for( FileItem item : items ){
                if( !item.isFormField() ){
                    String temFileName = name;
                    if( name != null ){
                        if( schunk != null ){
                            temFileName = schunk +"_" + name;
                        }
                        File temFile = new File( uploadPath,temFileName );
                        if(!temFile.exists()){ //断点续传 ---》 只有 分片文件 不存在的时候 才需要写
                            item.write(temFile);
                        }
                    }
                }
            }
            System.out.println("Flag1");
            //文件合并
            if( schunk != null && schunk.intValue() == schunks.intValue()-1 ){// 【1】分片是否为最后一个分片，分片数 不为空//[2]分片是否为最后一个分片
                File tempFile = new File(uploadPath,name);
                os = new BufferedOutputStream(new FileOutputStream(tempFile));

                for( int i=0 ; i < schunks ; i++ ){
                    File file = new File( uploadPath,i + "_" + name );
                    while( !file.exists() ){//保证 每一个分片 都存在
                        Thread.sleep(100);
                    }
                    byte[] bytes = FileUtils.readFileToByteArray(file);
                    os.write(bytes);
                    os.flush();
                    file.delete();
                }
                System.out.println("Flag2");
                os.flush();//o force any buffered data to be written immediately
            }
            response.getWriter().write("上传成功" + name );

        } finally {
            try{
                if(os != null){
                    os.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        System.out.println("Flag4");
        //return "index";//"redirect:page.do?operate=page&page=user/login"
    }

/*    public void getInfo(List<FileItem> fitems){
        System.out.println("当前分片获取结束=========================");
        for (FileItem item : fitems) {
            if (item.isFormField()) { // 是不是文件域；如果仅仅只是表单域
                String fieldName = item.getFieldName();
                String value = null;
                try {
                    value = item.getString("UTF-8");
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }

                if ("chunk".equals(fieldName)) { // 获得当前分片数
                    orderNum = Integer.parseInt(value);
                    System.out.println("Flag_1 schunk :" + orderNum);
                }

                if ("chunks".equals(fieldName)) { // 获得总分片数
                    totalNum = Integer.parseInt(value);
                    System.out.println("Flag_2 schunks :" + totalNum);
                }

                if ("name".equals(fieldName)) {
                    filename = value;
                    System.out.println("Flag_3 name :" + filename);
                }
            }
        }
    }

    //2. 写分片
    public void writeChunk(List<FileItem> items) throws Exception {
        for( FileItem item : items ){
            if( !item.isFormField() ){
                String temFileName = filename ;
                System.out.println("Flag_2 temFileName :" + temFileName );
                if( filename != null ){
                    if( totalNum != null ){
                        temFileName = totalNum + "_" + filename ;//写分片
                        System.out.println("Flag_2  temFileName :" + temFileName );{
                        }
                        File temFile = new File( uploadPath, temFileName );
                        if( !temFile.exists() ) //这就是 断点续传 的概念 ---》 只有 分片文件 不存在的时候 才需要写
                            item.write(temFile);
                    }
                }
            }
        }
    }
    //3. merge的逻辑
    public void merge(){
        System.out.println("********开始合并********： (!(+[])+{})[–[+\"\"][+[]]*[~+[]] + ~~!+[]]+({}+[])[[!+[]]*+[]]  ");
        File tempFile = new File( uploadPath , filename );  //创建一个 File 对象，用于表示最终合并后的文件的路径。name 表示文件名。
        try {
            os = new BufferedOutputStream(new FileOutputStream(tempFile));//创建一个输出流 os，用于将分片内容写入到合并后的文件中。
            for( int i = 0 ; i < totalNum ; i++ ){
                File file = new File( uploadPath,i + "_" + filename);
                while( !file.exists() ){                                // 保证 每一个分片 都存在;从第一个分片开始往里面写，如果第连续的分片不存在，就等
                    Thread.sleep(100);
                }
                byte[] bytes = FileUtils.readFileToByteArray(file);
                os.write(bytes);
                os.flush();
                file.delete();
            }
            System.out.println("Flag3");
            os.flush();// o force any buffered data to be written immediately
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }*/

}





