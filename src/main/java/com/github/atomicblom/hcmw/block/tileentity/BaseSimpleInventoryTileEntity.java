package com.github.atomicblom.hcmw.block.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class BaseSimpleInventoryTileEntity extends TileEntity implements IInventory {

    private final NonNullList<ItemStack> items;
    private int observingPlayerCount;

    protected BaseSimpleInventoryTileEntity(int itemStackSize) {
        this.items = NonNullList.withSize(itemStackSize, ItemStack.EMPTY);
    }

    @Override
    public boolean isItemValidForSlot(int index, @Nonnull ItemStack stack) {
        return true;
    }

    @Override
    public int getSizeInventory() {
        return items.size();
    }

    @Override
    public boolean isEmpty() {
        return items
                .stream()
                .anyMatch(itemStack -> !itemStack.isEmpty());
    }

    @Override
    @Nonnull
    public ItemStack getStackInSlot(int index) {
        return this.items.get(index);
    }

    @Override
    @Nonnull
    public ItemStack decrStackSize(int index, int count) {
        ItemStack itemstack = ItemStackHelper.getAndSplit(this.items, index, count);

        if (!itemstack.isEmpty())
        {
            this.markDirty();
        }

        return itemstack;
    }

    @Override
    @Nonnull
    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(this.items, index);
    }

    @Override
    public void setInventorySlotContents(int index, @Nonnull ItemStack stack) {
        this.items.set(index, stack);

        if (stack.getCount() > this.getInventoryStackLimit())
        {
            stack.setCount(this.getInventoryStackLimit());
        }

        this.markDirty();
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUsableByPlayer(@Nonnull EntityPlayer player)
    {
        BlockPos pos = this.getPos();
        return world.getTileEntity(pos) == this &&
                player.getDistanceSq((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public void openInventory(@Nonnull EntityPlayer player) {
        World world = this.world;
        BlockPos pos = getPos();
        if (player.isSpectator() || world == null) {
            return;
        }

        observingPlayerCount = Math.max(0, observingPlayerCount + 1);

        world.addBlockEvent(pos, blockType, 1, observingPlayerCount);
        world.notifyNeighborsOfStateChange(pos, blockType, false);
    }

    @Override
    public void closeInventory(@Nonnull EntityPlayer player) {
        if (!player.isSpectator())
        {
            BlockPos pos = getPos();
            if (world == null)
            {
                return;
            }

            observingPlayerCount = Math.max(0, observingPlayerCount - 1);

            world.addBlockEvent(pos, blockType, 1, observingPlayerCount);
            world.notifyNeighborsOfStateChange(pos, blockType, false);
        }
    }

    public int getField(int id)
    {
        return 0;
    }

    public void setField(int id, int value)
    {
    }

    public int getFieldCount()
    {
        return 0;
    }

    @Override
    public void clear() {
        items.clear();
    }

    public String getCustomName() { return null; }

    @Override
    public boolean hasCustomName()
    {
        String customName = getCustomName();
        return customName != null && !customName.isEmpty();
    }

    /**
     * Get the formatted ChatComponent that will be used for the sender's username in chat
     */
    @Nonnull
    public ITextComponent getDisplayName()
    {
        return this.hasCustomName() ?
                new TextComponentString(getCustomName()) :
                new TextComponentTranslation(this.getName());
    }

    public boolean receiveClientEvent(int id, int data)
    {
        if (id == 1)
        {
            observingPlayerCount = data;
        }

        return true;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        items.clear();
        ItemStackHelper.loadAllItems(compound, items);
    }

    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        ItemStackHelper.saveAllItems(compound, items);

        return compound;
    }

    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        final NBTTagCompound compound = new NBTTagCompound();

        ItemStackHelper.saveAllItems(compound, items);

        return new SPacketUpdateTileEntity(getPos(), 0, compound);
    }

    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
    {
        if (pkt.getTileEntityType() == 0)
        {
            ItemStackHelper.loadAllItems(pkt.getNbtCompound(), items);
        }
    }
}
