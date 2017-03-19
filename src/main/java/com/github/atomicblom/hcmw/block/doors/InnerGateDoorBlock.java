package com.github.atomicblom.hcmw.block.doors;

import com.foudroyantfactotum.tool.structure.tileentity.StructureTE;
import com.foudroyantfactotum.tool.structure.utility.StructureDefinitionBuilder;
import com.github.atomicblom.hcmw.block.BaseDoorBlock;
import com.github.atomicblom.hcmw.block.BlockProperties;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import javax.annotation.Nullable;

public class InnerGateDoorBlock extends BaseDoorBlock
{
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
                new float[] {0.0f, 0.0f, 1-pixelWidth * 3, 2.0f, 3.0f, 1}
        );

        return builder;
    }
}
