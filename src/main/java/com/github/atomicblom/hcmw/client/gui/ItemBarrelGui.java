package com.github.atomicblom.hcmw.client.gui;

import com.github.atomicblom.hcmw.container.ItemBarrelContainer;
import com.github.atomicblom.hcmw.library.Reference.Gui;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;

public class ItemBarrelGui extends GuiContainer {

    @SuppressWarnings("AssignmentToSuperclassField")
    public ItemBarrelGui(InventoryPlayer inventory, IInventory te) {
        super(new ItemBarrelContainer(inventory, te));

        allowUserInput = false;
        ySize = 204;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(Gui.item_barrel_texture);
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
