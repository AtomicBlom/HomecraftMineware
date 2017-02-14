package com.github.atomicblom.hcmw.item;

import com.foudroyantfactotum.tool.structure.item.StructureBlockItem;
import com.github.atomicblom.hcmw.block.ILocalizedSubBlock;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

/**
 * Created by codew on 20/12/2016.
 */
public class StructureBlockSubItem extends StructureBlockItem
{
    public StructureBlockSubItem(Block block)
    {
        super(block);
        this.hasSubtypes = true;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        if (block instanceof ILocalizedSubBlock) {

            @SuppressWarnings("deprecation")
            final IBlockState state = block.getStateFromMeta(stack.getMetadata());

            return ((ILocalizedSubBlock) block).getUnlocalizedName(state);
        }
        throw new RuntimeException("Attempt to get the unlocalized subblock name for a block that does not implement ILocalizedSubBlock");
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }
}
