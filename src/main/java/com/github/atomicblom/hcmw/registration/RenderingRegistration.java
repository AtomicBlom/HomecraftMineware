package com.github.atomicblom.hcmw.registration;

import com.github.atomicblom.hcmw.HomecraftMinewares;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = HomecraftMinewares.MODID)
public class RenderingRegistration {

    @SubscribeEvent
    public static void onModelRegistryReady(ModelRegistryEvent event) {
        OBJLoader.INSTANCE.addDomain(HomecraftMinewares.MODID);
    }
}
