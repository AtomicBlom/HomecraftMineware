package com.github.atomicblom.hcmw;

import com.foudroyantfactotum.tool.structure.StructureRegistry;
import com.foudroyantfactotum.tool.structure.renderer.HighlightBoundingBoxDebug;
import com.foudroyantfactotum.tool.structure.renderer.HighlightPreview;
import com.github.atomicblom.hcmw.gui.GuiHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid = HomecraftMineware.MODID, version = HomecraftMineware.VERSION)
public class HomecraftMineware
{
    public static final String MODID = "hcmw";
    public static final String VERSION = "1.0";
    public static final boolean DEBUG = false;

    @Mod.Instance
    public static HomecraftMineware INSTANCE = null;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        StructureRegistry.setMOD_ID(MODID);
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        StructureRegistry.loadRegisteredPatterns();
        NetworkRegistry.INSTANCE.registerGuiHandler(INSTANCE, GuiHandler.INSTANCE);
    }

    @EventHandler
    @SideOnly(Side.CLIENT)
    public void initClient(FMLInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(new HighlightPreview());
        if (DEBUG)
        {
            MinecraftForge.EVENT_BUS.register(new HighlightBoundingBoxDebug());
        }

    }

    @EventHandler
    public static void serverStart(FMLServerStartingEvent event)
    {
        if (DEBUG)
        {
            event.registerServerCommand(new StructureRegistry.CommandReloadStructures());
        }
    }
}
