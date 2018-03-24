package com.vgaw.nrfconnect.page.main.tab.advertiser;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.vgaw.nrfconnect.R;
import com.vgaw.nrfconnect.page.main.MainTabBaseFragment;

/**
 * @author caojin
 * @date 2018/3/4
 */

public class AdvertiserFragment extends MainTabBaseFragment {
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_tab_advertiser, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
}
