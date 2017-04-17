package com.github.atomicblom.hcmw.gui;

public enum GuiType {
    BEDSIDE_DRAWERS,
    ITEM_BARREL,
    FLUID_BARREL;

    private static final GuiType[] cache = values();

    public int getId() {
        return ordinal();
    }

    public static GuiType fromId(int id) {
        return cache[id];
    }
}
