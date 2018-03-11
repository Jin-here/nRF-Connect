package com.vgaw.nrfconnect.page.main;

import android.bluetooth.BluetoothDevice;

import com.vgaw.nrfconnect.page.main.tab.device.DeviceDetailFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by caojin on 2018/3/10.
 */

public class DeviceDetailFragmentManager {
    private OnDeviceDetailFragmentChangedListener listener;

    /**
     * 给DeviceDetailFragment获取对应的device
     */
    private List<BluetoothDevice> deviceList = new ArrayList<>();
    /**
     * 给FragmentPagerAdapter使用
     */
    private List<DeviceDetailFragment> fragmentList = new ArrayList<>();

    public void setOnDeviceDetailFragmentChangedListener(OnDeviceDetailFragmentChangedListener listener) {
        this.listener = listener;
    }

    /**
     * 给FragmentPagerAdapter使用
     * @param position
     * @return
     */
    public DeviceDetailFragment getFragment(int position) {
        return fragmentList.get(position);
    }

    /**
     * 1. 存入address, fragment
     * 2. 更新ViewPager adapter
     * @param device
     */
    public void addDeviceDetailFragment(BluetoothDevice device) {
        String name = device.getName();
        String address = device.getAddress();
        DeviceDetailFragment deviceDetailFragment = buildDeviceDetailFragment(name, address);

        fragmentList.add(deviceDetailFragment);
        deviceList.add(device);

        callDeviceDetailFragmentAdd(name, address);
    }

    public void removeDeviceDetailFragment(DeviceDetailFragment fragment) {
        int i = fragmentList.indexOf(fragment);
        fragmentList.remove(i);
        deviceList.remove(i);

        callDeviceDetailFragmentRemove(i);
    }

    public BluetoothDevice getBluetoothDeviceByFragment(DeviceDetailFragment fragment) {
        return deviceList.get(fragmentList.indexOf(fragment));
    }

    private DeviceDetailFragment buildDeviceDetailFragment(String name, String address) {
        return DeviceDetailFragment.newInstance(name, address);
    }

    private void callDeviceDetailFragmentAdd(String name, String address) {
        if (listener != null) {
            listener.onDeviceDetailFragmentAdd(name, address);
        }
    }

    private void callDeviceDetailFragmentRemove(int index) {
        if (listener != null) {
            listener.onDeviceDetailFragmentRemove(index);
        }
    }

    public interface OnDeviceDetailFragmentChangedListener {
        void onDeviceDetailFragmentAdd(String name, String address);

        void onDeviceDetailFragmentRemove(int index);
    }
}
