package com.github.atomicblom.hcmw.gui;

import com.github.atomicblom.hcmw.block.BedSideDrawersTileEntity;
import com.github.atomicblom.hcmw.client.gui.BedSideDrawersGui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

/**
 * Created by codew on 28/12/2016.
 */
public enum GuiHandler implements IGuiHandler {
    INSTANCE;

    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        final GuiType gui = GuiType.fromId(id);
        BlockPos pos = new BlockPos(x, y, z);
        switch(gui)
        {
            case BEDSIDE_DRAWERS:
                final BedSideDrawersTileEntity te = (BedSideDrawersTileEntity)world.getTileEntity(pos);
                return new BedsideDrawersContainer(player.inventory, te);
        }

        return null;
    }

    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        final GuiType gui = GuiType.fromId(id);
        BlockPos pos = new BlockPos(x, y, z);
        switch(gui)
        {
            case BEDSIDE_DRAWERS:
                final BedSideDrawersTileEntity te = (BedSideDrawersTileEntity)world.getTileEntity(pos);
                return new BedSideDrawersGui(player.inventory, te);
        }

        return null;
    }
}
