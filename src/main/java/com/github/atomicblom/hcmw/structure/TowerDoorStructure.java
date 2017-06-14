package com.github.atomicblom.hcmw.structure;

import com.foudroyantfactotum.tool.structure.utility.StructureDefinitionBuilder;
import com.foudroyantfactotum.tool.structure.utility.StructureDefinitionProviderBase;
import com.github.atomicblom.hcmw.BlockProperties;
import com.github.atomicblom.hcmw.library.BlockLibrary;
import com.google.common.collect.ImmutableMap;
import net.minecraft.util.math.BlockPos;

public class TowerDoorStructure extends StructureDefinitionProviderBase
{
    @Override
    public StructureDefinitionBuilder getStructureBuild()
    {
        final StructureDefinitionBuilder builder = new StructureDefinitionBuilder();

        builder.setMasterBlock(BlockLibrary.door_tower);
        builder.setShapeBlock(BlockLibrary.shape);

        builder.assignConstructionDef(ImmutableMap.of(
                'w', "minecraft:planks"
        ));

        builder.assignConstructionBlocks(
                new String[] {"w"},
                new String[] {"w"},
                new String[] {"w"},
                new String[] {"w"}
        );

        builder.assignToolFormPosition(BlockPos.ORIGIN);

        builder.setConfiguration(BlockPos.ORIGIN,
                new String[] {"M"},
                new String[] {"-"},
                new String[] {"-"},
                new String[] {"-"}
        );

        final float pixelWidth = 0.0625f;

        builder.setCollisionBoxRule(
                (state) -> !state.getValue(BlockProperties.IS_OPEN),
                new float[] {0.0f, 0.0f, 1.0f - pixelWidth * 3.0f, 1.0f, 4.0f, 1.0f}
        );

        builder.setCollisionBoxRule(
                (state) -> state.getValue(BlockProperties.IS_OPEN),
                new float[] {0.0f, 0.0f, 0.0f, pixelWidth * 3.0f, 4.0f, 1.0f}
        );

        return builder;
    }
}
