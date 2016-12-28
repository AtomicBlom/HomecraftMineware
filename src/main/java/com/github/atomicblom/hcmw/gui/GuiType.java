package com.github.atomicblom.hcmw.gui;

/**
 * Created by codew on 28/12/2016.
 */
public enum GuiType {
    BEDSIDE_DRAWERS;

    private static final GuiType[] cache = values();

    public int getId() {
        return ordinal();
    }

    public static GuiType fromId(int id) {
        return cache[id];
    }
}
