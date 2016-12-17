package com.github.atomicblom.hcmw.library;

import com.foudroyantfactotum.tool.structure.block.StructureBlock;
import com.github.atomicblom.hcmw.HomecraftMinewares;
import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry;

@GameRegistry.ObjectHolder(HomecraftMinewares.MODID)
public class BlockLibrary {
    public static final StructureBlock bed;

    static {
        bed = null;
    }
}