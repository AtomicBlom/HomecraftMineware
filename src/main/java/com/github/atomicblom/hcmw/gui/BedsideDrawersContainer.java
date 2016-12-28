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
    private final int numRows;

    public BedsideDrawersContainer(InventoryPlayer playerInventory, IInventory bedSideDrawersTileEntity) {
        this.playerInventory = playerInventory;
        this.bedSideDrawersTileEntity = bedSideDrawersTileEntity;

        //this.lowerChestInventory = chestInventory;
        this.numRows = bedSideDrawersTileEntity.getSizeInventory() / 9;
        playerInventory.openInventory(playerInventory.player);
        int i = (this.numRows - 4) * 18;

        for (int j = 0; j < this.numRows / 2; ++j)
        {
            for (int k = 0; k < 9; ++k)
            {
                this.addSlotToContainer(new Slot(bedSideDrawersTileEntity, k + j * 9, 8 + k * 18, 18 + j * 18));
            }
        }

        for (int j = this.numRows / 2; j < this.numRows; ++j)
        {
            for (int k = 0; k < 9; ++k)
            {
                this.addSlotToContainer(new Slot(bedSideDrawersTileEntity, k + j * 9, 8 + k * 18, 18 + j * 18 + 18));
            }
        }

        for (int l = 0; l < 3; ++l)
        {
            for (int j1 = 0; j1 < 9; ++j1)
            {
                this.addSlotToContainer(new Slot(playerInventory, j1 + l * 9 + 9, 8 + j1 * 18, 104 + l * 18 + i + 18));
            }
        }

        for (int i1 = 0; i1 < 9; ++i1)
        {
            this.addSlotToContainer(new Slot(playerInventory, i1, 8 + i1 * 18, 162 + i + 18));
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
