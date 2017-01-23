package com.github.atomicblom.hcmw.container;

import com.github.atomicblom.hcmw.block.tileentity.FluidBarrelTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;

/**
 * Created by codew on 30/12/2016.
 */
public class FluidBarrelContainer extends HCMWContainer {
    private FluidBarrelTileEntity fluidBarrelTileEntity;

    public FluidBarrelContainer(IInventory playerInventory, FluidBarrelTileEntity fluidBarrelTileEntity) {
        this.fluidBarrelTileEntity = fluidBarrelTileEntity;
        addPlayerInventory(playerInventory, 84);
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }

    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
        if (clickTypeIn == ClickType.QUICK_MOVE) {
            final Slot slot = this.inventorySlots.get(slotId);
            if (slot.getHasStack()) {
                IFluidHandler capability = fluidBarrelTileEntity.getCapability(FluidBarrelTileEntity.FLUID_HANDLER_CAPABILITY, null);

                final FluidActionResult fluidActionResult = FluidUtil.interactWithFluidHandler(slot.getStack(), capability, player);
                if (fluidActionResult.isSuccess()) {
                    slot.putStack(fluidActionResult.result);
                }
            }

            return ItemStack.EMPTY;
        }
        return super.slotClick(slotId, dragType, clickTypeIn, player);
    }
}
