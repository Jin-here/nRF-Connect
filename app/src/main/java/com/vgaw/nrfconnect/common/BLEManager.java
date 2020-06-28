package com.vgaw.nrfconnect.common;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.vgaw.nrfconnect.R;
import com.vgaw.nrfconnect.data.PreferenceManager;
import com.vgaw.nrfconnect.util.ToastUtil;
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
            if (!gpsValid()) {
                return false;
            }
            final BluetoothManager bluetoothManager =
                    (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
            mBluetoothAdapter = bluetoothManager.getAdapter();
            if (mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()) {
                if (!mBluetoothAdapter.isDiscovering()) {
                    boolean started = mBluetoothAdapter.startLeScan(this);
                    if (started) {
                        notifyScanStarted();

                        int scanPeriod = PreferenceManager.getScannerPeriod();
                        if (scanPeriod != -1) {
                            mHandler.removeCallbacks(stopScanRunnable);
                            if (scanPeriod != -1) {
                                mHandler.postDelayed(stopScanRunnable, scanPeriod);
                            }
                        }
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
            mHandler.removeCallbacks(stopScanRunnable);

            mBluetoothAdapter.stopLeScan(this);

            notifyScanStopped();
        }
    }

    public void connectDevice() {

    }

    public void disconnectDevice() {

    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (checkPermission()) {
                startScan(mActivity);
            }
        }
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

    private boolean gpsValid() {
        // TODO: 2020/6/28
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
            LocationManager alm = (LocationManager) mActivity.getSystemService(Context.LOCATION_SERVICE);
            if (!alm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)){
                ToastUtil.show(R.string.main_hint_bluetooth_gps_not_open);
                return false;
            }
        }
        return true;
    }

    private boolean checkPermission() {
        // TODO: 2020/6/28
        String permissionNeed = null;
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            permissionNeed = Manifest.permission.ACCESS_COARSE_LOCATION;
        } else {
            permissionNeed = Manifest.permission.ACCESS_FINE_LOCATION;
        }
        boolean gotPermission = (ContextCompat.checkSelfPermission(mActivity, permissionNeed) == PackageManager.PERMISSION_GRANTED);
        if (!gotPermission) {
            mFragment.requestPermissions(new String[]{permissionNeed}, REQUEST_CODE_PERMISSION);
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

    private Runnable stopScanRunnable = new Runnable() {
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
