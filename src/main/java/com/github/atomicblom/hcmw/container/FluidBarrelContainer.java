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
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class FluidBarrelContainer extends HCMWContainer {
    private final FluidBarrelTileEntity fluidBarrelTileEntity;

    public FluidBarrelContainer(IInventory playerInventory, FluidBarrelTileEntity barrelTileEntity) {
        fluidBarrelTileEntity = barrelTileEntity;
        addPlayerInventory(playerInventory, 84);
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }

    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
        if (clickTypeIn == ClickType.QUICK_MOVE) {
            final Slot slot = inventorySlots.get(slotId);
            if (slot.getHasStack()) {
                final IFluidHandler handler = fluidBarrelTileEntity.getCapability(FluidBarrelTileEntity.fluidHandlerCapability, null);

                IItemHandler playerInventory = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
                if (playerInventory != null)
                {
                    FluidActionResult fluidActionResult = FluidUtil.tryFillContainerAndStow(slot.getStack(), handler, playerInventory, Integer.MAX_VALUE, player);
                    if (!fluidActionResult.isSuccess())
                    {
                        fluidActionResult = FluidUtil.tryEmptyContainerAndStow(slot.getStack(), handler, playerInventory, Integer.MAX_VALUE, player);
                    }

                    if (fluidActionResult.isSuccess())
                    {
                        slot.putStack(fluidActionResult.getResult());
                    }
                }
            }

            return ItemStack.EMPTY;
        }
        return super.slotClick(slotId, dragType, clickTypeIn, player);
    }
}
