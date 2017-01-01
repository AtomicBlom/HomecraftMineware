package com.github.atomicblom.hcmw.block.tileentity;

import com.github.atomicblom.hcmw.container.BedsideDrawersContainer;
import com.github.atomicblom.hcmw.library.BlockLibrary;
import com.github.atomicblom.hcmw.library.SoundLibrary;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class BaseSimpleInventoryTileEntity extends TileEntity implements IInventory, ITickable
{
    private float animationProgress;
    private float previousAnimationProgress;

    private int ticksSinceSync = -1;


    private final NonNullList<ItemStack> items;
    private int observingPlayerCount;

    BaseSimpleInventoryTileEntity(int itemStackSize) {
        items = NonNullList.withSize(itemStackSize, ItemStack.EMPTY);
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
        return items.get(index);
    }

    @Override
    @Nonnull
    public ItemStack decrStackSize(int index, int count) {
        final ItemStack itemstack = ItemStackHelper.getAndSplit(items, index, count);

        if (!itemstack.isEmpty())
        {
            markDirty();
        }

        return itemstack;
    }

    @Override
    @Nonnull
    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(items, index);
    }

    @Override
    public void setInventorySlotContents(int index, @Nonnull ItemStack stack) {
        items.set(index, stack);

        if (stack.getCount() > getInventoryStackLimit())
        {
            stack.setCount(getInventoryStackLimit());
        }

        markDirty();
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUsableByPlayer(@Nonnull EntityPlayer player)
    {
        if (world.getTileEntity(pos) == this)
            if (player.getDistanceSq(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) <= 64.0D)
                return true;
        return false;
    }

    @Override
    public void openInventory(@Nonnull EntityPlayer player) {
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
            final BlockPos pos = getPos();
            if (world == null)
            {
                return;
            }

            observingPlayerCount = Math.max(0, observingPlayerCount - 1);

            world.addBlockEvent(pos, blockType, 1, observingPlayerCount);
            world.notifyNeighborsOfStateChange(pos, blockType, false);
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
        items.clear();
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
        items.clear();
        ItemStackHelper.loadAllItems(compound, items);
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        ItemStackHelper.saveAllItems(compound, items);

        return compound;
    }

    @Override
    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        final NBTTagCompound compound = new NBTTagCompound();

        ItemStackHelper.saveAllItems(compound, items);

        return new SPacketUpdateTileEntity(getPos(), 0, compound);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
    {
        if (pkt.getTileEntityType() == 0)
        {
            ItemStackHelper.loadAllItems(pkt.getNbtCompound(), items);
        }
    }
}
