package com.github.atomicblom.hcmw.structure;

import com.foudroyantfactotum.tool.structure.utility.StructureDefinitionBuilder;
import com.foudroyantfactotum.tool.structure.utility.StructureDefinitionProviderBase;
import com.github.atomicblom.hcmw.BlockProperties;
import com.github.atomicblom.hcmw.library.BlockLibrary;
import com.google.common.collect.ImmutableMap;
import net.minecraft.util.math.BlockPos;

public class SuperDoubleDoorStructure extends StructureDefinitionProviderBase
{
    @Override
    public StructureDefinitionBuilder getStructureBuild()
    {
        final StructureDefinitionBuilder builder = new StructureDefinitionBuilder();

        builder.setMasterBlock(BlockLibrary.door_super_double);
        builder.setShapeBlock(BlockLibrary.shape);

        builder.assignConstructionDef(ImmutableMap.of(
                'w', "minecraft:planks"
        ));

        builder.assignToolFormPosition(BlockPos.ORIGIN);

        builder.assignConstructionBlocks(
                new String[] {"w ", "ww"},
                new String[] {"w ", "ww"},
                new String[] {"w ", "ww"}
        );

        builder.setConfiguration(BlockPos.ORIGIN,
                new String[] {"- ", "M-"},
                new String[] {"- ", "--"},
                new String[] {"- ", "--"}
        );

        final float pixelWidth = 0.0625f;

        builder.setCollisionBoxRule(
                (state) -> !state.getValue(BlockProperties.IS_OPEN),
                new float[] {0.0f, 0.0f, 2-pixelWidth * 3, 1.5f, 2.0f, 2.0f}
        );

        builder.setCollisionBoxRule(
                (state) -> state.getValue(BlockProperties.IS_OPEN),
                new float[] {0.0f, 0.0f, 0.0f, pixelWidth * 3, 3.0f, 2}
        );

        return builder;
    }
}
