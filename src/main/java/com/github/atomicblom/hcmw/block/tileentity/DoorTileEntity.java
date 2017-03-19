package com.github.atomicblom.hcmw.block.tileentity;

import com.foudroyantfactotum.tool.structure.registry.StructureDefinition;
import com.foudroyantfactotum.tool.structure.tileentity.StructureTE;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DoorTileEntity extends StructureTE
{
    public DoorTileEntity(StructureDefinition sd, EnumFacing orientation, boolean mirror) {
        super(sd, orientation, mirror);
    }

    public DoorTileEntity() {}

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
    {
        return false;
    }
}
