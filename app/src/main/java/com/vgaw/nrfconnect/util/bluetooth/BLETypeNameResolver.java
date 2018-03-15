package com.vgaw.nrfconnect.util.bluetooth;

import com.vgaw.nrfconnect.util.HexTransform;
import com.vgaw.nrfconnect.util.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dell on 2018/3/15.
 */

public class BLETypeNameResolver {
    public static final byte TYPE_FLAGS = 0x01;
    public static final byte TYPE_MANUFACTURER_SPECIFIC_DATA = (byte) 0xFF;
    public static final byte TYPE_COMPLETE_LOCAL_NAME = 0x09;
    public static final byte TYPE_SHORTENED_LOCAL_NAME = 0x08;

    private static Map<Byte, String> nameMap = new HashMap<>();

    static {
        nameMap.put(TYPE_FLAGS, "Flags");
        nameMap.put(TYPE_MANUFACTURER_SPECIFIC_DATA, "Manufacturer Specific Data");
        nameMap.put(TYPE_COMPLETE_LOCAL_NAME, "Complete Local Name");
        nameMap.put(TYPE_SHORTENED_LOCAL_NAME, "Shortened Local Name");
    }

    public static String getName(byte raw) {
        return Utils.nullTo(nameMap.get(raw), HexTransform.byteToHexString(raw));
    }
}
