package powerNode.src.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtils {
    /**
     * [1] 传入要 下载 文件的 url, 建立 和 服务器的 连接;
     * @param url
     * @return
     * @throws IOException
     */
    public static HttpURLConnection getHttpURLConnection(String url) throws IOException{
        URL httpUrl = new URL(url) ;
        HttpURLConnection httpURLConnection = (HttpURLConnection)httpUrl.openConnection();
        httpURLConnection.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/14.0.835.163 Safari/535.1");
        return httpURLConnection;
    }

    /**
     * [2] 获取下载文件大小; 传入文件的URL，就可以知道;
     * @param url
     * @return
     * @throws IOException
     */
    public static long getHttpFileContentLength(String url) throws IOException{
        int contentLength;
        HttpURLConnection httpURLConnection = null;
        try{
            httpURLConnection = getHttpURLConnection(url);
            contentLength = httpURLConnection.getContentLength();
        }finally {
            if( httpURLConnection!=null ){
                httpURLConnection.disconnect();
            }
        }
        return contentLength;
    }

    /**
     * [3] 分块下载
     */
    public static HttpURLConnection getHttpURLConnection(String url, long startPos, long endPos) throws IOException{
        HttpURLConnection httpURLConnection = getHttpURLConnection(url);
        LogUtils.info("下载的区间为：{}-{}",startPos,endPos);
        if( endPos!=0 ){// 向远程 请求的 url的文件:起始文件大小和
            httpURLConnection.setRequestProperty("RANGE","bytes="+startPos+"-"+endPos);
        }else{
            httpURLConnection.setRequestProperty("RANGE","bytes="+startPos+"-");
        }
        return httpURLConnection;
    }

    /**
     * [4] 获取 下载文件的名字
     */
    public static String getHttpFileName(String url){
        int index = url.lastIndexOf("/");
        return url.substring(index+1);
    }

}
