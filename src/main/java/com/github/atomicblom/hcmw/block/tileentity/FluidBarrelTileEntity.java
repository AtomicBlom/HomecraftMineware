package com.github.atomicblom.hcmw.block.tileentity;

import com.github.atomicblom.hcmw.container.FluidBarrelContainer;
import com.github.atomicblom.hcmw.library.BlockLibrary;
import com.github.atomicblom.hcmw.library.Reference.Gui;
import com.github.atomicblom.hcmw.util.HCMWFluidTank;
import com.google.common.base.Preconditions;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IInteractionObject;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class FluidBarrelTileEntity extends TileEntity implements IInteractionObject {

    @SuppressWarnings({"StaticVariableMayNotBeInitialized", "StaticNonFinalField"})
    @CapabilityInject(IFluidHandler.class)
    @Nonnull
    public static Capability<IFluidHandler> fluidHandlerCapability;

    private final IFluidHandler inventory;

    public FluidBarrelTileEntity() {
        inventory = createInventory();
    }

    private IFluidHandler createInventory()
    {
        return new HCMWFluidTank(this, 8000);
    }

    @Override
    @Nonnull
    public Container createContainer(@Nonnull InventoryPlayer playerInventory, @Nonnull EntityPlayer playerIn) {
        return new FluidBarrelContainer(playerInventory, this);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        if (Objects.equals(capability, fluidHandlerCapability)) {
            return facing == null || facing == EnumFacing.UP || facing == EnumFacing.DOWN;
        }
        return super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (Objects.equals(capability, fluidHandlerCapability)) {
            if (facing == null || facing == EnumFacing.UP || facing == EnumFacing.DOWN) {
                return fluidHandlerCapability.cast(inventory);
            }
        }
        return super.getCapability(capability, facing);
    }

    @Override
    @Nonnull
    public String getGuiID() {
        return Gui.fluid_barrel_gui.toString();
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
            fluidHandlerCapability.readNBT(inventory, null, compound.getCompoundTag("contents"));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        final NBTBase nbtBase = fluidHandlerCapability.writeNBT(inventory, null);
        Preconditions.checkNotNull(nbtBase);
        compound.setTag("contents", nbtBase);

        return compound;
    }

    @Override
    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        final NBTTagCompound compound = new NBTTagCompound();
        final NBTBase value = fluidHandlerCapability.writeNBT(inventory, null);
        Preconditions.checkNotNull(value);

        compound.setTag("contents", value);

        return new SPacketUpdateTileEntity(getPos(), 0, compound);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
    {
        if (pkt.getTileEntityType() == 0)
        {
            final NBTTagCompound compound = pkt.getNbtCompound();
            if (compound.hasKey("contents")) {
                fluidHandlerCapability.readNBT(inventory, null, compound.getCompoundTag("contents"));
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
