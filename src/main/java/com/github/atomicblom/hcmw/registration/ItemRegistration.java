package com.github.atomicblom.hcmw.registration;

import com.foudroyantfactotum.tool.structure.block.StructureBlock;
import com.foudroyantfactotum.tool.structure.item.StructureBlockItem;
import com.github.atomicblom.hcmw.HomecraftMinewares;
import com.github.atomicblom.hcmw.client.CreativeTab;
import com.github.atomicblom.hcmw.library.BlockLibrary;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.IForgeRegistry;

@EventBusSubscriber(modid = HomecraftMinewares.MODID)
public final class ItemRegistration {

    @SubscribeEvent
    public static void registerItems(Register<Item> event) {
        final Items items = new Items(event.getRegistry());
        items.addStructure(BlockLibrary.bed_4post);
        items.addStructure(BlockLibrary.bed_canopy);
    }

    private static class Items {
        private final IForgeRegistry<Item> registry;

        Items(IForgeRegistry<Item> registry)
        {
            this.registry = registry;
        }

        StructureBlockItem addStructure(StructureBlock block)
        {
            final StructureBlockItem item = new StructureBlockItem(block);
            item
                    .setRegistryName(block.getRegistryName())
                    .setUnlocalizedName("item." + block.getRegistryName())
                    .setCreativeTab(CreativeTab.INSTANCE);

            registry.register(item);

            return item;
        }
    }
}
