package com.github.atomicblom.hcmw.registration;

import com.github.atomicblom.hcmw.HomecraftMineware;
import com.github.atomicblom.hcmw.library.Reference.Sound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

@EventBusSubscriber(modid = HomecraftMineware.MODID)
public final class SoundRegistration {

    @SubscribeEvent
    public static void registerBlocks(Register<SoundEvent> soundRegister) {
        final Sounds sounds = new Sounds(soundRegister.getRegistry());
        sounds.add(Sound.bed_side_drawers_open);
        sounds.add(Sound.bed_side_drawers_close);
    }

    private static class Sounds
    {
        private final IForgeRegistry<SoundEvent> registry;

        Sounds(IForgeRegistry<SoundEvent> registry)
        {
            this.registry = registry;
        }

        void add(ResourceLocation registryName)
        {
            final SoundEvent sound = new SoundEvent(registryName);
            sound.setRegistryName(registryName);
            registry.register(sound);
        }
    }
}
