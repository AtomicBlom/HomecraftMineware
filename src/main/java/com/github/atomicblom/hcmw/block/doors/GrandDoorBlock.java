package com.github.atomicblom.hcmw.block.doors;

import com.foudroyantfactotum.tool.structure.utility.StructureDefinitionBuilder;
import com.github.atomicblom.hcmw.block.BaseDoorBlock;
import com.google.common.collect.ImmutableMap;
import net.minecraft.util.math.BlockPos;

public class GrandDoorBlock extends BaseDoorBlock
{
    @Override
    public StructureDefinitionBuilder getStructureBuild()
    {
        final StructureDefinitionBuilder builder = new StructureDefinitionBuilder();

        builder.assignConstructionDef(ImmutableMap.of(
                'w', "minecraft:planks"
        ));

        builder.assignConstructionBlocks(
                new String[] {"w"},
                new String[] {"w"},
                new String[] {"w"}

        );

        builder.assignToolFormPosition(BlockPos.ORIGIN);

        builder.setConfiguration(BlockPos.ORIGIN,
                new String[] {"M"},
                new String[] {"-"},
                new String[] {"-"}

        );

        final float pixelWidth = 0.0625f;

        builder.setCollisionBoxes(
                new float[] {0.0f, 0.0f, 1.0f - pixelWidth * 3.0f, 1.0f, 3.0f, 1.0f}
        );

        return builder;
    }
}
