package tcking.github.com.giraffeplayer.example.vr250_example;

import android.util.Log;

/**
 *  author: xiefeng
 *  blog  : http://blankj.com
 *  time  : 2017/4/12
 *  desc  : 控制台日志相关工具类
 */
public class LogUtil {

    public static boolean DEBUG_FLAG = true; //是否打开日志打印

    public static void e(String tag, String msg) {
        if (DEBUG_FLAG) {
            Log.e(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (DEBUG_FLAG) {
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (DEBUG_FLAG) {
            Log.w(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (DEBUG_FLAG) {
            Log.d(tag, msg);
        }
    }

    public static void v(String tag, String msg) {
        if (DEBUG_FLAG) {
            Log.v(tag, msg);
        }
    }

    /**
     * 封装异常类打印方法，便于统一控制打开/关闭
     * @param e
     */
    public static void printStackTrace(Exception e) {
        if (DEBUG_FLAG) {
            e.printStackTrace();
        }
    }
}
