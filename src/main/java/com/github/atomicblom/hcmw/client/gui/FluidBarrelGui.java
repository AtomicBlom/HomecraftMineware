package com.github.atomicblom.hcmw.client.gui;

import com.github.atomicblom.hcmw.block.tileentity.FluidBarrelTileEntity;
import com.github.atomicblom.hcmw.container.FluidBarrelContainer;
import com.github.atomicblom.hcmw.container.ItemBarrelContainer;
import com.github.atomicblom.hcmw.library.Reference;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import java.util.Arrays;

public class FluidBarrelGui extends GuiContainer {
    //private final int inventoryRows;
    private final InventoryPlayer playerInventory;
    private final FluidBarrelTileEntity drawerInventory;

    //private final String barrelName;

    public FluidBarrelGui(InventoryPlayer inventory, FluidBarrelTileEntity te) {
        super(new FluidBarrelContainer(inventory, te));

        playerInventory = inventory;
        drawerInventory = te;

        allowUserInput = false;
        //inventoryRows = te.getSizeInventory() / 9 + 2;
        ySize = 204;

        //barrelName = te.getDisplayName().getUnformattedText();
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        //fontRendererObj.drawString(barrelName, 8, 6, 4210752);
        //fontRendererObj.drawString(playerInventory.getDisplayName().getUnformattedText(), 8, ySize - 96 + 2, 4210752);
    }

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(Reference.Gui.fluid_barrel_texture);
        final int offsetX = (width - xSize) / 2;
        final int offsetY = (height - ySize) / 2;
        drawTexturedModalRect(
                offsetX,
                offsetY,
                0,
                0,
                xSize,
                ySize
        );

        IFluidHandler capability = drawerInventory.getCapability(FluidBarrelTileEntity.FLUID_HANDLER_CAPABILITY, null);

        FluidStack contents = capability.getTankProperties()[0].getContents();

        ResourceLocation still = contents.getFluid().getStill();
        mc.getTextureManager().bindTexture(still);



        drawTexturedModalRect(
                offsetX + 69,
                offsetY + 10,
                xSize,
                0,
                38,
                65
        );
    }
}
