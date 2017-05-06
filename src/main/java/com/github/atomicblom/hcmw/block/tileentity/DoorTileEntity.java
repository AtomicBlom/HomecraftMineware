package com.github.atomicblom.hcmw.block.tileentity;

import com.foudroyantfactotum.tool.structure.registry.StructureDefinition;
import com.foudroyantfactotum.tool.structure.tileentity.StructureTE;
import com.foudroyantfactotum.tool.structure.utility.IStructureDefinitionProvider;
import com.github.atomicblom.hcmw.block.WoodVariant;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DoorTileEntity extends StructureTE
{
    private final WoodVariant type;

    public DoorTileEntity(IStructureDefinitionProvider structureDefinitionProvider, EnumFacing orientation, boolean mirror, WoodVariant type) {
        super(structureDefinitionProvider, orientation, mirror);
        this.type = type;
    }

    public DoorTileEntity() {type = WoodVariant.OAK;}

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
    {
        return false;
    }
}
