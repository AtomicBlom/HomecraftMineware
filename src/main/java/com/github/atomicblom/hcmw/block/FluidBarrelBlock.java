package com.github.atomicblom.hcmw.block;

import com.github.atomicblom.hcmw.block.properties.IHorizontalBlockHelper;
import com.github.atomicblom.hcmw.block.tileentity.FluidBarrelTileEntity;
import com.github.atomicblom.hcmw.block.tileentity.ItemBarrelTileEntity;
import com.github.atomicblom.hcmw.gui.GuiType;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;

import static com.github.atomicblom.hcmw.block.BlockProperties.HORIZONTAL_FACING;

public class FluidBarrelBlock extends BaseInventoryBlock implements IHorizontalBlockHelper
{
    public FluidBarrelBlock()
    {
        super(Material.WOOD);
        final IBlockState defaultState = blockState
                .getBaseState()
                .withProperty(HORIZONTAL_FACING, EnumFacing.NORTH);

        setHarvestLevel("axe", 2);

        setDefaultState(defaultState);
    }

    @Override
    @Nonnull
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, HORIZONTAL_FACING);
    }

    @Override
    @Deprecated
    @Nonnull
    public IBlockState getStateFromMeta(int meta)
    {
        IBlockState stateFromMeta = super.getStateFromMeta(meta);
        stateFromMeta = getHorizontalStateFromMeta(stateFromMeta, meta);
        return stateFromMeta;
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return getHorizontalMetaFromState(state);
    }
    @Override
    @Nonnull
    public IBlockState getStateForPlacement(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ, int meta, @Nonnull EntityLivingBase placer, EnumHand hand) {
        return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand)
                .withProperty(HORIZONTAL_FACING, placer.getHorizontalFacing().getOpposite());
    }

    @Override
    @Deprecated
    @Nonnull
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    protected GuiType getGuiType() {
        return GuiType.FLUID_BARREL;
    }

    @Override
    protected boolean canOpen(World world, BlockPos pos, IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state)
    {
        return new FluidBarrelTileEntity();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {


        final TileEntity te = world.getTileEntity(pos);

        if (te == null || !(te instanceof FluidBarrelTileEntity))
        {
            return true;
        }

        ItemStack heldItem = player.getHeldItem(hand);

        IFluidHandler capability = te.getCapability(FluidBarrelTileEntity.FLUID_HANDLER_CAPABILITY, null);


        FluidActionResult fluidActionResult = FluidUtil.interactWithFluidHandler(heldItem, capability, player);
        if (!fluidActionResult.isSuccess()) {
            return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
        }
        player.setHeldItem(hand, fluidActionResult.getResult());

        return true;
    }

    @Override
    public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side)
    {
        return side == EnumFacing.UP || side == EnumFacing.DOWN;
    }
}
