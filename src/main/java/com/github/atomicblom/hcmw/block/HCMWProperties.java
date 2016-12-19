package com.github.atomicblom.hcmw.block;

import net.minecraftforge.common.property.IUnlistedProperty;
import java.util.HashMap;

/**
 * Created by codew on 18/12/2016.
 */
public class HCMWProperties
{
    public static final IUnlistedProperty<HashMap> OBJ_TEXTURE_REMAP = new IUnlistedProperty<HashMap>()
    {
        @Override
        public String getName()
        {
            return "obj_texture_remap";
        }
        @Override
        public boolean isValid(HashMap value)
        {
            return true;
        }
        @Override
        public Class<HashMap> getType() {
            return HashMap.class;
        }
        @Override
        public String valueToString(HashMap value)
        {
            return value.toString();
        }
    };
}
