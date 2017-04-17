package com.github.atomicblom.hcmw.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

@SuppressWarnings("deprecation")
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
                .withProperty(BlockProperties.HORIZONTAL_FACING, EnumFacing.NORTH)
                .withProperty(BlockProperties.IS_LIT, false);

        setHarvestLevel("pickaxe", 2);

        setDefaultState(defaultState);
    }

    ///////////// Block State Management //////////////

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, BlockProperties.HORIZONTAL_FACING, BlockProperties.IS_LIT);
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
        stateFromMeta = stateFromMeta.withProperty(BlockProperties.IS_LIT, (meta & 8) != 0)
                .withProperty(BlockProperties.HORIZONTAL_FACING, facing);

        return stateFromMeta;
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        int meta = state.getValue(BlockProperties.HORIZONTAL_FACING).ordinal();
        meta |= state.getValue(BlockProperties.IS_LIT) ? 8 : 0;
        return meta;
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        return state.getValue(BlockProperties.IS_LIT) ? 10 : 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        if(stateIn.getValue(BlockProperties.IS_LIT)) {
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
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {

        if (canPlaceOn(world, pos.down())) {
            return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand)
                    .withProperty(BlockProperties.HORIZONTAL_FACING, placer.getHorizontalFacing());
        }
        return getDefaultState();
    }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos pos) {
        return canPlaceOn(world, pos.down());
    }

    private static boolean canPlaceOn(IBlockAccess worldIn, BlockPos pos) {
        final IBlockState state = worldIn.getBlockState(pos);
        return state.isSideSolid(worldIn, pos, EnumFacing.UP) || state.getBlock().canPlaceTorchOnTop(state, worldIn, pos);
    }

    @Override
    @Deprecated
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
        if (!canPlaceOn(world, pos.down())) {
            dropBlockAsItem(world, pos, state, 0);
            world.setBlockToAir(pos);
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
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return boundingBox;
    }

    @Override
    @Deprecated
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB mask, List<AxisAlignedBB> list, @Nullable Entity entityIn, boolean unknown) {
    }
}
