package com.vgaw.nrfconnect.util.bluetooth.manufacturer;

import java.util.Arrays;

/**
 * Created by dell on 2018/3/15.
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
        return manufactureName;
    }
}
