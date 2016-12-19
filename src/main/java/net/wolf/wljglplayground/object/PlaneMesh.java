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

import java.util.Arrays;
import org.joml.Vector2f;
import org.joml.Vector3f;

/**
 *
 * @author Volchkov Andrey
 */
public class PlaneMesh extends AbstractProceduralMesh {

    private final float length;
    private final float width;

    public PlaneMesh() {
        length = 1.0f;
        width = 1.0f;
    }

    public PlaneMesh(float length, float width) {
        this.length = length;
        this.width = width;
    }
    

    @Override
    protected Vector3f[] generateVertices() {
        return new Vector3f[] {
            new Vector3f(-length * 0.5f, 0.0f, -width * 0.5f),
            new Vector3f(length * 0.5f, 0.0f, -width * 0.5f),
            new Vector3f(length * 0.5f, 0.0f, width * 0.5f),
            new Vector3f(-length * 0.5f, 0.0f, width * 0.5f),
        };
    }

    @Override
    protected Vector3f[] generateNormals(Vector3f[] vertices) {
        Vector3f[] normals = new Vector3f[vertices.length];
        Arrays.fill(normals, new Vector3f(0.0f, 1.0f, 0.0f)); // up
        return normals;
    }

    @Override
    protected Vector2f[] generateUVs(int length) {
        return new Vector2f[] {
            new Vector2f(0.0f, 1.0f),
            new Vector2f(1.0f, 1.0f),
            new Vector2f(1.0f, 0.0f),
            new Vector2f(0.0f, 0.0f)
        };
    }

    @Override
    protected int[] createTrianglesIndexes(int length) {
        return new int[] {
            0, 1, 2,
            2, 3, 0
        };
    }

}

