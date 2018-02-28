package com.vgaw.nrfconnect.view;

import android.text.InputFilter;
import android.text.Spanned;

import com.vgaw.nrfconnect.util.Utils;

/**
 * Created by caojin on 2018/2/28.
 */

public class HexInputFilter implements InputFilter {
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        StringBuilder sb = new StringBuilder();
        for (int i = start;i < end;i++) {
            char item = source.charAt(i);
            if (Utils.hex(item)) {
                sb.append(item);
            }
        }
        return sb.toString();
    }
}
