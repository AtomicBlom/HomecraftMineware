package com.github.atomicblom.hcmw.library;

import com.foudroyantfactotum.tool.structure.registry.StructureDefinition;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.IForgeRegistry;

@Mod.EventBusSubscriber
public class StructureDefinitionLibrary
{
    @SubscribeEvent
    public static void registerStructureDefinitions(RegistryEvent.Register<StructureDefinition> event) {
        final IForgeRegistry<StructureDefinition> registry = event.getRegistry();

        //registry.register();
    }
}
