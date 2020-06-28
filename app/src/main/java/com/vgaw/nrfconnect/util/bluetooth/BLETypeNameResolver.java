package com.vgaw.nrfconnect.util.bluetooth;

import com.vgaw.nrfconnect.util.HexTransform;
import com.vgaw.nrfconnect.util.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author caojin
 * @date 2018/3/15
 */

public class BLETypeNameResolver {
    public static final byte TYPE_FLAGS = 0x01;
    public static final byte TYPE_MANUFACTURER_SPECIFIC_DATA = (byte) 0xFF;
    public static final byte TYPE_COMPLETE_LOCAL_NAME = 0x09;
    public static final byte TYPE_SHORTENED_LOCAL_NAME = 0x08;
    public static final byte TYPE_TX_POWER = 0x0A;
    public static final byte TYPE_COMPLETE_SERVICE_UUIDS_16 = 0x03;
    public static final byte TYPE_COMPLETE_SERVICE_UUIDS_32 = 0x05;
    public static final byte TYPE_COMPLETE_SERVICE_UUIDS_128 = 0x07;
    public static final byte TYPE_SERVICE_DATA_UUID_16 = 0x16;
    public static final byte TYPE_SERVICE_DATA_UUID_32 = 0x20;
    public static final byte TYPE_SERVICE_DATA_UUID_128 = 0x21;

    private static Map<Byte, String> nameMap = new HashMap<>();

    static {
        nameMap.put(TYPE_FLAGS, "Flags");
        nameMap.put(TYPE_MANUFACTURER_SPECIFIC_DATA, "Manufacturer Specific Data");
        nameMap.put(TYPE_COMPLETE_LOCAL_NAME, "Complete Local Name");
        nameMap.put(TYPE_SHORTENED_LOCAL_NAME, "Shortened Local Name");
        nameMap.put(TYPE_TX_POWER, "Tx Power Level");
        nameMap.put(TYPE_COMPLETE_SERVICE_UUIDS_16, "Complete List of 16-bit Service UUIDs");
        nameMap.put(TYPE_COMPLETE_SERVICE_UUIDS_32, "Complete List of 32-bit Service UUIDs");
        nameMap.put(TYPE_COMPLETE_SERVICE_UUIDS_128, "Complete List of 128-bit Service UUIDs");
        nameMap.put(TYPE_SERVICE_DATA_UUID_16, "Service Data");
        nameMap.put(TYPE_SERVICE_DATA_UUID_32, "Service Data");
        nameMap.put(TYPE_SERVICE_DATA_UUID_128, "Service Data");
    }

    public static String getName(byte raw) {
        return Utils.nullTo(nameMap.get(raw), HexTransform.byteToHexString(raw));
    }
}
