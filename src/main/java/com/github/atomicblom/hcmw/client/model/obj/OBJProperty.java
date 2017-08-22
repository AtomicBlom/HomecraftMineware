package com.github.atomicblom.hcmw.client.model.obj;

import net.minecraftforge.common.property.IUnlistedProperty;

@Deprecated
public enum OBJProperty implements IUnlistedProperty<OBJState>
{
    INSTANCE;
    @Override
    public String getName()
    {
        return "OBJProperty";
    }

    @Override
    public boolean isValid(OBJState value)
    {
        return value instanceof OBJState;
    }

    @Override
    public Class<OBJState> getType()
    {
        return OBJState.class;
    }

    @Override
    public String valueToString(OBJState value)
    {
        return value.toString();
    }
}
