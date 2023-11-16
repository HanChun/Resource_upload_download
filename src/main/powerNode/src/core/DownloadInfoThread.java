package powerNode.src.core;

import powerNode.src.constant.Constant;

import java.util.concurrent.atomic.LongAdder;

/**
 * 这是一个 专门 用来获取 当前下载进度 的 一个线程
 */
public class DownloadInfoThread implements Runnable {
    //Runnable : A class that implements the Runnable interface in Java represents a task that can be executed concurrently.
    private long httpFileContentLength;
    //本地已下载文件的大小; LongAdder 把文件设置成 原子型，因为 它会被多个线程更改
    public static LongAdder finishedSize =  new LongAdder();
    public static volatile LongAdder downSize = new LongAdder();
    // In Java, volatile is a keyword used as a modifier for a variable to indicate that its value may be modified by multiple threads
    // and that changes to its value should always be visible to other threads immediately.
    public double preSize;

    public DownloadInfoThread( long httpFileContentLength ){
        this.httpFileContentLength = httpFileContentLength ;
    }

    @Override
    public void run() {
        String httpFileSize = String.format("%.2f",httpFileContentLength/Constant.MB);

        int speed = (int)(( downSize.doubleValue() - preSize )/1024d);
        preSize = downSize.doubleValue();//这个 大小 会不断地更新； 算完一次速度后，当前的 preSize 就被更新为 downSize；

        double remainSize = httpFileContentLength - finishedSize.doubleValue() - downSize.doubleValue();
        //计算剩余时间
        String remainTime = String.format("%.1f", remainSize / 1024d / speed);

        if ( "Infinity".equalsIgnoreCase(remainTime) ) {
            remainTime = "-";
        }

        //已下载大小
        String currentFileSize = String.format("%.2f", (downSize.doubleValue() - finishedSize.doubleValue()) / Constant.MB);

        String downInfo = String.format("已下载 %smb/%smb,速度 %skb/s,剩余时间 %ss",
                currentFileSize, httpFileSize, speed, remainTime);

        System.out.print("\r");
        System.out.print(downInfo);
    }

}
