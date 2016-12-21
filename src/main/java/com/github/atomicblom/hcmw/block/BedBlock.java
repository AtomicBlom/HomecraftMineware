package com.github.atomicblom.hcmw.block;

import com.foudroyantfactotum.tool.structure.block.StructureBlock;
import com.foudroyantfactotum.tool.structure.tileentity.StructureTE;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayer.SleepResult;
import net.minecraft.init.Biomes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import javax.annotation.Nullable;

public abstract class BedBlock extends StructureBlock
{
    protected BedBlock() {
        super(false);

        final IBlockState defaultState = blockState
                .getBaseState()
                .withProperty(BlockHorizontal.FACING, EnumFacing.NORTH)
                .withProperty(BlockBed.OCCUPIED, false);
        setDefaultState(defaultState);
    }

    ////////////////// Bed Behaviour //////////////////

    @Override
    public boolean isBed(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable Entity player)
    {
        return true;
    }

    @Override
    public boolean onStructureBlockActivated(World world, BlockPos pos, EntityPlayer player, EnumHand hand, BlockPos callPos, EnumFacing side, BlockPos local, float sx, float sy, float sz) {
        final IBlockState state = world.getBlockState(pos);
        return onBlockActivated(world, pos, state, player, hand, side, sx, sy, sz);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState blockState, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        IBlockState state = blockState;
        if (worldIn.isRemote)
        {
            return true;
        } else
        {
            if (worldIn.provider.canRespawnHere() && worldIn.getBiome(pos) != Biomes.HELL)
            {
                if (state.getValue(BlockBed.OCCUPIED))
                {
                    boolean isBedOccupied = false;

                    for (final EntityPlayer player : worldIn.playerEntities)
                    {
                        if (player.isPlayerSleeping() && player.bedLocation.equals(pos))
                        {
                            isBedOccupied = true;
                        }
                    }

                    if (isBedOccupied)
                    {
                        playerIn.sendStatusMessage(new TextComponentTranslation("tile.bed.occupied"), true);
                        return true;
                    }

                    state = state.withProperty(BlockBed.OCCUPIED, Boolean.FALSE);
                    worldIn.setBlockState(pos, state, 4);
                }

                final SleepResult sleepResult = playerIn.trySleep(pos);

                if (sleepResult == SleepResult.OK)
                {
                    state = state.withProperty(BlockBed.OCCUPIED, Boolean.TRUE);
                    worldIn.setBlockState(pos, state, 4);
                    return true;
                } else
                {
                    if (sleepResult == SleepResult.NOT_POSSIBLE_NOW)
                    {
                        playerIn.sendStatusMessage(new TextComponentTranslation("tile.bed.noSleep"), true);
                    } else if (sleepResult == SleepResult.NOT_SAFE)
                    {
                        playerIn.sendStatusMessage(new TextComponentTranslation("tile.bed.notSafe"), true);
                    } else if (sleepResult == SleepResult.TOO_FAR_AWAY)
                    {
                        playerIn.sendStatusMessage(new TextComponentTranslation("tile.bed.tooFarAway"), true);
                    }

                    return true;
                }
            } else
            {
                worldIn.setBlockToAir(pos);
                final BlockPos blockpos = pos.offset(state.getValue(BlockHorizontal.FACING).getOpposite());

                if (worldIn.getBlockState(blockpos).getBlock() == this)
                {
                    worldIn.setBlockToAir(blockpos);
                }

                worldIn.newExplosion(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 5.0F, true, true);
                return true;
            }
        }
    }

    ///////////// Block State Management //////////////

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, BlockHorizontal.FACING, BlockBed.OCCUPIED);
    }

    @Override
    @Deprecated
    public IBlockState getStateFromMeta(int meta)
    {
        IBlockState stateFromMeta = super.getStateFromMeta(meta);
        stateFromMeta = stateFromMeta.withProperty(BlockBed.OCCUPIED, (meta & 4) != 0);
        return stateFromMeta;
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        // Bit 3 is deliberately overloaded with MIRROR, because beds don't mirror.
        int metaFromState = super.getMetaFromState(state);
        metaFromState |= state.getValue(BlockBed.OCCUPIED) ? 4 : 0;
        return metaFromState;
    }

    @Override
    @Deprecated
    public IBlockState withRotation(IBlockState state, Rotation rot)
    {
        return state.withProperty(BlockHorizontal.FACING, rot.rotate(state.getValue(BlockHorizontal.FACING)));
    }

    ///////////// Rendering //////////////
    @Override
    @Deprecated
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    ///////////// Structure //////////////
    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return new StructureTE();
    }

    @Override
    public void spawnBreakParticle(World world, StructureTE te, BlockPos local, float sx, float sy, float sz)
    {

    }

    @Override
    protected boolean shouldDecompose() {
        return false;
    }
}
