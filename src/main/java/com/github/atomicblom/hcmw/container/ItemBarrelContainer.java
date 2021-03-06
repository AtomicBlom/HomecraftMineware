package com.github.atomicblom.hcmw.container;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class ItemBarrelContainer extends HCMWItemContainer {
    public ItemBarrelContainer(InventoryPlayer playerInventory, IInventory barrelInventory) {
        super(barrelInventory);

        playerInventory.openInventory(playerInventory.player);

        int index = 0;
        final int offsetSpacing = 5 * 18;
        for (int xOffset = 0; xOffset < 2; ++xOffset) {
            for (int y = 0; y < 4; ++y) {
                for (int x = 0; x < 4; ++x) {
                    if ((y == 0 || y == 3) && (x == 0 || x == 3)) {
                        continue;
                    }

                    addSlotToContainer(new Slot(barrelInventory, index, 8 + x * 18 + (xOffset * offsetSpacing), 8 + y * 18));

                    index++;
                }
            }
        }

        addPlayerInventory(playerInventory, 8 + 4 * 18 + 4);
    }
}
