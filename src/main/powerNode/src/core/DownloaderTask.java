package powerNode.src.core;

import powerNode.src.constant.Constant;
import powerNode.src.core.DownloadInfoThread;
import powerNode.src.util.HttpUtils;
import powerNode.src.util.LogUtils;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

/**
 * 分块 的 下载 任务;
 */
public class DownloaderTask implements Callable<Boolean> {
    /**
     * By using Callable<Boolean> with ExecutorService, you can perform computations asynchronously,
     * and the Future allows you to retrieve the result once the task is completed.
     */
    private String url;// 下载链接
    private long startPos;
    private long endPos;

    //标识 当前 是 下载的 哪一部分
    private int part;
    private CountDownLatch countDownLatch;
    public DownloaderTask( String url, long startPos, long endPos, int part, CountDownLatch countDownLatch ) {
        this.url = url;
        this.startPos = startPos;
        this.endPos = endPos;
        this.part = part;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public Boolean call() throws Exception {

        String httpFileName = HttpUtils.getHttpFileName(url);

        // httpFileName 标识 当前 文件被 分为 的第几块 正在 被下载
        httpFileName = httpFileName + ".temp" + part;

        // 下载 路径
        httpFileName = Constant.PATH + httpFileName;
        System.out.println("Flag_httpFileName : "+ httpFileName );

        HttpURLConnection httpURLConnection = HttpUtils.getHttpURLConnection( url, startPos, endPos );

        try(
                InputStream input = httpURLConnection.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(input);
                RandomAccessFile accessFile = new RandomAccessFile(httpFileName,"rw");//这块是下载； why？如果 未来做 断点下载 会有帮助
           ){
            byte[] buffer = new byte[Constant.BYTE_SIZE];// 每次都读取 自定义的 BYTE_SIZE
            int len = -1;
            while ( (len = bis.read(buffer))!=-1 ){
                // 1秒内 下载 数据之和, 通过 原子类 进行操作； 把downSize 改成 原子型的了
                DownloadInfoThread.downSize.add(len);
                accessFile.write(buffer,0,len);
            }
        } catch ( FileNotFoundException e ) {
            LogUtils.error("下载文件不存在{}", url);
            return false;
        } catch ( Exception e ) {
            LogUtils.error("下载出现异常");
            return false;
        } finally {
            httpURLConnection.disconnect();
            countDownLatch.countDown();//每个线程 执行 结束，这里 就减1； 全部执行完毕后，这里置为0
        }
        return true;
    }
}

/**
 * import java.util.concurrent.*;
 *
 * public class Main {
 *     public static void main(String[] args) {
 *         ExecutorService executor = Executors.newSingleThreadExecutor();
 *
 *         Callable<Boolean> myTask = new MyCallableTask();
 *
 *         Future<Boolean> future = executor.submit(myTask);
 *
 *         try {
 *             Boolean result = future.get(); // This will block until the task is completed and return the result.
 *             System.out.println("Result: " + result);
 *         } catch (InterruptedException | ExecutionException e) {
 *             e.printStackTrace();
 *         }
 *
 *         executor.shutdown();
 *     }
 * }
 *
 * class MyCallableTask implements Callable<Boolean> {
 *     @Override
 *     public Boolean call() throws Exception {
 *         // Perform some computation or task here
 *         // For example, check if a condition is true and return the result as a Boolean value.
 *
 *         return true; // or false, based on your implementation.
 *     }
 * }
 */