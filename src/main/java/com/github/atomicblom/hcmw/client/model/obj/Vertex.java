package com.github.atomicblom.hcmw.client.model.obj;

import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

public class Vertex
{
    private Vector4f position;
    private Normal normal;
    private TextureCoordinate texCoord;
    private Material material = new Material();

    public Vertex(Vector4f position, Material material)
    {
        this.position = position;
        this.material = material;
    }

    public void setPos(Vector4f position)
    {
        this.position = position;
    }

    public Vector4f getPos()
    {
        return this.position;
    }

    public Vector3f getPos3()
    {
        return new Vector3f(this.position.x, this.position.y, this.position.z);
    }

    public boolean hasNormal()
    {
        return this.normal != null;
    }

    public void setNormal(Normal normal)
    {
        this.normal = normal;
    }

    public Normal getNormal()
    {
        return this.normal;
    }

    public boolean hasTextureCoordinate()
    {
        return this.texCoord != null;
    }

    public void setTextureCoordinate(TextureCoordinate texCoord)
    {
        this.texCoord = texCoord;
    }

    public TextureCoordinate getTextureCoordinate()
    {
        return this.texCoord;
    }

//        public boolean hasNormalizedUVs()
//        {
//            return this.texCoord.u >= 0.0f && this.texCoord.u <= 1.0f && this.texCoord.v >= 0.0f && this.texCoord.v <= 1.0f;
//        }

    public void setMaterial(Material material)
    {
        this.material = material;
    }

    public Material getMaterial()
    {
        return this.material;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("v:%n"));
        builder.append(String.format("    position: %s %s %s%n", position.x, position.y, position.z));
        builder.append(String.format("    material: %s %s %s %s %s%n", material.getName(), material.getColor().x, material.getColor().y, material.getColor().z, material.getColor().w));
        return builder.toString();
    }
}
