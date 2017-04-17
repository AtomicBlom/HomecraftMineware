package com.github.atomicblom.hcmw.container;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

abstract class HCMWContainer extends Container {
    void addPlayerInventory(IInventory playerInventory, int verticalOffset) {
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
}
