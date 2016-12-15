package com.github.atomicblom.hcmw.library;

import com.github.atomicblom.hcmw.HomecraftMinewares;
import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry;

@GameRegistry.ObjectHolder(HomecraftMinewares.MODID)
public class BlockLibrary {
    public static final Block bed;

    static {
        bed = null;
    }
}
