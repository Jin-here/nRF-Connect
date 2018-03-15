package com.vgaw.nrfconnect.util.bluetooth;

import com.vgaw.nrfconnect.util.HexTransform;

/**
 * Created by dell on 2018/3/15.
 */

public class BLEData {
    private byte type;
    private byte[] value;

    public BLEData() {}

    public BLEData(byte type, byte[] value) {
        this.type = type;
        this.value = value;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "BLEData{" +
                "type=" + HexTransform.byteToHexString(type) +
                ", value=" + HexTransform.bytesToHexString(value) +
                '}';
    }
}
