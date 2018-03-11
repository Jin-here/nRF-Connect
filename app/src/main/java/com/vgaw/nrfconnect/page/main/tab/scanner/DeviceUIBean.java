package com.vgaw.nrfconnect.page.main.tab.scanner;

import android.bluetooth.BluetoothDevice;

/**
 * Created by caojin on 2018/3/10.
 */

public class DeviceUIBean {
    public BluetoothDevice device;
    public int rssi;
    public byte[] scanRecord;

    public DeviceUIBean() {}

    public DeviceUIBean(BluetoothDevice device, int rssi, byte[] scanRecord) {
        this.device = device;
        this.rssi = rssi;
        this.scanRecord = scanRecord;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DeviceUIBean that = (DeviceUIBean) o;

        return device.equals(that.device);
    }

    @Override
    public int hashCode() {
        return device.hashCode();
    }
}
