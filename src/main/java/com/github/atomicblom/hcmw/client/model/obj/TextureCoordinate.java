package com.github.atomicblom.hcmw.client.model.obj;

import javax.vecmath.Vector3f;

public class TextureCoordinate
{
    public float u, v, w;

    public TextureCoordinate()
    {
        this(0.0f, 0.0f, 1.0f);
    }

    public TextureCoordinate(float[] data)
    {
        this(data[0], data[1], data[2]);
    }

    public TextureCoordinate(Vector3f data)
    {
        this(data.x, data.y, data.z);
    }

    public TextureCoordinate(float u, float v, float w)
    {
        this.u = u;
        this.v = v;
        this.w = w;
    }

    public Vector3f getData()
    {
        return new Vector3f(this.u, this.v, this.w);
    }

    public static TextureCoordinate[] getDefaultUVs()
    {
        TextureCoordinate[] texCoords = new TextureCoordinate[4];
        texCoords[0] = new TextureCoordinate(0.0f, 0.0f, 1.0f);
        texCoords[1] = new TextureCoordinate(1.0f, 0.0f, 1.0f);
        texCoords[2] = new TextureCoordinate(1.0f, 1.0f, 1.0f);
        texCoords[3] = new TextureCoordinate(0.0f, 1.0f, 1.0f);
        return texCoords;
    }
}
