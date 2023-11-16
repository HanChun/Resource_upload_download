package powerNode.src.core;

import powerNode.src.constant.Constant;
import powerNode.src.core.DownloadInfoThread;
import powerNode.src.core.DownloaderTask;
import powerNode.src.util.FileUtils;
import powerNode.src.util.HttpUtils;
import powerNode.src.util.LogUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.concurrent.*;

public class Downloader {
    public ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
    // newScheduledThreadPool(1): This method is used to create a ScheduledExecutorService that can schedule tasks to run
    // at a specified time or with a fixed delay.
    // The (1) argument specifies the number of threads in the pool. In this case, the pool has one thread.
    // 创建 线程 池 对象
    public ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(Constant.THREAD_NUM,Constant.THREAD_NUM,0,
            TimeUnit.SECONDS,new ArrayBlockingQueue<>(Constant.THREAD_NUM));//这里面 没有 非核心 线程
    private CountDownLatch countDownLatch = new CountDownLatch(Constant.THREAD_NUM);

    String httpFileName = null ;
    long localFileLength = 0 ;
    public void initializeBasicInfo( String url ){
        //1_获取 文件名
        httpFileName = HttpUtils.getHttpFileName(url);
        //2_文件下载 到 本机 的路径
        httpFileName = Constant.PATH + httpFileName ;
        //3_获取本地文件的大小
        localFileLength = FileUtils.getFileContentLength(httpFileName) ;

    }
    public void download(String url){
        initializeBasicInfo(url) ;
        //获取 连接 对象
        HttpURLConnection httpURLConnection = null ;
        DownloadInfoThread downloadInfoThread = null ;
        try {
            httpURLConnection = HttpUtils.getHttpURLConnection(url);

            int contentLength = httpURLConnection.getContentLength();// 通过 urlConnection 获取 文件的大小；

            if( localFileLength >= contentLength ){
                LogUtils.info("{}已经下载完毕，无需重新下载",httpFileName);
                return;
            }
            // 创建获取 下载信息 的 任务对象
            downloadInfoThread = new DownloadInfoThread(contentLength);
            // 将任务 交给 线程池 对象， 每隔 1s 执行一次
            scheduledExecutorService.scheduleAtFixedRate(downloadInfoThread,1,1,TimeUnit.SECONDS);
            // 切分任务
            ArrayList<Future> list = new ArrayList<>();
            split(url,list);

            countDownLatch.await(); //线程走到 这里；如果 其它线程 没有结束 这里就会等待

            /*list.forEach(future -> {
                try {
                    Object o = future.get();// 拿到 结果 目的是让 线程
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
            });*/
            // 合并文件
            if(merge(httpFileName)){
                //清除临时文件
                clearTemp(httpFileName);
            }


        } catch ( IOException  e ){
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            System.out.print("\r");
            System.out.print("下载完成");
            //关闭连接对象
            if ( httpURLConnection != null ) {
                httpURLConnection.disconnect();
            }
            //关闭
            scheduledExecutorService.shutdownNow();
            //关闭线程池
            poolExecutor.shutdown();
        }

    }
    // 把 文件切割为 多个 小部分 并实现 下载
    public void split( String url, ArrayList<Future> futureList ){
        try {
            // 获取 要 下载的 文件大小
            long contentLength = HttpUtils.getHttpFileContentLength(url);
            // 计算 切分 后 每块文件的 大小
            long size =  contentLength/Constant.THREAD_NUM;

            for( int i = 0 ; i < Constant.THREAD_NUM ; i++ ){// 从 0 开始，所以 结束是 N-1；
                long startPos = i*size ;
                long endPos ;
                if( i == (Constant.THREAD_NUM - 1)){// 说明 当前 已经是 最后一块了
                    endPos = 0;
                } else {
                    endPos = startPos + size ;
                }
                if( startPos != 0 ){
                    startPos++;
                }
                // 创建任务对象
                DownloaderTask downloaderTask = new DownloaderTask( url, startPos, endPos, i, countDownLatch);// countDownLatch
                // 将 任务 提交到 线程池 ； submit（）本质上也是调用execute（）方法
                Future<Boolean> future = poolExecutor.submit( downloaderTask );
                futureList.add(future);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 根据 下载的 文件的 名称顺序；把这些文件 读到内存 里面 ；然后再 合并 ，再写回磁盘

    /**
     * 文件合并
     * @param fileName
     * @return 返回 合并 是否 成功： True False
     */
    public boolean merge(String fileName){
        LogUtils.info("开始合并文件{}",fileName);
        byte[] buffer = new byte[Constant.BYTE_SIZE];
        int len = -1;

        try( RandomAccessFile accessFile = new RandomAccessFile(fileName,"rw") ){
            for( int i = 0 ; i< Constant.THREAD_NUM ; i++ ){
                try( BufferedInputStream bis = new BufferedInputStream( new FileInputStream( fileName + ".temp"+ i)) ){//读 这几个临时文件
                    while( (len = bis.read(buffer))!=-1 ){// 不停地读
                        accessFile.write(buffer,0,len);// 写出到硬盘的文件当中； ==》 最后写出的是一个文件 RandomAccessFile accessFile
                    }
                }
            }
            LogUtils.info("文件合并 完毕 File merge successfully:  {}"+fileName );
        } catch ( Exception e ){
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public boolean clearTemp(String fileName){
        for( int i = 0 ; i<Constant.THREAD_NUM ; i++){
            File file = new File(fileName+".temp"+i);
            file.delete();
        }
        return true;
    }
}
