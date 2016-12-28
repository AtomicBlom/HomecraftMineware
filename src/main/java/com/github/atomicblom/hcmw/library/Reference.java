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
        public static final ResourceLocation bed_side_drawers = resource("bed_side_drawers");
        public static final ResourceLocation lantern = resource("lantern");
        public static final ResourceLocation candleholder = resource("candle_holder");


        private Block() {}
    }

    public static class Model {
        public static final ResourceLocation lantern_wall_hook = resource("block/hcmwlanternwallhook.obj");
        public static final ResourceLocation lantern_roof_hook = resource("block/hcmwlanternroofhook.obj");

        private Model() {}
    }

    public static class Sound {
        public static final ResourceLocation bed_side_drawers_open = resource("bed_side_drawers_open");
        public static final ResourceLocation bed_side_drawers_close = resource("bed_side_drawers_close");

        private Sound() {}
    }

    public static class Gui {
        public static final ResourceLocation bed_side_drawers_gui = resource("gui." + Block.bed_side_drawers.getResourcePath());
        public static final ResourceLocation bed_side_drawers_upper_label = resource(Block.bed_side_drawers.getResourcePath() + "_upper");
        public static final ResourceLocation bed_side_drawers_lower_label = resource(Block.bed_side_drawers.getResourcePath() + "_lower");

        //public static final ResourceLocation bed_side_drawers_texture = resource("" + Block.bed_side_drawers.getResourcePath());
    }

    private static ResourceLocation resource(String name) {
        return new ResourceLocation(HomecraftMinewares.MODID, name);
    }

    private Reference() {}
}
