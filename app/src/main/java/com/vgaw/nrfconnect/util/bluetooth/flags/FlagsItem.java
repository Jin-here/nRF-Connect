package com.vgaw.nrfconnect.util.bluetooth.flags;

/**
 * Created by dell on 2018/3/15.
 */

public class FlagsItem {
    private int startPosition;
    private int endPosition;
    private String description;
    private boolean enabled;

    public FlagsItem() {}

    public FlagsItem(int startPosition, int endPosition, String description, boolean enabled) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.description = description;
        this.enabled = enabled;
    }

    public int getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(int startPosition) {
        this.startPosition = startPosition;
    }

    public int getEndPosition() {
        return endPosition;
    }

    public void setEndPosition(int endPosition) {
        this.endPosition = endPosition;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "FlagsItem{" +
                "startPosition=" + startPosition +
                ", endPosition=" + endPosition +
                ", description='" + description + '\'' +
                ", enabled=" + enabled +
                '}';
    }
}
