package com.vgaw.nrfconnect.util;

/**
 * Created by dell on 2018/3/15.
 */

public class StringAssembler {
    private StringBuilder sb;
    private String separator;

    public void start(String separator) {
        this.separator = separator;
        this.sb = new StringBuilder();
    }

    public void append(String item) {
        this.sb.append(item).append(Utils.nullToEmpty(separator));
    }

    public String end() {
        if (this.sb != null) {
            if (this.sb.length() > 0) {
                int separatorLength = Utils.nullToEmpty(separator).length();
                return this.sb.substring(0, sb.length() - separatorLength);
            }
        }
        return null;
    }
}
