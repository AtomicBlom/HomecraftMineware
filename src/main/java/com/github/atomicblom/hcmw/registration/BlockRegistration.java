package com.github.atomicblom.hcmw.registration;

import com.foudroyantfactotum.tool.structure.StructureRegistry;
import com.foudroyantfactotum.tool.structure.block.StructureBlock;
import com.foudroyantfactotum.tool.structure.block.StructureShapeBlock;
import com.foudroyantfactotum.tool.structure.tileentity.StructureShapeTE;
import com.foudroyantfactotum.tool.structure.tileentity.StructureTE;
import com.github.atomicblom.hcmw.HomecraftMinewares;
import com.github.atomicblom.hcmw.block.*;
import com.github.atomicblom.hcmw.client.CreativeTab;
import com.github.atomicblom.hcmw.library.Reference;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistry;

@EventBusSubscriber(modid = HomecraftMinewares.MODID)
public class BlockRegistration {

    @SubscribeEvent
    public static void registerBlocks(Register<Block> blockRegister) {
        final Blocks blocks = new Blocks(blockRegister.getRegistry());
        blocks.addStructure(new FourPostBedBlock(), Reference.Block.bed_4post);
        blocks.addStructure(new CanopyBedBlock(), Reference.Block.bed_canopy);
        blocks.add(new BarrelBlock(), Reference.Block.barrel);
        blocks.add(new LanternBlock(), Reference.Block.lantern);
        blocks.add(new CandleHolderBlock(), Reference.Block.candleholder);
        blocks.add(new BedSideDrawersBlock(), Reference.Block.bed_side_drawers);
    }

    private static class Blocks
    {
        private final IForgeRegistry<Block> registry;
        private StructureShapeBlock shapeBlock = null;

        Blocks(IForgeRegistry<Block> registry)
        {
            this.registry = registry;
        }

        <B extends Block> B add(B block, ResourceLocation registryName) {
            block.setRegistryName(registryName)
                    .setUnlocalizedName(registryName.toString())
                    .setCreativeTab(CreativeTab.INSTANCE);

            registry.register(block);

            return block;
        }

        <B extends StructureBlock> void addStructure(B block, ResourceLocation registryName)
        {
            if (shapeBlock == null) {
                registerShape();
            }

            add(block, registryName);
            StructureRegistry.registerStructureForLoad(block, shapeBlock);
        }

        private void registerShape()
        {
            shapeBlock = new StructureShapeBlock();
            add(shapeBlock, Reference.Block.shape);

            GameRegistry.registerTileEntity(StructureTE.class, "tile.hcmw:structure");
            GameRegistry.registerTileEntity(StructureShapeTE.class, "tile.hcmw:shape");
        }

        public void add(Class<? extends TileEntity> tileEntityClass, String id)
        {
            GameRegistry.registerTileEntity(tileEntityClass, id);
        }
    }
}
