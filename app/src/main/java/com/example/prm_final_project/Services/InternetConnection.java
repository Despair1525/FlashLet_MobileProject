package com.example.prm_final_project.Services;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

public class InternetConnection {

    public static boolean isConnectedToInternet(Context context){
        ConnectivityManager connectivity = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo netInfo = connectivity.getActiveNetworkInfo();
//        //should check null because in airplane mode it will be null
//        NetworkCapabilities nc = connectivity.getNetworkCapabilities(connectivity.getActiveNetwork());
//        String downSpeed = nc.getLinkDownstreamBandwidthKbps() +"";
//        String upSpeed = nc.getLinkUpstreamBandwidthKbps() +"";
//        Log.i("INTERNET-CONNECTIONS","downSpeed("+downSpeed+")");
//        Log.i("INTERNET-CONNECTIONS","upSpeed("+upSpeed+")");

        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }
        }

        return false;
    }


}
