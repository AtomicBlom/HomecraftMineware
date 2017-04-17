package com.github.atomicblom.hcmw.library;

import com.github.atomicblom.hcmw.HomecraftMineware;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@SuppressWarnings("AssignmentToNull")
@ObjectHolder(HomecraftMineware.MODID)
public final class SoundLibrary {
    public static final SoundEvent bed_side_drawers_open;
    public static final SoundEvent bed_side_drawers_close;

    static {
        bed_side_drawers_open = null;
        bed_side_drawers_close = null;
    }
}
