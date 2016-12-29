package com.github.atomicblom.hcmw.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class BedsideDrawersContainer extends HCMWContainer {

    public BedsideDrawersContainer(InventoryPlayer playerInventory, IInventory inventory) {
        super(inventory);

        final int numRows = inventory.getSizeInventory() / 9;
        playerInventory.openInventory(playerInventory.player);

        for (int j = 0; j < numRows / 2; ++j)
        {
            for (int k = 0; k < 9; ++k)
            {
                addSlotToContainer(new Slot(inventory, k + j * 9, 8 + k * 18, 18 + j * 18));
            }
        }

        for (int j = numRows / 2; j < numRows; ++j)
        {
            for (int k = 0; k < 9; ++k)
            {
                addSlotToContainer(new Slot(inventory, k + j * 9, 8 + k * 18, 18 + j * 18 + 18));
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
}
