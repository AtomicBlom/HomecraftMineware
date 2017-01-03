package com.github.atomicblom.hcmw.block;

import com.github.atomicblom.hcmw.HomecraftMineware;
import com.github.atomicblom.hcmw.gui.GuiType;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BaseInventoryBlock extends Block {

    protected BaseInventoryBlock(Material materialIn) {
        super(materialIn);
    }

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Override
    @Deprecated
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    @Deprecated
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        final TileEntity te = worldIn.getTileEntity(pos);
        if (te != null && te instanceof IInventory)
        {
            InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory) te);
        }
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    @Deprecated
    public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param)
    {
        final TileEntity te = worldIn.getTileEntity(pos);
        return te != null && te.receiveClientEvent(id, param);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        final TileEntity te = world.getTileEntity(pos);

        if (te == null)
        {
            return true;
        }

        if (!canOpen(world, pos, state)) {
            return true;
        }

        player.openGui(HomecraftMineware.INSTANCE, getGuiType().getId(), world, pos.getX(), pos.getY(), pos.getZ());

        return true;
    }

    protected abstract GuiType getGuiType();

    protected abstract boolean canOpen(World world, BlockPos pos, IBlockState state);
}
