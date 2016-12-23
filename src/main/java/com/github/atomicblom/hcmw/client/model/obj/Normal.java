package com.github.atomicblom.hcmw.client.model.obj;

import javax.vecmath.Vector3f;

/**
 * Created by codew on 23/12/2016.
 */
public class Normal {
    public float x, y, z;

    public Normal() {
        this(0.0f, 0.0f, 0.0f);
    }

    public Normal(float[] data) {
        this(data[0], data[1], data[2]);
    }

    public Normal(Vector3f vector3f) {
        this(vector3f.x, vector3f.y, vector3f.z);
    }

    public Normal(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3f getData() {
        return new Vector3f(this.x, this.y, this.z);
    }
}
