package com.ugarsoft.wicryptsdk_android.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class WifiHelper {
    public static final String WICRYPT_PRO_PREFIX = ".wc";

    public static boolean isWicryptProDevice(String ssid) {
        return ssid.endsWith(WICRYPT_PRO_PREFIX) ||
                ssid.endsWith(WICRYPT_PRO_PREFIX + "\"");
    }

    public static String getMacAddress(Context context){
        WifiManager wifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null){
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            return wifiInfo.getMacAddress();
        }
        return null;
    }

}
