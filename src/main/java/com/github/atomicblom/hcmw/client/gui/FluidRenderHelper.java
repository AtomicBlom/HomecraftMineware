package com.github.atomicblom.hcmw.client.gui;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

@SuppressWarnings("UtilityClass")
final class FluidRenderHelper
{
    static void drawRepeatedFluidSprite(TextureAtlasSprite sprite, float x, float y, float w, float h)
    {
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        final int iW = sprite.getIconWidth();
        final int iH = sprite.getIconHeight();
        if(iW > 0 && iH > 0) {
            drawRepeatedSprite(bufferBuilder, x, y, w, h, iW, iH, sprite.getMinU(), sprite.getMaxU(), sprite.getMinV(), sprite.getMaxV());
        }

        tessellator.draw();
    }

    @SuppressWarnings({"NumericCastThatLosesPrecision", "TooBroadScope"})
    private static void drawRepeatedSprite(BufferBuilder bufferBuilder, float x, float y, float w, float h, int iconWidth, int iconHeight, float uMin, float uMax, float vMin, float vMax)
    {
        final int iterMaxW = (int) (w / iconWidth);
        final int iterMaxH = (int) (h / iconHeight);
        final float leftoverW = w % iconWidth;
        final float leftoverH = h % iconHeight;
        final float leftoverWf = leftoverW / iconWidth;
        final float leftoverHf = leftoverH / iconHeight;
        final float iconUDif = uMax - uMin;
        final float iconVDif = vMax - vMin;
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

    private static void drawTexturedRect(BufferBuilder bufferBuilder, float x, float y, float w, float h, double... uv)
    {
        bufferBuilder.pos(x, y + h, 0).tex(uv[0], uv[3]).endVertex();
        bufferBuilder.pos(x + w, y + h, 0).tex(uv[1], uv[3]).endVertex();
        bufferBuilder.pos(x + w, y, 0).tex(uv[1], uv[2]).endVertex();
        bufferBuilder.pos(x, y, 0).tex(uv[0], uv[2]).endVertex();
    }

    private FluidRenderHelper() {}
}
