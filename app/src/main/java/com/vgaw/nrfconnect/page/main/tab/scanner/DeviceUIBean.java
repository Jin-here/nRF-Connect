package com.vgaw.nrfconnect.page.main.tab.scanner;

import android.bluetooth.BluetoothDevice;

/**
 * @author caojin
 * @date 2018/3/10
 */

public class DeviceUIBean {
    public BluetoothDevice device;
    public int rssi;
    public byte[] scanRecord;
    public boolean expanded;
    public boolean deviceFragmentAdded;
    public boolean advertiseStopped;
    public long period;
    public boolean favorite;

    public DeviceUIBean() {}
}
