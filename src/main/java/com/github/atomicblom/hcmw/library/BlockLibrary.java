package com.github.atomicblom.hcmw.library;

import com.foudroyantfactotum.tool.structure.block.StructureBlock;
import com.github.atomicblom.hcmw.HomecraftMinewares;
import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry;

@GameRegistry.ObjectHolder(HomecraftMinewares.MODID)
public class BlockLibrary {
    public static final StructureBlock bed_4post;
    public static final StructureBlock bed_canopy;
    public static final Block barrel;
    public static final Block lantern;
    public static final Block candle_holder;
    public static final Block bed_side_drawers;

    static {
        bed_4post = null;
        bed_canopy = null;
        barrel = null;
        lantern = null;
        candle_holder = null;
        bed_side_drawers = null;
    }
}
