package com.github.atomicblom.hcmw.client.gui;

import com.github.atomicblom.hcmw.HomecraftMinewares;
import com.github.atomicblom.hcmw.block.BedSideDrawersTileEntity;
import com.github.atomicblom.hcmw.gui.BedsideDrawersContainer;
import com.github.atomicblom.hcmw.library.Reference;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class BedSideDrawersGui extends GuiContainer {
    private final int inventoryRows;
    private final InventoryPlayer playerInventory;
    private final BedSideDrawersTileEntity drawerInventory;
    private final String upperUnlocalizedLabel;
    private final String lowerUnlocalizedLabel;

    public BedSideDrawersGui(InventoryPlayer inventory, BedSideDrawersTileEntity te) {
        super(new BedsideDrawersContainer(inventory, te));

        this.playerInventory = inventory;
        this.drawerInventory = te;

        this.allowUserInput = false;
        this.inventoryRows = te.getSizeInventory() / 9 + 2;
        this.ySize = 204; //114 + (this.inventoryRows) * 18;
        this.upperUnlocalizedLabel = "gui." + Reference.Gui.bed_side_drawers_upper_label.toString();
        this.lowerUnlocalizedLabel = "gui." + Reference.Gui.bed_side_drawers_lower_label.toString();
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        this.fontRendererObj.drawString(upperUnlocalizedLabel, 8, 6, 4210752);
        this.fontRendererObj.drawString(lowerUnlocalizedLabel, 8, 6 + 18 * 3, 4210752);
        this.fontRendererObj.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
    }

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(new ResourceLocation(HomecraftMinewares.MODID, "textures/gui/hcmwbedsidedrawergui.png"));
        int offsetX = (this.width - this.xSize) / 2;
        int offsetY = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(
                offsetX,
                offsetY,
                0,
                0,
                this.xSize,
                this.ySize
        );
        /*this.drawTexturedModalRect(
                offsetX,
                offsetY + (this.inventoryRows) * 18 + 17,
                0,
                126,
                this.xSize,
                96);*/
    }
}
