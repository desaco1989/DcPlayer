package tcking.github.com.giraffeplayer.example.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;

/**
 * Created by desaco on 2017/8/2.
 */

public class CommonUtils {
    /**
     * 获取屏幕宽高，单位px
     *
     * @param context
     * @return
     */
//    public static Point getScreenMetrics(Context context){
//        DisplayMetrics dm = context.getResources().getDisplayMetrics();
//        int screenWidth = dm.widthPixels;
//        int screenHeight = dm.heightPixels;
//        return new Point(screenWidth, screenHeight);
//    }

    public static int getDeviceScreenHeight(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int screenHeight = dm.heightPixels;
        return screenHeight;
    }

    public static int getDeviceScreenWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        return screenWidth;
    }

    //是否有网络
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                //mNetworkInfo.isAvailable();
                return true;//有网
            }
        }
        return false;//没有网
    }
}
