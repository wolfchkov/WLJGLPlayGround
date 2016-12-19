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

import java.util.Objects;
import net.wolf.wljglplayground.shader.ProgramGLSL;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

/**
 *
 * @author Volchkov Andrey
 */
public class Simple3dObject implements Object3d {

    private final Mesh3d mesh;
    private final Matrix4f modelMatrix;
    private final Material material;

    public Simple3dObject(Mesh3d mesh, Vector3f position, Material material) {
        this.mesh = mesh;
        this.modelMatrix = new Matrix4f()
                .identity()
                .translate(position);
        this.material = material;
    }

    public Simple3dObject(Mesh3d mesh, Matrix4f modelMatrix, Material material) {
        this.mesh = mesh;
        this.modelMatrix = new Matrix4f(modelMatrix);
        this.material = material;
    }

    @Override
    public void init() {
    }

    /**
     * Drawing this object
     *
     * @param program - GLSL program for setting unifrom params
     */
    @Override
    public void draw(final ProgramGLSL program) {
        applyMatrixes(program);
        if (Objects.nonNull(material)) {
            material.apply(program);
            material.use();
        }
        mesh.draw();
    }
    
    private void applyMatrixes(ProgramGLSL program) {
        program.setParam("modelMatrix", modelMatrix);
        Matrix4f normalMatrix = new Matrix4f(modelMatrix)
                .invert()
                .transpose();
        Matrix3f normMatrix = new Matrix3f(normalMatrix);
        program.setParam("normalMatrix", normMatrix);

    }    

    @Override
    public void delete() {
        mesh.delete();
    }

    public void translate(Vector3f pos) {
        modelMatrix.translate(pos);
    }

    public void setPosition(Vector3f pos) {
        modelMatrix.setTranslation(pos);
    }

    public void rotate(Quaternionf quat) {
        modelMatrix.rotate(quat);
    }

    public void rotateX(float angle) {
        modelMatrix.rotateX((float) Math.toRadians(angle));
    }

    public void rotateY(float angle) {
        modelMatrix.rotateY((float) Math.toRadians(angle));
    }

    public void rotateZ(float angle) {
        modelMatrix.rotateZ((float) Math.toRadians(angle));
    }

}
