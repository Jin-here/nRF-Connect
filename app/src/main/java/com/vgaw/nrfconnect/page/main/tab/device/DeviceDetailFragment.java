package com.vgaw.nrfconnect.page.main.tab.device;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vgaw.nrfconnect.page.main.MainBaseTabFragment;
import com.vgaw.nrfconnect.page.main.MainTabController;

/**
 * Created by caojin on 2018/3/4.
 *
 * todo:
 * 1. notifyService.getIncludedServices();
 * 2. notifyService.getInstanceId();
 */

public class DeviceDetailFragment extends MainBaseTabFragment {
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
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.mBluetoothDevice.connectGatt(mActivity, false, new BluetoothGattCallback() {
            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                super.onConnectionStateChange(gatt, status, newState);
            }

            @Override
            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                super.onServicesDiscovered(gatt, status);
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
