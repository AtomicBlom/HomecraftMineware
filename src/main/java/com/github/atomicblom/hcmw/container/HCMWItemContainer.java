package com.github.atomicblom.hcmw.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import javax.annotation.Nonnull;

public abstract class HCMWItemContainer extends HCMWContainer {
    @SuppressWarnings("ProtectedField")
    protected final IInventory inventory;

    HCMWItemContainer(IInventory inventory) {
        this.inventory = inventory;
    }

    @Override
    @Nonnull
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
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
                    return ItemStack.EMPTY;
                }
            }
            else if (!mergeItemStack(itemstack1, 0, size, false))
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
    public boolean canInteractWith(EntityPlayer playerIn) {
        return inventory.isUsableByPlayer(playerIn) ;
    }

    @Override
    public void onContainerClosed(EntityPlayer entityplayer)
    {
        super.onContainerClosed(entityplayer);
        inventory.closeInventory(entityplayer);
    }

}
