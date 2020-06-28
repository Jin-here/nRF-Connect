package com.vgaw.nrfconnect.data;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * @author caojin
 * @date 2018/3/25
 */
@Entity
public class DeviceFavorite {
    @Id
    private long id;
    private String name;
    private String address;
    private String addedTime;
    private String lastSeenTime;

    public DeviceFavorite() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddedTime() {
        return addedTime;
    }

    public void setAddedTime(String addedTime) {
        this.addedTime = addedTime;
    }

    public String getLastSeenTime() {
        return lastSeenTime;
    }

    public void setLastSeenTime(String lastSeenTime) {
        this.lastSeenTime = lastSeenTime;
    }
}
