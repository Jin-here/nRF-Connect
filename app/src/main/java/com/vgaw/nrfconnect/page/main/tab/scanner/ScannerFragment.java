package com.vgaw.nrfconnect.page.main.tab.scanner;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.vgaw.nrfconnect.R;
import com.vgaw.nrfconnect.databinding.FragmentDeviceScannerBinding;
import com.vgaw.nrfconnect.page.main.MainBaseTabFragment;

/**
 * Created by caojin on 2018/2/27.
 */

public class ScannerFragment extends MainBaseTabFragment {
    public static final String TAG = "ScannerFragment";
    private FragmentDeviceScannerBinding binding;
    private ScannerFilterController mScannerFilterController;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_tab_scanner, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mScannerFilterController = new ScannerFilterController();
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
    }

    private void initView() {
        mScannerFilterController.onActivityCreated();
    }
}
