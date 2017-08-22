package com.github.atomicblom.hcmw.registration;

import com.foudroyantfactotum.tool.structure.block.StructureShapeBlock;
import com.foudroyantfactotum.tool.structure.tileentity.StructureShapeTE;
import com.foudroyantfactotum.tool.structure.tileentity.StructureTE;
import com.github.atomicblom.hcmw.HomecraftMineware;
import com.github.atomicblom.hcmw.block.beds.CanopyBedBlock;
import com.github.atomicblom.hcmw.block.beds.FourPostBedBlock;
import com.github.atomicblom.hcmw.block.doors.GrandDoorBlock;
import com.github.atomicblom.hcmw.block.doors.InnerGateDoorBlock;
import com.github.atomicblom.hcmw.block.doors.TowerDoorBlock;
import com.github.atomicblom.hcmw.block.fluidstorage.FluidBarrelBlock;
import com.github.atomicblom.hcmw.block.itemstorage.BedSideDrawersBlock;
import com.github.atomicblom.hcmw.block.itemstorage.ItemBarrelBlock;
import com.github.atomicblom.hcmw.block.lighting.CandleHolderBlock;
import com.github.atomicblom.hcmw.block.lighting.LanternBlock;
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
import net.minecraftforge.registries.IForgeRegistry;

@EventBusSubscriber(modid = HomecraftMineware.MODID)
@SuppressWarnings("OverlyCoupledMethod")
public final class BlockRegistration
{
	@SubscribeEvent
	public static void registerBlocks(Register<Block> blockRegister)
	{
		final Blocks blocks = new Blocks(blockRegister.getRegistry());
		blocks.add(new FourPostBedBlock().setResistance(2.0f).setHardness(2.0f), Reference.Block.bed_4post);
		blocks.add(new CanopyBedBlock().setResistance(2.0f).setHardness(2.0f), Reference.Block.bed_canopy);

		blocks.add(new InnerGateDoorBlock().setResistance(2.0f).setHardness(2.0f), Reference.Block.door_inner_gate);
		blocks.add(new TowerDoorBlock().setResistance(2.0f).setHardness(2.0f), Reference.Block.door_tower);
		blocks.add(new GrandDoorBlock().setResistance(2.0f).setHardness(2.0f), Reference.Block.door_grand);

		blocks.add(new StructureShapeBlock(), Reference.Block.shape);

		blocks.add(new ItemBarrelBlock().setResistance(2.0f).setHardness(2.0f), Reference.Block.item_barrel);
		blocks.add(new FluidBarrelBlock().setResistance(2.0f).setHardness(2.0f), Reference.Block.fluid_barrel);
		blocks.add(new LanternBlock().setResistance(2.0f).setHardness(2.0f), Reference.Block.lantern);
		blocks.add(new CandleHolderBlock().setResistance(2.0f).setHardness(2.0f), Reference.Block.candleholder);
		blocks.add(new BedSideDrawersBlock().setResistance(2.0f).setHardness(2.0f), Reference.Block.bed_side_drawers);

		blocks.add(BedSideDrawersTileEntity.class, Reference.Block.bed_side_drawers);
		blocks.add(ItemBarrelTileEntity.class, Reference.Block.item_barrel);
		blocks.add(FluidBarrelTileEntity.class, Reference.Block.fluid_barrel);
		blocks.add(DoorTileEntity.class, Reference.Block.door);

		blocks.add(StructureTE.class, Reference.TileEntity.structure);
		blocks.add(StructureShapeTE.class, Reference.TileEntity.shape);
	}

	private static class Blocks
	{
		private final IForgeRegistry<Block> registry;

		Blocks(IForgeRegistry<Block> registry)
		{
			this.registry = registry;
		}

		<B extends Block> void add(B block, ResourceLocation registryName)
		{
			block.setRegistryName(registryName)
					.setUnlocalizedName(registryName.toString())
					.setCreativeTab(CreativeTab.INSTANCE);

			registry.register(block);
		}

		@SuppressWarnings("MethodMayBeStatic")
		public void add(Class<? extends TileEntity> tileEntityClass, ResourceLocation id)
		{
			GameRegistry.registerTileEntity(tileEntityClass, "tile." + id);
		}
	}
}