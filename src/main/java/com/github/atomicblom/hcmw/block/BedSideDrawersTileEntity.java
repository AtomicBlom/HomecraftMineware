package com.github.atomicblom.hcmw.block;

import com.github.atomicblom.hcmw.gui.BedsideDrawersContainer;
import com.github.atomicblom.hcmw.library.BlockLibrary;
import com.github.atomicblom.hcmw.library.Reference;
import com.github.atomicblom.hcmw.library.SoundLibrary;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;

public class BedSideDrawersTileEntity extends TileEntityLockableLoot implements ITickable, IInventory {
    public float lidAngle;
    public float prevLidAngle;

    private NonNullList<ItemStack> contents;

    private int ticksSinceSync = -1;

    public int observers;

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
        return null;
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
            world.notifyNeighborsOfStateChange(pos.down(), BlockLibrary.bed_side_drawers, false);
        }
    }

    @Override
    public boolean receiveClientEvent(int id, int data)
    {
        if (id == 1)
        {
            this.observers = data;
        }

        return true;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player)
    {
        if (this.world == null)
        {
            return true;
        }

        if (this.world.getTileEntity(this.pos) != this)
        {
            return false;
        }

        return player.getDistanceSq(this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D) <= 64D;
    }

    @Override
    public void update()
    {
        // Resynchronizes clients with the server state
        if (this.world != null && !this.world.isRemote && this.observers != 0 && (this.ticksSinceSync + this.pos.getX() + this.pos.getY() + this.pos.getZ()) % 200 == 0)
        {
            this.observers = 0;

            float radius = 5.0F;

            AxisAlignedBB searchBounds = new AxisAlignedBB(this.pos.getX() - radius, this.pos.getY() - radius, this.pos.getZ() - radius, this.pos.getX() + 1 + radius, this.pos.getY() + 1 + radius, this.pos.getZ() + 1 + radius)
            for (EntityPlayer player : this.world.getEntitiesWithinAABB(EntityPlayer.class, searchBounds))
            {
                if (player.openContainer instanceof BedsideDrawersContainer)
                {
                    ++this.observers;
                }
            }
        }

        if (this.world != null && !this.world.isRemote && this.ticksSinceSync < 0)
        {
            this.world.addBlockEvent(this.pos, BlockLibrary.bed_side_drawers, 1, observers);
        }

        this.ticksSinceSync++;

        this.prevLidAngle = this.lidAngle;

        float angle = 0.1F;

        if (this.observers > 0 && this.lidAngle == 0.0F)
        {
            double x = this.pos.getX() + 0.5D;
            double y = this.pos.getY() + 0.5D;
            double z = this.pos.getZ() + 0.5D;

            this.world.playSound(null, x, y, z, SoundLibrary.bed_side_drawers_open, SoundCategory.BLOCKS, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
        }

        if (this.observers == 0 && this.lidAngle > 0.0F || this.observers > 0 && this.lidAngle < 1.0F)
        {
            float currentAngle = this.lidAngle;

            if (this.observers > 0)
            {
                this.lidAngle += angle;
            }
            else
            {
                this.lidAngle -= angle;
            }

            if (this.lidAngle > 1.0F)
            {
                this.lidAngle = 1.0F;
            }

            float maxAngle = 0.5F;

            if (this.lidAngle < maxAngle && currentAngle >= maxAngle)
            {
                double x = this.pos.getX() + 0.5D;
                double y = this.pos.getY() + 0.5D;
                double z = this.pos.getZ() + 0.5D;

                this.world.playSound(null, x, y, z, SoundLibrary.bed_side_drawers_close, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
            }

            if (this.lidAngle < 0.0F)
            {
                this.lidAngle = 0.0F;
            }
        }
    }
}
