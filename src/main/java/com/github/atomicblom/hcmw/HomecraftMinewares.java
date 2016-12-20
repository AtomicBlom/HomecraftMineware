package com.github.atomicblom.hcmw;

import com.foudroyantfactotum.tool.structure.StructureRegistry;
import com.foudroyantfactotum.tool.structure.renderer.HighlightPreview;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

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
        MinecraftForge.EVENT_BUS.register(new HighlightPreview());
    }

    @EventHandler
    public static void serverStart(FMLServerStartingEvent event)
    {
        //TODO: Only load this if debug mode is enabled.
        event.registerServerCommand(new StructureRegistry.CommandReloadStructures());
    }
}
