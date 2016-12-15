package com.github.atomicblom.hcmw.registration;

import com.github.atomicblom.hcmw.HomecraftMinewares;
import com.github.atomicblom.hcmw.block.Bed;
import com.github.atomicblom.hcmw.library.Reference;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.IForgeRegistry;

@Mod.EventBusSubscriber(modid = HomecraftMinewares.MODID)
public class BlockRegistration {

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> blockRegister) {
        IForgeRegistry<Block> registry = blockRegister.getRegistry();
        registry.register(configure(new Bed(), Reference.Block.bed));
    }

    private static Block configure(Block block, ResourceLocation registryName) {
        return block
                .setRegistryName(registryName)
                .setUnlocalizedName(registryName.toString());
    }
}
