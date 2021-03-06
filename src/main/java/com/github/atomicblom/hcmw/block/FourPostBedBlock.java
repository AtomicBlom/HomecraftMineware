package com.github.atomicblom.hcmw.block;

import com.foudroyantfactotum.tool.structure.utility.StructureDefinitionBuilder;
import com.google.common.collect.ImmutableMap;
import net.minecraft.util.math.BlockPos;

/**
 * Created by codew on 21/12/2016.
 */
public class FourPostBedBlock extends BedBlockBase
{
    @Override
    public StructureDefinitionBuilder getStructureBuild()
    {
        final StructureDefinitionBuilder builder = new StructureDefinitionBuilder();

        builder.assignConstructionDef(ImmutableMap.of(
                'b', "minecraft:bed",
                'w', "minecraft:wool"
        ));

        builder.assignConstructionBlocks(
                new String[] {"bb", "bb"},
                new String[] {"ww", "--"}
        );

        builder.assignToolFormPosition(BlockPos.ORIGIN);

        builder.setConfiguration(BlockPos.ORIGIN,
                new String[] {"--", "-M"},
                new String[] {"--", "  "}

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
