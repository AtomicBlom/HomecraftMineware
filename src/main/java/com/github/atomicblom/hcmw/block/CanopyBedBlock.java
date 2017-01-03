package com.github.atomicblom.hcmw.block;

import com.foudroyantfactotum.tool.structure.utility.StructureDefinitionBuilder;
import com.google.common.collect.ImmutableMap;
import net.minecraft.util.math.BlockPos;

public class CanopyBedBlock extends BedBlockBase
{
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
        final float pixelWidth = 0.0625f;
        builder.setCollisionBoxes(
                //Head board
                new float[] {0.0f, 0.0f, 0.0f, 2.0f, 1.4f, pixelWidth * 2},
                //Posts
                new float[] {0.0f, 0.0f, 0.0f, pixelWidth*3, 3.0f, pixelWidth*3},
                new float[] {2.0f, 0.0f, 0.0f, 2-pixelWidth*3, 3.0f, pixelWidth*3},
                new float[] {2.0f, 0.0f, 2.0f, 2-pixelWidth*3, 3.0f, 2-pixelWidth*3},
                new float[] {0.0f, 0.0f, 2.0f, pixelWidth*3, 3.0f, 2-pixelWidth*3},
                //Matress
                new float[] {pixelWidth * 3, pixelWidth * 6, 0.0f, 2.0f-pixelWidth * 3, pixelWidth * 11, 2.0f-pixelWidth * 2}

        );

        return builder;
    }
}
