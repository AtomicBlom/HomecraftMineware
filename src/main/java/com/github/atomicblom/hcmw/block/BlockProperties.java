package com.github.atomicblom.hcmw.block;

import com.foudroyantfactotum.tool.structure.block.StructureBlock;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;

/**
 * Created by codew on 23/12/2016.
 */
public class BlockProperties {
    /** Whether this block connects in the northern direction */
    public static final PropertyBool CONNECT_NORTH = PropertyBool.create("north");
    /** Whether this block connects in the eastern direction */
    public static final PropertyBool CONNECT_EAST = PropertyBool.create("east");
    /** Whether this block connects in the southern direction */
    public static final PropertyBool CONNECT_SOUTH = PropertyBool.create("south");
    /** Whether this block connects in the western direction */
    public static final PropertyBool CONNECT_WEST = PropertyBool.create("west");
    /** Whether this block connects in the up direction */
    public static final PropertyBool CONNECT_UP = PropertyBool.create("up");
    /** Whether this block connects in the down direction */
    public static final PropertyBool CONNECT_DOWN = PropertyBool.create("down");

    /** Types of wood supported */
    public static final PropertyEnum<WoodVariant> WOOD_VARIANT = PropertyEnum.create("variant", WoodVariant.class);

    public static final PropertyBool MIRROR = StructureBlock.MIRROR;

    /** Whether doors are open or not */
    public static final PropertyBool IS_OPEN = PropertyBool.create("is_open");

    public static final PropertyDirection HORIZONTAL_FACING = BlockHorizontal.FACING;

    public static final PropertyDirection FACING = BlockDirectional.FACING;

    public static final PropertyBool OCCUPIED = PropertyBool.create("occupied");

    public static final PropertyBool IS_LIT = PropertyBool.create("lit");
}
