package com.github.atomicblom.hcmw.gui;

import com.github.atomicblom.hcmw.block.BedSideDrawersTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

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
        return bedSideDrawersTileEntity.isUsableByPlayer(playerIn) ;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            final int size = bedSideDrawersTileEntity.getSizeInventory();
            if (index < size)
            {
                if (!this.mergeItemStack(itemstack1, size, this.inventorySlots.size(), true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 0, size, false))
            {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty())
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

    @Override
    public void onContainerClosed(EntityPlayer entityplayer)
    {
        super.onContainerClosed(entityplayer);
        bedSideDrawersTileEntity.closeInventory(entityplayer);
    }
}
