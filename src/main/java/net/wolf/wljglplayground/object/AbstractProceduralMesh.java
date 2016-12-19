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

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import static net.wolf.wljglplayground.object.Mesh3d.FLOAT_SIZE;
import static net.wolf.wljglplayground.object.Mesh3d.TRIANGLE_VERTEXES;
import net.wolf.wljglplayground.util.Deletable;
import org.joml.Vector2f;
import org.joml.Vector3f;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import org.lwjgl.system.MemoryUtil;
import static org.lwjgl.system.MemoryUtil.memAllocFloat;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.system.MemoryUtil.memFree;

/**
 * Abstract class for procedural meshes
 * @author Volchkov Andrey
 */
public abstract class AbstractProceduralMesh implements Mesh3d {
    
    protected int indexCount;
    
    //OpenGL objects id;
    protected int vao;
    protected int vbo;
    protected int ebo;


    @Override
    public void init() {
        Vector3f[] vertices = generateVertices();

        Vector3f[] normales = generateNormals(vertices);

        Vector2f[] uvs = generateUVs(vertices.length);
        
        int[] triangles = createTrianglesIndexes(vertices.length);
        indexCount = triangles.length;
        
        int vertLnegth = vertices.length;
        // vertices + normals + uvs
        int stride = TRIANGLE_VERTEXES + TRIANGLE_VERTEXES + UV_VERTEXES; 
        
        //create off-heap buffer        
        FloatBuffer dataVerts = memAllocFloat(vertLnegth * stride);
        for (int i = 0; i < vertLnegth; ++i) {
            dataVerts.put(vertices[i].x());
            dataVerts.put(vertices[i].y());
            dataVerts.put(vertices[i].z());
            
            dataVerts.put(normales[i].x());
            dataVerts.put(normales[i].y());
            dataVerts.put(normales[i].z());
            
            dataVerts.put(uvs[i].x());
            dataVerts.put(uvs[i].y());            
        }        
        dataVerts.flip();
     
        IntBuffer dataTriangles = MemoryUtil.memAllocInt(triangles.length);
        dataTriangles.put(triangles);
        dataTriangles.flip();    
        
         //generate GL buffers
        vao = glGenVertexArrays();
        vbo = glGenBuffers();
        ebo = glGenBuffers();

        glBindVertexArray(vao);        
        //send data to GPU
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, dataVerts, GL_STATIC_DRAW);
        
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, dataTriangles, GL_STATIC_DRAW);

        //setup vertexes
        glVertexAttribPointer(0, TRIANGLE_VERTEXES, GL_FLOAT, false, stride * FLOAT_SIZE, 0L);
        glEnableVertexAttribArray(0);
        //setup normals
        glVertexAttribPointer(1, TRIANGLE_VERTEXES, GL_FLOAT, false, stride * FLOAT_SIZE, TRIANGLE_VERTEXES * FLOAT_SIZE);
        glEnableVertexAttribArray(1);
        //setup UVs
        glVertexAttribPointer(2, UV_VERTEXES, GL_FLOAT, false, stride * FLOAT_SIZE, TRIANGLE_VERTEXES * 2 * FLOAT_SIZE);
        glEnableVertexAttribArray(2);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
        
        memFree(dataVerts);
        memFree(dataTriangles);
    }

    @Override
    public void draw() {
        glBindVertexArray(vao);
            glDrawElements(GL_TRIANGLES, indexCount, GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);
    }

    @Override
    public void delete() {
        if (vao != 0) {
            glDeleteVertexArrays(vao);
        }
        if (vbo != 0) {
            glDeleteVertexArrays(vbo);
        }
        if (ebo != 0) {
            glDeleteVertexArrays(vbo);
        }
        vbo = vao = ebo = 0;
    }

    protected abstract Vector3f[] generateVertices();

    protected abstract Vector3f[] generateNormals(Vector3f[] vertices);

    protected abstract Vector2f[] generateUVs(int length);
    
    protected abstract int[] createTrianglesIndexes(int length);
    
}
