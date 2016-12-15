package com.github.atomicblom.hcmw;

import net.minecraft.init.Blocks;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = HomecraftMinewares.MODID, version = HomecraftMinewares.VERSION)
public class HomecraftMinewares
{
    public static final String MODID = "hcmw";
    public static final String VERSION = "1.0";
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        // some example code
        System.out.println("DIRT BLOCK >> "+Blocks.DIRT.getUnlocalizedName());
    }
}
