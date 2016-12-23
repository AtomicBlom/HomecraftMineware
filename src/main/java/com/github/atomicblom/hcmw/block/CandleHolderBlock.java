package com.github.atomicblom.hcmw.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;

/**
 * Created by codew on 23/12/2016.
 */
public class CandleHolderBlock extends Block
{
    public CandleHolderBlock()
    {
        super(Material.REDSTONE_LIGHT);
    }

    @Override
    @Deprecated
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }
}
