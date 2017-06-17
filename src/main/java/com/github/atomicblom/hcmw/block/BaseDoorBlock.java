package com.github.atomicblom.hcmw.block;

import com.foudroyantfactotum.tool.structure.block.StructureBlock;
import com.foudroyantfactotum.tool.structure.registry.StructureDefinition;
import com.foudroyantfactotum.tool.structure.tileentity.StructureTE;
import com.github.atomicblom.hcmw.BlockProperties;
import com.github.atomicblom.hcmw.block.tileentity.DoorTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import javax.annotation.Nullable;
import java.util.List;

import static com.foudroyantfactotum.tool.structure.coordinates.TransformLAG.localToGlobalCollisionBoxes;

@SuppressWarnings("deprecation")
public abstract class BaseDoorBlock extends StructureBlock
{
    protected BaseDoorBlock()
    {
        super(true);

        final IBlockState defaultState = blockState
                .getBaseState()
                .withProperty(BlockProperties.HORIZONTAL_FACING, EnumFacing.NORTH)
                .withProperty(BlockProperties.WOOD_VARIANT, WoodVariant.OAK)
                .withProperty(BlockProperties.IS_OPEN, false);
        setHarvestLevel("axe", 2);
        setDefaultState(defaultState);
    }

    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list)
    {
        for (final WoodVariant woodVariant : WoodVariant.values())
        {
            final NBTTagCompound nbtTagCompound = new NBTTagCompound();
            nbtTagCompound.setString("variant", woodVariant.getName());

            final ItemStack itemStack = new ItemStack(this, 1, 0);
            itemStack.setTagCompound(nbtTagCompound);
            list.add(itemStack);
        }
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return new DoorTileEntity(
                getStructureDefinitionProvider(),
                state.getValue(BlockProperties.HORIZONTAL_FACING),
                state.getValue(BlockProperties.MIRROR),
                state.getValue(BlockProperties.WOOD_VARIANT)
        );
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(
                this,
                BlockProperties.HORIZONTAL_FACING,
                BlockProperties.MIRROR,
                BlockProperties.WOOD_VARIANT,
                BlockProperties.IS_OPEN);
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
    {
        IBlockState placementState = super.getStateForPlacement(world, pos, facing.getOpposite(), hitX, hitY, hitZ, meta, placer, hand);
        placementState = placementState.withProperty(MIRROR, false);

        final EnumFacing opposite = placementState.getValue(BlockProperties.HORIZONTAL_FACING);
        final IBlockState rightBlock = world.getBlockState(pos.offset(opposite.rotateYCCW()));
        if (rightBlock.getBlock() == this) {
            placementState = placementState.withProperty(BlockProperties.MIRROR, true);
        }
        placementState = placementState.withProperty(BlockProperties.HORIZONTAL_FACING, opposite);
        placementState = placementState.withProperty(BlockProperties.IS_OPEN, false);
        return placementState;
    }

    @Override
    @Deprecated
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        final IBlockState actualState = super.getActualState(state, worldIn, pos);

        final TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (!(tileEntity instanceof DoorTileEntity)) {
            return actualState;
        }

        return actualState
                .withProperty(BlockProperties.WOOD_VARIANT, ((DoorTileEntity)tileEntity).getType());
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
    {
        final ItemStack pickBlock = super.getPickBlock(state, target, world, pos, player);
        final TileEntity tileEntity = world.getTileEntity(pos);
        if (!(tileEntity instanceof DoorTileEntity))
        {
            return pickBlock;
        }

        NBTTagCompound tagCompound = pickBlock.getTagCompound();
        if (tagCompound == null) {
            tagCompound = new NBTTagCompound();
            pickBlock.setTagCompound(tagCompound);
        }
        tagCompound.setString("variant", ((DoorTileEntity)tileEntity).getType().getName());
        return pickBlock;
    }

    @Override
    public void spawnBreakParticle(World world, StructureTE te, BlockPos local, float sx, float sy, float sz)
    {
        final IBlockState state = world.getBlockState(te.getPos());

        for (int x = 0; x < 4; ++x)
        {
            for (int y = 0; y < 4; ++y)
            {
                for (int z = 0; z < 4; ++z)
                {
                    final double particleX = local.getX() + (x + 0.5D) / 4.0D;
                    final double particleY = local.getY() + (y + 0.5D) / 4.0D;
                    final double particleZ = local.getZ() + (z + 0.5D) / 4.0D;

                    world.spawnParticle(
                            EnumParticleTypes.BLOCK_CRACK,
                            particleX,
                            particleY,
                            particleZ,
                            particleX - local.getX() - 0.5D,
                            particleY - local.getY() - 0.5D,
                            particleZ - local.getZ() - 0.5D,
                            Block.getStateId(state)
                    );
                }
            }
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        final boolean isOpen = !state.getValue(BlockProperties.IS_OPEN);
        worldIn.setBlockState(
                pos,
                state.withProperty(BlockProperties.IS_OPEN, isOpen),
                1 | 2);

        int sound = 1006;
        final WoodVariant woodVariant = state.getValue(BlockProperties.WOOD_VARIANT);
        if (isOpen && woodVariant == WoodVariant.IRON) {
            sound = 1005;
        } else if (!isOpen && woodVariant == woodVariant.IRON) {
            sound = 1011;
        } else if (!isOpen) {
            sound = 1012;
        }

        worldIn.playEvent(playerIn, sound, pos, 0);

        return true;
    }

    @Override
    public boolean onStructureBlockActivated(World world, BlockPos pos, EntityPlayer player, EnumHand hand, BlockPos callPos, EnumFacing side, BlockPos local, float sx, float sy, float sz)
    {
        return onBlockActivated(world, pos, world.getBlockState(pos), player, hand, side, sx, sy, sz);
    }

    @SuppressWarnings("deprecation")
    @Override
    @Deprecated
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    @Override
    protected boolean shouldDecompose() {
        return false;
    }
}
