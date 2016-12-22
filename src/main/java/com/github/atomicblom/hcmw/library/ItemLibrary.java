package com.github.atomicblom.hcmw.library;

import com.github.atomicblom.hcmw.HomecraftMinewares;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

@GameRegistry.ObjectHolder(HomecraftMinewares.MODID)
public class ItemLibrary {
    public static final Item bed_4post;
    public static final Item bed_canopy;
    public static final Item barrel;

    static {
        bed_4post = null;
        bed_canopy = null;
        barrel = null;
    }
}
