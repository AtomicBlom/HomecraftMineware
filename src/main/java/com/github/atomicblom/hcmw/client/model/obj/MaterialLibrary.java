package com.github.atomicblom.hcmw.client.model.obj;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLLog;
import javax.vecmath.Vector4f;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;

public class MaterialLibrary
{
    private static final Pattern WHITE_SPACE = Pattern.compile("\\s+");
    private Set<String> unknownMaterialCommands = new HashSet<String>();
    private Map<String, Material> materials = new HashMap<String, Material>();
    private Map<String, Group> groups = new HashMap<String, Group>();
    private InputStreamReader mtlStream;
    private BufferedReader mtlReader;

//        private float[] minUVBounds = new float[] {0.0f, 0.0f};
//        private float[] maxUVBounds = new float[] {1.0f, 1.0f};

    public MaterialLibrary()
    {
        this.groups.put(Group.DEFAULT_NAME, new Group(Group.DEFAULT_NAME, null));
        Material def = new Material();
        def.setName(Material.DEFAULT_NAME);
        this.materials.put(Material.DEFAULT_NAME, def);
    }

    public MaterialLibrary makeLibWithReplacements(ImmutableMap<String, String> replacements)
    {
        Map<String, Material> mats = new HashMap<String, Material>();
        for (Map.Entry<String, Material> e : this.materials.entrySet())
        {
            // key for the material name, with # added if missing
            String keyMat = e.getKey();
            if(!keyMat.startsWith("#")) keyMat = "#" + keyMat;
            // key for the texture name, with ".png" stripped and # added if missing
            String keyTex = e.getValue().getTexture().getPath();
            if(keyTex.endsWith(".png")) keyTex = keyTex.substring(0, keyTex.length() - ".png".length());
            if(!keyTex.startsWith("#")) keyTex = "#" + keyTex;
            if (replacements.containsKey(keyMat))
            {
                Texture currentTexture = e.getValue().getTexture();
                Texture replacementTexture = new Texture(replacements.get(keyMat), currentTexture.getPosition(), currentTexture.getScale(), currentTexture.getRotation());
                Material replacementMaterial = new Material(e.getValue().getColor(), replacementTexture, e.getValue().getName());
                mats.put(e.getKey(), replacementMaterial);
            }
            else if (replacements.containsKey(keyTex))
            {
                Texture currentTexture = e.getValue().getTexture();
                Texture replacementTexture = new Texture(replacements.get(keyTex), currentTexture.getPosition(), currentTexture.getScale(), currentTexture.getRotation());
                Material replacementMaterial = new Material(e.getValue().getColor(), replacementTexture, e.getValue().getName());
                mats.put(e.getKey(), replacementMaterial);
            }
            else
            {
                mats.put(e.getKey(), e.getValue());
            }
        }
        MaterialLibrary ret = new MaterialLibrary();
        ret.unknownMaterialCommands = this.unknownMaterialCommands;
        ret.materials = mats;
        ret.groups = this.groups;
        ret.mtlStream = this.mtlStream;
        ret.mtlReader = this.mtlReader;
//            ret.minUVBounds = this.minUVBounds;
//            ret.maxUVBounds = this.maxUVBounds;
        return ret;
    }

//        public float[] getMinUVBounds()
//        {
//            return this.minUVBounds;
//        }

//        public float[] getMaxUVBounds()
//        {
//            return this.maxUVBounds;
//        }

//        public void setUVBounds(float minU, float maxU, float minV, float maxV)
//        {
//            this.minUVBounds[0] = minU;
//            this.maxUVBounds[0] = maxU;
//            this.minUVBounds[1] = minV;
//            this.maxUVBounds[1] = maxV;
//        }

    public Map<String, Group> getGroups()
    {
        return this.groups;
    }

    public List<Group> getGroupsContainingFace(Face f)
    {
        List<Group> groupList = Lists.newArrayList();
        for (Group g : this.groups.values())
        {
            if (g.getFaces().contains(f)) groupList.add(g);
        }
        return groupList;
    }

    public void changeMaterialColor(String name, int color)
    {
        Vector4f colorVec = new Vector4f();
        colorVec.w = (color >> 24 & 255) / 255;
        colorVec.x = (color >> 16 & 255) / 255;
        colorVec.y = (color >> 8 & 255) / 255;
        colorVec.z = (color & 255) / 255;
        this.materials.get(name).setColor(colorVec);
    }

    public Material getMaterial(String name)
    {
        return this.materials.get(name);
    }

    public ImmutableList<String> getMaterialNames()
    {
        return ImmutableList.copyOf(this.materials.keySet());
    }

    public void parseMaterials(IResourceManager manager, String path, ResourceLocation from) throws IOException
    {
        this.materials.clear();
        boolean hasSetTexture = false;
        boolean hasSetColor = false;
        String domain = from.getResourceDomain();
        if (!path.contains("/"))
            path = from.getResourcePath().substring(0, from.getResourcePath().lastIndexOf("/") + 1) + path;
        mtlStream = new InputStreamReader(manager.getResource(new ResourceLocation(domain, path)).getInputStream(), StandardCharsets.UTF_8);
        mtlReader = new BufferedReader(mtlStream);

        String currentLine = "";
        Material material = new Material();
        material.setName(Material.WHITE_NAME);
        material.setTexture(Texture.WHITE);
        this.materials.put(Material.WHITE_NAME, material);
        this.materials.put(Material.DEFAULT_NAME, new Material(Texture.WHITE));

        for (;;)
        {
            currentLine = mtlReader.readLine();
            if (currentLine == null) break;
            currentLine.trim();
            if (currentLine.isEmpty() || currentLine.startsWith("#")) continue;

            String[] fields = WHITE_SPACE.split(currentLine, 2);
            String key = fields[0];
            String data = fields[1];

            if (key.equalsIgnoreCase("newmtl"))
            {
                hasSetColor = false;
                hasSetTexture = false;
                material = new Material();
                material.setName(data);
                this.materials.put(data, material);
            }
            else if (key.equalsIgnoreCase("Ka") || key.equalsIgnoreCase("Kd") || key.equalsIgnoreCase("Ks"))
            {
                if (key.equalsIgnoreCase("Kd") || !hasSetColor)
                {
                    String[] rgbStrings = WHITE_SPACE.split(data, 3);
                    Vector4f color = new Vector4f(Float.parseFloat(rgbStrings[0]), Float.parseFloat(rgbStrings[1]), Float.parseFloat(rgbStrings[2]), 1.0f);
                    hasSetColor = true;
                    material.setColor(color);
                }
                else
                {
                    FMLLog.log.info("OBJModel: A color has already been defined for material '{}' in '{}'. The color defined by key '{}' will not be applied!", material.getName(), new ResourceLocation(domain, path).toString(), key);
                }
            }
            else if (key.equalsIgnoreCase("map_Ka") || key.equalsIgnoreCase("map_Kd") || key.equalsIgnoreCase("map_Ks"))
            {
                if (key.equalsIgnoreCase("map_Kd") || !hasSetTexture)
                {
                    if (data.contains(" "))
                    {
                        String[] mapStrings = WHITE_SPACE.split(data);
                        String texturePath = mapStrings[mapStrings.length - 1];
                        Texture texture = new Texture(texturePath);
                        hasSetTexture = true;
                        material.setTexture(texture);
                    }
                    else
                    {
                        Texture texture = new Texture(data);
                        hasSetTexture = true;
                        material.setTexture(texture);
                    }
                }
                else
                {
                    FMLLog.log.info("OBJModel: A texture has already been defined for material '{}' in '{}'. The texture defined by key '{}' will not be applied!", material.getName(), new ResourceLocation(domain, path).toString(), key);
                }
            }
            else if (key.equalsIgnoreCase("d") || key.equalsIgnoreCase("Tr"))
            {
                //d <-optional key here> float[0.0:1.0, 1.0]
                //Tr r g b OR Tr spectral map file OR Tr xyz r g b (CIEXYZ colorspace)
                String[] splitData = WHITE_SPACE.split(data);
                float alpha = Float.parseFloat(splitData[splitData.length - 1]);
                material.getColor().setW(alpha);
            }
            else
            {
                if (!unknownMaterialCommands.contains(key))
                {
                    unknownMaterialCommands.add(key);
                    FMLLog.log.debug("OBJLoader.MaterialLibrary: key '{}' (model: '{}') is not currently supported, skipping", key, new ResourceLocation(domain, path));
                }
            }
        }
    }

    public Map<String, Material> getMaterials()
    {
        return materials;
    }
}
