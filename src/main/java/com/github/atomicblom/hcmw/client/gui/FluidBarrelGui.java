package com.github.atomicblom.hcmw.client.gui;

import com.github.atomicblom.hcmw.block.tileentity.FluidBarrelTileEntity;
import com.github.atomicblom.hcmw.container.FluidBarrelContainer;
import com.github.atomicblom.hcmw.library.Reference.Gui;
import com.google.common.base.Preconditions;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

public class FluidBarrelGui extends GuiContainer {
    private final FluidBarrelTileEntity drawerInventory;
    private final TextureMap blocksTextureMap;
    private final TextureManager textureManager;

    @SuppressWarnings("AssignmentToSuperclassField")
    public FluidBarrelGui(InventoryPlayer inventory, FluidBarrelTileEntity te) {
        super(new FluidBarrelContainer(inventory, te));

        drawerInventory = te;

        allowUserInput = false;
        ySize = 204;
        //final Minecraft mc = Minecraft.getMinecraft();

        blocksTextureMap = mc.getTextureMapBlocks();
        textureManager = mc.getTextureManager();
    }

    @SuppressWarnings("MethodWithMoreThanThreeNegations")
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        textureManager.bindTexture(Gui.fluid_barrel_texture);
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

        final IFluidHandler capability = drawerInventory.getCapability(FluidBarrelTileEntity.fluidHandlerCapability, null);

        Preconditions.checkNotNull(capability);

        final IFluidTankProperties[] tankProperties = capability.getTankProperties();
        if (tankProperties != null && tankProperties.length > 0) {
            final IFluidTankProperties tankProperty = tankProperties[0];
            if (tankProperty != null) {
                final FluidStack contents = tankProperty.getContents();
                if (contents != null) {
                    final Fluid fluid = contents.getFluid();
                    if (fluid != null) {
                        final ResourceLocation still = fluid.getStill();
                        if (still != null) {


                            textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                            final TextureAtlasSprite sprite = blocksTextureMap.getAtlasSprite(still.toString());

                            final float amountStored = 8 * contents.amount / 1000.0f;

                            FluidRenderHelper.drawRepeatedFluidSprite(
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
        textureManager.bindTexture(Gui.fluid_barrel_texture);

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
