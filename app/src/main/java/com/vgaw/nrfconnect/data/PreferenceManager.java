package com.vgaw.nrfconnect.data;

import android.content.SharedPreferences;

import com.vgaw.nrfconnect.bean.ScannerFilter;
import com.vgaw.nrfconnect.thirdparty.json.JsonUtil;
import com.vgaw.nrfconnect.util.ContextUtil;

/**
 * @author caojin
 * @date 2018/3/1
 */

public class PreferenceManager {
    private static SharedPreferences getDefault() {
        return android.preference.PreferenceManager.getDefaultSharedPreferences(ContextUtil.getApplicationContext());
    }

    public static void setScannerFilter(ScannerFilter scannerFilter) {
        getDefault().edit().putString("scannerFilter", JsonUtil.toJson(scannerFilter)).commit();
    }

    public static ScannerFilter getScannerFilter() {
        return JsonUtil.fromJson(getDefault().getString("scannerFilter", null), ScannerFilter.class);
    }

    public static int getScannerPeriod() {
        return getDefault().getInt("scanner_period", -1);
    }

    public static void setScannerPeriod(int period) {
        getDefault().edit().putInt("scanner_period", period).apply();
    }
}
