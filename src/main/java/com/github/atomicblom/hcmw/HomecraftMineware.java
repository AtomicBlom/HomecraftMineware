package com.github.atomicblom.hcmw;

import com.foudroyantfactotum.tool.structure.StructureRegistry;
import com.foudroyantfactotum.tool.structure.net.StructureNetwork;
import com.foudroyantfactotum.tool.structure.CommandReloadStructures;
import com.foudroyantfactotum.tool.structure.renderer.HighlightBoundingBoxDebug;
import com.foudroyantfactotum.tool.structure.renderer.HighlightPreview;
import com.github.atomicblom.hcmw.gui.GuiHandler;
import com.github.atomicblom.hcmw.util.Logger;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings({"AssignmentToStaticFieldFromInstanceMethod", "MethodMayBeStatic"})
@Mod(modid = HomecraftMineware.MODID, version = HomecraftMineware.MOD_VERSION)
public class HomecraftMineware
{
    public static final String MODID = "hcmw";
    public static final String MOD_VERSION = "@MOD_VERSION@";
    public static boolean DEBUG = true;
    private static final String IS_CI_BUILD = "@CI_BUILD@";

    public static final SimpleNetworkWrapper CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(HomecraftMineware.MODID);
    @Instance
    public static HomecraftMineware INSTANCE;

    @SuppressWarnings("ConstantConditions")
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {

        if ("true".equals(IS_CI_BUILD)) {
            DEBUG = false;
        }
        if (DEBUG) {
            Logger.info("Homecraft Mineware Debugging is enabled.");
        }
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        if (Boolean.parseBoolean(IS_CI_BUILD)) {
            DEBUG = false;
        } else {
            Logger.info("You are not running a release build of Homecraft Mineware. This message is purely for informational purposes.");
        }

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
            Logger.info("Homecraft Mineware Structure Highlighting enabled.");
        }

    }

    @EventHandler
    public static void serverStart(FMLServerStartingEvent event)
    {
        if (DEBUG)
        {

            event.registerServerCommand(new CommandReloadStructures());
            Logger.info("Reload Structures command enabled.");
        }
    }

}
