package com.shenjing.dimension.dimension.base.util.file;

import java.io.Closeable;


/**
 * Created by Administrator on 2017/8/22.
 */

public class CloseableUtils {
    public static void close(Closeable...closeables ){
        for (Closeable closeable : closeables) {
            if(closeable != null){
                try {
                    closeable.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
