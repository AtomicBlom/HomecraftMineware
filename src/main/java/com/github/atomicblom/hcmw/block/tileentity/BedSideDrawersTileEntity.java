package com.github.atomicblom.hcmw.block.tileentity;

import com.github.atomicblom.hcmw.container.BedsideDrawersContainer;
import com.github.atomicblom.hcmw.library.BlockLibrary;
import com.github.atomicblom.hcmw.library.Reference.Gui;
import com.github.atomicblom.hcmw.library.SoundLibrary;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.IInteractionObject;

public class BedSideDrawersTileEntity extends BaseSimpleInventoryTileEntity implements IInteractionObject
{
    public BedSideDrawersTileEntity() {
        super(4* 9);
    }

    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return new BedsideDrawersContainer(playerInventory, this);
    }

    @Override
    public String getGuiID() {
        return Gui.bed_side_drawers_gui.toString();
    }

    @Override
    public String getName() {
        return "gui." + BlockLibrary.bed_side_drawers.getRegistryName();
    }

    @Override
    public boolean canRenderBreaking()
    {
        return true;
    }

    @Override
    protected SoundEvent getOpenSound() { return SoundLibrary.bed_side_drawers_open; }
    @Override
    protected SoundEvent getCloseSound() { return SoundLibrary.bed_side_drawers_close; }
}
