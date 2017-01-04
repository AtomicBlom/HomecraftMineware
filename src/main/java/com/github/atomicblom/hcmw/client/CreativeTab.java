package com.github.atomicblom.hcmw.client;

import com.github.atomicblom.hcmw.HomecraftMineware;
import com.github.atomicblom.hcmw.library.ItemLibrary;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CreativeTab extends CreativeTabs
{
    private CreativeTab()
    {
        super(HomecraftMineware.MODID);
    }

    public static CreativeTab INSTANCE = new CreativeTab();

    @Override
    public Item getTabIconItem() {
        return ItemLibrary.bed_canopy;
    }
}
