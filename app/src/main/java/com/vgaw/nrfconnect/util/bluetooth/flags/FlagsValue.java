package com.vgaw.nrfconnect.util.bluetooth.flags;

import com.vgaw.nrfconnect.util.StringAssembler;

import java.util.List;

/**
 * Created by dell on 2018/3/15.
 */

public class FlagsValue {
    private List<FlagsItem> flagsItemList;

    public FlagsValue() {}

    public FlagsValue(List<FlagsItem> flagsItemList) {
        this.flagsItemList = flagsItemList;
    }

    public List<FlagsItem> getFlagsItemList() {
        return flagsItemList;
    }

    public void setFlagsItemList(List<FlagsItem> flagsItemList) {
        this.flagsItemList = flagsItemList;
    }

    @Override
    public String toString() {
        if (this.flagsItemList != null) {
            StringAssembler assembler = new StringAssembler();
            assembler.start(",");
            for (FlagsItem item : flagsItemList) {
                if (item.isEnabled()) {
                    assembler.append(item.getDescription());
                }
            }
            return assembler.end();
        }
        return "";
    }
}
