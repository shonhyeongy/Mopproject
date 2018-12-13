package com.example.sonhyeongi.bus_v1.Function.Device;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Net_Check {
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if(activeNetwork !=null){
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI){
                return true;
            }
            else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE){
                return true;
            }
        }
        return false;

    }


}
