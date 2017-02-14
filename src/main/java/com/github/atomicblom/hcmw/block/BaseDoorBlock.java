package com.github.atomicblom.hcmw.block;

import com.foudroyantfactotum.tool.structure.block.StructureBlock;
import com.foudroyantfactotum.tool.structure.tileentity.StructureTE;
import com.foudroyantfactotum.tool.structure.utility.StructureDefinitionBuilder;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import javax.annotation.Nullable;

/**
 * Created by codew on 14/02/2017.
 */
public class BaseDoorBlock extends StructureBlock
{
    public BaseDoorBlock()
    {
        super(true);

        final IBlockState defaultState = blockState
                .getBaseState()
                .withProperty(BlockProperties.HORIZONTAL_FACING, EnumFacing.NORTH);
        setDefaultState(defaultState);
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, BlockProperties.HORIZONTAL_FACING, MIRROR);
    }

    @Override
    public void spawnBreakParticle(World world, StructureTE te, BlockPos local, float sx, float sy, float sz)
    {

    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return new StructureTE(getPattern(), state.getValue(BlockProperties.HORIZONTAL_FACING), state.getValue(MIRROR));
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
                'w', "minecraft:planks"
        ));

        builder.assignConstructionBlocks(
                new String[] {"ww"},
                new String[] {"ww"},
                new String[] {"ww"}

        );

        builder.assignToolFormPosition(BlockPos.ORIGIN);

        builder.setConfiguration(BlockPos.ORIGIN,
                new String[] {"M-"},
                new String[] {"--"},
                new String[] {"--"}

        );

        final float pixelWidth = 0.0625f;

        builder.setCollisionBoxes(
                //Head board
                new float[] {0.0f, 0.0f, 0.0f, 2f, 1.4f, pixelWidth * 2},
                //Posts
                new float[] {2.0f, 0.0f, 2.0f, 2-pixelWidth*3, 1.0f-pixelWidth*1.5f, 2-pixelWidth*3},
                new float[] {0.0f, 0.0f, 2.0f, pixelWidth*3, 1.0f-pixelWidth*1.5f, 2-pixelWidth*3},
                //Matress
                new float[] {pixelWidth * 3, pixelWidth * 6, 0.0f, 2.0f-pixelWidth * 3, pixelWidth * 11, 2.0f-pixelWidth * 2}
        );

        return builder;
    }
}
