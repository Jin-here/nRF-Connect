package com.vgaw.nrfconnect.page.main.tab.scanner;

import android.bluetooth.BluetoothDevice;
import android.graphics.drawable.Drawable;
import androidx.annotation.StringRes;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.vgaw.nrfconnect.R;
import com.vgaw.nrfconnect.util.Utils;
import com.vgaw.nrfconnect.view.adapter.EasyHolder;
import com.vgaw.nrfconnect.view.expansion.ExpansionLayout;

/**
 * @author caojin
 * @date 2018/3/10
 */

public abstract class DeviceListHolder extends EasyHolder<DeviceUIBean> {
    private ImageButton ibMainTabScannerItemDeviceIcon;
    private ImageView ivMainTabScannerItemDeviceFavorite;
    private TextView tvMainTabScannerItemDeviceName;
    private TextView tvMainTabScannerItemDeviceAddress;
    private TextView tvMainTabScannerItemDeviceBoundState;
    private TextView tvMainTabScannerItemDeviceRSSI;
    private TextView tvMainTabScannerItemDevicePeriod;
    private Button btnMainTabScannerItemDeviceConnect;
    private ExpansionLayout expansionLayoutMainTabScannerItemDevice;
    private BLEDataContainerView vMainTabScannerItemDataContainer;

    @Override
    public View createView(int position) {
        ibMainTabScannerItemDeviceIcon = view.findViewById(R.id.ibMainTabScannerItemDeviceIcon);
        ivMainTabScannerItemDeviceFavorite = view.findViewById(R.id.ivMainTabScannerItemDeviceFavorite);
        tvMainTabScannerItemDeviceName = view.findViewById(R.id.tvMainTabScannerItemDeviceName);
        tvMainTabScannerItemDeviceAddress = view.findViewById(R.id.tvMainTabScannerItemDeviceAddress);
        tvMainTabScannerItemDeviceBoundState = view.findViewById(R.id.tvMainTabScannerItemDeviceBoundState);
        tvMainTabScannerItemDeviceRSSI = view.findViewById(R.id.tvMainTabScannerItemDeviceRSSI);
        tvMainTabScannerItemDevicePeriod = view.findViewById(R.id.tvMainTabScannerItemDevicePeriod);
        btnMainTabScannerItemDeviceConnect = view.findViewById(R.id.btnMainTabScannerItemDeviceConnect);
        expansionLayoutMainTabScannerItemDevice = view.findViewById(R.id.expansionLayoutMainTabScannerItemDevice);
        updateTag(position);
        expansionLayoutMainTabScannerItemDevice.addIndicatorListener(new ExpansionLayout.IndicatorListener() {
            @Override
            public void onStartedExpand(ExpansionLayout expansionLayout, boolean willExpand) {
                int position = (int) expansionLayout.getTag();
                DeviceListHolder.this.dataList.get(position).expanded = willExpand;
            }
        });
        vMainTabScannerItemDataContainer = view.findViewById(R.id.vMainTabScannerItemDataContainer);
        return view;
    }

    private void updateTag(int position) {
        expansionLayoutMainTabScannerItemDevice.setTag(position);
    }

    @Override
    public void refreshView(int position, final DeviceUIBean item) {
        updateTag(position);

        tvMainTabScannerItemDeviceName.setText(Utils.nullTo(item.device.getName(), context.getString(R.string.main_tab_scanner_device_name_unknown)));
        tvMainTabScannerItemDeviceAddress.setText(item.device.getAddress());
        tvMainTabScannerItemDeviceBoundState.setText(proBondState(item.device.getBondState()));
        proRSSI(tvMainTabScannerItemDeviceRSSI, item.advertiseStopped, item.rssi);
        proPeriod(tvMainTabScannerItemDevicePeriod, item.advertiseStopped, item.period);
        btnMainTabScannerItemDeviceConnect.setText(item.deviceFragmentAdded ?
                R.string.main_tab_scanner_action_open_tab : R.string.main_tab_scanner_action_connect);
        btnMainTabScannerItemDeviceConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.deviceFragmentAdded) {
                    openTab(item.device);
                } else {
                    askedForConnect(item.device);
                }
            }
        });
        vMainTabScannerItemDataContainer.updateData(item.device.getType(), item.scanRecord);
        if (expansionLayoutMainTabScannerItemDevice.expanded() ^ item.expanded) {
            expansionLayoutMainTabScannerItemDevice.toggle(false);
        }
        Object tag0 = ivMainTabScannerItemDeviceFavorite.getTag();
        if (tag0 == null || (((boolean) tag0) ^ item.favorite)) {
            Log.d("HELLO", "refreshView: " + item.favorite);
            ivMainTabScannerItemDeviceFavorite.setTag(item.favorite);
            markFavorite(item.favorite);
            Object tag = ibMainTabScannerItemDeviceIcon.getTag();
            if (tag != null) {
                ((ViewPropertyAnimator) tag).cancel();
            }
        }
        ibMainTabScannerItemDeviceIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.favorite = !item.favorite;

                Object tag = ibMainTabScannerItemDeviceIcon.getTag();
                if (tag != null) {
                    ((ViewPropertyAnimator) tag).cancel();
                    animateFavorite(item.favorite);
                } else {
                    ibMainTabScannerItemDeviceIcon.setTag(animateFavorite(item.favorite));
                }
            }
        });
    }

    @Override
    public int getLayout() {
        return R.layout.main_tab_scanner_item_device;
    }

    protected abstract void openTab(BluetoothDevice device);

    protected abstract void askedForConnect(BluetoothDevice device);

    private ViewPropertyAnimator animateFavorite(boolean favorite) {
        float offset = (favorite ? context.getResources().getDimensionPixelOffset(R.dimen.space_20) : 0);
        ViewPropertyAnimator animator = ivMainTabScannerItemDeviceFavorite
                .animate()
                .translationY(offset);
        animator.start();
        return animator;
    }

    private void markFavorite(boolean favorite) {
        float offset = (favorite ? context.getResources().getDimensionPixelOffset(R.dimen.space_20) : 0);
        ivMainTabScannerItemDeviceFavorite.setTranslationY(offset);
    }

    private void proPeriod(TextView tv, boolean advertiseStopped, long period) {
        Drawable drawable = context.getResources().getDrawable(advertiseStopped ? R.drawable.ic_signal_interval_d :
                R.drawable.ic_signal_interval_e);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tv.setCompoundDrawables(drawable, null, null, null);

        String result = (period == -1 ? "N/A" : String.valueOf(period)) + " ms";
        SpannableString spannableString = new SpannableString(result);
        spannableString.setSpan(new ForegroundColorSpan(context.getResources().getColor(advertiseStopped ? R.color.txt_dark_3 : R.color.txt_dark_1)),
                0, result.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(spannableString);
    }

    private void proRSSI(TextView tv, boolean advertiseStopped, int rssi) {
        String result = rssi + "dBm";
        SpannableString spannableString = new SpannableString(result);
        spannableString.setSpan(new ForegroundColorSpan(context.getResources().getColor(advertiseStopped ? R.color.txt_dark_3 : R.color.txt_dark_1)),
                0, result.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(spannableString);

        Drawable drawable = context.getResources().getDrawable(advertiseStopped ? R.drawable.ic_signal_d :
                R.drawable.ic_signal_e);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tv.setCompoundDrawables(drawable, null, null, null);
    }

    private @StringRes int proBondState(int state) {
        switch (state) {
            case BluetoothDevice.BOND_NONE:
                return R.string.main_tab_scanner_bond_state_none;
            case BluetoothDevice.BOND_BONDING:
                return R.string.main_tab_scanner_bond_state_bonding;
            case BluetoothDevice.BOND_BONDED:
                return R.string.main_tab_scanner_bond_state_bonded;
        }
        return -1;
    }
}
