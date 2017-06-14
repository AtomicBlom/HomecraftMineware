package com.github.atomicblom.hcmw.registration;

import com.foudroyantfactotum.tool.structure.utility.IStructureDefinitionProvider;
import com.github.atomicblom.hcmw.HomecraftMineware;
import com.github.atomicblom.hcmw.library.Reference.Block;
import com.github.atomicblom.hcmw.structure.*;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.IForgeRegistry;

@Mod.EventBusSubscriber(modid = HomecraftMineware.MODID)
public final class StructureRegistration
{
    @SubscribeEvent
    public static void registerStructures(Register<IStructureDefinitionProvider> event) {
        final IForgeRegistry<IStructureDefinitionProvider> registry = event.getRegistry();
        registry.register(new CanopyBedStructure().setRegistryName(Block.bed_canopy));
        registry.register(new FourPostBedStructure().setRegistryName(Block.bed_4post));
        registry.register(new GrandDoorStructure().setRegistryName(Block.door_grand));
        registry.register(new InnerGateDoorStructure().setRegistryName(Block.door_inner_gate));
        registry.register(new TowerDoorStructure().setRegistryName(Block.door_tower));
        registry.register(new SuperDoubleDoorStructure().setRegistryName(Block.door_super_double));
    }
}
