package com.vgaw.nrfconnect.util;

import android.support.annotation.StringRes;
import android.widget.Toast;

/**
 * Created by caojin on 2017/9/16.
 */

public class ToastUtil {
    public static void show(String msg){
        Toast.makeText(ContextUtil.getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    public static void show(@StringRes int resId) {
        Toast.makeText(ContextUtil.getApplicationContext(), resId, Toast.LENGTH_LONG).show();
    }
}
