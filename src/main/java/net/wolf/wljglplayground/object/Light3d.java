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

import net.wolf.wljglplayground.shader.ProgramGLSL;
import net.wolf.wljglplayground.util.ColorUtils;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector3fc;

/**
 *
 * @author Volchkov Andrey
 */
public class Light3d implements Object3d {

    private final Mesh3d mesh;
    private final Vector3f color;
    private final Vector3f position;
    private final Vector3f ambient;
    private final Vector3f diffuse;
    private final Vector3f specular;
    private final Matrix4f modelMatrix = new Matrix4f();

    public Light3d(Mesh3d mesh, Vector3f position,
            Vector3f color, float ambient, float diffuse, float specular) {
        this.mesh = mesh;
        this.position = position;
        this.color = color;
        this.ambient = new Vector3f();
        this.diffuse = new Vector3f();
        this.specular = new Vector3f();
        color.mul(ambient, this.ambient);
        color.mul(diffuse, this.diffuse);
        color.mul(specular, this.specular);
    }
    
    public Light3d(Mesh3d mesh, Vector3f position,
            String color, float ambient, float diffuse, float specular) {
        this(mesh, position, ColorUtils.parseRGBColor(color), ambient, diffuse, specular);
    }
    

    /**
     * Drawing this object
     *
     * @param program - GLSL program for setting unifrom params
     */
    @Override
    public void draw(final ProgramGLSL program) {
        program.setParam("lightColor", color);
        modelMatrix.identity().translate(position);
        program.setParam("modelMatrix", modelMatrix);
        mesh.draw();
    }
    
    public void apply(final ProgramGLSL program) {
        program.setParam("light.position", position);
        program.setParam("light.ambient", ambient);
        program.setParam("light.diffuse", diffuse);
        program.setParam("light.specular", specular);
    }

    @Override
    public void delete() {
        mesh.delete();
    }

    public void setPosition(Vector3fc pos) {
        position.set(pos);
    }

    public void setX(float x) {
        position.x = x;
    }
    
    public void setY(float y) {
        position.y = y;
    }
    
    public void setZ(float z) {
        position.z = z;
    }

    @Override
    public void init() {
        mesh.delete();
    }

}

