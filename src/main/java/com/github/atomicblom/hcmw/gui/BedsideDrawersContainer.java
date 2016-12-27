package com.github.atomicblom.hcmw.gui;

import com.github.atomicblom.hcmw.block.BedSideDrawersTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;

/**
 * Created by codew on 26/12/2016.
 */
public class BedsideDrawersContainer extends Container {
    private final InventoryPlayer playerInventory;
    private final IInventory bedSideDrawersTileEntity;

    public BedsideDrawersContainer(InventoryPlayer playerInventory, IInventory bedSideDrawersTileEntity) {
        this.playerInventory = playerInventory;
        this.bedSideDrawersTileEntity = bedSideDrawersTileEntity;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return bedSideDrawersTileEntity.canInteractWith(playerIn) ;
    }
}
