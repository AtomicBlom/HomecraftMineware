package com.github.atomicblom.hcmw.block.properties;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;

import static com.github.atomicblom.hcmw.block.BlockProperties.HORIZONTAL_FACING;

public interface IHorizontalBlockHelper {
    default IBlockState getHorizontalStateFromMeta(IBlockState existingState, int meta) {
        return getHorizontalStateFromMeta(existingState, meta, 0);
    }

    default IBlockState getHorizontalStateFromMeta(IBlockState existingState, int meta, int shift) {
        EnumFacing facing = EnumFacing.VALUES[(meta & 7) >> shift];
        if (facing == EnumFacing.UP || facing == EnumFacing.DOWN) {
            facing = EnumFacing.NORTH;
        }
        existingState = existingState.withProperty(HORIZONTAL_FACING, facing);

        return existingState;
    }

    default int getHorizontalMetaFromState(IBlockState state) {
        return getHorizontalMetaFromState(state, 0);
    }

    default int getHorizontalMetaFromState(IBlockState state, int shift) {
        return state.getValue(HORIZONTAL_FACING).ordinal() << shift;
    }
}
