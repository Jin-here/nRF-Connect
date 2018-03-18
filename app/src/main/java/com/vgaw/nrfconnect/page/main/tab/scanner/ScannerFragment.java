package com.vgaw.nrfconnect.page.main.tab.scanner;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.vgaw.nrfconnect.R;
import com.vgaw.nrfconnect.common.BLEManager;
import com.vgaw.nrfconnect.databinding.FragmentDeviceScannerBinding;
import com.vgaw.nrfconnect.page.main.DeviceDetailFragmentManager;
import com.vgaw.nrfconnect.page.main.MainBaseTabFragment;
import com.vgaw.nrfconnect.page.main.MainTabController;
import com.vgaw.nrfconnect.util.ContextUtil;
import com.vgaw.nrfconnect.view.HorizontalSwipeLayout;
import com.vgaw.nrfconnect.view.adapter.EasyAdapter;
import com.vgaw.nrfconnect.view.adapter.EasyHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by caojin on 2018/2/27.
 */

public class ScannerFragment extends MainBaseTabFragment implements BLEManager.BLEListener, SwipeRefreshLayout.OnRefreshListener, HorizontalSwipeLayout.HorizontalListener, DeviceDetailFragmentManager.OnDeviceDetailFragmentChangedListener {
    public static final String TAG = "ScannerFragment";
    private FragmentDeviceScannerBinding binding;
    private ScannerFilterController mScannerFilterController;
    private BLEManager mBLEManager;

    private EasyAdapter mAdapter;
    private List<DeviceUIBean> dataList = new ArrayList<DeviceUIBean>();

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_tab_scanner, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // 开始/停止扫描
            case R.id.main_tab_scanner_menu_scan:
                if (mBLEManager.scanning()) {
                    mBLEManager.stopScan(mActivity);
                } else {
                    mBLEManager.startScan(mActivity);
                }
                break;
            // 刷新
            case R.id.main_tab_scanner_menu_refresh:
                if (!mBLEManager.scanning()) {
                    mBLEManager.startScan(mActivity);
                }
                break;
            // show rssi
            case R.id.main_tab_scanner_menu_show_rssi_graph:
                binding.horizontalSwipeLayoutScanner.expand();
                break;
            // show legend
            case R.id.main_tab_scanner_menu_show_legend:
                item.setChecked(!item.isChecked());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mScannerFilterController = new ScannerFilterController();
        mBLEManager = new BLEManager(this);
        getMainTabController().addOnDeviceDetailFragmentChangedListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_device_scanner, container, false);
        mScannerFilterController.setBinding(getActivity(), binding);
        initView();
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mScannerFilterController.onActivityCreated();
    }

    @Override
    public void onStop() {
        super.onStop();
        mScannerFilterController.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBLEManager.setBLEListener(null);
        if (mBLEManager.scanning()) {
            mBLEManager.stopScan(mActivity);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getMainTabController().removeOnDeviceDetailFragmentChangedListener(this);
        mScannerFilterController.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initView() {
        mBLEManager.setBLEListener(this);
        mScannerFilterController.onCreateView();

        binding.swipeRefreshScanner.setColorSchemeResources(android.R.color.black,
                android.R.color.holo_red_light,
                android.R.color.holo_blue_dark,
                android.R.color.holo_orange_dark);
        binding.swipeRefreshScanner.setOnRefreshListener(this);
        binding.horizontalSwipeLayoutScanner.setHorizontalSwipeListener(this);

        mAdapter = new EasyAdapter<DeviceUIBean>(mActivity, dataList) {
            @Override
            public EasyHolder<DeviceUIBean> getHolder(int type) {
                return new DeviceListHolder() {
                    @Override
                    protected void openTab(BluetoothDevice device) {
                        getMainTabController().openTab(device);
                    }

                    @Override
                    protected void askedForConnect(BluetoothDevice device) {
                        getMainTabController().addDeviceDetailFragment(device);
                    }
                };
            }
        };
        binding.lvScanner.setAdapter(mAdapter);
    }

    @Override
    public void onScanStarted() {
        getActivityMenu().findItem(R.id.main_tab_scanner_menu_scan).setTitle(R.string.main_tab_scanner_menu_stop_scan);
    }

    @Override
    public void onScanStopped() {
        getActivityMenu().findItem(R.id.main_tab_scanner_menu_scan).setTitle(R.string.main_tab_scanner_menu_start_scan);

        binding.swipeRefreshScanner.setRefreshing(false);
    }

    @Override
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        if (getIndexByDevice(device) == -1) {
            DeviceUIBean deviceUIBean = new DeviceUIBean();
            deviceUIBean.device = device;
            deviceUIBean.rssi = rssi;
            deviceUIBean.scanRecord = scanRecord;
            deviceUIBean.deviceFragmentAdded = getMainTabController().fragmentAdded(device);
            this.dataList.add(deviceUIBean);

            notifyListViewAdapterChanged();
        }
    }

    @Override
    public void onRefresh() {
        if (!mBLEManager.startScan(mActivity)) {
            binding.swipeRefreshScanner.setRefreshing(false);
        }
    }

    private void notifyListViewAdapterChanged() {
        ContextUtil.getHandler().post(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private MainTabController getMainTabController() {
        return mActivity.getMainTabController();
    }

    @Override
    public void onStateChanged(boolean expand) {}

    @Override
    public void onDeviceDetailFragmentAdd(BluetoothDevice device) {
        int i = getIndexByDevice(device);
        if (i != -1) {
            this.dataList.get(i).deviceFragmentAdded = true;
            notifyListViewAdapterChanged();
        }
    }

    @Override
    public void onDeviceDetailFragmentRemove(BluetoothDevice device) {
        Log.d(TAG, "onDeviceDetailFragmentRemove: ");
        int i = getIndexByDevice(device);
        if (i != -1) {
            this.dataList.get(i).deviceFragmentAdded = false;
            notifyListViewAdapterChanged();
        }
    }

    private int getIndexByDevice(BluetoothDevice device) {
        for (int i = 0;i < dataList.size();i++){
            if (dataList.get(i).device.equals(device)) {
                return i;
            }
        }
        return -1;
    }
}
