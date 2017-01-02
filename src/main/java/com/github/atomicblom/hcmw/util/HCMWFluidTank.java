package com.github.atomicblom.hcmw.util;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

public class HCMWFluidTank extends FluidTank
{

    private final TileEntity tileEntity;

    public HCMWFluidTank(TileEntity tileEntity, int capacity)
    {
        super(capacity);
        this.tileEntity = tileEntity;
    }

    @Override
    protected void onContentsChanged()
    {
        tileEntity.markDirty();
        final World world = tileEntity.getWorld();
        final IBlockState blockState = world.getBlockState(tileEntity.getPos());
        world.notifyBlockUpdate(tileEntity.getPos(), blockState, blockState, 2);
    }
}
