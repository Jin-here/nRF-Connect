package com.vgaw.nrfconnect.util.bluetooth;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dell on 2018/3/15.
 */

public class BLETypeNameResolver {
    public static final byte TYPE_FLAGS = 0x01;
    public static final byte TYPE_MANUFACTURER_SPECIFIC_DATA = (byte) 0xFF;

    private static Map<Byte, String> nameMap = new HashMap<>();

    static {
        nameMap.put(TYPE_FLAGS, "Flags");
        nameMap.put(TYPE_MANUFACTURER_SPECIFIC_DATA, "Manufacturer Specific Data");
    }

    public static String getName(byte raw) {
        return nameMap.get(raw);
    }
}
