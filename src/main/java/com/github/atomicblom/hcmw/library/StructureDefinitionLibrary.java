package com.github.atomicblom.hcmw.library;

import com.foudroyantfactotum.tool.structure.registry.StructureDefinition;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.IForgeRegistry;

@EventBusSubscriber
public class StructureDefinitionLibrary
{
    @SubscribeEvent
    public static void registerStructureDefinitions(Register<StructureDefinition> event) {
        final IForgeRegistry<StructureDefinition> registry = event.getRegistry();

        //registry.register();
    }
}
