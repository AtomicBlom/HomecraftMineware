package com.github.atomicblom.hcmw.library;

import com.foudroyantfactotum.tool.structure.block.StructureBlock;
import com.foudroyantfactotum.tool.structure.block.StructureShapeBlock;
import com.github.atomicblom.hcmw.HomecraftMineware;
import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@SuppressWarnings({"Duplicates", "AssignmentToNull"})
@ObjectHolder(HomecraftMineware.MODID)
public final class BlockLibrary {
    public static final StructureBlock bed_4post;
    public static final StructureBlock bed_canopy;
    public static final StructureBlock door_inner_gate;
    public static final StructureBlock door_tower;
    public static final StructureBlock door_grand;
    public static final StructureBlock door_super_double;
    public static final Block item_barrel;
    public static final Block fluid_barrel;
    public static final Block lantern;
    public static final Block candle_holder;
    public static final Block bed_side_drawers;
    public static final StructureShapeBlock shape;

	static {
        bed_4post = null;
        bed_canopy = null;
        door_inner_gate = null;
        door_tower = null;
        door_grand = null;
        door_super_double = null;
        item_barrel = null;
        fluid_barrel = null;
        lantern = null;
        candle_holder = null;
        bed_side_drawers = null;
        shape = null;
    }
}
