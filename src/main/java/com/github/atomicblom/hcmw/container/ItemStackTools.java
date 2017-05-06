package com.github.atomicblom.hcmw.container;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemStackTools
{

    public static boolean isEmpty(ItemStack itemStack)
    {
        return itemStack == null || itemStack.getItem() == Item.getItemFromBlock(Blocks.AIR) || itemStack.stackSize == 0;
    }
}
