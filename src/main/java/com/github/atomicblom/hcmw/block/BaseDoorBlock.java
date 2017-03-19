package com.github.atomicblom.hcmw.block;

import com.foudroyantfactotum.tool.structure.block.StructureBlock;
import com.foudroyantfactotum.tool.structure.tileentity.StructureTE;
import com.github.atomicblom.hcmw.block.tileentity.DoorTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import javax.annotation.Nullable;

public abstract class BaseDoorBlock extends StructureBlock
{
    protected BaseDoorBlock()
    {
        super(true);

        final IBlockState defaultState = blockState
                .getBaseState()
                .withProperty(BlockProperties.HORIZONTAL_FACING, EnumFacing.NORTH);
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
        return new BlockStateContainer(this, BlockProperties.HORIZONTAL_FACING, MIRROR, BlockProperties.IS_OPEN);
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
    {
        IBlockState placementState = super.getStateForPlacement(world, pos, facing.getOpposite(), hitX, hitY, hitZ, meta, placer, hand);
        placementState = placementState.withProperty(MIRROR, false);

        final EnumFacing opposite = placementState.getValue(BlockProperties.HORIZONTAL_FACING).getOpposite();
        final IBlockState rightBlock = world.getBlockState(pos.offset(opposite.rotateY()));
        if (rightBlock.getBlock() == this) {
            placementState = placementState.withProperty(MIRROR, true);
        }
        placementState = placementState.withProperty(BlockProperties.HORIZONTAL_FACING, opposite);
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
                state.withProperty(BlockProperties.IS_OPEN, !state.getValue(BlockProperties.IS_OPEN)),
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


}
