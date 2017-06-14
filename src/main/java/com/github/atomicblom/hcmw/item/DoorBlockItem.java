package com.github.atomicblom.hcmw.item;

import com.foudroyantfactotum.tool.structure.item.StructureBlockItem;
import com.github.atomicblom.hcmw.BlockProperties;
import com.github.atomicblom.hcmw.block.WoodVariant;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class DoorBlockItem extends StructureBlockItem
{
    public DoorBlockItem(Block block)
    {
        super(block);
        setHasSubtypes(true);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        final String unlocalizedName = stack.getItem().getUnlocalizedName();
        final NBTTagCompound tagCompound = stack.getTagCompound();
        if (tagCompound == null) {
            return unlocalizedName;
        }

        return unlocalizedName + '.' + tagCompound.getString("variant");
    }

    @Override
    public IBlockState getInitialStateForSubItem(ItemStack stack, IBlockState initialState)
    {
        final NBTTagCompound tagCompound = stack.getTagCompound();
        if (tagCompound == null) { return initialState; }

        final String variantName = tagCompound.getString("variant");
        WoodVariant variant = WoodVariant.fromName(variantName);
        if (variant == null) {
            variant = WoodVariant.OAK;
        }
        return initialState.withProperty(BlockProperties.WOOD_VARIANT, variant);
    }
}
