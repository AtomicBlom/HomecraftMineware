package com.github.atomicblom.hcmw.client.gui;

import com.github.atomicblom.hcmw.block.tileentity.BedSideDrawersTileEntity;
import com.github.atomicblom.hcmw.container.BedsideDrawersContainer;
import com.github.atomicblom.hcmw.library.Reference.Gui;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.text.TextComponentTranslation;

public class BedSideDrawersGui extends GuiContainer {
    private final InventoryPlayer playerInventory;
    private final String upperUnlocalizedLabel;
    private final String lowerUnlocalizedLabel;

    @SuppressWarnings("AssignmentToSuperclassField")
    public BedSideDrawersGui(InventoryPlayer inventory, BedSideDrawersTileEntity te) {
        super(new BedsideDrawersContainer(inventory, te));

        playerInventory = inventory;

        allowUserInput = false;
        ySize = 204;
        upperUnlocalizedLabel = new TextComponentTranslation("gui." + Gui.bed_side_drawers_upper_label).getUnformattedComponentText();
        lowerUnlocalizedLabel = new TextComponentTranslation("gui." + Gui.bed_side_drawers_lower_label).getUnformattedComponentText();
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        fontRendererObj.drawString(upperUnlocalizedLabel, 8, 6, 4210752);
        fontRendererObj.drawString(lowerUnlocalizedLabel, 8, 6 + 18 * 3, 4210752);
        fontRendererObj.drawString(playerInventory.getDisplayName().getUnformattedText(), 8, ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(Gui.bed_side_drawers_texture);
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
    }
}
