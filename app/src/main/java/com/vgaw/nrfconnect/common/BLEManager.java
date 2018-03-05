package com.vgaw.nrfconnect.common;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.ListView;

import com.vgaw.nrfconnect.data.PreferenceManager;

/**
 * Created by caojin on 2018/2/27.
 */

public class BLEManager implements BluetoothAdapter.LeScanCallback {
    private static final String TAG = "BLEManager";
    private static final int REQUEST_ENABLE_BT = 0x01;
    private BluetoothAdapter mBluetoothAdapter;
    private ListView listView;
    private Context context;
    private Handler mHandler;
    private BLEListener listener;

    public BLEManager(Context context) {
        this.context = context;
        mHandler = new Handler();
    }

    public void setListView(ListView listView) {
        this.listView = listView;
    }

    public void setBLEListener(BLEListener listener) {
        this.listener = listener;
    }

    public void check(Fragment fragment) {
        if (supported(this.context)) {
            final BluetoothManager bluetoothManager =
                    (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
            mBluetoothAdapter = bluetoothManager.getAdapter();
            if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
                openBLE(fragment);
            }
        }
    }

    public boolean supported(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
    }

    public void openBLE(Fragment fragment) {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        fragment.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
    }

    public void closeBLE() {}

    public boolean isDiscovering() {
        final BluetoothManager bluetoothManager =
                (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        if (mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()) {
            return mBluetoothAdapter.isDiscovering();
        }
        return false;
    }

    public boolean startScan(Context context) {
        final BluetoothManager bluetoothManager =
                (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        if (mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()) {

            if (!mBluetoothAdapter.isDiscovering()) {
                boolean started = mBluetoothAdapter.startLeScan(this);
                if (started) {
                    int scanPeriod = PreferenceManager.getScannerPeriod();
                    mHandler.removeCallbacks(scanRunnable);
                    if (scanPeriod != -1) {
                        mHandler.postDelayed(scanRunnable, scanPeriod);
                    }

                    notifyScanStarted();
                }
                return started;
            }
        }
        return false;
    }

    public void stopScan(Context context) {
        final BluetoothManager bluetoothManager =
                (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        if (mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()) {
            mHandler.removeCallbacks(scanRunnable);

            mBluetoothAdapter.stopLeScan(this);

            notifyScanStopped();
        }
    }

    public void connectDevice() {

    }

    public void disconnectDevice() {

    }

    @Override
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {}

    private void notifyScanStarted() {
        if (listener != null) {
            listener.onScanStarted();
        }
    }

    private void notifyScanStopped() {
        if (listener != null) {
            listener.onScanStopped();
        }
    }

    private Runnable scanRunnable = new Runnable() {
        @Override
        public void run() {
            stopScan(context);
        }
    };

    public interface BLEListener {
        void onScanStarted();

        void onScanStopped();
    }
}
