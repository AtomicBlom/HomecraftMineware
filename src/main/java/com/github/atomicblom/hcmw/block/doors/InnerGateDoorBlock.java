package com.github.atomicblom.hcmw.block.doors;

import com.foudroyantfactotum.tool.structure.utility.StructureDefinitionBuilder;
import com.github.atomicblom.hcmw.block.BaseDoorBlock;
import com.github.atomicblom.hcmw.BlockProperties;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import java.util.Objects;

public class InnerGateDoorBlock extends BaseDoorBlock
{
    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
    {
        IBlockState placementState = super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand);
        placementState = placementState.withProperty(MIRROR, false);
        final EnumFacing opposite = placementState.getValue(BlockProperties.HORIZONTAL_FACING);
        final IBlockState rightBlock = world.getBlockState(pos.offset(opposite.rotateYCCW(), 1));
        if (Objects.equals(rightBlock.getBlock(), this)) {
            placementState = placementState.withProperty(MIRROR, true);
        }
        return placementState;
    }
}
