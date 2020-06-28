package com.vgaw.nrfconnect.page.main;

import android.bluetooth.BluetoothDevice;

import com.vgaw.nrfconnect.page.main.tab.device.DeviceDetailFragment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author caojin
 * @date 2018/3/10
 */

public class DeviceDetailFragmentManager {
    private List<OnDeviceDetailFragmentChangedListener> listenerList = new ArrayList<>();

    /**
     * 给DeviceDetailFragment获取对应的device
     */
    private List<BluetoothDevice> deviceList = new ArrayList<>();
    /**
     * 给FragmentPagerAdapter使用
     */
    private List<DeviceDetailFragment> fragmentList = new ArrayList<>();

    public void addOnDeviceDetailFragmentChangedListener(OnDeviceDetailFragmentChangedListener listener) {
        this.listenerList.add(listener);
    }

    public void removeOnDeviceDetailFragmentChangedListener(OnDeviceDetailFragmentChangedListener listener) {
        this.listenerList.remove(listener);
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

        callDeviceDetailFragmentAdd(device);
    }

    public void removeDeviceDetailFragment(String deviceAddress) {
        int index = getDeviceIndexByAddress(deviceAddress);
        if (index != -1) {
            fragmentList.remove(index);
            BluetoothDevice device = deviceList.remove(index);

            callDeviceDetailFragmentRemove(device);
        }
    }

    /**
     * 检测BluetoothDevice对应的framgent是否显示
     * @param device
     * @return
     */
    public boolean fragmentAdded(BluetoothDevice device) {
        return deviceList.contains(device);
    }

    public BluetoothDevice getDeviceByAddress(String deviceAddress) {
        int index = getDeviceIndexByAddress(deviceAddress);
        if (index != -1) {
            return deviceList.get(index);
        }
        return null;
    }

    private int getDeviceIndexByAddress(String deviceAddress) {
        for (int i = 0;i < deviceList.size();i++) {
            if (deviceList.get(i).getAddress().equals(deviceAddress)) {
                return i;
            }
        }
        return -1;
    }

    private DeviceDetailFragment buildDeviceDetailFragment(String name, String address) {
        return DeviceDetailFragment.newInstance(name, address);
    }

    private void callDeviceDetailFragmentAdd(BluetoothDevice device) {
        Iterator<OnDeviceDetailFragmentChangedListener> iterator = listenerList.iterator();
        while (iterator.hasNext()) {
            iterator.next().onDeviceDetailFragmentAdd(device);
        }
    }

    private void callDeviceDetailFragmentRemove(BluetoothDevice device) {
        Iterator<OnDeviceDetailFragmentChangedListener> iterator = listenerList.iterator();
        while (iterator.hasNext()) {
            iterator.next().onDeviceDetailFragmentRemove(device);
        }
    }

    public interface OnDeviceDetailFragmentChangedListener {
        void onDeviceDetailFragmentAdd(BluetoothDevice device);

        void onDeviceDetailFragmentRemove(BluetoothDevice device);
    }
}
