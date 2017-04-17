package com.github.atomicblom.hcmw;

import com.foudroyantfactotum.tool.structure.Structure;
import net.minecraftforge.event.RegistryEvent.NewRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public class StructureEventHandler extends Structure
{
    @SubscribeEvent
    public static void onCreateRegistry(NewRegistry event) {
        Structure.Configure(HomecraftMineware.MODID);
    }
}
