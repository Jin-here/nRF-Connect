package com.vgaw.nrfconnect.common;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.vgaw.nrfconnect.data.PreferenceManager;
import com.vgaw.nrfconnect.util.bluetooth.BLEDataResolver;

import java.io.IOException;

/**
 * @author caojin
 * @date 2018/2/27
 */

public class BLEManager implements BluetoothAdapter.LeScanCallback {
    private static final String TAG = "BLEManager";

    private static final int REQUEST_CODE_PERMISSION = 0x00;
    private BluetoothAdapter mBluetoothAdapter;
    private Handler mHandler;
    private BLEListener listener;
    private Activity mActivity;
    private Fragment mFragment;

    private boolean scanning;

    public static boolean supported(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
    }

    public static boolean enabled(Context context) {
        final BluetoothManager bluetoothManager =
                (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter mBluetoothAdapter = bluetoothManager.getAdapter();
        return mBluetoothAdapter != null && mBluetoothAdapter.isEnabled();
    }

    public BLEManager(Fragment fragment) {
        this.mFragment = fragment;
        this.mActivity = fragment.getActivity();
        mHandler = new Handler();
    }

    public void setBLEListener(BLEListener listener) {
        this.listener = listener;
    }

    public boolean scanning() {
        return this.scanning;
    }

    public boolean startScan(Context context) {
        if (checkPermission()) {
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
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        try {
            BLEDataResolver.group(scanRecord);
        } catch (IOException e) {
            e.printStackTrace();
        }
        notifyLeScan(device, rssi, scanRecord);
    }

    private boolean checkPermission() {
        boolean gotPermission = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        if (!gotPermission) {
            mFragment.requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_PERMISSION);
        }
        return gotPermission;
    }

    private void notifyLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        if (listener != null) {
            listener.onLeScan(device, rssi, scanRecord);
        }
    }

    private void notifyScanStarted() {
        scanning = true;
        if (listener != null) {
            listener.onScanStarted();
        }
    }

    private void notifyScanStopped() {
        scanning = false;
        if (listener != null) {
            listener.onScanStopped();
        }
    }

    private Runnable scanRunnable = new Runnable() {
        @Override
        public void run() {
            stopScan(mActivity);
        }
    };

    public interface BLEListener {
        void onScanStarted();

        void onScanStopped();

        void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord);
    }
}
