package com.vgaw.nrfconnect.page.main.tab.device;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vgaw.nrfconnect.R;
import com.vgaw.nrfconnect.databinding.FragmentDeviceDetailBinding;
import com.vgaw.nrfconnect.page.main.MainTabBaseFragment;
import com.vgaw.nrfconnect.page.main.MainTabController;
import com.vgaw.nrfconnect.util.bluetooth.BLENamesResolver;

import java.util.List;

/**
 *
 * @author caojin
 * @date 2018/3/4
 *
 * todo:
 * 1. notifyService.getIncludedServices();
 * 2. notifyService.getInstanceId();
 */

public class DeviceDetailFragment extends MainTabBaseFragment {
    private static final String TAG = "DeviceDetailFragment";
    private String name;
    private String address;

    private BluetoothDevice mBluetoothDevice;

    public static DeviceDetailFragment newInstance(String name, String address) {
        Bundle args = new Bundle();
        args.putString("name", name);
        args.putString("address", address);

        DeviceDetailFragment fragment = new DeviceDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.name = getArguments().getString("name");
        this.address = getArguments().getString("address");

        this.mBluetoothDevice = getBluetoothDevice();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentDeviceDetailBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_device_detail, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.mBluetoothDevice.connectGatt(mActivity, false, new BluetoothGattCallback() {
            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                super.onConnectionStateChange(gatt, status, newState);
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    gatt.discoverServices();

                    Log.d(TAG, "Connected to GATT server.");
                    Log.d(TAG, "Attempting to start service discovery:");
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    Log.d(TAG, "Disconnected from GATT server.");
                }
            }

            @Override
            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                super.onServicesDiscovered(gatt, status);
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    Log.d(TAG, "onServicesDiscovered: ");
                    List<BluetoothGattService> serviceList = gatt.getServices();
                    for (BluetoothGattService service : serviceList) {
                        String serviceUuid = service.getUuid().toString();
                        int serviceType = service.getType();
                        Log.d(TAG, "onServicesDiscovered: " + BLENamesResolver.resolveServiceName(serviceUuid) + ":" + serviceType);
                        List<BluetoothGattCharacteristic> characteristicList = service.getCharacteristics();
                        for (BluetoothGattCharacteristic characteristic : characteristicList) {
                            String uuid = characteristic.getUuid().toString();
                            Log.d(TAG, "onServicesDiscovered1: " + BLENamesResolver.resolveCharacteristicName(uuid));
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void proTabUI() {}

    private void disconnect() {}

    private void removeSelfTab() {}

    private MainTabController getMainTabController() {
        return mActivity.getMainTabController();
    }

    private BluetoothDevice getBluetoothDevice() {
        return getMainTabController().getDeviceByAddress(this.address);
    }
}
