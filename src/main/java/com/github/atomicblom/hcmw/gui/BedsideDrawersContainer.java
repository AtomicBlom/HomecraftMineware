package com.github.atomicblom.hcmw.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class BedsideDrawersContainer extends Container {
    private final IInventory bedSideDrawersTileEntity;

    public BedsideDrawersContainer(InventoryPlayer playerInventory, IInventory bedSideDrawersTE) {
        bedSideDrawersTileEntity = bedSideDrawersTE;

        final int numRows = bedSideDrawersTE.getSizeInventory() / 9;
        playerInventory.openInventory(playerInventory.player);

        for (int j = 0; j < numRows / 2; ++j)
        {
            for (int k = 0; k < 9; ++k)
            {
                addSlotToContainer(new Slot(bedSideDrawersTE, k + j * 9, 8 + k * 18, 18 + j * 18));
            }
        }

        for (int j = numRows / 2; j < numRows; ++j)
        {
            for (int k = 0; k < 9; ++k)
            {
                addSlotToContainer(new Slot(bedSideDrawersTE, k + j * 9, 8 + k * 18, 18 + j * 18 + 18));
            }
        }

        final int i = (numRows - 4) * 18;
        for (int l = 0; l < 3; ++l)
        {
            for (int j1 = 0; j1 < 9; ++j1)
            {
                addSlotToContainer(new Slot(playerInventory, j1 + l * 9 + 9, 8 + j1 * 18, 104 + l * 18 + i + 18));
            }
        }

        for (int i1 = 0; i1 < 9; ++i1)
        {
            addSlotToContainer(new Slot(playerInventory, i1, 8 + i1 * 18, 162 + i + 18));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return bedSideDrawersTileEntity.isUsableByPlayer(playerIn) ;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        final Slot slot = inventorySlots.get(index);

        if (slot != null && slot.getHasStack())
        {
            final ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            final int size = bedSideDrawersTileEntity.getSizeInventory();
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
    public void onContainerClosed(EntityPlayer entityplayer)
    {
        super.onContainerClosed(entityplayer);
        bedSideDrawersTileEntity.closeInventory(entityplayer);
    }
}
