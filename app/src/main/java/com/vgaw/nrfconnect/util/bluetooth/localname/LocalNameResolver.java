package com.vgaw.nrfconnect.util.bluetooth.localname;

/**
 * @author caojin
 * @date 2018/3/15
 */

public class LocalNameResolver {
    public static String resolve(byte[] value) {
        return new String(value);
    }
}
