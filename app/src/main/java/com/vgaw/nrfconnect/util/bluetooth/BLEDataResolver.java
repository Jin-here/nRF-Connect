package com.vgaw.nrfconnect.util.bluetooth;

import android.util.Log;

import com.vgaw.nrfconnect.util.HexTransform;
import com.vgaw.nrfconnect.util.bluetooth.flags.FlagsResolver;
import com.vgaw.nrfconnect.util.bluetooth.localname.LocalNameResolver;
import com.vgaw.nrfconnect.util.bluetooth.manufacturer.ManufacturerResolver;
import com.vgaw.nrfconnect.util.bluetooth.txpower.TXPowerResolver;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author caojin
 * @date 2018/3/15
 *
 * 1. 获取所有列表，可显示
 * 1. type和value
 * 2. value可以切换处理过的结果和原始数据
 * 2. 获取所有的flags和services
 */

public class BLEDataResolver {
    public static List<BLEData> group(byte[] raw) throws IOException {
        Log.d("BLE", "group: " + HexTransform.bytesToHexString(raw));
        List<BLEData> resultList = new ArrayList<>();
        ByteArrayInputStream in = new ByteArrayInputStream(raw);
        for (; ; ) {
            BLEData bleData = readOneGroup(in);
            if (bleData != null) {
                resultList.add(bleData);
            } else {
                break;
            }
        }
        Log.d("BLE", "group: " + resultList);
        return resultList;
    }

    // 1eff06000109200008fc
    // 3b464a3223c84b377a4d
    // 9c8d8d0dc7a4bbc4c23c
    // aa000000000000000000
    // 00000000000000000000
    // 00000000000000000000
    // 0000
    public static BLEData readOneGroup(ByteArrayInputStream in) throws IOException {
        int groupLength = in.read();
        if (groupLength > 0) {
            byte type = (byte) in.read();
            byte[] value = new byte[groupLength - 1];
            in.read(value);

            return new BLEData(type, value);
        }
        return null;
    }

    public static String[] resolve(BLEData data) {
        switch (data.getType()) {
            case BLETypeNameResolver.TYPE_FLAGS:
                return new String[]{BLETypeNameResolver.getName(data.getType()),
                        String.valueOf(FlagsResolver.resolve(data.getValue()))};
            case BLETypeNameResolver.TYPE_MANUFACTURER_SPECIFIC_DATA:
                return new String[]{BLETypeNameResolver.getName(data.getType()),
                        String.valueOf(ManufacturerResolver.resolve(data.getValue()))};
            case BLETypeNameResolver.TYPE_COMPLETE_LOCAL_NAME:
            case BLETypeNameResolver.TYPE_SHORTENED_LOCAL_NAME:
                return new String[]{BLETypeNameResolver.getName(data.getType()),
                        LocalNameResolver.resolve(data.getValue())};
            case BLETypeNameResolver.TYPE_TX_POWER:
                return new String[]{BLETypeNameResolver.getName(data.getType()),
                        TXPowerResolver.resolve(data.getValue())};
        }
        return new String[]{HexTransform.byteToHexString(data.getType()),
                HexTransform.bytesToHexString(data.getValue())};
    }
}
