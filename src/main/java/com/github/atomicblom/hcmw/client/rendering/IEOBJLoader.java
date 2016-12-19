/*
 * This code is heavily borrowed from Immersive Engineering by BluSunrize
 *
 * It has been used with his permission
 *
 * Copyright 2016 BluSunrize, All rights Reserved
 */

package com.github.atomicblom.hcmw.client.rendering;

import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.client.model.obj.OBJModel;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class IEOBJLoader implements ICustomModelLoader
{
    private IResourceManager manager;
    private final Set<String> enabledDomains = new HashSet<>();
    private final Map<ResourceLocation, IEOBJModel> cache = new HashMap<>();
    private final Map<ResourceLocation, Exception> errors = new HashMap<>();
    public static IEOBJLoader instance = new IEOBJLoader();

    public void addDomain(String domain)
    {
        enabledDomains.add(domain.toLowerCase());
    }

    @Override
    public boolean accepts(ResourceLocation modelLocation)
    {
        return enabledDomains.contains(modelLocation.getResourceDomain()) && modelLocation.getResourcePath().endsWith(".obj.ie");
    }

    @Override
    public IModel loadModel(ResourceLocation modelLocation) throws Exception
    {
        ResourceLocation file = new ResourceLocation(modelLocation.getResourceDomain(), modelLocation.getResourcePath());
        if(!cache.containsKey(file))
        {
            IModel model = OBJLoader.INSTANCE.loadModel(modelLocation);
            if(model instanceof OBJModel)
            {
                IEOBJModel ieobj = new IEOBJModel(((OBJModel)model).getMatLib(), file);
                cache.put(modelLocation, ieobj);
            }
        }
        IEOBJModel model = cache.get(file);
        if(model == null)
            return ModelLoaderRegistry.getMissingModel();
        return model;
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager)
    {
        this.manager = resourceManager;
        cache.clear();
        errors.clear();
    }
}