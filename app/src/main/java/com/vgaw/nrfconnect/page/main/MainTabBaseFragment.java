package com.vgaw.nrfconnect.page.main;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.Menu;

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
