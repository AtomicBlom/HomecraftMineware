package com.github.atomicblom.hcmw.block.tileentity;

import com.github.atomicblom.hcmw.container.FluidBarrelContainer;
import com.github.atomicblom.hcmw.container.ItemBarrelContainer;
import com.github.atomicblom.hcmw.library.BlockLibrary;
import com.github.atomicblom.hcmw.library.Reference;
import com.github.atomicblom.hcmw.util.HCMWFluidTank;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IInteractionObject;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.TileFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FluidBarrelTileEntity extends TileEntity implements IInteractionObject {

    @CapabilityInject(IFluidHandler.class)
    public static Capability<IFluidHandler> FLUID_HANDLER_CAPABILITY = null;

    IFluidHandler inventory;

    public FluidBarrelTileEntity() {
        inventory = new HCMWFluidTank(this, 8000);
    }

    @Override
    @Nonnull
    public Container createContainer(@Nonnull InventoryPlayer playerInventory, @Nonnull EntityPlayer playerIn) {
        return new FluidBarrelContainer(playerInventory, this);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return facing == null || facing == EnumFacing.UP || facing == EnumFacing.DOWN;
        }
        return super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            if (facing == null || facing == EnumFacing.UP || facing == EnumFacing.DOWN) {
                return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(inventory);
            }
        }
        return super.getCapability(capability, facing);
    }

    @Override
    @Nonnull
    public String getGuiID() {
        return Reference.Gui.fluid_barrel_gui.toString();
    }

    @Override
    @Nonnull
    public String getName() {
        return "gui." + BlockLibrary.fluid_barrel.getRegistryName();
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        if (compound.hasKey("contents")) {
            CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.readNBT(inventory, null, compound.getCompoundTag("contents"));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        NBTBase nbtBase = CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.writeNBT(inventory, null);
        compound.setTag("contents", nbtBase);

        return compound;
    }

    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        final NBTTagCompound compound = new NBTTagCompound();

        compound.setTag("contents", CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.writeNBT(inventory, null));

        return new SPacketUpdateTileEntity(getPos(), 0, compound);
    }

    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
    {
        if (pkt.getTileEntityType() == 0)
        {
            NBTTagCompound compound = pkt.getNbtCompound();
            if (compound.hasKey("contents")) {
                CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.readNBT(inventory, null, compound.getCompoundTag("contents"));
            }
        }
    }

    @Nonnull
    @Override
    public NBTTagCompound getUpdateTag() {
        // new tag instead of super since default implementation calls the super of writeToNBT
        return writeToNBT(new NBTTagCompound());
    }

    @Override
    public void handleUpdateTag(@Nonnull NBTTagCompound tag) {
        readFromNBT(tag);
    }
}
