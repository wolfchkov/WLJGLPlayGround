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
public class BoxMesh extends AbstractProceduralMesh {

    private final float length;
    private final float width;
    private final float height;

    public BoxMesh(float length, float width, float height) {
        this.length = length;
        this.width = width;
        this.height = height;
    }

    /**
     * Create cube 1.0f
     */
    public BoxMesh() {
        this(1.0f, 1.0f, 1.0f);
    }

    @Override
    protected Vector3f[] generateVertices() {
        //create vertices
        Vector3f p0 = new Vector3f(-length * 0.5f, -width * 0.5f, height * 0.5f);
        Vector3f p1 = new Vector3f(length * 0.5f, -width * 0.5f, height * 0.5f);
        Vector3f p2 = new Vector3f(length * 0.5f, -width * 0.5f, -height * 0.5f);
        Vector3f p3 = new Vector3f(-length * 0.5f, -width * 0.5f, -height * 0.5f);
        Vector3f p4 = new Vector3f(-length * 0.5f, width * 0.5f, height * 0.5f);
        Vector3f p5 = new Vector3f(length * 0.5f, width * 0.5f, height * 0.5f);
        Vector3f p6 = new Vector3f(length * 0.5f, width * 0.5f, -height * 0.5f);
        Vector3f p7 = new Vector3f(-length * 0.5f, width * 0.5f, -height * 0.5f);
        Vector3f[] vertices = new Vector3f[]{
            //bottom
            p0, p1, p2, p3,
            //left
            p7, p4, p0, p3,
            //front
            p4, p5, p1, p0,
            //back
            p6, p7, p3, p2,
            //right
            p5, p6, p2, p1,
            //top
            p7, p6, p5, p4
        };
        return vertices;
    }

    @Override
    protected Vector3f[] generateNormals(Vector3f[] vertices) {
        //create normales
        Vector3f up = new Vector3f(0.0f, 1.0f, 0.0f);
        Vector3f down = new Vector3f(0.0f, -1.0f, 0.0f);
        Vector3f front = new Vector3f(0.0f, 0.0f, 1.0f);
        Vector3f back = new Vector3f(0.0f, 0.0f, -1.0f);
        Vector3f left = new Vector3f(-1.0f, 0.0f, 0.0f);
        Vector3f right = new Vector3f(1.0f, 0.0f, 0.0f);

        Vector3f[] normales = new Vector3f[]{
            //bottom
            down, down, down, down,
            //left
            left, left, left, left,
            //front
            front, front, front, front,
            //back
            back, back, back, back,
            //right
            right, right, right, right,
            //top
            up, up, up, up
        };
        return normales;
    }

    @Override
    protected Vector2f[] generateUVs(int length) {
        Vector2f c00 = new Vector2f(0.0f, 0.0f);
        Vector2f c10 = new Vector2f(1.0f, 0.0f);
        Vector2f c01 = new Vector2f(0.0f, 1.0f);
        Vector2f c11 = new Vector2f(1.0f, 1.0f);

        Vector2f[] uvs = new Vector2f[]{
            //bottom
            c11, c01, c00, c10,
            //left
            c11, c01, c00, c10,
            //front
            c11, c01, c00, c10,
            //back
            c11, c01, c00, c10,
            //right
            c11, c01, c00, c10,
            //top
            c11, c01, c00, c10
        };
        return uvs;
    }

    @Override
    protected int[] createTrianglesIndexes(int length) {
        return new int[]{
            // Bottom
            3, 1, 0,
            3, 2, 1,
            // Left
            3 + 4 * 1, 1 + 4 * 1, 0 + 4 * 1,
            3 + 4 * 1, 2 + 4 * 1, 1 + 4 * 1,
            // Front
            3 + 4 * 2, 1 + 4 * 2, 0 + 4 * 2,
            3 + 4 * 2, 2 + 4 * 2, 1 + 4 * 2,
            // Back
            3 + 4 * 3, 1 + 4 * 3, 0 + 4 * 3,
            3 + 4 * 3, 2 + 4 * 3, 1 + 4 * 3,
            // Right
            3 + 4 * 4, 1 + 4 * 4, 0 + 4 * 4,
            3 + 4 * 4, 2 + 4 * 4, 1 + 4 * 4,
            // Top
            3 + 4 * 5, 1 + 4 * 5, 0 + 4 * 5,
            3 + 4 * 5, 2 + 4 * 5, 1 + 4 * 5
        };
    }

}
