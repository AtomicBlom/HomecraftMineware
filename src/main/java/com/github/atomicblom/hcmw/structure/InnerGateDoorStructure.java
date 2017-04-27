package com.github.atomicblom.hcmw.structure;

import com.foudroyantfactotum.tool.structure.utility.StructureDefinitionBuilder;
import com.foudroyantfactotum.tool.structure.utility.StructureDefinitionProviderBase;
import com.github.atomicblom.hcmw.library.BlockLibrary;
import com.google.common.collect.ImmutableMap;
import net.minecraft.util.math.BlockPos;

public class InnerGateDoorStructure extends StructureDefinitionProviderBase
{
    @Override
    public StructureDefinitionBuilder getStructureBuild()
    {
        final StructureDefinitionBuilder builder = new StructureDefinitionBuilder();

        builder.setMasterBlock(BlockLibrary.door_inner_gate);
        builder.setShapeBlock(BlockLibrary.shape);

        builder.assignConstructionDef(ImmutableMap.of(
                'w', "minecraft:planks"
        ));

        builder.assignConstructionBlocks(
                new String[] {"w ", "ww"},
                new String[] {"w ", "ww"},
                new String[] {"w ", "ww"}

        );

        builder.assignToolFormPosition(BlockPos.ORIGIN);

        builder.setConfiguration(BlockPos.ORIGIN,
                new String[] {"- ", "-M"},
                new String[] {"- ", "--"},
                new String[] {"- ", "--"}

        );

        final float pixelWidth = 0.0625f;

        builder.setCollisionBoxes(
                new float[] {0.0f, 0.0f, 2-pixelWidth * 3, 2.0f, 3.0f, 2}
        );

        return builder;
    }
}
