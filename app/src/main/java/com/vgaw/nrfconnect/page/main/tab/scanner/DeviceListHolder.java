package com.vgaw.nrfconnect.page.main.tab.scanner;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCallback;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.vgaw.nrfconnect.R;
import com.vgaw.nrfconnect.view.adapter.EasyHolder;

/**
 * Created by caojin on 2018/3/10.
 */

public abstract class DeviceListHolder extends EasyHolder {
    private TextView tvMainTabScannerItemDeviceName;
    private Button btnMainTabScannerItemDeviceConnect;

    @Override
    public View createView(int position) {
        tvMainTabScannerItemDeviceName = view.findViewById(R.id.tvMainTabScannerItemDeviceName);
        btnMainTabScannerItemDeviceConnect = view.findViewById(R.id.btnMainTabScannerItemDeviceConnect);
        return view;
    }

    @Override
    public void refreshView(int position, Object item) {
        final BluetoothDevice device = (BluetoothDevice) item;
        tvMainTabScannerItemDeviceName.setText(device.getName());
        btnMainTabScannerItemDeviceConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askedForConnect(device);
            }
        });
    }

    @Override
    public int getLayout() {
        return R.layout.main_tab_scanner_item_device;
    }

    protected abstract void askedForConnect(BluetoothDevice device);
}
