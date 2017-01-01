package com.github.atomicblom.hcmw.registration;

import com.github.atomicblom.hcmw.HomecraftMinewares;
import com.github.atomicblom.hcmw.client.model.HCMWMultiModel;
import com.github.atomicblom.hcmw.client.model.LanternMultiModel;
import com.github.atomicblom.hcmw.client.model.obj.OBJLoader;
import com.github.atomicblom.hcmw.library.ItemLibrary;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@EventBusSubscriber(Side.CLIENT)
@SideOnly(Side.CLIENT)
public final class RenderingRegistration {
    private static HCMWMultiModel[] multiModels = new HCMWMultiModel[]{
            new LanternMultiModel()
    };

    @SubscribeEvent
    public static void onModelRegistryReady(ModelRegistryEvent event) {
        OBJLoader.INSTANCE.addDomain(HomecraftMinewares.MODID);
        ModelLoaderRegistry.registerLoader(OBJLoader.INSTANCE);

        setItemModel(ItemLibrary.bed_4post);
        setItemModel(ItemLibrary.bed_canopy);
        setItemModel(ItemLibrary.item_barrel);
        setItemModel(ItemLibrary.fluid_barrel);
        setItemModel(ItemLibrary.bed_side_drawers);
        setItemModel(ItemLibrary.candle_holder);
        setItemModel(ItemLibrary.lantern);
    }

    @SubscribeEvent
    public static void onModelBake(ModelBakeEvent event)
    {
        for (HCMWMultiModel multiModel : multiModels)
        {
            multiModel.loadModel(event);
        }
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
