package com.github.atomicblom.hcmw.client.model.obj;

import net.minecraft.util.ResourceLocation;

/**
 * Created by codew on 23/12/2016.
 */
@SuppressWarnings("serial")
public class UVsOutOfBoundsException extends RuntimeException {
    public ResourceLocation modelLocation;

    public UVsOutOfBoundsException(ResourceLocation modelLocation) {
        super(String.format("Model '%s' has UVs ('vt') out of bounds 0-1! The missing model will be used instead. Support for UV processing will be added to the OBJ loader in the future.", modelLocation));
        this.modelLocation = modelLocation;
    }
}
