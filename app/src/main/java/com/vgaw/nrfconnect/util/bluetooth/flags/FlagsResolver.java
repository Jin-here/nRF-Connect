package com.vgaw.nrfconnect.util.bluetooth.flags;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2018/3/15.
 */

public class FlagsResolver {
    private static List<String> descriptionList = new ArrayList<>(6);

    static {
        descriptionList.add("LE Limited Discoverable Mode");
        descriptionList.add("LE General Discoverable Mode");
        descriptionList.add("BR/EDR Not Supported. Bit 37 of LMP\n" +
                "Feature Mask Definitions (Page 0)");
        descriptionList.add("Simultaneous LE and BR/EDR to Same\n" +
                "Device Capable (Controller). Bit 49 of\n" +
                "LMP Feature Mask Definitions (Page 0)");
        descriptionList.add("Simultaneous LE and BR/EDR to Same\n" +
                "Device Capable (Host). Bit 66 of LMP\n" +
                "Feature Mask Definitions (Page 1)");
        descriptionList.add("Reserved for future use");
    }

    public static FlagsValue resolve(byte[] value) {
        byte raw = value[0];
        List<FlagsItem> resultList = new ArrayList<>(6);
        for (int i = 0;i < 5;i++) {
            int enabled = (raw >> i) & (0x01);
            resultList.add(new FlagsItem(i, i, descriptionList.get(i), enabled == 1));
        }
        resultList.add(new FlagsItem(5, 7, descriptionList.get(5), false));
        Log.d("BLE", "resolve: " + resultList);
        return new FlagsValue(resultList);
    }
}
