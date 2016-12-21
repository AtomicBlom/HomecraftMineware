package com.github.atomicblom.hcmw.client;

import com.github.atomicblom.hcmw.HomecraftMinewares;
import com.github.atomicblom.hcmw.library.ItemLibrary;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class CreativeTab extends CreativeTabs
{
    private CreativeTab()
    {
        super(HomecraftMinewares.MODID);
    }

    public static CreativeTab INSTANCE = new CreativeTab();

    private ItemStack itemStack = null;

    @Override
    public ItemStack getTabIconItem() {
        if (itemStack == null) {
            itemStack = new ItemStack(ItemLibrary.bed_canopy);
        }
        return itemStack;
    }
}
