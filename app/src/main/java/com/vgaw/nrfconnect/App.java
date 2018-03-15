package com.vgaw.nrfconnect;

import android.app.Application;

import com.vgaw.nrfconnect.util.ContextUtil;
import com.vgaw.nrfconnect.util.bluetooth.manufacturer.ManufacturerResolver;

import java.io.IOException;

/**
 * Created by caojin on 2018/2/28.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ContextUtil.init(this);
        try {
            ManufacturerResolver.readDataFromFile(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
