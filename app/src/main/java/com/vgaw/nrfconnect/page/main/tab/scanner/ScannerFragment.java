package com.vgaw.nrfconnect.page.main.tab.scanner;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.vgaw.nrfconnect.R;
import com.vgaw.nrfconnect.common.BLEManager;
import com.vgaw.nrfconnect.databinding.FragmentDeviceScannerBinding;
import com.vgaw.nrfconnect.page.main.MainBaseTabFragment;

/**
 * Created by caojin on 2018/2/27.
 */

public class ScannerFragment extends MainBaseTabFragment implements BLEManager.BLEListener, SwipeRefreshLayout.OnRefreshListener {
    public static final String TAG = "ScannerFragment";
    private FragmentDeviceScannerBinding binding;
    private ScannerFilterController mScannerFilterController;
    private BLEManager mBLEManager;

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
        mBLEManager.setBLEListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_device_scanner, container, false);
        mScannerFilterController.setBinding(getActivity(), binding);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    @Override
    public void onStop() {
        super.onStop();
        mScannerFilterController.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mScannerFilterController.onDestroy();
        mBLEManager.setBLEListener(null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initView() {
        mScannerFilterController.onActivityCreated();

        mBLEManager.setListView(binding.lvScanner);

        binding.swipeRefreshScanner.setColorSchemeResources(android.R.color.black,
                android.R.color.holo_red_light,
                android.R.color.holo_blue_dark,
                android.R.color.holo_orange_dark);
        binding.swipeRefreshScanner.setOnRefreshListener(this);
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
    public void onRefresh() {
        if (!mBLEManager.startScan(mActivity)) {
            binding.swipeRefreshScanner.setRefreshing(false);
        }
    }
}
