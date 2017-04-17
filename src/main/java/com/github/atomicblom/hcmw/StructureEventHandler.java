package com.github.atomicblom.hcmw;

import com.foudroyantfactotum.tool.structure.Structure;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class StructureEventHandler extends Structure
{
    @SubscribeEvent
    public static void onCreateRegistry(RegistryEvent.NewRegistry event) {
        Structure.Configure(HomecraftMineware.MODID);
    }
}
