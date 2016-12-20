package com.github.atomicblom.hcmw.block;

import com.foudroyantfactotum.tool.structure.block.StructureBlock;
import com.foudroyantfactotum.tool.structure.tileentity.StructureTE;
import com.foudroyantfactotum.tool.structure.utility.StructureDefinitionBuilder;
import com.github.atomicblom.hcmw.library.BlockLibrary;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
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
                .withProperty(HAS_CANOPY, false);
        setDefaultState(defaultState);
    }

    ///////////// Block State Management //////////////

    private static final IProperty<Boolean> HAS_CANOPY = PropertyBool.create("canopy");

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, BlockHorizontal.FACING, HAS_CANOPY);
    }

    @Override
    @Deprecated
    public IBlockState getStateFromMeta(int meta)
    {
        // Bit 3 is deliberately overloaded with MIRROR, because beds don't mirror.
        final IBlockState stateFromMeta = super.getStateFromMeta(meta);
        return stateFromMeta.withProperty(HAS_CANOPY, (meta & 4) != 0);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        // Bit 3 is deliberately overloaded with MIRROR, because beds don't mirror.
        int metaFromState = super.getMetaFromState(state);
        metaFromState |= state.getValue(HAS_CANOPY) ? 4 : 0;
        return metaFromState;
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
    public StructureDefinitionBuilder getStructureBuild()
    {
        final StructureDefinitionBuilder builder = new StructureDefinitionBuilder();

        builder.assignConstructionDef(ImmutableMap.of(
                'b', "minecraft:bed"
        ));

        builder.assignConstructionBlocks(
                new String[] {"bb", "bb"}
        );

        builder.assignToolFormPosition(BlockPos.ORIGIN);

        builder.setConfiguration(BlockPos.ORIGIN,
                new String[] {"--", "-M"}
                );

        builder.setCollisionBoxes(
                new float[] {0.0f, 0.0f, 0.0f, 2.0f, 0.625f, 2.0f},
                new float[] {0.0f, 0.0f, 0.0f, 0.0625f, 2.0f, 0.0625f},
                new float[] {2-0.0f, 0.0f, 0.0f, 2-0.0625f, 2.0f, 0.0625f},
                new float[] {2-0.0f, 0.0f, 2-0.0f, 2-0.0625f, 2.0f, 2-0.0625f},
                new float[] {0.0f, 0.0f, 2-0.0f, 0.0625f, 2.0f, 2-0.0625f},
                new float[] {0.0f, 0.0f, 0.0f, 2f, 1.0f, 0.0625f}
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
