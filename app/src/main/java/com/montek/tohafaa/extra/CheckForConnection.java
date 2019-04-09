package com.montek.tohafaa.extra;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
public class CheckForConnection
{
   public static boolean getWifiStatus(Context context)
    {
        // To get System Connectivity status
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork)
        {
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)  // Check For Wifi Status
                return true;
            else
                return false;
        }
        return false;
    }
    public static boolean getMobileDataStatus(Context context)
    {
        // To get System Connectivity status
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork)
        {
            // Check For Mobile Data Status
            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return true;
            else
                return false;
        }
        return false;
    }
}