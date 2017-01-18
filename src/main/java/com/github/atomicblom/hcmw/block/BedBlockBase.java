package com.github.atomicblom.hcmw.block;

import com.foudroyantfactotum.tool.structure.block.StructureBlock;
import com.foudroyantfactotum.tool.structure.registry.StructureDefinition;
import com.foudroyantfactotum.tool.structure.tileentity.StructureTE;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleDigging;
import net.minecraft.client.particle.ParticleManager;
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
import java.util.Random;

public abstract class BedBlockBase extends StructureBlock
{
    protected BedBlockBase() {
        super(false);

        final IBlockState defaultState = blockState
                .getBaseState()
                .withProperty(BlockProperties.HORIZONTAL_FACING, EnumFacing.NORTH)
                .withProperty(BlockProperties.OCCUPIED, false);
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
    public void setBedOccupied(IBlockAccess world, BlockPos pos, EntityPlayer player, boolean occupied)
    {
        if (world instanceof World)
        {
            final StructureTE tileEntity = (StructureTE)world.getTileEntity(pos);
            IBlockState state = world.getBlockState(pos);
            state = state.getBlock()
                    .getActualState(state, world, pos)
                    .withProperty(BlockProperties.OCCUPIED, occupied);

            ((World)world).setBlockState(pos, state, 6);
            final StructureTE newTileEntity = (StructureTE)world.getTileEntity(pos);
            newTileEntity.configureBlock(tileEntity.getLocal(), tileEntity.getRegHash());
        }
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
                if (state.getValue(BlockProperties.OCCUPIED))
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

                    state = state.withProperty(BlockProperties.OCCUPIED, Boolean.FALSE);
                    final StructureTE tileEntity = (StructureTE)worldIn.getTileEntity(pos);
                    worldIn.setBlockState(pos, state, 6);
                    final StructureTE newTileEntity = (StructureTE)worldIn.getTileEntity(pos);
                    newTileEntity.configureBlock(tileEntity.getLocal(), tileEntity.getRegHash());
                }

                final BlockPos sleepPosition = pos.offset(state.getValue(BlockProperties.HORIZONTAL_FACING));

                final SleepResult sleepResult = playerIn.trySleep(sleepPosition);

                if (sleepResult == SleepResult.OK)
                {
                    final StructureTE tileEntity = (StructureTE)worldIn.getTileEntity(pos);

                    state = state.withProperty(BlockProperties.OCCUPIED, Boolean.TRUE);
                    worldIn.setBlockState(pos, state, 6);

                    final StructureTE newTileEntity = (StructureTE)worldIn.getTileEntity(pos);
                    newTileEntity.configureBlock(tileEntity.getLocal(), tileEntity.getRegHash());

                    playerIn.setPosition(playerIn.posX, playerIn.posY + 1, playerIn.posZ);
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
                final BlockPos blockpos = pos.offset(state.getValue(BlockProperties.HORIZONTAL_FACING).getOpposite());

                if (worldIn.getBlockState(blockpos).getBlock() == this)
                {
                    worldIn.setBlockToAir(blockpos);
                }

                worldIn.newExplosion(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 5.0F, true, true);
                return true;
            }
        }
    }

    @Override
    public int quantityDropped(Random rnd)
    {
        return 1;
    }

    ///////////// Block State Management //////////////

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, BlockProperties.HORIZONTAL_FACING, BlockProperties.OCCUPIED);
    }

    @Override
    @Deprecated
    public IBlockState getStateFromMeta(int meta)
    {
        IBlockState stateFromMeta = super.getStateFromMeta(meta);
        stateFromMeta = stateFromMeta.withProperty(BlockProperties.OCCUPIED, (meta & 4) != 0);
        return stateFromMeta;
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        // Bit 3 is deliberately overloaded with MIRROR, because beds don't mirror.
        int metaFromState = super.getMetaFromState(state);
        metaFromState |= state.getValue(BlockProperties.OCCUPIED) ? 4 : 0;
        return metaFromState;
    }

    @Override
    @Deprecated
    public IBlockState withRotation(IBlockState state, Rotation rot)
    {
        return state.withProperty(BlockProperties.HORIZONTAL_FACING, rot.rotate(state.getValue(BlockProperties.HORIZONTAL_FACING)));
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
        final StructureDefinition pattern = getPattern();
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
    protected boolean shouldDecompose() {
        return false;
    }
}
