package com.github.atomicblom.hcmw.client.model.obj;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.commons.lang3.StringUtils;

import javax.vecmath.Vector4f;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created by codew on 23/12/2016.
 */
public class Parser {
    private static final Pattern WHITE_SPACE = Pattern.compile("\\s+");
    private static Set<String> unknownObjectCommands = new HashSet<String>();
    private MaterialLibrary materialLibrary = new MaterialLibrary();
    private IResourceManager manager;
    private InputStreamReader objStream;
    private BufferedReader objReader;
    private ResourceLocation objFrom;

    private List<String> groupList = Lists.newArrayList();
    private List<Vertex> vertices = Lists.newArrayList();
    private List<Normal> normals = Lists.newArrayList();
    private List<TextureCoordinate> texCoords = Lists.newArrayList();

    public Parser(IResource from, IResourceManager manager) throws IOException {
        this.manager = manager;
        this.objFrom = from.getResourceLocation();
        this.objStream = new InputStreamReader(from.getInputStream(), Charsets.UTF_8);
        this.objReader = new BufferedReader(objStream);
    }

    public List<String> getElements() {
        return this.groupList;
    }

    private float[] parseFloats(String[] data) // Helper converting strings to floats
    {
        float[] ret = new float[data.length];
        for (int i = 0; i < data.length; i++)
            ret[i] = Float.parseFloat(data[i]);
        return ret;
    }

    //Partial reading of the OBJ format. Documentation taken from http://paulbourke.net/dataformats/obj/
    public OBJModel parse() throws IOException {
        String currentLine = "";
        Material material = new Material();
        material.setName(Material.DEFAULT_NAME);
        int usemtlCounter = 0;
        int lineNum = 0;

        for (; ; ) {
            lineNum++;
            currentLine = objReader.readLine();
            if (currentLine == null) break;
            currentLine.trim();
            if (currentLine.isEmpty() || currentLine.startsWith("#")) continue;

            try {
                String[] fields = WHITE_SPACE.split(currentLine, 2);
                String key = fields[0];
                String data = fields[1];
                String[] splitData = WHITE_SPACE.split(data);

                if (key.equalsIgnoreCase("mtllib")) {
                    this.materialLibrary.parseMaterials(manager, data, objFrom);
                } else if (key.equalsIgnoreCase("usemtl")) {
                    material = this.materialLibrary.materials.get(data);
                    usemtlCounter++;
                } else if (key.equalsIgnoreCase("v")) // Vertices: x y z [w] - w Defaults to 1.0
                {
                    float[] coords = parseFloats(splitData);
                    Vector4f pos = new Vector4f(coords[0], coords[1], coords[2], coords.length == 4 ? coords[3] : 1.0F);
                    this.vertices.add(new Vertex(pos, material));
                } else if (key.equalsIgnoreCase("vn")) // Vertex normals: x y z
                {
                    this.normals.add(new Normal(parseFloats(splitData)));
                } else if (key.equalsIgnoreCase("vt")) // Vertex Textures: u [v] [w] - v/w Defaults to 0
                {
                    float[] coords = parseFloats(splitData);
                    TextureCoordinate texCoord = new TextureCoordinate(coords[0],
                            coords.length >= 2 ? coords[1] : 0.0F,
                            coords.length >= 3 ? coords[2] : 0.0F);
                    if (texCoord.u < 0.0f || texCoord.u > 1.0f || texCoord.v < 0.0f || texCoord.v > 1.0f)
                        throw new UVsOutOfBoundsException(this.objFrom);
                    this.texCoords.add(texCoord);
                } else if (key.equalsIgnoreCase("f")) // Face Elements: f v1[/vt1][/vn1] ...
                {
                    if (splitData.length > 4)
                        FMLLog.warning("OBJModel.Parser: found a face ('f') with more than 4 vertices, only the first 4 of these vertices will be rendered!");

                    List<Vertex> v = Lists.newArrayListWithCapacity(splitData.length);

                    for (int i = 0; i < splitData.length; i++) {
                        String[] pts = splitData[i].split("/");

                        int vert = Integer.parseInt(pts[0]);
                        Integer texture = pts.length < 2 || StringUtils.isEmpty(pts[1]) ? null : Integer.parseInt(pts[1]);
                        Integer normal = pts.length < 3 || StringUtils.isEmpty(pts[2]) ? null : Integer.parseInt(pts[2]);

                        vert = vert < 0 ? this.vertices.size() - 1 : vert - 1;
                        Vertex newV = new Vertex(new Vector4f(this.vertices.get(vert).getPos()), this.vertices.get(vert).getMaterial());

                        if (texture != null)
                            newV.setTextureCoordinate(this.texCoords.get(texture < 0 ? this.texCoords.size() - 1 : texture - 1));
                        if (normal != null)
                            newV.setNormal(this.normals.get(normal < 0 ? this.normals.size() - 1 : normal - 1));

                        v.add(newV);
                    }

                    Vertex[] va = v.toArray(new Vertex[v.size()]);

                    Face face = new Face(va, material.name);
                    if (usemtlCounter < this.vertices.size()) {
                        for (Vertex ver : face.getVertices()) {
                            ver.setMaterial(material);
                        }
                    }

                    if (groupList.isEmpty()) {
                        if (this.materialLibrary.getGroups().containsKey(Group.DEFAULT_NAME)) {
                            this.materialLibrary.getGroups().get(Group.DEFAULT_NAME).addFace(face);
                        } else {
                            Group def = new Group(Group.DEFAULT_NAME, null);
                            def.addFace(face);
                            this.materialLibrary.getGroups().put(Group.DEFAULT_NAME, def);
                        }
                    } else {
                        for (String s : groupList) {
                            if (this.materialLibrary.getGroups().containsKey(s)) {
                                this.materialLibrary.getGroups().get(s).addFace(face);
                            } else {
                                Group e = new Group(s, null);
                                e.addFace(face);
                                this.materialLibrary.getGroups().put(s, e);
                            }
                        }
                    }
                } else if (key.equalsIgnoreCase("g") || key.equalsIgnoreCase("o")) {
                    groupList.clear();
                    if (key.equalsIgnoreCase("g")) {
                        String[] splitSpace = data.split(" ");
                        for (String s : splitSpace)
                            groupList.add(s);
                    } else {
                        groupList.add(data);
                    }
                } else {
                    if (!unknownObjectCommands.contains(key)) {
                        unknownObjectCommands.add(key);
                        FMLLog.info("OBJLoader.Parser: command '%s' (model: '%s') is not currently supported, skipping. Line: %d '%s'", key, objFrom, lineNum, currentLine);
                    }
                }
            } catch (RuntimeException e) {
                throw new RuntimeException(String.format("OBJLoader.Parser: Exception parsing line #%d: `%s`", lineNum, currentLine), e);
            }
        }

        return new OBJModel(this.materialLibrary, this.objFrom);
    }
}
