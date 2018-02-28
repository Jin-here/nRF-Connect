package com.vgaw.nrfconnect.common;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

/**
 * Created by caojin on 2018/2/27.
 */

public class BLEManager implements BluetoothAdapter.LeScanCallback {
    private static final int REQUEST_ENABLE_BT = 0x01;
    private BluetoothAdapter mBluetoothAdapter;

    public boolean supported(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
    }

    public void openBLE(Activity activity) {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
    }

    public void closeBLE() {}

    public boolean startScan(Context context) {
        final BluetoothManager bluetoothManager =
                (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        if (mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()) {
            return mBluetoothAdapter.startLeScan(this);
        }
        return false;
    }

    public void stopScan(Context context) {
        final BluetoothManager bluetoothManager =
                (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        if (mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.stopLeScan(this);
        }
    }

    public void connectDevice() {

    }

    public void disconnectDevice() {

    }

    @Override
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {

    }
}
