package com.github.atomicblom.hcmw.block;

import com.foudroyantfactotum.tool.structure.block.StructureBlock;
import com.foudroyantfactotum.tool.structure.tileentity.StructureTE;
import com.foudroyantfactotum.tool.structure.utility.StructureDefinitionBuilder;
import com.github.atomicblom.hcmw.library.BlockLibrary;
import com.github.atomicblom.hcmw.library.ItemLibrary;
import com.google.common.collect.ImmutableMap;
import javafx.geometry.HorizontalDirection;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Biomes;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import javax.annotation.Nullable;

public class Bed extends StructureBlock implements ILocalizedSubBlock
{
    public Bed() {
        super(false);

        IBlockState defaultState = blockState
                .getBaseState()
                .withProperty(BlockHorizontal.FACING, EnumFacing.NORTH)
                .withProperty(HAS_CANOPY, false)
                .withProperty(BlockBed.OCCUPIED, false);
        setDefaultState(defaultState);
    }

    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        return new ItemStack(ItemLibrary.bed, 1, 2);
    }

    ////////////////// Bed Behaviour //////////////////

    public boolean isBed(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable Entity player)
    {
        return true;
    }

    @Override
    public boolean onStructureBlockActivated(World world, BlockPos pos, EntityPlayer player, EnumHand hand, BlockPos callPos, EnumFacing side, BlockPos local, float sx, float sy, float sz) {
        IBlockState blockState = world.getBlockState(pos);
        return onBlockActivated(world, pos, blockState, player, hand, side, sx, sy, sz);
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (worldIn.isRemote)
        {
            return true;
        }
        else
        {
            if (worldIn.provider.canRespawnHere() && worldIn.getBiome(pos) != Biomes.HELL)
            {
                if (state.getValue(BlockBed.OCCUPIED))
                {
                    EntityPlayer entityplayer = null;

                    for (EntityPlayer player : worldIn.playerEntities)
                    {
                        if (entityplayer.isPlayerSleeping() && entityplayer.bedLocation.equals(pos))
                        {
                            entityplayer = player;
                        }
                    }

                    if (entityplayer != null)
                    {
                        playerIn.sendStatusMessage(new TextComponentTranslation("tile.bed.occupied"), true);
                        return true;
                    }

                    state = state.withProperty(BlockBed.OCCUPIED, Boolean.FALSE);
                    worldIn.setBlockState(pos, state, 4);
                }

                EntityPlayer.SleepResult sleepResult = playerIn.trySleep(pos);

                if (sleepResult == EntityPlayer.SleepResult.OK)
                {
                    state = state.withProperty(BlockBed.OCCUPIED, Boolean.TRUE);
                    worldIn.setBlockState(pos, state, 4);
                    return true;
                }
                else
                {
                    if (sleepResult == EntityPlayer.SleepResult.NOT_POSSIBLE_NOW)
                    {
                        playerIn.sendStatusMessage(new TextComponentTranslation("tile.bed.noSleep"), true);
                    }
                    else if (sleepResult == EntityPlayer.SleepResult.NOT_SAFE)
                    {
                        playerIn.sendStatusMessage(new TextComponentTranslation("tile.bed.notSafe"), true);
                    }
                    else if (sleepResult == EntityPlayer.SleepResult.TOO_FAR_AWAY)
                    {
                        playerIn.sendStatusMessage(new TextComponentTranslation("tile.bed.tooFarAway"), true);
                    }

                    return true;
                }
            }
            else
            {
                worldIn.setBlockToAir(pos);
                BlockPos blockpos = pos.offset(state.getValue(BlockHorizontal.FACING).getOpposite());

                if (worldIn.getBlockState(blockpos).getBlock() == this)
                {
                    worldIn.setBlockToAir(blockpos);
                }

                worldIn.newExplosion(null, (double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, 5.0F, true, true);
                return true;
            }
        }
    }

    ///////////// Block State Management //////////////

    private static final IProperty<Boolean> HAS_CANOPY = PropertyBool.create("canopy");

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, BlockHorizontal.FACING, HAS_CANOPY, BlockBed.OCCUPIED);
    }

    @Override
    @Deprecated
    public IBlockState getStateFromMeta(int meta)
    {
        // Bit 3 is deliberately overloaded with MIRROR, because beds don't mirror.
        IBlockState stateFromMeta = super.getStateFromMeta(meta);
        stateFromMeta = stateFromMeta.withProperty(HAS_CANOPY, (meta & 4) != 0);
        stateFromMeta = stateFromMeta.withProperty(BlockBed.OCCUPIED, (meta & 8) != 0);
        return stateFromMeta;
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        // Bit 3 is deliberately overloaded with MIRROR, because beds don't mirror.
        int metaFromState = super.getMetaFromState(state);
        metaFromState |= state.getValue(HAS_CANOPY) ? 4 : 0;
        metaFromState |= state.getValue(BlockBed.OCCUPIED) ? 8 : 0;
        return metaFromState;
    }

    /**
     * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     */
    public IBlockState withRotation(IBlockState state, Rotation rot)
    {
        return state.withProperty(BlockHorizontal.FACING, rot.rotate(state.getValue(BlockHorizontal.FACING)));
    }

    ///////////// SubBlocks //////////////
    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> list)
    {
        list.add(new ItemStack(BlockLibrary.bed, 1, getMetaFromState(getDefaultState().withProperty(HAS_CANOPY, false))));
        list.add(new ItemStack(BlockLibrary.bed, 1, getMetaFromState(getDefaultState().withProperty(HAS_CANOPY, true))));
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

    @Override
    public StructureDefinitionBuilder getStructureBuild()
    {
        final StructureDefinitionBuilder builder = new StructureDefinitionBuilder();

        builder.assignConstructionDef(ImmutableMap.of(
                'b', "minecraft:bed",
                'f', "minecraft:fence",
                'c', "minecraft:carpet"
        ));

        builder.assignConstructionBlocks(
                new String[] {"bb", "bb"},
                new String[] {"ff", "ff"},
                new String[] {"cc", "cc"}
        );

        builder.assignToolFormPosition(BlockPos.ORIGIN);

        builder.setConfiguration(BlockPos.ORIGIN,
                new String[] {"--", "-M"},
                new String[] {"--", "--"},
                new String[] {"--", "--"}

                );

        builder.setCollisionBoxes(
                //Matress
                new float[] {0.0f, 0.0f, 0.0f, 2.0f, 0.625f, 2.0f},
                //Posts
                new float[] {0.0f, 0.0f, 0.0f, 0.0625f, 2.0f, 0.0625f},
                new float[] {2-0.0f, 0.0f, 0.0f, 2-0.0625f, 2.0f, 0.0625f},
                new float[] {2-0.0f, 0.0f, 2-0.0f, 2-0.0625f, 2.0f, 2-0.0625f},
                new float[] {0.0f, 0.0f, 2-0.0f, 0.0625f, 2.0f, 2-0.0625f},
                //Head board
                new float[] {0.0f, 0.0f, 0.0f, 2f, 1.4f, 0.0625f * 2}
        );

        return builder;
    }

    @Override
    public String unlocalizedVariantPostfix(IBlockState state)
    {
        final Boolean value = state.getValue(HAS_CANOPY);

        return value ? "canopy" : "4post";
    }
}
