package com.github.atomicblom.hcmw.block.tileentity;

import com.github.atomicblom.hcmw.library.BlockLibrary;
import com.github.atomicblom.hcmw.library.Reference;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.IInteractionObject;

public class ItemBarrelTileEntity extends BaseSimpleInventoryTileEntity implements IInteractionObject {

    public ItemBarrelTileEntity() {
        super(24);
    }

    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return null;
    }

    @Override
    public String getGuiID() {
        return Reference.Gui.item_barrel_gui.toString();
    }

    @Override
    public String getName() {
        return "gui." + BlockLibrary.item_barrel.getRegistryName();
    }
}
