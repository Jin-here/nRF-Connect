package com.vgaw.nrfconnect.util.bluetooth.flags;

import java.util.ArrayList;
import java.util.List;

/**
 * @author caojin
 * @date 2018/3/15
 */

public class FlagsResolver {
    private static List<String> descriptionList = new ArrayList<>(6);

    static {
        descriptionList.add("LE Limited Discoverable Mode");
        descriptionList.add("LE General Discoverable Mode");
        descriptionList.add("BR/EDR Not Supported");
        descriptionList.add("LE and BR/EDR Capable (Controller)");
        descriptionList.add("LE and BR/EDR Capable (Host)");
        descriptionList.add("Reserved");
    }

    public static FlagsValue resolve(byte[] value) {
        byte raw = value[0];
        List<FlagsItem> resultList = new ArrayList<>(6);
        for (int i = 0;i < 5;i++) {
            int enabled = (raw >> i) & (0x01);
            resultList.add(new FlagsItem(i, i, descriptionList.get(i), enabled == 1));
        }
        resultList.add(new FlagsItem(5, 7, descriptionList.get(5), false));
        return new FlagsValue(resultList);
    }
}
