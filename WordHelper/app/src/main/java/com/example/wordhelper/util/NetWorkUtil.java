package com.example.wordhelper.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetWorkUtil {

    public static boolean isNetWorkAvailable(Context context){
        ConnectivityManager manager =(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(manager!= null){
            NetworkInfo info =manager.getActiveNetworkInfo();
            if(info !=null &&info.isConnected()){
                return info.getState() == NetworkInfo.State.CONNECTED;
            }
        }
        return false;
    }
}
