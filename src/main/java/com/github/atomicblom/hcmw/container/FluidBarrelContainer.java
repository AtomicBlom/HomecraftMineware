package com.github.atomicblom.hcmw.container;

import com.github.atomicblom.hcmw.block.tileentity.FluidBarrelTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;

/**
 * Created by codew on 30/12/2016.
 */
public class FluidBarrelContainer extends HCMWContainer {
    private FluidBarrelTileEntity fluidBarrelTileEntity;

    public FluidBarrelContainer(IInventory playerInventory, FluidBarrelTileEntity fluidBarrelTileEntity) {
        this.fluidBarrelTileEntity = fluidBarrelTileEntity;
        addPlayerInventory(playerInventory, 84);
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }
}
