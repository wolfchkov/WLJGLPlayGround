/*
 * The MIT License
 *
 * Copyright 2016 Volchkov Andrey.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package net.wolf.wljglplayground.object;

import org.joml.Vector2f;
import org.joml.Vector3f;

/**
 *
 * @author Volchkov Andrey
 */
public class SphereMesh extends AbstractProceduralMesh {

    private final float radius;
    private final int lngC;
    private final int latC;
    

    public SphereMesh(float radius, int lngC, int latC) {
        this.radius = radius;
        this.lngC = lngC;
        this.latC = latC;
    }

    @Override
    protected Vector3f[] generateVertices() {
        //generate Vertices
        Vector3f[] vertices = new Vector3f[(lngC + 1) * latC + 2];
        float pi = (float) Math.PI;
        float pi2 = pi * 2.f;

        vertices[0] = new Vector3f(0.f, 1.f, 0.0f).mul(radius);
        for (int lat = 0; lat < latC; lat++) {
            float a1 = pi * (float) (lat + 1) / (float) (latC + 1);
            float sin1 = (float) Math.sin(a1);
            float cos1 = (float) Math.cos(a1);

            for (int lon = 0; lon <= lngC; lon++) {
                float a2 = pi2 * (float) (lon == lngC ? 0 : lon) / (float) lngC;
                float sin2 = (float) Math.sin(a2);
                float cos2 = (float) Math.cos(a2);

                vertices[lon + lat * (lngC + 1) + 1] = new Vector3f(sin1 * cos2, cos1, sin1 * sin2)
                        .mul(radius);
            }
        }
        vertices[vertices.length - 1] = new Vector3f(0.f, 1.f, 0.0f).mul(-radius);
        
        return vertices;
    }
    
    @Override
    protected Vector3f[] generateNormals(Vector3f[] vertices) {
         //generate normals
        Vector3f[] normales = new Vector3f[vertices.length];
        for (int n = 0; n < vertices.length; n++) {
            normales[n] = new Vector3f(vertices[n]).normalize();
        }
        return normales;
    }
    
    @Override
    protected Vector2f[] generateUVs(int vertices) {
        //generate UV coords
        Vector2f[] uvs = new Vector2f[vertices];
        uvs[0] = new Vector2f(0.f, 1.f); // up
        uvs[uvs.length - 1] = new Vector2f().zero();
        for (int lat = 0; lat < latC; lat++) {
            for (int lon = 0; lon <= lngC; lon++) {
                uvs[lon + lat * (lngC + 1) + 1]
                        = new Vector2f((float) lon / (float) lngC,
                                1.0f - (float) (lat + 1) / (float) (latC + 1));
            }
        }
        
        return uvs;
    }
    
    @Override
    protected int[] createTrianglesIndexes(int vertices) {
        int faces = vertices;
        int triangles = faces * 2;
        indexCount = triangles * 3;
        int[] indexes = new int[indexCount];


        //top
        int i = 0;
        for (int lon = 0; lon < lngC; lon++) {
            indexes[i++] = lon + 2;
            indexes[i++] = lon + 1;
            indexes[i++] = 0;
        }

        //middle
        for (int lat = 0; lat < latC - 1; lat++) {
            for (int lon = 0; lon < lngC; lon++) {
                int current = lon + lat * (lngC + 1) + 1;
                int next = current + lngC + 1;

                indexes[i++] = current;
                indexes[i++] = current + 1;
                indexes[i++] = next + 1;

                indexes[i++] = current;
                indexes[i++] = next + 1;
                indexes[i++] = next;
            }
        }

        //bottom
        for (int lon = 0; lon < lngC; lon++) {
            indexes[i++] = vertices - 1;
            indexes[i++] = vertices - (lon + 2) - 1;
            indexes[i++] = vertices - (lon + 1) - 1;
        }
        
        return indexes;
    }

}
