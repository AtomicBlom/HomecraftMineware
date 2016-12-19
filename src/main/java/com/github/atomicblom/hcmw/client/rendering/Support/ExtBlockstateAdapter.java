package com.github.atomicblom.hcmw.client.rendering.Support;

import com.github.atomicblom.hcmw.client.rendering.IOBJModelCallback;
import com.google.common.collect.ImmutableSet;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.BlockRenderLayer;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import java.util.Set;

public class ExtBlockstateAdapter
{
    public static final Set<Object> ONLY_OBJ_CALLBACK = ImmutableSet.of(IOBJModelCallback.PROPERTY);
    final IExtendedBlockState state;
    final BlockRenderLayer layer;
    final String extraCacheKey;
    final Set<Object> ignoredProperties;
    public ExtBlockstateAdapter(IExtendedBlockState s, BlockRenderLayer l, Set<Object> ignored)
    {
        state = s;
        layer = l;
        ignoredProperties = ignored;
        if (s.getUnlistedNames().contains(IOBJModelCallback.PROPERTY))
        {
            IOBJModelCallback callback = s.getValue(IOBJModelCallback.PROPERTY);
            if (callback!=null)
                extraCacheKey = callback.getClass()+";"+callback.getCacheKey(state);
            else
                extraCacheKey = null;
        }
        else
            extraCacheKey = null;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == this)
            return true;
        if (!(obj instanceof ExtBlockstateAdapter))
            return false;
        ExtBlockstateAdapter o = (ExtBlockstateAdapter) obj;
        if (o.layer!=layer)
            return false;
        if (extraCacheKey==null^o.extraCacheKey==null)
            return false;
        if (extraCacheKey!=null&&!extraCacheKey.equals(o.extraCacheKey))
            return false;
        for(IProperty<?> i : state.getPropertyKeys())
        {
            if(!o.state.getProperties().containsKey(i))
                return false;
            if (ignoredProperties.contains(i))
                continue;
            Object valThis = state.getValue(i);
            Object valOther = o.state.getValue(i);
            if(valThis==null&&valOther==null)
                continue;
            else if(valOther == null || !valOther.equals(state.getValue(i)))
                return false;
        }
        for(IUnlistedProperty<?> i : state.getUnlistedNames())
        {
            if(!o.state.getUnlistedProperties().containsKey(i))
                return false;
            if (ignoredProperties.contains(i))
                continue;
            Object valThis = state.getValue(i);
            Object valOther = o.state.getValue(i);
            if(valThis==null&&valOther==null)
                continue;
            else if (valOther == null || !valOther.equals(valThis))
                return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int val = layer==null?0:layer.ordinal();
        final int prime = 31;
        if (extraCacheKey!=null)
            val = val*prime+extraCacheKey.hashCode();
        for (IProperty<?> n : state.getPropertyKeys())
            if (!ignoredProperties.contains(n))
            {
                Object o = state.getValue(n);
                val = prime * val + (o == null ? 0 : o.hashCode());
            }
        for (IUnlistedProperty<?> n : state.getUnlistedNames())
            if (!ignoredProperties.contains(n))
            {
                Object o = state.getValue(n);
                val = prime * val + (o == null ? 0 : o.hashCode());
            }
        return val;
    }
}
