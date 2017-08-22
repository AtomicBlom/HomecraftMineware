/*
 * Minecraft Forge
 * Copyright (c) 2016.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation version 2.1
 * of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package com.github.atomicblom.hcmw.client.model.obj;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.fml.common.FMLLog;
import java.util.*;
import java.util.function.Function;

public class OBJModel implements IModel
{
    //private Gson GSON = new GsonBuilder().create();
    private MaterialLibrary matLib;
    private final ResourceLocation modelLocation;
    private CustomData customData;

    public OBJModel(MaterialLibrary matLib, ResourceLocation modelLocation)
    {
        this(matLib, modelLocation, new CustomData());
    }

    public OBJModel(MaterialLibrary matLib, ResourceLocation modelLocation, CustomData customData)
    {
        this.matLib = matLib;
        this.modelLocation = modelLocation;
        this.customData = customData;
    }

    @Override
    public Collection<ResourceLocation> getTextures()
    {
        Iterator<Material> materialIterator = this.matLib.getMaterials().values().iterator();
        List<ResourceLocation> textures = Lists.newArrayList();
        while (materialIterator.hasNext())
        {
            Material mat = materialIterator.next();
            ResourceLocation textureLoc = new ResourceLocation(mat.getTexture().getPath());
            if (!textures.contains(textureLoc) && !mat.isWhite())
                textures.add(textureLoc);
        }
        return textures;
    }

    @Override
    public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter)
    {
        ImmutableMap.Builder<String, TextureAtlasSprite> builder = ImmutableMap.builder();
        builder.put(ModelLoader.White.LOCATION.toString(), ModelLoader.White.INSTANCE);
        TextureAtlasSprite missing = bakedTextureGetter.apply(new ResourceLocation("missingno"));
        for (Map.Entry<String, Material> e : matLib.getMaterials().entrySet())
        {
            if (e.getValue().getTexture().getTextureLocation().getResourcePath().startsWith("#"))
            {
                FMLLog.log.fatal("OBJLoader: Unresolved texture '{}' for obj model '{}'", e.getValue().getTexture().getTextureLocation().getResourcePath(), modelLocation);
                builder.put(e.getKey(), missing);
            }
            else
            {
                builder.put(e.getKey(), bakedTextureGetter.apply(e.getValue().getTexture().getTextureLocation()));
            }
        }
        builder.put("missingno", missing);
        return new OBJBakedModel(this, state, format, builder.build());
    }

    public MaterialLibrary getMatLib()
    {
        return this.matLib;
    }

    @Override
    public IModel process(ImmutableMap<String, String> customData)
    {
        OBJModel ret = new OBJModel(this.matLib, this.modelLocation, new CustomData(this.customData, customData));
        return ret;
    }

    @Override
    public IModel retexture(ImmutableMap<String, String> textures)
    {
        OBJModel ret = new OBJModel(this.matLib.makeLibWithReplacements(textures), this.modelLocation, this.customData);
        return ret;
    }

    public CustomData getCustomData()
    {
        return customData;
    }

    public ResourceLocation getModelLocation()
    {
        return modelLocation;
    }
}