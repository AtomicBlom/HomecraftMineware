package com.github.atomicblom.hcmw.block.tileentity;

import com.github.atomicblom.hcmw.container.BedsideDrawersContainer;
import com.github.atomicblom.hcmw.library.BlockLibrary;
import com.github.atomicblom.hcmw.library.Reference;
import com.github.atomicblom.hcmw.library.SoundLibrary;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.IInteractionObject;
import javax.annotation.Nullable;

public class BedSideDrawersTileEntity extends BaseSimpleInventoryTileEntity implements IInteractionObject
{
    public BedSideDrawersTileEntity() {
        super(4* 9);
    }

    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return new BedsideDrawersContainer(playerInventory, this);
    }

    @Override
    public String getGuiID() {
        return Reference.Gui.bed_side_drawers_gui.toString();
    }

    @Override
    public String getName() {
        return "gui." + BlockLibrary.bed_side_drawers.getRegistryName();
    }

    @Override
    public boolean canRenderBreaking()
    {
        return true;
    }

    protected SoundEvent getOpenSound() { return SoundLibrary.bed_side_drawers_open; }
    protected SoundEvent getCloseSound() { return SoundLibrary.bed_side_drawers_close; }
}
