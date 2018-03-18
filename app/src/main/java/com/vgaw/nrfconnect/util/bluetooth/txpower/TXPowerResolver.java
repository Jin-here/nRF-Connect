package com.vgaw.nrfconnect.util.bluetooth.txpower;

/**
 * Created by caojin on 2018/3/18.
 */

public class TXPowerResolver {
    public static String resolve(byte[] value) {
        byte b = value[0];
        return String.valueOf(b);
    }
}
