package com.vgaw.nrfconnect.util.bluetooth.manufacturer;

import com.vgaw.nrfconnect.util.HexTransform;
import com.vgaw.nrfconnect.util.Utils;

/**
 * @author caojin
 * @date 2018/3/15
 */

public class ManufacturerValue {
    private String manufactureName;
    private byte[] manufactureId;
    private byte[] specificData;

    public ManufacturerValue() {}

    public ManufacturerValue(String manufactureName, byte[] manufactureId, byte[] specificData) {
        this.manufactureName = manufactureName;
        this.manufactureId = manufactureId;
        this.specificData = specificData;
    }

    public String getManufactureName() {
        return manufactureName;
    }

    public void setManufactureName(String manufactureName) {
        this.manufactureName = manufactureName;
    }

    public byte[] getManufactureId() {
        return manufactureId;
    }

    public void setManufactureId(byte[] manufactureId) {
        this.manufactureId = manufactureId;
    }

    public byte[] getSpecificData() {
        return specificData;
    }

    public void setSpecificData(byte[] specificData) {
        this.specificData = specificData;
    }

    @Override
    public String toString() {
        return Utils.nullTo(manufactureName, "Reserved ID") +
                "<0x" + Utils.toUpperCase(HexTransform.byteToHexString(manufactureId[1])) +
                Utils.toUpperCase(HexTransform.byteToHexString(manufactureId[0])) + "> 0x" +
                Utils.toUpperCase(HexTransform.bytesToHexString(specificData));
    }
}
