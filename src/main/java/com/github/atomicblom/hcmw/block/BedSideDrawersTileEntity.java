package com.github.atomicblom.hcmw.block;

import com.github.atomicblom.hcmw.gui.BedsideDrawersContainer;
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
import net.minecraft.util.math.AxisAlignedBB;
import javax.annotation.Nullable;

public class BedSideDrawersTileEntity extends TileEntityLockableLoot implements ITickable, IInventory {
    private float animationProgress;
    private float previousAnimationProgress;

    private NonNullList<ItemStack> contents;

    private int ticksSinceSync = -1;

    private int observers;

    public BedSideDrawersTileEntity() {
        contents = NonNullList.withSize(4* 9, ItemStack.EMPTY);
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return contents;
    }

    @Override
    public int getSizeInventory() {
        return contents.size();
    }

    @Override
    public boolean isEmpty() {
        return contents
                .stream()
                .anyMatch(itemStack -> !itemStack.isEmpty());
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
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
    public void openInventory(EntityPlayer player)
    {
        if (player.isSpectator() || world == null) {
            return;
        }

        observers = Math.max(0, observers + 1);

        world.addBlockEvent(pos, BlockLibrary.bed_side_drawers, 1, observers);
        world.notifyNeighborsOfStateChange(pos, BlockLibrary.bed_side_drawers, false);
    }

    @Override
    public void closeInventory(EntityPlayer player)
    {
        if (!player.isSpectator())
        {
            if (world == null)
            {
                return;
            }

            observers = Math.max(0, observers - 1);

            world.addBlockEvent(pos, BlockLibrary.bed_side_drawers, 1, observers);
            world.notifyNeighborsOfStateChange(pos, BlockLibrary.bed_side_drawers, false);
        }
    }

    @Override
    public boolean receiveClientEvent(int id, int data)
    {
        if (id == 1)
        {
            observers = data;
        }

        return true;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player)
    {
        if (world == null)
        {
            return true;
        }

        if (world.getTileEntity(pos) != this)
        {
            return false;
        }

        return player.getDistanceSq(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) <= 64D;
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
        if (world != null && !world.isRemote && observers != 0 && (ticksSinceSync + pos.getX() + pos.getY() + pos.getZ()) % 200 == 0)
        {
            observers = 0;

            final float radius = 5.0F;
            final AxisAlignedBB searchBounds = new AxisAlignedBB(pos).expandXyz(radius);
            for (final EntityPlayer player : world.getEntitiesWithinAABB(EntityPlayer.class, searchBounds))
            {
                if (player.openContainer instanceof BedsideDrawersContainer)
                {
                    ++observers;
                }
            }
        }

        if (world != null && !world.isRemote && ticksSinceSync < 0)
        {
            world.addBlockEvent(pos, BlockLibrary.bed_side_drawers, 1, observers);
        }
    }

    private void updateAnimation()
    {
        previousAnimationProgress = animationProgress;

        if (observers > 0 && animationProgress == 0.0F)
        {
            final double x = pos.getX() + 0.5D;
            final double y = pos.getY() + 0.5D;
            final double z = pos.getZ() + 0.5D;

            world.playSound(null, x, y, z, SoundLibrary.bed_side_drawers_open, SoundCategory.BLOCKS, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
        }

        if (observers == 0 && animationProgress > 0.0F || observers > 0 && animationProgress < 1.0F)
        {
            final float currentAngle = animationProgress;

            final float angle = 0.1F;
            if (observers > 0)
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

                world.playSound(null, x, y, z, SoundLibrary.bed_side_drawers_close, SoundCategory.BLOCKS, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
            }

            if (animationProgress < 0.0F)
            {
                animationProgress = 0.0F;
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);

        contents = NonNullList.withSize(getSizeInventory(), ItemStack.EMPTY);

        if (!checkLootAndRead(compound))
        {
            ItemStackHelper.loadAllItems(compound, contents);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);

        if (!checkLootAndWrite(compound))
        {
            ItemStackHelper.saveAllItems(compound, contents);
        }

        return compound;
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        final NBTTagCompound compound = new NBTTagCompound();

        if (!checkLootAndWrite(compound))
        {
            ItemStackHelper.saveAllItems(compound, contents);
        }

        return new SPacketUpdateTileEntity(pos, 0, compound);
    }

    @Override
    public NBTTagCompound getUpdateTag()
    {
        return writeToNBT(new NBTTagCompound());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
    {
        if (pkt.getTileEntityType() == 0)
        {
            ItemStackHelper.loadAllItems(pkt.getNbtCompound(), contents);
        }
    }

    @Override
    public boolean canRenderBreaking()
    {
        return true;
    }
}
