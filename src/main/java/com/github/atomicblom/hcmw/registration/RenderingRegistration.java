package com.github.atomicblom.hcmw.registration;

import com.github.atomicblom.hcmw.HomecraftMineware;
import com.github.atomicblom.hcmw.block.WoodVariant;
import com.github.atomicblom.hcmw.client.CreativeTab;
import com.github.atomicblom.hcmw.client.model.HCMWMultiModel;
import com.github.atomicblom.hcmw.client.model.LanternMultiModel;
import com.github.atomicblom.hcmw.client.model.obj.OBJLoader;
import com.github.atomicblom.hcmw.library.ItemLibrary;
import com.github.atomicblom.hcmw.library.Reference;
import com.google.common.base.Preconditions;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import javax.annotation.Nonnull;

@EventBusSubscriber(Side.CLIENT)
@SideOnly(Side.CLIENT)
public final class RenderingRegistration
{
    private static HCMWMultiModel[] multiModels = new HCMWMultiModel[]{
            new LanternMultiModel()
    };

    @SubscribeEvent
    public static void onModelRegistryReady(ModelRegistryEvent event)
    {
        OBJLoader.INSTANCE.addDomain(HomecraftMineware.MODID);
        ModelLoaderRegistry.registerLoader(OBJLoader.INSTANCE);

        setItemModel(ItemLibrary.bed_4post);
        setItemModel(ItemLibrary.bed_canopy);
        setItemModel(ItemLibrary.door_inner_gate);
        setItemModel(ItemLibrary.door_tower);
        setItemModel(ItemLibrary.door_grand);
        setItemModel(ItemLibrary.item_barrel);
        setItemModel(ItemLibrary.fluid_barrel);
        setItemModel(ItemLibrary.bed_side_drawers);
        setItemModel(ItemLibrary.candle_holder);
        setItemModel(ItemLibrary.lantern);
    }

    @SubscribeEvent
    public static void onModelBake(ModelBakeEvent event)
    {
        for (HCMWMultiModel multiModel : multiModels)
        {
            multiModel.loadModel(event);
        }
    }

    private static void setItemModel(Item item, int meta, String variant)
    {
        ModelLoader.setCustomModelResourceLocation(
                item,
                meta,
                new ModelResourceLocation(item.getRegistryName(), variant)
        );
    }

    private static void setItemModel(Item item)
    {
        final ResourceLocation registryName = item.getRegistryName();
        Preconditions.checkNotNull(registryName);

        if (!item.getHasSubtypes())
        {

            ModelLoader.setCustomModelResourceLocation(
                    item,
                    0,
                    new ModelResourceLocation(registryName, "inventory")
            );
        } else
        {
            final NonNullList<ItemStack> subTypes = NonNullList.create();

            item.getSubItems(item, CreativeTab.INSTANCE, subTypes);
//          for (final ItemStack subType : subTypes)
//          {
//                final StringBuilder builder = new StringBuilder(128);
//                builder.append("inventory");
//                final NBTTagCompound nbt = subType.getTagCompound();
//                if (nbt != null) {
//                    for (final String key : nbt.getKeySet())
//                    {
//                        final NBTBase tag = nbt.getTag(key);
//                        if (tag instanceof NBTTagString) {
//                            builder.append(',');
//                            builder.append(key);
//                            builder.append('=');
//                            builder.append(((NBTTagString) tag).getString());
//                        }
//                    }
//                }

            ModelLoader.setCustomMeshDefinition(item, stack ->
            {
                final ResourceLocation itemRegistryName = stack.getItem().getRegistryName();
                Preconditions.checkNotNull(itemRegistryName);
                return new ModelResourceLocation(itemRegistryName, getInventoryString(stack));
            });

            for (final ItemStack subType : subTypes) {
                ModelBakery.registerItemVariants(item, new ModelResourceLocation(registryName, getInventoryString(subType)));
            }

//                ModelLoader.setCustomModelResourceLocation(
//                        item,
//                        0,
//                        new ModelResourceLocation(registryName, builder.toString())
//                );
        }
    }

    private static String getInventoryString(ItemStack stack)
    {
        final StringBuilder builder = new StringBuilder(128);
        builder.append("inventory");
        final NBTTagCompound nbt = stack.getTagCompound();
        if (nbt != null)
        {
            for (final String key : nbt.getKeySet())
            {
                final NBTBase tag = nbt.getTag(key);
                if (tag instanceof NBTTagString)
                {
                    builder.append(',');
                    builder.append(key);
                    builder.append('=');
                    builder.append(((NBTTagString) tag).getString());
                }
            }
        }
        return builder.toString();
    }
}
