package com.github.atomicblom.hcmw.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

import static com.github.atomicblom.hcmw.block.BlockProperties.FACING;
import static com.github.atomicblom.hcmw.block.BlockProperties.IS_LIT;

public class LanternBlock extends Block
{

    private final AxisAlignedBB boundingBox = new AxisAlignedBB(0.25f, 0, 0.25f, 0.75f, 0.75f, 0.75f);

    public LanternBlock()
    {
        super(Material.REDSTONE_LIGHT);
        final IBlockState defaultState = blockState
                .getBaseState()
                .withProperty(FACING, EnumFacing.DOWN)
                .withProperty(IS_LIT, false);

        setDefaultState(defaultState);
    }

    @Override
    @Deprecated
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return boundingBox;
    }

    ///////////// Block State Management //////////////

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING, IS_LIT);
    }

    @Override
    @Deprecated
    public IBlockState getStateFromMeta(int meta)
    {
        IBlockState stateFromMeta = super.getStateFromMeta(meta);
        stateFromMeta = stateFromMeta.withProperty(IS_LIT, (meta & 8) != 0)
        .withProperty(FACING, EnumFacing.VALUES[(meta & 7)]);

        return stateFromMeta;
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        int meta = state.getValue(FACING).ordinal();
        meta |= state.getValue(IS_LIT) ? 8 : 0;
        return meta;
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

    @Override
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        if (stateIn.getValue(IS_LIT)) {
            final double x = pos.getX() + 0.5D;
            final double y = pos.getY() + 0.4D;
            final double z = pos.getZ() + 0.5D;

            worldIn.spawnParticle(EnumParticleTypes.FLAME, x, y, z, 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack heldItem = playerIn.getHeldItem(hand);
        Boolean isLit = state.getValue(IS_LIT);
        if (heldItem.getItem() == Items.FLINT_AND_STEEL) {
            if (!isLit)
            {
                worldIn.setBlockState(pos, state.withProperty(IS_LIT, true), 3);
                heldItem.damageItem(1, playerIn);
                worldIn.playSound(playerIn, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, worldIn.rand.nextFloat() * 0.4F + 0.8F);
            }
            return true;
        } else if (heldItem.isEmpty() && isLit) {
            worldIn.setBlockState(pos, state.withProperty(IS_LIT, false), 3);
            return true;
        }
        return false;
    }

    @Override
    @Deprecated
    public int getLightValue(IBlockState state) {
        return state.getValue(IS_LIT) ? 15 : 0;
    }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos pos) {
        return FACING.getAllowedValues()
                .stream()
                .anyMatch(facing -> canPlaceAt(world, pos, facing));
    }

    private boolean canPlaceAt(IBlockAccess worldIn, BlockPos pos, EnumFacing facing)
    {
        BlockPos blockpos = pos.offset(facing.getOpposite());
        return worldIn.isSideSolid(blockpos, facing, true) || facing.equals(EnumFacing.UP) && this.canPlaceOn(worldIn, blockpos);
    }

    private boolean canPlaceOn(IBlockAccess worldIn, BlockPos pos) {
        IBlockState state = worldIn.getBlockState(pos);
        return state.isSideSolid(worldIn, pos, EnumFacing.UP) || state.getBlock().canPlaceTorchOnTop(state, worldIn, pos);
    }

    EnumFacing[] preferredDirections = new EnumFacing[] {
            EnumFacing.DOWN,
            EnumFacing.UP,
            EnumFacing.NORTH,
            EnumFacing.SOUTH,
            EnumFacing.EAST,
            EnumFacing.WEST,
    };

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {

        if (canPlaceAt(world, pos, facing)) {
            return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand)
                    .withProperty(FACING, facing.getOpposite());
        } else {
            for (EnumFacing preferredDirection : preferredDirections) {
                if (canPlaceAt(world, pos, preferredDirection)) {
                    return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand)
                            .withProperty(FACING, preferredDirection.getOpposite());
                }
            }
        }
        return this.getDefaultState();
    }

    @Override
    @Deprecated
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
        if (!canPlaceAt(world, pos, state.getValue(FACING).getOpposite())) {
            for (EnumFacing preferredDirection : preferredDirections) {
                if (canPlaceAt(world, pos, preferredDirection)) {
                    world.setBlockState(pos, state.withProperty(FACING, preferredDirection.getOpposite()), 3);
                    return;
                }
            }

            this.dropBlockAsItem(world, pos, state, 0);
            world.setBlockToAir(pos);
        }
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
        IBlockState blockState = world.getBlockState(pos);

        super.onNeighborChange(world, pos, neighbor);
    }
}
