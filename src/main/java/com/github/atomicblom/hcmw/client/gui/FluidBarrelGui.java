package com.github.atomicblom.hcmw.client.gui;

import com.github.atomicblom.hcmw.block.tileentity.FluidBarrelTileEntity;
import com.github.atomicblom.hcmw.container.FluidBarrelContainer;
import com.github.atomicblom.hcmw.library.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import org.lwjgl.opengl.GL11;

public class FluidBarrelGui extends GuiContainer {
    //private final int inventoryRows;
    private final InventoryPlayer playerInventory;
    private final FluidBarrelTileEntity drawerInventory;
    private final TextureMap blocksTextureMap;
    private TextureManager textureManager;

    //private final String barrelName;

    public FluidBarrelGui(InventoryPlayer inventory, FluidBarrelTileEntity te) {
        super(new FluidBarrelContainer(inventory, te));

        playerInventory = inventory;
        drawerInventory = te;

        allowUserInput = false;
        //inventoryRows = te.getSizeInventory() / 9 + 2;
        ySize = 204;
        Minecraft mc = Minecraft.getMinecraft();

        blocksTextureMap = mc.getTextureMapBlocks();
        textureManager = mc.getTextureManager();
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

        textureManager.bindTexture(Reference.Gui.fluid_barrel_texture);
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

        IFluidTankProperties[] tankProperties = capability.getTankProperties();
        if (tankProperties != null && tankProperties.length > 0) {
            IFluidTankProperties tankProperty = tankProperties[0];
            if (tankProperty != null) {
                FluidStack contents = tankProperty.getContents();
                if (contents != null) {
                    Fluid fluid = contents.getFluid();
                    if (fluid != null) {
                        ResourceLocation still = fluid.getStill();
                        if (still != null) {


                            textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                            TextureAtlasSprite sprite = blocksTextureMap.getAtlasSprite(fluid.getStill().toString());

                            float amountStored = 8 * contents.amount / 1000.0f;

                            if (sprite != null) {
                                drawRepeatedFluidSprite(
                                        sprite,
                                        offsetX + 70,
                                        offsetY + 10 + 64 - amountStored,
                                        36,
                                        amountStored
                                );
                            }
                        }
                    }
                }
            }
        }
        textureManager.bindTexture(Reference.Gui.fluid_barrel_texture);

        drawTexturedModalRect(
                offsetX + 69,
                offsetY + 10,
                xSize,
                0,
                38,
                65
        );
    }

    public static void drawRepeatedFluidSprite(TextureAtlasSprite sprite, float x, float y, float w, float h)
    {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexBuffer = tessellator.getBuffer();
        vertexBuffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        int iW = sprite.getIconWidth();
        int iH = sprite.getIconHeight();
        if(iW > 0 && iH > 0) {
            drawRepeatedSprite(vertexBuffer, x, y, w, h, iW, iH, sprite.getMinU(), sprite.getMaxU(), sprite.getMinV(), sprite.getMaxV());
        }

        tessellator.draw();
    }


    public static void drawRepeatedSprite(BufferBuilder bufferBuilder, float x, float y, float w, float h, int iconWidth, int iconHeight, float uMin, float uMax, float vMin, float vMax)
    {
        int iterMaxW = (int) (w / iconWidth);
        int iterMaxH = (int) (h / iconHeight);
        float leftoverW = w % iconWidth;
        float leftoverH = h % iconHeight;
        float leftoverWf = leftoverW / (float) iconWidth;
        float leftoverHf = leftoverH / (float) iconHeight;
        float iconUDif = uMax - uMin;
        float iconVDif = vMax - vMin;
        for(int ww = 0; ww < iterMaxW; ww++)
        {
            for(int hh = 0; hh < iterMaxH; hh++)
                drawTexturedRect(bufferBuilder, x + ww * iconWidth, y + hh * iconHeight, iconWidth, iconHeight, uMin, uMax, vMin, vMax);
            drawTexturedRect(bufferBuilder, x + ww * iconWidth, y + iterMaxH * iconHeight, iconWidth, leftoverH, uMin, uMax, vMin, (vMin + iconVDif * leftoverHf));
        }
        if(leftoverW > 0)
        {
            for(int hh = 0; hh < iterMaxH; hh++)
                drawTexturedRect(bufferBuilder, x + iterMaxW * iconWidth, y + hh * iconHeight, leftoverW, iconHeight, uMin, (uMin + iconUDif * leftoverWf), vMin, vMax);
            drawTexturedRect(bufferBuilder, x + iterMaxW * iconWidth, y + iterMaxH * iconHeight, leftoverW, leftoverH, uMin, (uMin + iconUDif * leftoverWf), vMin, (vMin + iconVDif * leftoverHf));
        }
    }


    public static void drawTexturedRect(BufferBuilder bufferBuilder, float x, float y, float w, float h, double... uv)
    {
        bufferBuilder.pos(x, y + h, 0).tex(uv[0], uv[3]).endVertex();
        bufferBuilder.pos(x + w, y + h, 0).tex(uv[1], uv[3]).endVertex();
        bufferBuilder.pos(x + w, y, 0).tex(uv[1], uv[2]).endVertex();
        bufferBuilder.pos(x, y, 0).tex(uv[0], uv[2]).endVertex();
    }

}
