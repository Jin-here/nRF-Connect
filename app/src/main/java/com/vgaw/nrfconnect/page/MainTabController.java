package com.vgaw.nrfconnect.page;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.widget.TextView;

import com.vgaw.nrfconnect.R;
import com.vgaw.nrfconnect.databinding.ActivityMainBinding;
import com.vgaw.nrfconnect.page.scanner.ScannerFragment;
import com.vgaw.nrfconnect.view.tab.TabAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.vgaw.nrfconnect.util.ContextUtil.getString;

/**
 * Created by dell on 2018/3/4.
 */

public class MainTabController {
    private MainActivity activity;
    private ActivityMainBinding binding;

    private List<MainTabBean> tabList;
    private FragmentPagerAdapter fragmentPagerAdapter;

    public MainTabController(MainActivity activity, ActivityMainBinding binding) {
        this.activity = activity;
        this.binding = binding;
    }

    public void onCreate() {
        tabList = new ArrayList<>();
        tabList.add(new MainTabBean(getString(R.string.main_tab_scanner)));
        tabList.add(new MainTabBean(getString(R.string.main_tab_bonded)));
        tabList.add(new MainTabBean(getString(R.string.main_tab_advertiser)));
        tabList.add(new MainTabBean("N/A", "22:11:22:22:34:21"));
        fragmentPagerAdapter = new FragmentPagerAdapter(activity.getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return new ScannerFragment();
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
            protected View getTabView(Context context, int position, int type, MainTabBean item) {
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
                            tabList.remove(3);
                            fragmentPagerAdapter.notifyDataSetChanged();
                            binding.stbMain.setViewPager(binding.vpMain);
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

    public void addTab(MainTabBean bean) {

    }

    public void removeTab(MainTabBean bean) {

    }
}
