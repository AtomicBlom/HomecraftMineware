package com.github.atomicblom.hcmw.client.model.obj;

import net.minecraftforge.common.model.TRSRTransformation;

import javax.vecmath.Matrix3f;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

/**
 * Created by codew on 23/12/2016.
 */
public class Face {
    Vertex[] verts = new Vertex[4];
    //        private Normal[] norms = new Normal[4];
//        private TextureCoordinate[] texCoords = new TextureCoordinate[4];
    private String materialName = Material.DEFAULT_NAME;
    private boolean isTri = false;

    public Face(Vertex[] verts) {
        this(verts, Material.DEFAULT_NAME);
    }

    public Face(Vertex[] verts, String materialName) {
        this.verts = verts != null && verts.length > 2 ? verts : null;
        setMaterialName(materialName);
        checkData();
    }

//        public Face(Vertex[] verts, Normal[] norms)
//        {
//            this(verts, norms, null);
//        }

//        public Face(Vertex[] verts, TextureCoordinate[] texCoords)
//        {
//            this(verts, null, texCoords);
//        }

//        public Face(Vertex[] verts, Normal[] norms, TextureCoordinate[] texCoords)
//        {
//            this(verts, norms, texCoords, Material.DEFAULT_NAME);
//        }

//        public Face(Vertex[] verts, Normal[] norms, TextureCoordinate[] texCoords, String materialName)
//        {
//            this.verts = verts != null && verts.length > 2 ? verts : null;
//            this.norms = norms != null && norms.length > 2 ? norms : null;
//            this.texCoords = texCoords != null && texCoords.length > 2 ? texCoords : null;
//            setMaterialName(materialName);
//            checkData();
//        }

    private void checkData() {
        if (this.verts != null && this.verts.length == 3) {
            this.isTri = true;
            this.verts = new Vertex[]{this.verts[0], this.verts[1], this.verts[2], this.verts[2]};
        }
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName != null && !materialName.isEmpty() ? materialName : this.materialName;
    }

    public String getMaterialName() {
        return this.materialName;
    }

    public boolean isTriangles() {
        return isTri;
    }

    public boolean setVertices(Vertex[] verts) {
        if (verts == null) return false;
        else this.verts = verts;
        checkData();
        return true;
    }

    public Vertex[] getVertices() {
        return this.verts;
    }

//        public boolean areUVsNormalized()
//        {
//            for (Vertex v : this.verts)
//                if (!v.hasNormalizedUVs())
//                    return false;
//            return true;
//        }

//        public void normalizeUVs(float[] min, float[] max)
//        {
//            if (!this.areUVsNormalized())
//            {
//                for (int i = 0; i < this.verts.length; i++) {
//                    TextureCoordinate texCoord = this.verts[i].getTextureCoordinate();
//                    min[0] = texCoord.u < min[0] ? texCoord.u : min[0];
//                    max[0] = texCoord.u > max[0] ? texCoord.u : max[0];
//                    min[1] = texCoord.v < min[1] ? texCoord.v : min[1];
//                    max[1] = texCoord.v > max[1] ? texCoord.v : max[1];
//                }
//
//                for (Vertex v : this.verts) {
//                    v.texCoord.u = (v.texCoord.u - min[0]) / (max[0] - min[0]);
//                    v.texCoord.v = (v.texCoord.v - min[1]) / (max[1] - max[1]);
//                }
//            }
//        }

    public Face bake(TRSRTransformation transform) {
        Matrix4f m = transform.getMatrix();
        Matrix3f mn = null;
        Vertex[] vertices = new Vertex[verts.length];
//            Normal[] normals = norms != null ? new Normal[norms.length] : null;
//            TextureCoordinate[] textureCoords = texCoords != null ? new TextureCoordinate[texCoords.length] : null;

        for (int i = 0; i < verts.length; i++) {
            Vertex v = verts[i];
//                Normal n = norms != null ? norms[i] : null;
//                TextureCoordinate t = texCoords != null ? texCoords[i] : null;

            Vector4f pos = new Vector4f(v.getPos()), newPos = new Vector4f();
            pos.w = 1;
            m.transform(pos, newPos);
            vertices[i] = new Vertex(newPos, v.getMaterial());

            if (v.hasNormal()) {
                if (mn == null) {
                    mn = new Matrix3f();
                    m.getRotationScale(mn);
                    mn.invert();
                    mn.transpose();
                }
                Vector3f normal = new Vector3f(v.getNormal().getData()), newNormal = new Vector3f();
                mn.transform(normal, newNormal);
                newNormal.normalize();
                vertices[i].setNormal(new Normal(newNormal));
            }

            if (v.hasTextureCoordinate()) vertices[i].setTextureCoordinate(v.getTextureCoordinate());
            else v.setTextureCoordinate(TextureCoordinate.getDefaultUVs()[i]);

            //texCoords TODO
//                if (t != null) textureCoords[i] = t;
        }
        return new Face(vertices, this.materialName);
    }

    public Normal getNormal() {
        Vector3f a = this.verts[2].getPos3();
        a.sub(this.verts[0].getPos3());
        Vector3f b = this.verts[3].getPos3();
        b.sub(this.verts[1].getPos3());
        a.cross(a, b);
        a.normalize();
        return new Normal(a);
    }
}
