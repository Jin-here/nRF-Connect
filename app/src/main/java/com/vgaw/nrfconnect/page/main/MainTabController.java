package com.vgaw.nrfconnect.page.main;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.vgaw.nrfconnect.R;
import com.vgaw.nrfconnect.databinding.ActivityMainBinding;
import com.vgaw.nrfconnect.page.main.tab.advertiser.AdvertiserFragment;
import com.vgaw.nrfconnect.page.main.tab.bonded.BondedFragment;
import com.vgaw.nrfconnect.page.main.tab.device.DeviceDetailFragment;
import com.vgaw.nrfconnect.page.main.tab.scanner.ScannerFragment;
import com.vgaw.nrfconnect.view.tab.TabAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.vgaw.nrfconnect.util.ContextUtil.getString;

/**
 * Created by dell on 2018/3/4.
 */

public class MainTabController implements DeviceDetailFragmentManager.OnDeviceDetailFragmentChangedListener {
    private static final String TAG = "MainTabController";
    private static final int DEFAULT_TAB_COUNT = 3;

    private MainActivity activity;
    private ActivityMainBinding binding;

    private List<MainTabBean> tabList;
    private List<MainBaseTabFragment> fragmentList;
    private FragmentPagerAdapter fragmentPagerAdapter;

    private DeviceDetailFragmentManager mDeviceDetailFragmentManger;

    public MainTabController(MainActivity activity, ActivityMainBinding binding) {
        this.activity = activity;
        this.binding = binding;

        mDeviceDetailFragmentManger = new DeviceDetailFragmentManager();
    }

    public void init() {
        mDeviceDetailFragmentManger.setOnDeviceDetailFragmentChangedListener(this);

        tabList = new ArrayList<>();
        tabList.add(new MainTabBean(getString(R.string.main_tab_scanner)));
        tabList.add(new MainTabBean(getString(R.string.main_tab_bonded)));
        tabList.add(new MainTabBean(getString(R.string.main_tab_advertiser)));
        fragmentList = new ArrayList<>();
        fragmentList.add(new ScannerFragment());
        fragmentList.add(new BondedFragment());
        fragmentList.add(new AdvertiserFragment());
        fragmentPagerAdapter = new FragmentPagerAdapter(activity.getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                if (position < DEFAULT_TAB_COUNT) {
                    return fragmentList.get(position);
                } else {
                    return mDeviceDetailFragmentManger.getFragment(position - DEFAULT_TAB_COUNT);
                }
            }

            @Override
            public int getCount() {
                return tabList.size();
            }
        };
        binding.vpMain.setAdapter(fragmentPagerAdapter);

        binding.stbMain.setSelectedIndicatorColors(Color.WHITE);
        binding.stbMain.setDividerColors(Color.TRANSPARENT);
        binding.stbMain.setTabAdapter(new TabAdapter<MainTabBean>(tabList) {
            @Override
            protected View getTabView(Context context, final int position, int type, MainTabBean item) {
                View view = null;
                if (getItemViewType(position) == 0) {
                    view = View.inflate(context, R.layout.main_tab_style1, null);
                    ((TextView) view).setText(item.title);
                } else {
                    view = View.inflate(context, R.layout.main_tab_style2, null);
                    ((TextView) view.findViewById(R.id.tvMainTabTitle)).setText(item.title);
                    ((TextView) view.findViewById(R.id.tvMainTabSubTitle)).setText(item.subTitle);
                    view.findViewById(R.id.ivMainTabClear).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mDeviceDetailFragmentManger.removeDeviceDetailFragment(mDeviceDetailFragmentManger.getFragment(DEFAULT_TAB_COUNT - position));
                        }
                    });
                }
                return view;
            }

            @Override
            protected int getItemViewType(int position) {
                return device(position) ? 1 : 0;
            }

            private boolean device(int position) {
                MainTabBean tabData = getTabData(position);
                return tabData != null && tabData.subTitle != null;
            }
        });
        binding.stbMain.setViewPager(binding.vpMain);
    }

    public void release() {
        mDeviceDetailFragmentManger.setOnDeviceDetailFragmentChangedListener(null);
    }

    private void addTab(MainTabBean bean) {
        tabList.add(bean);
    }

    private void removeTab(int index) {
        tabList.remove(index);
    }

    @Override
    public void onDeviceDetailFragmentAdd(String name, String address) {
        addTab(new MainTabBean(name, address));
        notifyViewPagerChanged();
    }

    @Override
    public void onDeviceDetailFragmentRemove(int index) {
        removeTab(DEFAULT_TAB_COUNT + index);
        notifyViewPagerChanged();
    }

    private void notifyViewPagerChanged() {
        fragmentPagerAdapter.notifyDataSetChanged();
        binding.stbMain.setViewPager(binding.vpMain);
    }

    public void addDeviceDetailFragment(BluetoothDevice device) {
        mDeviceDetailFragmentManger.addDeviceDetailFragment(device);
    }

    public void removeDeviceDetailFragment(DeviceDetailFragment fragment) {
        mDeviceDetailFragmentManger.removeDeviceDetailFragment(fragment);
    }

    public BluetoothDevice getBluetoothDeviceByFragment(DeviceDetailFragment fragment) {
        return mDeviceDetailFragmentManger.getBluetoothDeviceByFragment(fragment);
    }
}
