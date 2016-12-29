package com.github.atomicblom.hcmw.block;

import com.github.atomicblom.hcmw.HomecraftMinewares;
import com.github.atomicblom.hcmw.block.tileentity.BedSideDrawersTileEntity;
import com.github.atomicblom.hcmw.gui.GuiType;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static com.github.atomicblom.hcmw.block.BlockProperties.HORIZONTAL_FACING;

//TODO: Bounding Boxes
public class BedSideDrawersBlock extends BaseInventoryBlock
{
    public BedSideDrawersBlock()
    {
        super(Material.WOOD);
        final IBlockState defaultState = blockState
                .getBaseState()
                .withProperty(HORIZONTAL_FACING, EnumFacing.NORTH);

        setDefaultState(defaultState);
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, HORIZONTAL_FACING);
    }

    @Override
    @Deprecated
    public IBlockState getStateFromMeta(int meta)
    {
        IBlockState stateFromMeta = super.getStateFromMeta(meta);
        EnumFacing facing = EnumFacing.VALUES[meta & 7];
        if (facing == EnumFacing.UP || facing == EnumFacing.DOWN) {
            facing = EnumFacing.NORTH;
        }
        stateFromMeta = stateFromMeta.withProperty(HORIZONTAL_FACING, facing);

        return stateFromMeta;
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(HORIZONTAL_FACING).ordinal();
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand)
                .withProperty(HORIZONTAL_FACING, placer.getHorizontalFacing().getOpposite());
    }

    @Override
    @Deprecated
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return new BedSideDrawersTileEntity();
    }

    @Override
    protected GuiType getGuiType() {
        return GuiType.BEDSIDE_DRAWERS;
    }

    @Override
    protected boolean canOpen(World world, BlockPos pos, IBlockState state) {
        EnumFacing facing = state.getValue(HORIZONTAL_FACING);
        return world.isSideSolid(pos.offset(facing), facing.getOpposite());
    }
}
