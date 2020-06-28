package com.vgaw.nrfconnect.util.bluetooth.serviceuuids;

/**
 * @author caojin
 * @date 2018/3/24
 */
public class ServiceUUIDSResolver {
    public static ServiceUUIDSValue resolve(byte[] value) {
        return new ServiceUUIDSValue(value);
    }
}
