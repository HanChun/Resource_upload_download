package powerNode.src.util;

import java.io.File;

public class FileUtils {
    /**
     * 获取本地文件的大小
     * 传入文件地址；
     */
    public static long getFileContentLength( String path ){
        File file = new File(path);
        return file.exists() && file.isFile() ? file.length() :0;
    }
}
