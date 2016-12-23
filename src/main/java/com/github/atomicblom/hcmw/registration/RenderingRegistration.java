package com.github.atomicblom.hcmw.registration;

import com.github.atomicblom.hcmw.HomecraftMinewares;
import com.github.atomicblom.hcmw.library.BlockLibrary;
import com.github.atomicblom.hcmw.library.ItemLibrary;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber(modid = HomecraftMinewares.MODID)
public final class RenderingRegistration {

    @SubscribeEvent
    public static void onModelRegistryReady(ModelRegistryEvent event) {
        OBJLoader.INSTANCE.addDomain(HomecraftMinewares.MODID);

        setItemModel(ItemLibrary.bed_4post);
        setItemModel(ItemLibrary.bed_canopy);
        setItemModel(ItemLibrary.barrel);
        setItemModel(ItemLibrary.bed_side_drawers);
        setItemModel(ItemLibrary.candle_holder);
        setItemModel(ItemLibrary.lantern);
    }

    private static void setItemModel(Item item, int meta, String variant)
    {
        ModelLoader.setCustomModelResourceLocation(
                item,
                meta,
                new ModelResourceLocation(item.getRegistryName(), variant)
        );
    }

    private static void setItemModel(Item item)
    {
        ModelLoader.setCustomModelResourceLocation(
                item,
                0,
                new ModelResourceLocation(item.getRegistryName(), "inventory")
        );
    }
}
