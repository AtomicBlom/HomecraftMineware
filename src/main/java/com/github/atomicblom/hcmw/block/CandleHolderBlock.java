package com.github.atomicblom.hcmw.block;

import mcjty.lib.tools.ItemStackTools;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

import static com.github.atomicblom.hcmw.block.BlockProperties.HORIZONTAL_FACING;
import static com.github.atomicblom.hcmw.block.BlockProperties.IS_LIT;

public class CandleHolderBlock extends Block
{
    private final AxisAlignedBB boundingBox = new AxisAlignedBB(0.25f, 0, 0.25f, 0.75f, 0.5f, 0.75f);

    public CandleHolderBlock()
    {
        super(Material.CIRCUITS);
        setHardness(0.0f);
        setSoundType(SoundType.METAL);
        final IBlockState defaultState = blockState
                .getBaseState()
                .withProperty(HORIZONTAL_FACING, EnumFacing.NORTH)
                .withProperty(IS_LIT, false);

        setDefaultState(defaultState);
    }

    ///////////// Block State Management //////////////

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, HORIZONTAL_FACING, IS_LIT);
    }

    @Override
    @Deprecated
    public IBlockState getStateFromMeta(int meta)
    {
        IBlockState stateFromMeta = super.getStateFromMeta(meta);
        EnumFacing facing = EnumFacing.VALUES[(meta & 7)];
        if (facing == EnumFacing.UP || facing == EnumFacing.DOWN) {
            facing = EnumFacing.NORTH;
        }
        stateFromMeta = stateFromMeta.withProperty(IS_LIT, (meta & 8) != 0)
                .withProperty(BlockProperties.HORIZONTAL_FACING, facing);

        return stateFromMeta;
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        int meta = state.getValue(BlockProperties.HORIZONTAL_FACING).ordinal();
        meta |= state.getValue(IS_LIT) ? 8 : 0;
        return meta;
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        return state.getValue(IS_LIT) ? 10 : 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        if(stateIn.getValue(IS_LIT)) {
            final double x = pos.getX() + 0.5D;
            final double y = pos.getY() + 0.5D + 0.1D;
            final double z = pos.getZ() + 0.5D;

            worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, y, z, 0.0D, 0.0D, 0.0D);
            worldIn.spawnParticle(EnumParticleTypes.FLAME, x, y, z, 0.0D, 0.0D, 0.0D);
        }
    }

    ///////////// Rendering //////////////

    @Override
    @Deprecated
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    @Deprecated
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    // Behaviour


    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, ItemStack stack) {
        if (canPlaceOn(world, pos.down())) {
            return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, stack)
                    .withProperty(HORIZONTAL_FACING, placer.getHorizontalFacing());
        }
        return this.getDefaultState();
    }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos pos) {
        return canPlaceOn(world, pos.down());
    }

    private boolean canPlaceOn(IBlockAccess worldIn, BlockPos pos) {
        IBlockState state = worldIn.getBlockState(pos);
        return state.isSideSolid(worldIn, pos, EnumFacing.UP) || state.getBlock().canPlaceTorchOnTop(state, worldIn, pos);
    }


    @Override
    @Deprecated
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
        if (!canPlaceOn(worldIn, pos.down())) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        Boolean isLit = state.getValue(IS_LIT);
        if (heldItem.getItem() == Items.FLINT_AND_STEEL && !isLit) {
            worldIn.setBlockState(pos, state.withProperty(IS_LIT, true), 3);
            heldItem.damageItem(1, playerIn);
            return true;
        } else if (ItemStackTools.isEmpty(heldItem) && isLit) {
            worldIn.setBlockState(pos, state.withProperty(IS_LIT, false), 3);
            return true;
        }
        return false;
    }

    @Override
    @Deprecated
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return boundingBox;
    }

    @Override
    @Deprecated
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn) {
    }
}
