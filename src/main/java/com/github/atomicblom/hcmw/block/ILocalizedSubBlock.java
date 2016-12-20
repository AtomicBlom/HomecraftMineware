package com.github.atomicblom.hcmw.block;

import net.minecraft.block.state.IBlockState;

public interface ILocalizedSubBlock
{
    String unlocalizedVariantPostfix(IBlockState state);

    default String getUnlocalizedName(IBlockState state) {
        return state.getBlock().getUnlocalizedName() + '_' + unlocalizedVariantPostfix(state);
    }
}
