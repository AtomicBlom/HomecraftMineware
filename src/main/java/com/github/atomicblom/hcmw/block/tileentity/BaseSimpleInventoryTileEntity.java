package com.github.atomicblom.hcmw.block.tileentity;

import com.github.atomicblom.hcmw.container.BedsideDrawersContainer;
import com.github.atomicblom.hcmw.container.ItemStackTools;
import com.github.atomicblom.hcmw.library.BlockLibrary;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class BaseSimpleInventoryTileEntity extends TileEntity implements IInventory, ITickable
{
    private float animationProgress;
    private float previousAnimationProgress;

    private int ticksSinceSync = -1;


    private final ItemStack[] items;
    private int observingPlayerCount;

    BaseSimpleInventoryTileEntity(int itemStackSize) {
        items = new ItemStack[itemStackSize];
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return true;
    }

    @Override
    public int getSizeInventory() {
        return items.length;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return items[index];
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        final ItemStack itemstack = getAndSplit(items, index, count);


        if (!ItemStackTools.isEmpty(itemstack))
        {
            markDirty();
        }

        return itemstack;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {

        return getAndRemove(items, index);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        items[index] = stack;


        if (stack != null && stack.stackSize > getInventoryStackLimit())
        {
            stack.stackSize = getInventoryStackLimit();
        }

        markDirty();
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(@Nonnull EntityPlayer player)
    {
        World world = getWorld();
        if (world.getTileEntity(pos) == this)
            if (player.getDistanceSq(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) <= 64.0D)
                return true;
        return false;
    }

    @Override
    public void openInventory(@Nonnull EntityPlayer player) {
        World world = getWorld();
        if (player.isSpectator() || world == null) {
            return;
        }

        observingPlayerCount = Math.max(0, observingPlayerCount + 1);

        world.addBlockEvent(pos, blockType, 1, observingPlayerCount);
        world.notifyNeighborsOfStateChange(pos, blockType);
    }

    @Override
    public void closeInventory(@Nonnull EntityPlayer player) {
        if (!player.isSpectator())
        {
            final BlockPos pos = getPos();
            World world = getWorld();
            if (world == null)
            {
                return;
            }

            observingPlayerCount = Math.max(0, observingPlayerCount - 1);

            world.addBlockEvent(pos, blockType, 1, observingPlayerCount);
            world.notifyNeighborsOfStateChange(pos, blockType);
        }
    }

    @Override
    public void update()
    {
        resynchronize();
        ticksSinceSync++;
        updateAnimation();
    }

    private void resynchronize()
    {
        World world = getWorld();
        // Resynchronizes clients with the server state
        if (world != null && !world.isRemote && observingPlayerCount != 0 && (ticksSinceSync + pos.getX() + pos.getY() + pos.getZ()) % 200 == 0)
        {
            observingPlayerCount = 0;

            final float radius = 5.0F;
            final AxisAlignedBB searchBounds = new AxisAlignedBB(pos).expandXyz(radius);
            for (final EntityPlayer player : world.getEntitiesWithinAABB(EntityPlayer.class, searchBounds))
            {
                if (player.openContainer instanceof BedsideDrawersContainer)
                {
                    ++observingPlayerCount;
                }
            }
        }

        if (world != null && !world.isRemote && ticksSinceSync < 0)
        {
            world.addBlockEvent(pos, BlockLibrary.bed_side_drawers, 1, observingPlayerCount);
        }
    }

    private void updateAnimation()
    {
        World world = getWorld();
        previousAnimationProgress = animationProgress;

        if (observingPlayerCount > 0 && animationProgress == 0.0F)
        {
            final double x = pos.getX() + 0.5D;
            final double y = pos.getY() + 0.5D;
            final double z = pos.getZ() + 0.5D;

            final SoundEvent openSound = getOpenSound();
            if (openSound != null)
            {
                world.playSound(null, x, y, z, openSound, SoundCategory.BLOCKS, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
            }
        }

        if (observingPlayerCount == 0 && animationProgress > 0.0F || observingPlayerCount > 0 && animationProgress < 1.0F)
        {
            final float currentAngle = animationProgress;

            final float angle = 0.1F;
            if (observingPlayerCount > 0)
            {
                animationProgress += angle;
            }
            else
            {
                animationProgress -= angle;
            }

            if (animationProgress > 1.0F)
            {
                animationProgress = 1.0F;
            }

            final float maxAngle = 0.5F;

            if (animationProgress < maxAngle && currentAngle >= maxAngle)
            {
                final double x = pos.getX() + 0.5D;
                final double y = pos.getY() + 0.5D;
                final double z = pos.getZ() + 0.5D;

                final SoundEvent closeSound = getCloseSound();
                if (closeSound != null)
                {
                    world.playSound(null, x, y, z, closeSound, SoundCategory.BLOCKS, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
                }
            }

            if (animationProgress < 0.0F)
            {
                animationProgress = 0.0F;
            }
        }
    }

    protected SoundEvent getOpenSound() { return null;}
    protected SoundEvent getCloseSound() { return null;}

    @Override
    public int getField(int id)
    {
        return 0;
    }

    @Override
    public void setField(int id, int value)
    {
    }

    @Override
    public int getFieldCount()
    {
        return 0;
    }

    @Override
    public void clear() {
        for (int i = 0; i < items.length; i++)
        {
            items[i] = null;
        }
    }

    public String getCustomName() { return null; }

    @Override
    public boolean hasCustomName()
    {
        final String customName = getCustomName();
        return customName != null && !customName.isEmpty();
    }

    /**
     * Get the formatted ChatComponent that will be used for the sender's username in chat
     */
    @Override
    @Nonnull
    public ITextComponent getDisplayName()
    {
        return hasCustomName() ?
                new TextComponentString(getCustomName()) :
                new TextComponentTranslation(getName());
    }

    @Override
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
        clear();
        loadAllItems(compound, items);
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        saveAllItems(compound, items);

        return compound;
    }

    @Override
    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        final NBTTagCompound compound = new NBTTagCompound();

        saveAllItems(compound, items);

        return new SPacketUpdateTileEntity(getPos(), 0, compound);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
    {
        if (pkt.getTileEntityType() == 0)
        {
            loadAllItems(pkt.getNbtCompound(), items);
        }
    }

    @Nullable
    private static ItemStack getAndSplit(ItemStack[] stacks, int index, int amount)
    {
        if (index >= 0 && index < stacks.length && stacks[index] != null && amount > 0)        {
            ItemStack itemstack = stacks[index].splitStack(amount);

            if (stacks[index].stackSize == 0)
            {
                stacks[index] = null;
            }

            return itemstack;
        }
        else
        {
            return null;
        }
    }

    private static ItemStack getAndRemove(ItemStack[] stacks, int index)
    {
        if (index >= 0 && index < stacks.length)
        {
            ItemStack itemstack = stacks[index];
            stacks[index] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
    }

    private static void loadAllItems(NBTTagCompound compound, ItemStack[] items) {
        NBTTagList nbttaglist = compound.getTagList("Items", 10);

        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
            int slot = nbttagcompound.getByte("Slot") & 255;

            if (slot >= 0 && slot < items.length)
            {
                items[slot] = ItemStack.loadItemStackFromNBT(nbttagcompound);
            }
        }
    }

    private static void saveAllItems(NBTTagCompound compound, ItemStack[] items) {
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < items.length; ++i)
        {
            ItemStack itemStack = items[i];
            if (itemStack != null)
            {
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setByte("Slot", (byte)i);
                itemStack.writeToNBT(nbttagcompound);
                nbttaglist.appendTag(nbttagcompound);
            }
        }

        compound.setTag("Items", nbttaglist);
    }
}
