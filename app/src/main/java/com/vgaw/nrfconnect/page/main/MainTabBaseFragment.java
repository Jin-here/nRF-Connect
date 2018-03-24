package com.vgaw.nrfconnect.page.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.vgaw.nrfconnect.R;

/**
 * @author caojin
 * @date 2018/3/4
 */

public class MainTabBaseFragment extends Fragment {
    protected MainActivity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MainActivity) getActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public Menu getActivityMenu() {
        return mActivity.getMenu();
    }
}
