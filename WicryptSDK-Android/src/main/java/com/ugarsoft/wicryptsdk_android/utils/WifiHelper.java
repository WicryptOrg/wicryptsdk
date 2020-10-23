package com.ugarsoft.wicryptsdk_android.utils;

public class WifiHelper {
    public static final String WICRYPT_PRO_PREFIX = ".wc";

    public static boolean isWicryptProDevice(String ssid) {
        return ssid.endsWith(WICRYPT_PRO_PREFIX) ||
                ssid.endsWith(WICRYPT_PRO_PREFIX + "\"");
    }

}
