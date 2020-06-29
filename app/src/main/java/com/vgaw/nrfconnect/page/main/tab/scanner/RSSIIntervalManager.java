package com.vgaw.nrfconnect.page.main.tab.scanner;

import android.bluetooth.BluetoothDevice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author caojin
 * @date 2018/3/24
 */
public class RSSIIntervalManager {
    private static final int MAX_HIT_LENGTH = 100;
    private RSSIIntervalListener mListener;
    private HashMap<BluetoothDevice, List<RSSIBean>> map = new HashMap<>();

    public void setRSSIIntervalListener(RSSIIntervalListener listener) {
        mListener = listener;
    }

    public void hit(BluetoothDevice device, int rssi) {
        List<RSSIBean> rssiBeanList = map.get(device);
        if (rssiBeanList == null) {
            rssiBeanList = new ArrayList<>();
            map.put(device, rssiBeanList);
        }
        long currentTime = System.currentTimeMillis();
        RSSIBean rssiBean = new RSSIBean(currentTime, rssi);
        long period = -1;
        if (rssiBeanList.size() > 0) {
            period = currentTime - rssiBeanList.get(rssiBeanList.size() - 1).hitTime;
        }
        if (rssiBeanList.size() > MAX_HIT_LENGTH) {
            rssiBeanList.remove(0);
        }
        rssiBeanList.add(rssiBean);

        callGetRSSI(device, rssi, period);
    }

    private void callGetRSSI(BluetoothDevice device, int rssi, long period) {
        if (mListener != null) {
            mListener.onGetRSSI(device, rssi, period);
        }
    }

    public interface RSSIIntervalListener {
        /**
         * @param device
         * @param rssi
         * @param period ms（第一次没有间隔所以为-1）
         */
        void onGetRSSI(BluetoothDevice device, int rssi, long period);
    }

    public static class RSSIBean {
        public long hitTime;
        public int rssi;

        public RSSIBean() {}

        public RSSIBean(long hitTime, int rssi) {
            this.hitTime = hitTime;
            this.rssi = rssi;
        }
    }
}
