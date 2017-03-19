package com.github.atomicblom.hcmw.registration;

import com.foudroyantfactotum.tool.structure.StructureRegistry;
import com.foudroyantfactotum.tool.structure.block.StructureBlock;
import com.foudroyantfactotum.tool.structure.block.StructureShapeBlock;
import com.foudroyantfactotum.tool.structure.tileentity.StructureShapeTE;
import com.foudroyantfactotum.tool.structure.tileentity.StructureTE;
import com.github.atomicblom.hcmw.HomecraftMineware;
import com.github.atomicblom.hcmw.block.*;
import com.github.atomicblom.hcmw.block.doors.GrandDoorBlock;
import com.github.atomicblom.hcmw.block.doors.InnerGateDoorBlock;
import com.github.atomicblom.hcmw.block.doors.TowerDoorBlock;
import com.github.atomicblom.hcmw.block.tileentity.BedSideDrawersTileEntity;
import com.github.atomicblom.hcmw.block.tileentity.DoorTileEntity;
import com.github.atomicblom.hcmw.block.tileentity.FluidBarrelTileEntity;
import com.github.atomicblom.hcmw.block.tileentity.ItemBarrelTileEntity;
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

@EventBusSubscriber(modid = HomecraftMineware.MODID)
public class BlockRegistration {

    @SubscribeEvent
    public static void registerBlocks(Register<Block> blockRegister) {
        final Blocks blocks = new Blocks(blockRegister.getRegistry());
        blocks.addStructure((StructureBlock)new FourPostBedBlock().setResistance(2f).setHardness(2f), Reference.Block.bed_4post);
        blocks.addStructure((StructureBlock)new CanopyBedBlock().setResistance(2f).setHardness(2f), Reference.Block.bed_canopy);
        blocks.addStructure((StructureBlock)new InnerGateDoorBlock().setResistance(2f).setHardness(2f), Reference.Block.door_inner_gate);
        blocks.addStructure((StructureBlock)new TowerDoorBlock().setResistance(2f).setHardness(2f), Reference.Block.door_tower);
        blocks.addStructure((StructureBlock)new GrandDoorBlock().setResistance(2f).setHardness(2f), Reference.Block.door_grand);

        blocks.add(new ItemBarrelBlock().setResistance(2f).setHardness(2f), Reference.Block.item_barrel);
        blocks.add(new FluidBarrelBlock().setResistance(2f).setHardness(2f), Reference.Block.fluid_barrel);
        blocks.add(new LanternBlock().setResistance(2f).setHardness(2f), Reference.Block.lantern);
        blocks.add(new CandleHolderBlock().setResistance(2f).setHardness(2f), Reference.Block.candleholder);
        blocks.add(new BedSideDrawersBlock().setResistance(2f).setHardness(2f), Reference.Block.bed_side_drawers);

        blocks.add(BedSideDrawersTileEntity.class, Reference.Block.bed_side_drawers.toString());
        blocks.add(ItemBarrelTileEntity.class, Reference.Block.item_barrel.toString());
        blocks.add(FluidBarrelTileEntity.class, Reference.Block.fluid_barrel.toString());
        blocks.add(DoorTileEntity.class, Reference.Block.door.toString());
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

            add(StructureTE.class, "hcmw:structure");
            add(StructureShapeTE.class, "hcmw:shape");
        }

        public void add(Class<? extends TileEntity> tileEntityClass, String id)
        {
            GameRegistry.registerTileEntity(tileEntityClass, "tile." + id);
        }
    }
}
