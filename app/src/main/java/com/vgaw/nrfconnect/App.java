package com.vgaw.nrfconnect;

import android.app.Application;

import com.vgaw.nrfconnect.util.ContextUtil;

/**
 * Created by caojin on 2018/2/28.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ContextUtil.init(this);
    }
}
