package com.vgaw.nrfconnect.bean;

/**
 * @author caojin
 * @date 2018/3/1
 */

public class ScannerFilter {
    private String nameAddress;
    private String data;
    private int rssi;
    private boolean favorite;

    public ScannerFilter() {}

    public String getNameAddress() {
        return nameAddress;
    }

    public void setNameAddress(String nameAddress) {
        this.nameAddress = nameAddress;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
}
