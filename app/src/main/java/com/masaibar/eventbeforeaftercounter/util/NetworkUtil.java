package com.masaibar.eventbeforeaftercounter.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by masaibar on 2016/03/13.
 */
public class NetworkUtil {
    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        //参考：http://qiita.com/ikota/items/591860bcb9f3d9cb9fc9
        if(connectivityManager == null) {
            return false;
        }
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo == null) {
            return false;
        }

        return networkInfo.isConnectedOrConnecting();
    }
}
