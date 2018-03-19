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

    public DeviceUIBean() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || (getClass() != o.getClass()) && o.getClass() != BluetoothDevice.class) {
            return false;
        }

        if (o.getClass() == BluetoothDevice.class) {
            return device.equals(o);
        }
        DeviceUIBean that = (DeviceUIBean) o;

        return device.equals(that.device);
    }

    @Override
    public int hashCode() {
        return device.hashCode();
    }
}
