package com.github.atomicblom.hcmw.block.lighting;

import com.github.atomicblom.hcmw.BlockProperties;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
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

@SuppressWarnings("deprecation")
public class LanternBlock extends Block
{

    private final AxisAlignedBB boundingBox = new AxisAlignedBB(0.25f, 0, 0.25f, 0.75f, 0.75f, 0.75f);

    public LanternBlock()
    {
        super(Material.REDSTONE_LIGHT);
        final IBlockState defaultState = blockState
                .getBaseState()
                .withProperty(BlockProperties.FACING, EnumFacing.DOWN)
                .withProperty(BlockProperties.IS_LIT, false);

        setHarvestLevel("pickaxe", 2);

        setDefaultState(defaultState);
    }

    @Override
    @Deprecated
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return boundingBox;
    }

    ///////////// Items //////////////
    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items)
    {
        items.add(new ItemStack(this, 1, 0));
        items.add(new ItemStack(this, 1, 1));
    }

    ///////////// Block State Management //////////////

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, BlockProperties.FACING, BlockProperties.IS_LIT);
    }

    @Override
    @Deprecated
    public IBlockState getStateFromMeta(int meta)
    {
        IBlockState stateFromMeta = super.getStateFromMeta(meta);
        stateFromMeta = stateFromMeta.withProperty(BlockProperties.IS_LIT, (meta & 8) != 0)
        .withProperty(BlockProperties.FACING, EnumFacing.VALUES[(meta & 7)]);

        return stateFromMeta;
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        int meta = state.getValue(BlockProperties.FACING).ordinal();
        meta |= state.getValue(BlockProperties.IS_LIT) ? 8 : 0;
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
        if (stateIn.getValue(BlockProperties.IS_LIT)) {
            final double x = pos.getX() + 0.5D;
            final double y = pos.getY() + 0.4D;
            final double z = pos.getZ() + 0.5D;

            worldIn.spawnParticle(EnumParticleTypes.FLAME, x, y, z, 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        final ItemStack heldItem = playerIn.getHeldItem(hand);
        final Boolean isLit = state.getValue(BlockProperties.IS_LIT);

        if (heldItem.getItem() == Items.FLINT_AND_STEEL) {
            if (!isLit)
            {
                worldIn.setBlockState(pos, state.withProperty(BlockProperties.IS_LIT, true), 3);
                heldItem.damageItem(1, playerIn);
                worldIn.playSound(playerIn, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, worldIn.rand.nextFloat() * 0.4F + 0.8F);
            }
            return true;
        }

        if (heldItem.isEmpty() && isLit) {
            worldIn.setBlockState(pos, state.withProperty(BlockProperties.IS_LIT, false), 3);
            return true;
        }
        return false;
    }

    @Override
    @Deprecated
    public int getLightValue(IBlockState state) {
        return state.getValue(BlockProperties.IS_LIT) ? 15 : 0;
    }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos pos) {
        return BlockProperties.FACING.getAllowedValues()
                .stream()
                .anyMatch(facing -> canPlaceAt(world, pos, facing));
    }

    private static boolean canPlaceAt(IBlockAccess worldIn, BlockPos pos, EnumFacing facing)
    {
        final BlockPos blockpos = pos.offset(facing.getOpposite());
        return worldIn.isSideSolid(blockpos, facing, true) || facing == EnumFacing.UP && canPlaceOn(worldIn, blockpos);
    }

    private static boolean canPlaceOn(IBlockAccess worldIn, BlockPos pos) {
        final IBlockState state = worldIn.getBlockState(pos);
        return state.isSideSolid(worldIn, pos, EnumFacing.UP) || state.getBlock().canPlaceTorchOnTop(state, worldIn, pos);
    }

    private final EnumFacing[] preferredDirections = {
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
            final ItemStack heldItem = placer.getHeldItem(hand);
            final boolean isLit = heldItem.getItemDamage() != 0;

            return super
                    .getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand)
                    .withProperty(BlockProperties.FACING, facing.getOpposite())
                    .withProperty(BlockProperties.IS_LIT, isLit);
        }

        for (final EnumFacing preferredDirection : preferredDirections) {
            if (canPlaceAt(world, pos, preferredDirection)) {
                return super
                        .getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand)
                        .withProperty(BlockProperties.FACING, preferredDirection.getOpposite());
            }
        }
        return getDefaultState();
    }

    @Override
    @Deprecated
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
        if (!canPlaceAt(world, pos, state.getValue(BlockProperties.FACING).getOpposite())) {
            for (final EnumFacing preferredDirection : preferredDirections) {
                if (canPlaceAt(world, pos, preferredDirection)) {
                    world.setBlockState(pos, state.withProperty(BlockProperties.FACING, preferredDirection.getOpposite()), 3);
                    return;
                }
            }

            dropBlockAsItem(world, pos, state, 0);
            world.setBlockToAir(pos);
        }
    }
}
