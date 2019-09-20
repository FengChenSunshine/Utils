package com.duanlu.utils;

import java.io.Closeable;
import java.io.IOException;

/********************************
 * @name CloseUtils
 * @author 段露
 * @createDate 2019/3/7  14:00.
 * @updateDate 2019/3/7  14:00.
 * @version V1.0.0
 * @describe IO流关闭工具类.
 ********************************/
public class CloseUtils {

    private CloseUtils() {

    }

    public static void closeIO(Closeable... closeables) {
        if (null != closeables) {
            for (Closeable closeable : closeables) {
                if (null != closeable) {
                    try {
                        closeable.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void closeIOQuietly(Closeable... closeables) {
        if (null != closeables) {
            for (Closeable closeable : closeables) {
                if (null != closeable) {
                    try {
                        closeable.close();
                    } catch (IOException e) {
                        //nothing.
                    }
                }
            }
        }
    }

}
