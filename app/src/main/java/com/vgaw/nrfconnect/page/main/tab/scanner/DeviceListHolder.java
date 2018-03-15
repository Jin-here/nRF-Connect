package com.vgaw.nrfconnect.page.main.tab.scanner;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCallback;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.vgaw.nrfconnect.R;
import com.vgaw.nrfconnect.util.Utils;
import com.vgaw.nrfconnect.view.adapter.EasyHolder;
import com.vgaw.nrfconnect.view.expansion.ExpansionLayout;

/**
 * Created by caojin on 2018/3/10.
 */

public abstract class DeviceListHolder extends EasyHolder<DeviceUIBean> {
    private TextView tvMainTabScannerItemDeviceName;
    private TextView tvMainTabScannerItemDeviceAddress;
    private TextView tvMainTabScannerItemDeviceBoundState;
    private TextView tvMainTabScannerItemDeviceRSSI;
    private Button btnMainTabScannerItemDeviceConnect;
    private ExpansionLayout expansionLayoutMainTabScannerItemDevice;
    private BLEDataContainerView vMainTabScannerItemDataContainer;

    @Override
    public View createView(int position) {
        tvMainTabScannerItemDeviceName = view.findViewById(R.id.tvMainTabScannerItemDeviceName);
        tvMainTabScannerItemDeviceAddress = view.findViewById(R.id.tvMainTabScannerItemDeviceAddress);
        tvMainTabScannerItemDeviceBoundState = view.findViewById(R.id.tvMainTabScannerItemDeviceBoundState);
        tvMainTabScannerItemDeviceRSSI = view.findViewById(R.id.tvMainTabScannerItemDeviceRSSI);
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
        tvMainTabScannerItemDeviceRSSI.setText(proRSSI());
        btnMainTabScannerItemDeviceConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askedForConnect(item.device);
            }
        });
        if (expansionLayoutMainTabScannerItemDevice.expanded() ^ item.expanded) {
            expansionLayoutMainTabScannerItemDevice.toggle(false);
        }
        vMainTabScannerItemDataContainer.updateData(item.device.getType(), item.scanRecord);
    }

    @Override
    public int getLayout() {
        return R.layout.main_tab_scanner_item_device;
    }

    protected abstract void askedForConnect(BluetoothDevice device);

    private String proRSSI() {
        return null;
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
