package com.vgaw.nrfconnect.bean;

import android.bluetooth.BluetoothDevice;
import android.text.TextUtils;
import android.util.Log;

import com.vgaw.nrfconnect.page.main.tab.scanner.DeviceUIBean;
import com.vgaw.nrfconnect.util.HexTransform;

/**
 * @author caojin
 * @date 2018/3/1
 */

public class ScannerFilter {
    private String nameAddress;
    private String data;
    private int rssi;
    private boolean favorite;

    public static boolean deviceValid(ScannerFilter scannerFilter, DeviceUIBean deviceUIBean) {
        if (scannerFilter == null) {
            return true;
        }
        BluetoothDevice device = deviceUIBean.device;
        String deviceName = device.getName();
        boolean deviceFavorite = deviceUIBean.favorite;
        byte[] deviceScanRecord = deviceUIBean.scanRecord;
        int deviceRssi = deviceUIBean.rssi;
        return contain(deviceName, scannerFilter.nameAddress) &&
                contain(HexTransform.bytesToHexString(deviceScanRecord), scannerFilter.data) &&
                scannerFilter.favorite == deviceFavorite && scannerFilter.rssi <= deviceRssi;
    }

    private static boolean contain(String one, String filter) {
        return TextUtils.isEmpty(filter) || (one != null && one.contains(filter));
    }

    public ScannerFilter() {}

    public String getNameAddress() {
        return nameAddress;
    }

    public void setNameAddress(String nameAddress) {
        this.nameAddress = nameAddress;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
}
