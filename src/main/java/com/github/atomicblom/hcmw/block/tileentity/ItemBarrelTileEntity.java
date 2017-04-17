package com.github.atomicblom.hcmw.block.tileentity;

import com.github.atomicblom.hcmw.container.ItemBarrelContainer;
import com.github.atomicblom.hcmw.library.BlockLibrary;
import com.github.atomicblom.hcmw.library.Reference;
import com.github.atomicblom.hcmw.library.Reference.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.IInteractionObject;

import javax.annotation.Nonnull;

public class ItemBarrelTileEntity extends BaseSimpleInventoryTileEntity implements IInteractionObject {

    public ItemBarrelTileEntity() {
        super(24);
    }

    @Override
    @Nonnull
    public Container createContainer(@Nonnull InventoryPlayer playerInventory, @Nonnull EntityPlayer playerIn) {
        return new ItemBarrelContainer(playerInventory, this);
    }

    @Override
    @Nonnull
    public String getGuiID() {
        return Gui.item_barrel_gui.toString();
    }

    @Override
    @Nonnull
    public String getName() {
        return "gui." + BlockLibrary.item_barrel.getRegistryName();
    }
}
