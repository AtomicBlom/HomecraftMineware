package com.github.atomicblom.hcmw.container;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class BedsideDrawersContainer extends HCMWItemContainer {

    public BedsideDrawersContainer(InventoryPlayer playerInventory, IInventory inventory) {
        super(inventory);

        final int numRows = inventory.getSizeInventory() / 9;
        inventory.openInventory(playerInventory.player);

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

        addPlayerInventory(playerInventory, 122);
    }
}
