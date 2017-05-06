package com.github.atomicblom.hcmw.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public abstract class HCMWItemContainer extends HCMWContainer {
    protected final IInventory inventory;

    HCMWItemContainer(IInventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        ItemStack itemstack = null;
        final Slot slot = inventorySlots.get(index);

        if (slot != null && slot.getHasStack())
        {
            final ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            final int size = inventory.getSizeInventory();
            if (index < size)
            {
                if (!mergeItemStack(itemstack1, size, inventorySlots.size(), true))
                {
                    return null;
                }
            }
            else if (!mergeItemStack(itemstack1, 0, size, false))
            {
                return null;
            }

            if (ItemStackTools.isEmpty(itemstack1))
            {
                slot.putStack(null);
            }
            else
            {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }


    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return inventory.isUseableByPlayer(playerIn) ;
    }

    @Override
    public void onContainerClosed(EntityPlayer entityplayer)
    {
        super.onContainerClosed(entityplayer);
        inventory.closeInventory(entityplayer);
    }

}
