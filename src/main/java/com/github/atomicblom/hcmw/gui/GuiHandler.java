package com.github.atomicblom.hcmw.gui;

import com.github.atomicblom.hcmw.block.tileentity.BedSideDrawersTileEntity;
import com.github.atomicblom.hcmw.block.tileentity.FluidBarrelTileEntity;
import com.github.atomicblom.hcmw.block.tileentity.ItemBarrelTileEntity;
import com.github.atomicblom.hcmw.client.gui.BedSideDrawersGui;
import com.github.atomicblom.hcmw.client.gui.FluidBarrelGui;
import com.github.atomicblom.hcmw.client.gui.ItemBarrelGui;
import com.github.atomicblom.hcmw.container.BedsideDrawersContainer;
import com.github.atomicblom.hcmw.container.FluidBarrelContainer;
import com.github.atomicblom.hcmw.container.ItemBarrelContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public enum GuiHandler implements IGuiHandler {
    INSTANCE;

    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        final GuiType gui = GuiType.fromId(id);
        final BlockPos pos = new BlockPos(x, y, z);
        switch(gui)
        {
            case BEDSIDE_DRAWERS:
                final BedSideDrawersTileEntity bedsideDrawersTE = (BedSideDrawersTileEntity)world.getTileEntity(pos);
                return new BedsideDrawersContainer(player.inventory, bedsideDrawersTE);
            case ITEM_BARREL:
                final ItemBarrelTileEntity itemBarrelTE = (ItemBarrelTileEntity)world.getTileEntity(pos);
                return new ItemBarrelContainer(player.inventory, itemBarrelTE);
            case FLUID_BARREL:
                final FluidBarrelTileEntity fluidBarrelTE = (FluidBarrelTileEntity)world.getTileEntity(pos);
                return new FluidBarrelContainer(player.inventory, fluidBarrelTE);
        }

        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        final GuiType gui = GuiType.fromId(id);
        final BlockPos pos = new BlockPos(x, y, z);
        switch(gui)
        {
            case BEDSIDE_DRAWERS:
                final BedSideDrawersTileEntity bedsideDrawersTE = (BedSideDrawersTileEntity)world.getTileEntity(pos);
                return new BedSideDrawersGui(player.inventory, bedsideDrawersTE);
            case ITEM_BARREL:
                final ItemBarrelTileEntity itemBarrelTE = (ItemBarrelTileEntity)world.getTileEntity(pos);
                return new ItemBarrelGui(player.inventory, itemBarrelTE);
            case FLUID_BARREL:
                final FluidBarrelTileEntity fluidBarrelTE = (FluidBarrelTileEntity)world.getTileEntity(pos);
                return new FluidBarrelGui(player.inventory, fluidBarrelTE);
        }

        return null;
    }
}
