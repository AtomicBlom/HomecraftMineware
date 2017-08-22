package com.github.atomicblom.hcmw.client.model.obj;

import javax.vecmath.Vector4f;

public class Material
{
    public static final String WHITE_NAME = "OBJModel.White.Texture.Name";
    public static final String DEFAULT_NAME = "OBJModel.Default.Texture.Name";
    private Vector4f color;
    private Texture texture = Texture.WHITE;
    private String name = DEFAULT_NAME;

    public Material()
    {
        this(new Vector4f(1f, 1f, 1f, 1f));
    }

    public Material(Vector4f color)
    {
        this(color, Texture.WHITE, WHITE_NAME);
    }

    public Material(Texture texture)
    {
        this(new Vector4f(1f, 1f, 1f, 1f), texture, DEFAULT_NAME);
    }

    public Material(Vector4f color, Texture texture, String name)
    {
        this.color = color;
        this.texture = texture;
        this.name = name != null ? name : DEFAULT_NAME;
    }

    public void setName(String name)
    {
        this.name = name != null ? name : DEFAULT_NAME;
    }

    public String getName()
    {
        return this.name;
    }

    public void setColor(Vector4f color)
    {
        this.color = color;
    }

    public Vector4f getColor()
    {
        return this.color;
    }

    public void setTexture(Texture texture)
    {
        this.texture = texture;
    }

    public Texture getTexture()
    {
        return this.texture;
    }

    public boolean isWhite()
    {
        return this.texture.equals(Texture.WHITE);
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder(String.format("%nMaterial:%n"));
        builder.append(String.format("    Name: %s%n", this.name));
        builder.append(String.format("    Color: %s%n", this.color.toString()));
        builder.append(String.format("    Is White: %b%n", this.isWhite()));
        return builder.toString();
    }
}
