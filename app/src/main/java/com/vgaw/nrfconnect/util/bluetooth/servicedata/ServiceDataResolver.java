package com.vgaw.nrfconnect.util.bluetooth.servicedata;

import com.vgaw.nrfconnect.util.bluetooth.BLETypeNameResolver;

/**
 * @author caojin
 * @date 2018/3/24
 */
public class ServiceDataResolver {
    public static ServiceDataValue resolve(byte type, byte[] value) {
        int serviceUUIDLength;
        switch (type) {
            case BLETypeNameResolver.TYPE_SERVICE_DATA_UUID_16:
                serviceUUIDLength = 2;
                break;
            case BLETypeNameResolver.TYPE_SERVICE_DATA_UUID_32:
                serviceUUIDLength = 4;
                break;
            case BLETypeNameResolver.TYPE_SERVICE_DATA_UUID_128:
                serviceUUIDLength = 16;
                break;
            default:
                return null;
        }
        byte[] serviceUUID;
        byte[] serviceData;
        serviceUUID = new byte[serviceUUIDLength];
        serviceData = new byte[value.length - serviceUUIDLength];
        System.arraycopy(value, 0, serviceUUID, 0, serviceUUIDLength);
        System.arraycopy(value, serviceUUIDLength, serviceData, 0, serviceData.length);
        return new ServiceDataValue(serviceUUID, serviceData);
    }
}
