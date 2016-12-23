package com.github.atomicblom.hcmw.client.model.obj;

import com.google.common.base.Optional;
import net.minecraftforge.common.model.IModelPart;
import net.minecraftforge.common.model.TRSRTransformation;

import java.util.LinkedHashSet;
import java.util.List;

/**
 * Created by codew on 23/12/2016.
 */
@Deprecated
public class Group implements IModelPart {
    public static final String DEFAULT_NAME = "OBJModel.Default.Element.Name";
    public static final String ALL = "OBJModel.Group.All.Key";
    public static final String ALL_EXCEPT = "OBJModel.Group.All.Except.Key";
    private String name = DEFAULT_NAME;
    LinkedHashSet<Face> faces = new LinkedHashSet<Face>();
    public float[] minUVBounds = new float[]{0.0f, 0.0f};
    public float[] maxUVBounds = new float[]{1.0f, 1.0f};

//        public float[] minUVBounds = new float[] {0.0f, 0.0f};
//        public float[] maxUVBounds = new float[] {1.0f, 1.0f};

    public Group(String name, LinkedHashSet<Face> faces) {
        this.name = name != null ? name : DEFAULT_NAME;
        this.faces = faces == null ? new LinkedHashSet<Face>() : faces;
    }

    public LinkedHashSet<Face> applyTransform(Optional<TRSRTransformation> transform) {
        LinkedHashSet<Face> faceSet = new LinkedHashSet<Face>();
        for (Face f : this.faces) {
//                if (minUVBounds != null && maxUVBounds != null) f.normalizeUVs(minUVBounds, maxUVBounds);
            faceSet.add(f.bake(transform.or(TRSRTransformation.identity())));
        }
        return faceSet;
    }

    public String getName() {
        return this.name;
    }

    public LinkedHashSet<Face> getFaces() {
        return this.faces;
    }

    public void setFaces(LinkedHashSet<Face> faces) {
        this.faces = faces;
    }

    public void addFace(Face face) {
        this.faces.add(face);
    }

    public void addFaces(List<Face> faces) {
        this.faces.addAll(faces);
    }
}
