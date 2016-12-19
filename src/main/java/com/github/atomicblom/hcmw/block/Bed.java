package com.github.atomicblom.hcmw.block;

import com.foudroyantfactotum.tool.structure.block.StructureBlock;
import com.foudroyantfactotum.tool.structure.tileentity.StructureTE;
import com.foudroyantfactotum.tool.structure.utility.StructureDefinitionBuilder;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import javax.annotation.Nullable;

public class Bed extends StructureBlock
{
    public Bed() {
        super(false);
    }

    @Override
    public void spawnBreakParticle(World world, StructureTE te, BlockPos local, float sx, float sy, float sz)
    {

    }

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return new StructureTE();
    }

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

    @Override
    public StructureDefinitionBuilder getStructureBuild()
    {
        final StructureDefinitionBuilder builder = new StructureDefinitionBuilder();

        builder.assignConstructionDef(ImmutableMap.of(
                'b', "minecraft:dirt"
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
}
