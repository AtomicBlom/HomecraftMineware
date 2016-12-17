package com.github.atomicblom.hcmw.library;

import com.github.atomicblom.hcmw.HomecraftMinewares;
import net.minecraft.util.ResourceLocation;

/**
 * Created by codew on 15/12/2016.
 */
public class Reference {
    public static class Block {
        public static final ResourceLocation bed = new ResourceLocation(HomecraftMinewares.MODID, "bed");
        public static ResourceLocation shape = new ResourceLocation(HomecraftMinewares.MODID, "shape");

        private Block() {}
    }

    private Reference() {}
}
