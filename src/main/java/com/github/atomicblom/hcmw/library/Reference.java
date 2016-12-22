package com.github.atomicblom.hcmw.library;

import com.github.atomicblom.hcmw.HomecraftMinewares;
import net.minecraft.util.ResourceLocation;

/**
 * Created by codew on 15/12/2016.
 */
public class Reference {
    public static class Block {
        public static final ResourceLocation shape = resource("shape");
        public static final ResourceLocation bed_4post = resource("bed_4post");
        public static final ResourceLocation bed_canopy = resource("bed_canopy");
        public static final ResourceLocation barrel = resource("barrel");


        private Block() {}
    }

    private static ResourceLocation resource(String name) {
        return new ResourceLocation(HomecraftMinewares.MODID, name);
    }

    private Reference() {}
}
