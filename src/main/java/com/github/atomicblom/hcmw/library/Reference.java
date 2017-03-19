package com.github.atomicblom.hcmw.library;

import com.github.atomicblom.hcmw.HomecraftMineware;
import net.minecraft.util.ResourceLocation;

/**
 * Created by codew on 15/12/2016.
 */
public class Reference {
    public static class Block {
        public static final ResourceLocation shape = resource("shape");
        public static final ResourceLocation bed_4post = resource("bed_4post");
        public static final ResourceLocation bed_canopy = resource("bed_canopy");
        public static final ResourceLocation door_inner_gate = resource("door_inner_gate");
        public static final ResourceLocation door_grand = resource("door_grand");
        public static final ResourceLocation door_tower = resource("door_tower");
        public static final ResourceLocation item_barrel = resource("item_barrel");
        public static final ResourceLocation fluid_barrel = resource("fluid_barrel");
        public static final ResourceLocation bed_side_drawers = resource("bed_side_drawers");
        public static final ResourceLocation lantern = resource("lantern");
        public static final ResourceLocation candleholder = resource("candle_holder");
        public static final ResourceLocation door = resource("door");


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
        public static final ResourceLocation bed_side_drawers_texture = resource("textures/gui/hcmwbedsidedrawergui.png");

        public static final ResourceLocation item_barrel_gui = resource("gui." + Block.item_barrel.getResourcePath());
        public static final ResourceLocation item_barrel_texture = resource("textures/gui/hcmwbarrelitemgui.png");

        public static final ResourceLocation fluid_barrel_gui = resource("gui." + Block.fluid_barrel.getResourcePath());
        public static final ResourceLocation fluid_barrel_texture = resource("textures/gui/hcmwbarrelfluidgui.png");
    }

    private static ResourceLocation resource(String name) {
        return new ResourceLocation(HomecraftMineware.MODID, name);
    }

    private Reference() {}
}
