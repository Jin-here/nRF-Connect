package com.vgaw.nrfconnect.util.bluetooth.manufacturer;

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
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;

/**
 * Created by dell on 2018/3/15.
 *
 * 数据来源：https://www.bluetooth.com/specifications/assigned-numbers/company-identifiers
 */

public class ManufacturerResolver {
    private static final String NAME = "company_identifier.json";
    private static final String TAG = "ManufacturerResolver";

    private static SparseArray<String> map;

    private static void readDataFromFile(Context context) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open(NAME)));
        String raw = reader.readLine();
        JsonUtil.iterate(raw, new IterationArrayCallback() {
            @Override
            public void onIterate(String json) {
                List<String> list = JsonUtil.fromJson(json, TypeBuilder.newInstance(List.class).addTypeParam(String.class).build());
                map.put(Integer.parseInt(list.get(0)), list.get(1));
            }
        });
    }

    public static ManufacturerValue resolve(byte[] value) {
        if (map == null) {
            map = new SparseArray<>();
            try {
                readDataFromFile(ContextUtil.getApplicationContext());
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "resolve: " + e.getLocalizedMessage());
            }
        }
        byte[] manufactureId = new byte[]{value[0], value[1]};
        ByteBuffer buffer = ByteBuffer.wrap(manufactureId);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        int idInt = buffer.getShort();
        byte[] specificData = new byte[value.length - 2];
        System.arraycopy(value, 2, specificData, 0, specificData.length);
        return new ManufacturerValue(map.get(idInt), manufactureId, specificData);
    }
}
