package com.vgaw.nrfconnect.util.bluetooth;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;

import com.vgaw.nrfconnect.thirdparty.json.JsonUtil;
import com.vgaw.nrfconnect.thirdparty.json.TypeBuilder;
import com.vgaw.nrfconnect.thirdparty.json.callback.IterationArrayCallback;
import com.vgaw.nrfconnect.util.ContextUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;

/**
 * 数据来源：
 * service:https://www.bluetooth.com/specifications/gatt/services
 * characteristic:https://www.bluetooth.com/specifications/gatt/characteristics
 */
public class BLENamesResolver {
    private static final String NAME_SERVICE = "gatt_service.json";
    private static final String NAME_CHARACTERISTIC = "gatt_characteristic.json";
    private static final String TAG = "BLENamesResolver";

    private static HashMap<String, String> mServices;
    private static HashMap<String, String> mCharacteristics;
    private static SparseArray<String> mValueFormats = new SparseArray<String>();
    private static SparseArray<String> mAppearance = new SparseArray<String>();
    private static SparseArray<String> mHeartRateSensorLocation = new SparseArray<String>();

    private static void readFromFile(Context context, final boolean service) throws IOException {
        String fileName = (service ? NAME_SERVICE : NAME_CHARACTERISTIC);
        BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open(fileName)));
        String raw = reader.readLine();
        JsonUtil.iterate(raw, new IterationArrayCallback() {
            @Override
            public void onIterate(String json) {
                List<String> list = JsonUtil.fromJson(json, TypeBuilder.newInstance(List.class).addTypeParam(String.class).build());
                if (service) {
                    mServices.put(list.get(1), list.get(0));
                } else {
                    mCharacteristics.put(list.get(1), list.get(0));
                }
            }
        });
    }

    public static String resolveServiceName(final String uuid) {
        if (mServices == null) {
            mServices = new HashMap<>();
            try {
                readFromFile(ContextUtil.getApplicationContext(), true);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "resolveServiceName: " + e.getLocalizedMessage());
            }
        }
        String prefix = uuid.substring(4, uuid.indexOf("-"));
        String result = mServices.get(prefix);
        if (result == null) {
            result = "Unknown Service";
        }
        return result;
    }

    public static String resolveCharacteristicName(final String uuid) {
        if (mCharacteristics == null) {
            mCharacteristics = new HashMap<>();
            try {
                readFromFile(ContextUtil.getApplicationContext(), false);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "resolveCharacteristicName: " + e.getLocalizedMessage());
            }
        }
        String prefix = uuid.substring(4, uuid.indexOf("-"));
        String result = mCharacteristics.get(prefix);
        if (result == null) {
            result = "Unknown Characteristic";
        }
        return result;
    }

    public static String resolveValueTypeDescription(final int format) {
        Integer tmp = Integer.valueOf(format);
        return mValueFormats.get(tmp, "Unknown Format");
    }

    public static String resolveUuid(final String uuid) {
        String result = mServices.get(uuid);
        if (result != null) {
            return "Service: " + result;
        }

        result = mCharacteristics.get(uuid);
        if (result != null) {
            return "Characteristic: " + result;
        }

        result = "Unknown UUID";
        return result;
    }

    public static String resolveAppearance(int key) {
        Integer tmp = Integer.valueOf(key);
        return mAppearance.get(tmp, "Unknown Appearance");
    }

    public static String resolveHeartRateSensorLocation(int key) {
        Integer tmp = Integer.valueOf(key);
        return mHeartRateSensorLocation.get(tmp, "Other");
    }

    public static boolean isService(final String uuid) {
        return mServices.containsKey(uuid);
    }

    public static boolean isCharacteristic(final String uuid) {
        return mCharacteristics.containsKey(uuid);
    }

    static {
        mValueFormats.put(Integer.valueOf(52), "32bit float");
        mValueFormats.put(Integer.valueOf(50), "16bit float");
        mValueFormats.put(Integer.valueOf(34), "16bit signed int");
        mValueFormats.put(Integer.valueOf(36), "32bit signed int");
        mValueFormats.put(Integer.valueOf(33), "8bit signed int");
        mValueFormats.put(Integer.valueOf(18), "16bit unsigned int");
        mValueFormats.put(Integer.valueOf(20), "32bit unsigned int");
        mValueFormats.put(Integer.valueOf(17), "8bit unsigned int");

        // lets add also couple appearance string description
        // https://developer.bluetooth.org/gatt/characteristics/Pages/CharacteristicViewer.aspx?u=org.bluetooth.characteristic.gap.appearance.xml
        mAppearance.put(Integer.valueOf(833), "Heart Rate Sensor: Belt");
        mAppearance.put(Integer.valueOf(832), "Generic Heart Rate Sensor");
        mAppearance.put(Integer.valueOf(0), "Unknown");
        mAppearance.put(Integer.valueOf(64), "Generic Phone");
        mAppearance.put(Integer.valueOf(1157), "Cycling: Speed and Cadence Sensor");
        mAppearance.put(Integer.valueOf(1152), "General Cycling");
        mAppearance.put(Integer.valueOf(1153), "Cycling Computer");
        mAppearance.put(Integer.valueOf(1154), "Cycling: Speed Sensor");
        mAppearance.put(Integer.valueOf(1155), "Cycling: Cadence Sensor");
        mAppearance.put(Integer.valueOf(1156), "Cycling: Speed and Cadence Sensor");
        mAppearance.put(Integer.valueOf(1157), "Cycling: Power Sensor");

        mHeartRateSensorLocation.put(Integer.valueOf(0), "Other");
        mHeartRateSensorLocation.put(Integer.valueOf(1), "Chest");
        mHeartRateSensorLocation.put(Integer.valueOf(2), "Wrist");
        mHeartRateSensorLocation.put(Integer.valueOf(3), "Finger");
        mHeartRateSensorLocation.put(Integer.valueOf(4), "Hand");
        mHeartRateSensorLocation.put(Integer.valueOf(5), "Ear Lobe");
        mHeartRateSensorLocation.put(Integer.valueOf(6), "Foot");
    }
}
