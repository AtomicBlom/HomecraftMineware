package com.github.atomicblom.hcmw;

import com.foudroyantfactotum.tool.structure.StructureRegistry;
import net.minecraft.init.Blocks;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = HomecraftMinewares.MODID, version = HomecraftMinewares.VERSION)
public class HomecraftMinewares
{
    public static final String MODID = "hcmw";
    public static final String VERSION = "1.0";

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        StructureRegistry.setMOD_ID(MODID);

    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        StructureRegistry.loadRegisteredPatterns();
        // some example code
        System.out.println("DIRT BLOCK >> "+Blocks.DIRT.getUnlocalizedName());
    }
}
