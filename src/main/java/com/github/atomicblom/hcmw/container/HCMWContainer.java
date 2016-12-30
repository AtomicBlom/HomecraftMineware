package com.github.atomicblom.hcmw.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

/**
 * Created by codew on 30/12/2016.
 */
public abstract class HCMWContainer extends Container {
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
}
