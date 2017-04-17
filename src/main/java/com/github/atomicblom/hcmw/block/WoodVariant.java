package com.github.atomicblom.hcmw.block;

import com.google.common.collect.Maps;
import net.minecraft.util.IStringSerializable;
import java.util.Map;

public enum WoodVariant implements IStringSerializable
{
    OAK("oak"),
    IRON("iron"),
    ACACIA("acacia"),
    BIRCH("birch"),
    JUNGLE("jungle"),
    SPRUCE("spruce"),
    DARK_OAK("dark_oak"),
    ;

    private final String name;
    private static final Map<String, WoodVariant> nameToVariantMap = Maps.newHashMap();

    static
    {
        for (final WoodVariant woodVariant : values())
        {
            nameToVariantMap.put(woodVariant.name, woodVariant);
        }
    }

    WoodVariant(String name)
    {

        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public static WoodVariant fromName(String name) {
        return nameToVariantMap.get(name);
    }
}
