package com.github.atomicblom.hcmw.registration;

import com.github.atomicblom.hcmw.HomecraftMinewares;
import com.github.atomicblom.hcmw.library.BlockLibrary;
import com.github.atomicblom.hcmw.library.ItemLibrary;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemLilyPad;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.IForgeRegistry;

@Mod.EventBusSubscriber(modid = HomecraftMinewares.MODID)
public class ItemRegistration {

    private static CreativeTabs creativeTab = new CreativeTabs(HomecraftMinewares.MODID) {

        private ItemStack itemStack;

        @Override
        public ItemStack getTabIconItem() {
            if (itemStack == null) {
                itemStack = new ItemStack(ItemLibrary.bed);
            }
            return itemStack;
        }
    };

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();
        registry.register(getItemFromBlock(BlockLibrary.bed));
    }

    private static Item getItemFromBlock(Block block) {
        return new ItemBlock(block)
                .setRegistryName(block.getRegistryName())
                .setCreativeTab(creativeTab);
    }
}
