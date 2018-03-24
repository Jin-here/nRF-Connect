package com.vgaw.nrfconnect.util.bluetooth.serviceuuids;

import com.vgaw.nrfconnect.util.HexTransform;
import com.vgaw.nrfconnect.util.Utils;

/**
 * @author caojin
 * @date 2018/3/24
 */
public class ServiceUUIDSValue {
    private byte[] serviceUUIDS;

    public ServiceUUIDSValue() {}

    public ServiceUUIDSValue(byte[] serviceUUIDS) {
        this.serviceUUIDS = serviceUUIDS;
    }

    @Override
    public String toString() {
        return Utils.toUpperCase(HexTransform.bytesToHexString(serviceUUIDS));
    }
}
