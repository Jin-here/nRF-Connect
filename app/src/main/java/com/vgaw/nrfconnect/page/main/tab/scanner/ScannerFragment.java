package com.vgaw.nrfconnect.page.main.tab.scanner;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.slidingpanelayout.widget.SlidingPaneLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.vgaw.nrfconnect.App;
import com.vgaw.nrfconnect.R;
import com.vgaw.nrfconnect.bean.ScannerFilter;
import com.vgaw.nrfconnect.common.BLEManager;
import com.vgaw.nrfconnect.data.BLEConstant;
import com.vgaw.nrfconnect.data.DeviceFavorite;
import com.vgaw.nrfconnect.data.DeviceFavorite_;
import com.vgaw.nrfconnect.databinding.FragmentDeviceScannerBinding;
import com.vgaw.nrfconnect.page.main.DeviceDetailFragmentManager;
import com.vgaw.nrfconnect.page.main.MainTabBaseFragment;
import com.vgaw.nrfconnect.page.main.MainTabController;
import com.vgaw.nrfconnect.util.Utils;
import com.vgaw.nrfconnect.view.adapter.EasyAdapter;
import com.vgaw.nrfconnect.view.adapter.EasyHolder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.objectbox.Box;

/**
 * @author caojin
 * @date 2018/2/27
 */

public class ScannerFragment extends MainTabBaseFragment implements BLEManager.BLEListener, SwipeRefreshLayout.OnRefreshListener, DeviceDetailFragmentManager.OnDeviceDetailFragmentChangedListener, SlidingPaneLayout.PanelSlideListener, RSSIIntervalManager.RSSIIntervalListener {
    private static final long MIN_REFRESH_DELAY = 300;
    public static final String TAG = "ScannerFragment";
    private FragmentDeviceScannerBinding binding;
    private ScannerFilterController mScannerFilterController;
    private ScannerFilter mScannerFilter;
    private BLEManager mBLEManager;
    private RSSIIntervalManager mRssiIntervalManager;

    private EasyAdapter mAdapter;
    private List<DeviceUIBean> rawList = new ArrayList<>();
    private List<DeviceUIBean> dataList = new ArrayList<>();

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
                binding.slidingPanelLayoutScanner.openPane();
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
        mScannerFilterController.setOnScannerFilterChangeListener(scannerFilter -> {
            mScannerFilter = scannerFilter;
            notifyListViewAdapterChanged();
        });
        mBLEManager = new BLEManager(this);
        getMainTabController().addOnDeviceDetailFragmentChangedListener(this);

        mRssiIntervalManager = new RSSIIntervalManager();
        mRssiIntervalManager.setRSSIIntervalListener(this);
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
        mHandler.removeCallbacksAndMessages(null);
        synchronizeDeviceFavorite();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mBLEManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void initView() {
        mBLEManager.setBLEListener(this);
        mScannerFilterController.onCreateView();

        binding.swipeRefreshScanner.setColorSchemeResources(android.R.color.black,
                android.R.color.holo_red_light,
                android.R.color.holo_blue_dark,
                android.R.color.holo_orange_dark);
        binding.swipeRefreshScanner.setOnRefreshListener(this);
        binding.slidingPanelLayoutScanner.setSliderFadeColor(Color.TRANSPARENT);
        binding.slidingPanelLayoutScanner.setPanelSlideListener(this);

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

        proScanStoppedState(false);
    }

    @Override
    public void onScanStopped() {
        getActivityMenu().findItem(R.id.main_tab_scanner_menu_scan).setTitle(R.string.main_tab_scanner_menu_start_scan);

        binding.swipeRefreshScanner.setRefreshing(false);

        proScanStoppedState(true);
    }

    @Override
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        int index = -1;
        if ((index = getIndexByDevice(device)) == -1) {
            if (deviceValid(rssi)) {
                DeviceUIBean deviceUIBean = new DeviceUIBean();
                deviceUIBean.device = device;
                deviceUIBean.rssi = rssi;
                deviceUIBean.scanRecord = scanRecord;
                deviceUIBean.deviceFragmentAdded = getMainTabController().fragmentAdded(device);
                deviceUIBean.favorite = checkDeviceFavorite(device.getAddress());
                this.rawList.add(deviceUIBean);

                notifyListViewAdapterChanged();
            }
        } else {
            if (deviceValid(rssi)) {
                DeviceUIBean deviceUIBean = this.rawList.get(index);
                deviceUIBean.rssi = rssi;
                deviceUIBean.scanRecord = scanRecord;

            } else {
                this.rawList.remove(index);
            }
            notifyListViewAdapterChanged();
        }
        mRssiIntervalManager.hit(device, rssi);
    }

    @Override
    public void onRefresh() {
        binding.swipeRefreshScanner.setRefreshing(false);
        mBLEManager.startScan(mActivity);
    }

    private boolean deviceValid(int rssi) {
        return (rssi >= BLEConstant.RSSI_MIN && rssi <= BLEConstant.RSSI_MAX);
    }

    private void proScanStoppedState(boolean stopped) {
        Iterator<DeviceUIBean> iterator = this.rawList.iterator();
        while (iterator.hasNext()) {
            iterator.next().advertiseStopped = stopped;
        }
        notifyListViewAdapterChanged();
    }

    private void proRSSIPeriodChanged(BluetoothDevice device, int rssi, long period) {
        int index = getIndexByDevice(device);
        if (index != -1) {
            this.rawList.get(index).rssi = rssi;
            this.rawList.get(index).period = period;

            notifyListViewAdapterChanged();
        }
    }

    private static final int WHAT_REFRESH = 1;
    private long mLstRefreshTime;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == WHAT_REFRESH) {
                actualRefresh();
            }
        }
    };

    private void notifyListViewAdapterChanged() {
        long crtTime = System.currentTimeMillis();
        if (!mHandler.hasMessages(WHAT_REFRESH)) {
            long offset;
            if ((offset = crtTime - mLstRefreshTime) > MIN_REFRESH_DELAY) {
                mHandler.sendEmptyMessage(WHAT_REFRESH);
            } else {
                mHandler.sendEmptyMessageDelayed(WHAT_REFRESH, MIN_REFRESH_DELAY - offset);
            }
        }
    }

    private void actualRefresh() {
        mLstRefreshTime = System.currentTimeMillis();

        dataList.clear();
        Iterator<DeviceUIBean> iterator = rawList.iterator();
        while (iterator.hasNext()) {
            DeviceUIBean item = iterator.next();
            if (ScannerFilter.deviceValid(mScannerFilter, item)) {
                dataList.add(item);
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    private MainTabController getMainTabController() {
        return mActivity.getMainTabController();
    }

    @Override
    public void onDeviceDetailFragmentAdd(BluetoothDevice device) {
        int i = getIndexByDevice(device);
        if (i != -1) {
            this.rawList.get(i).deviceFragmentAdded = true;
            notifyListViewAdapterChanged();
        }
    }

    @Override
    public void onDeviceDetailFragmentRemove(BluetoothDevice device) {
        Log.d(TAG, "onDeviceDetailFragmentRemove: ");
        int i = getIndexByDevice(device);
        if (i != -1) {
            this.rawList.get(i).deviceFragmentAdded = false;
            notifyListViewAdapterChanged();
        }
    }

    private int getIndexByDevice(BluetoothDevice device) {
        Iterator<DeviceUIBean> iterator = this.rawList.iterator();
        int index = -1;
        while (iterator.hasNext()) {
            index++;
            if (iterator.next().device.equals(device)) {
                return index;
            }
        }
        return -1;
    }

    @Override
    public void onPanelSlide(View panel, float slideOffset) {

    }

    @Override
    public void onPanelOpened(View panel) {

    }

    @Override
    public void onPanelClosed(View panel) {

    }

    @Override
    public void onGetRSSI(BluetoothDevice device, int rssi, long period) {
        proRSSIPeriodChanged(device, rssi, period);
    }

    private boolean checkDeviceFavorite(String address) {
        List<DeviceFavorite> deviceFavoriteList = getDeviceFavoriteByAddress(getDeviceFavoriteBox(), address);
        if (deviceFavoriteList != null && deviceFavoriteList.size() > 0) {
            return true;
        }
        return false;
    }

    private void synchronizeDeviceFavorite() {
        Iterator<DeviceUIBean> iterator = this.rawList.iterator();
        Box<DeviceFavorite> deviceFavoriteBox = getDeviceFavoriteBox();
        while (iterator.hasNext()) {
            DeviceUIBean next = iterator.next();
            List<DeviceFavorite> deviceFavoriteByAddress = getDeviceFavoriteByAddress(deviceFavoriteBox, next.device.getAddress());
            // 之前是否favorite
            boolean lstFavorite = (deviceFavoriteByAddress != null && deviceFavoriteByAddress.size() > 0);
            String currentTime = Utils.getTime("yyyy/M/dd");
            if (lstFavorite) {
                DeviceFavorite deviceFavoriteStored = deviceFavoriteByAddress.get(0);
                if (!next.favorite) {
                    deviceFavoriteBox.remove(deviceFavoriteStored.getId());
                } else {
                    // 更新时间
                    deviceFavoriteStored.setLastSeenTime(currentTime);
                    deviceFavoriteBox.put(deviceFavoriteStored);
                }
            } else {
                if (next.favorite) {
                    DeviceFavorite deviceFavorite = new DeviceFavorite();
                    deviceFavorite.setName(next.device.getName());
                    deviceFavorite.setAddress(next.device.getAddress());
                    deviceFavorite.setAddedTime(currentTime);
                    deviceFavorite.setLastSeenTime(currentTime);
                    deviceFavoriteBox.put(deviceFavorite);
                }
            }
        }
    }

    private List<DeviceFavorite> getDeviceFavoriteByAddress(Box<DeviceFavorite> deviceFavoriteBox, String address) {
        return deviceFavoriteBox.query()
                .equal(DeviceFavorite_.address, address)
                .build()
                .find();
    }

    private Box<DeviceFavorite> getDeviceFavoriteBox() {
        return ((App) mActivity.getApplicationContext()).getBoxStore().boxFor(DeviceFavorite.class);
    }
}
