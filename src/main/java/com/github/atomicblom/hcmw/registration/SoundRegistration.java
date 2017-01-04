package com.github.atomicblom.hcmw.registration;

import com.github.atomicblom.hcmw.HomecraftMineware;
import com.github.atomicblom.hcmw.library.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.IForgeRegistry;

@Mod.EventBusSubscriber()
public class SoundRegistration {

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<SoundEvent> soundRegister) {
        Sounds sounds = new Sounds(soundRegister.getRegistry());
        sounds.add(Reference.Sound.bed_side_drawers_open);
        sounds.add(Reference.Sound.bed_side_drawers_close);
    }

    private static class Sounds
    {
        private final IForgeRegistry<SoundEvent> registry;

        Sounds(IForgeRegistry<SoundEvent> registry)
        {
            this.registry = registry;
        }

        SoundEvent add(ResourceLocation registryName)
        {
            final SoundEvent sound = new SoundEvent(registryName);
            sound.setRegistryName(registryName);
            registry.register(sound);
            return sound;
        }
    }
}
