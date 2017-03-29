package com.github.atomicblom.hcmw.block;

import com.foudroyantfactotum.tool.structure.block.StructureBlock;
import com.foudroyantfactotum.tool.structure.registry.StructureDefinition;
import com.foudroyantfactotum.tool.structure.tileentity.StructureTE;
import com.github.atomicblom.hcmw.block.tileentity.DoorTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import javax.annotation.Nullable;
import java.util.List;

import static com.foudroyantfactotum.tool.structure.coordinates.TransformLAG.localToGlobalCollisionBoxes;
import static com.github.atomicblom.hcmw.block.BlockProperties.IS_OPEN;

public abstract class BaseDoorBlock extends StructureBlock
{
    protected BaseDoorBlock()
    {
        super(true);

        final IBlockState defaultState = blockState
                .getBaseState()
                .withProperty(BlockProperties.HORIZONTAL_FACING, EnumFacing.NORTH)
                .withProperty(IS_OPEN, false);
        setHarvestLevel("axe", 2);
        setDefaultState(defaultState);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return new DoorTileEntity(getPattern(), state.getValue(BlockProperties.HORIZONTAL_FACING), state.getValue(MIRROR));
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, BlockProperties.HORIZONTAL_FACING, MIRROR, IS_OPEN);
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
    {
        IBlockState placementState = super.getStateForPlacement(world, pos, facing.getOpposite(), hitX, hitY, hitZ, meta, placer, hand);
        placementState = placementState.withProperty(MIRROR, false);

        final EnumFacing opposite = placementState.getValue(BlockProperties.HORIZONTAL_FACING);
        final IBlockState rightBlock = world.getBlockState(pos.offset(opposite.rotateYCCW()));
        if (rightBlock.getBlock() == this) {
            placementState = placementState.withProperty(MIRROR, true);
        }
        placementState = placementState.withProperty(BlockProperties.HORIZONTAL_FACING, opposite);
        placementState = placementState.withProperty(IS_OPEN, false);
        return placementState;
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
        worldIn.setBlockState(
                pos,
                state.withProperty(IS_OPEN, !state.getValue(IS_OPEN)),
                1 | 2);

        return true;
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

    @Override
    @Deprecated
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB mask, List<AxisAlignedBB> list, @Nullable Entity entityIn, boolean p_185477_7_) {
        StructureDefinition pattern = getPattern();
        float[][] collisionBoxes = pattern.getCollisionBoxes();
        if (collisionBoxes != null)
        {
            EnumFacing rotation = state.getValue(BlockHorizontal.FACING);
            boolean mirror = getMirror(state);

            if (state.getValue(IS_OPEN)) {
                rotation = mirror ? rotation.rotateYCCW() : rotation.rotateY();
            }

            localToGlobalCollisionBoxes(
                    pos,
                    0, 0, 0,
                    mask, list, collisionBoxes,
                    rotation, mirror
            );
        }
    }

}
