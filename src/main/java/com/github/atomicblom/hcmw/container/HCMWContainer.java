package com.github.atomicblom.hcmw.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public abstract class HCMWContainer extends Container {
    protected final IInventory inventory;

    HCMWContainer(IInventory inventory) {
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

    protected void addPlayerInventory(IInventory playerInventory, int verticalOffset) {
        for (int row = 0; row < 3; ++row)
        {
            for (int column = 0; column < 9; ++column)
            {
                addSlotToContainer(new Slot(playerInventory, column + row * 9 + 9, 8 + column * 18, row * 18 + verticalOffset));
            }
        }

        for (int column = 0; column < 9; ++column)
        {
            addSlotToContainer(new Slot(playerInventory, column, 8 + column * 18, verticalOffset + 3 * 18 + 4));
        }
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
