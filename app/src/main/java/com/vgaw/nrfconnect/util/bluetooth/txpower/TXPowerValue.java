package com.vgaw.nrfconnect.util.bluetooth.txpower;

/**
 * @author caojin
 * @date 2018/3/24
 */
public class TXPowerValue {
    private byte txPower;

    public TXPowerValue() {}

    public TXPowerValue(byte txPower) {
        this.txPower = txPower;
    }

    @Override
    public String toString() {
        return String.valueOf(this.txPower) + "dBm";
    }
}
