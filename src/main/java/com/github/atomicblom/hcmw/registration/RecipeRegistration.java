package com.github.atomicblom.hcmw.registration;

import com.github.atomicblom.hcmw.HomecraftMinewares;
import com.github.atomicblom.hcmw.library.BlockLibrary;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

@Mod.EventBusSubscriber(modid = HomecraftMinewares.MODID)
public class RecipeRegistration
{
    //I'd prefer if Forge had a dedicated event for registering recipes, SoundEvent is the last
    //of the vanilla registrations, so I'm hoping all blocks are ready at this point.
    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<SoundEvent> event) {
        GameRegistry.addRecipe(new ItemStack(BlockLibrary.bed_4post),
                new String[] { "bb" }, 'b', Items.BED);

        GameRegistry.addRecipe(new ShapedOreRecipe(
                new ItemStack(BlockLibrary.bed_canopy),
                new String[] {"ww","ll","bb" }, 'b', Items.BED, 'w', "blockWool", 'l', "plankWood"
        ));
        GameRegistry.addRecipe(
                new ItemStack(BlockLibrary.bed_canopy),
                new String[] {"ww","ll","bb" }, 'b', Items.BED, 'w', Blocks.WOOL, 'l', Blocks.PLANKS
        );

        GameRegistry.addRecipe(
                new ItemStack(BlockLibrary.lantern),
                new String[] { "i", "t", "i" }, 't', Blocks.TORCH, 'i', Items.IRON_INGOT
        );

        GameRegistry.addRecipe(
                new ItemStack(BlockLibrary.candle_holder),
                new String[] { "t", "i" }, 't', Blocks.TORCH, 'i', Items.IRON_INGOT
        );

        GameRegistry.addRecipe(new ShapedOreRecipe(
                new ItemStack(BlockLibrary.item_barrel),
                new String[] {"ppp", "i i", "ppp"}, 'p', "plankWood", 'i', "ingotIron"
        ));

        GameRegistry.addRecipe(new ShapedOreRecipe(
                new ItemStack(BlockLibrary.fluid_barrel),
                new String[] {"ppp", "ibi", "ppp"}, 'p', "plankWood", 'i', "ingotIron", 'b', Items.GLASS_BOTTLE
        ));

    }
}
