package com.vgaw.nrfconnect.util.bluetooth.servicedata;

import com.vgaw.nrfconnect.util.HexTransform;
import com.vgaw.nrfconnect.util.Utils;

/**
 * @author caojin
 * @date 2018/3/24
 */
public class ServiceDataValue {
    private byte[] serviceUUID;
    private byte[] serviceData;

    public ServiceDataValue() {}

    public ServiceDataValue(byte[] serviceUUID, byte[] serviceData) {
        this.serviceUUID = serviceUUID;
        this.serviceData = serviceData;
    }

    @Override
    public String toString() {
        return "UUID: 0x" + Utils.toUpperCase(HexTransform.bytesToHexString(serviceUUID)) +
                " Data: 0x" + Utils.toUpperCase(HexTransform.bytesToHexString(serviceData));
    }
}
