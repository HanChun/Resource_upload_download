package powerNode.src.main;

import powerNode.src.core.Downloader;
import powerNode.src.util.LogUtils;

import java.util.Scanner;

/**
 * 程序的主入口
 */
public class Main {
    public static void main(String[] args){
        long startTime = System.currentTimeMillis() / 1000;
        String url = null ;
        if( args == null || args.length ==0 ){
            for(;;){
                LogUtils.info("请输入下载地址：");
                Scanner scanner = new Scanner(System.in);
                url = scanner.next();
                if(url!=null){
                    break;
                }
            }
        } else{
            url = args[0];
        }
        //*** https://www.zhihu.com/question/376805151
        //https://dldir1.qq.com/qqfile/qq/QQNT/7547c696/QQ_v6.9.17-12118.dmg
        //https://dldir1.qq.com/music/clntupate/mac/QQMusicMac_Mgr.dmg
        Downloader downloader = new Downloader();
        downloader.download(url);
        Downloader downloader1 = new Downloader();
        downloader1.download(url);
        long endTime = System.currentTimeMillis() / 1000;
        long timeTaken = endTime - startTime;
        System.out.println("Flag Time taken : " + timeTaken + " seconds");
    }
}


/** https://www.zhihu.com/question/376805151
 * 首先如果你真的在万兆网当中测试过的话，会发现只要配置得当，一个TCP连接也完全可以将一张万兆网卡的带宽占满了。
 * 所以“多线程下载一个大文件的速度更快”这句话本质上来说就是错误的。
 * 那为什么真实场景下用多线程下载似乎会快一些呢？
 * 延迟高的情况下，一个TCP连接要占满线路带宽，需要有足够大的TCP window，通常超过默认的TCP window上限，需要通过TCP window scale扩展来增加window大小。
 * 当年的操作系统对window scale的支持不太好，增加TCP连接数相当于变相增大window。操作系统的TCP流控实现不太合理的时候，遇到丢包可能速度会掉得很快，
 * 多个连接可以缓解这个问题，至少看到速度比较平缓，不会一下掉一半某些网站限制了单个TCP连接的速度，或者本身因为实现问题单个TCP连接就有性能上限（比如错误地使用了很小的buffer来做IO），
 * 多个连接也就可以增加性能。
 *
 * 作者：灵剑
 * 链接：https://www.zhihu.com/question/376805151/answer/1069449732
 */















