package com.vgaw.nrfconnect;

import android.app.Application;

import com.vgaw.nrfconnect.data.MyObjectBox;
import com.vgaw.nrfconnect.util.ContextUtil;

import io.objectbox.BoxStore;

/**
 * @author caojin
 * @date 2018/2/28
 */

public class App extends Application {

    private BoxStore boxStore;

    @Override
    public void onCreate() {
        super.onCreate();
        ContextUtil.init(this);
        boxStore = MyObjectBox.builder().androidContext(App.this).build();
    }

    public BoxStore getBoxStore() {
        return this.boxStore;
    }
}
