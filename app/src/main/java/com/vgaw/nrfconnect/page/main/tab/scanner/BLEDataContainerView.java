package com.vgaw.nrfconnect.page.main.tab.scanner;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.ScaleXSpan;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.text.style.URLSpan;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vgaw.nrfconnect.R;
import com.vgaw.nrfconnect.util.bluetooth.BLEData;
import com.vgaw.nrfconnect.util.bluetooth.BLEDataResolver;
import com.vgaw.nrfconnect.view.MeasurableTextView;

import java.io.IOException;
import java.util.List;

/**
 *
 * @author caojin
 * @date 2018/3/15
 */

public class BLEDataContainerView extends LinearLayout {
    public BLEDataContainerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
    }

    public void updateData(int deviceType, byte[] data) {
        removeAllViews();

        try {
            addItem(false, "Device type", getResources().getString(proDeviceType(deviceType)));
            List<BLEData> group = BLEDataResolver.group(data);
            for (BLEData item : group) {
                String[] resolve = BLEDataResolver.resolve(item);
                addItem(true, resolve[0], resolve[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addItem(boolean clickable, String type, String value) {
        TextView tv = new MeasurableTextView(getContext());
        if (clickable) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                // If we're running on Honeycomb or newer, then we can use the Theme's
                // selectableItemBackground to ensure that the View has a pressed state
                TypedValue outValue = new TypedValue();
                getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground,
                        outValue, true);
                tv.setBackgroundResource(outValue.resourceId);
            }
            tv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
        String result = type + ": " + value;
        SpannableString spannableString = new SpannableString(result);
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(clickable ? R.color.colorAccent : R.color.txt_dark_2)), 0, type.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.txt_dark_1)), type.length() + 1, result.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(spannableString);

        addView(tv, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    }

    private @StringRes
    int proDeviceType(int type) {
        switch (type) {
            case BluetoothDevice.DEVICE_TYPE_UNKNOWN:
                return R.string.main_tab_scanner_device_type_unknown;
            case BluetoothDevice.DEVICE_TYPE_CLASSIC:
                return R.string.main_tab_scanner_device_type_classic;
            case BluetoothDevice.DEVICE_TYPE_LE:
                return R.string.main_tab_scanner_device_type_le;
            case BluetoothDevice.DEVICE_TYPE_DUAL:
                return R.string.main_tab_scanner_device_type_dual;
        }
        return -1;
    }
}
