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
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * Represents the camera
 * @author Volchkov Andrey
 */
public class Camera {
    
    //position and direction vectors
    private final Vector3f wup;
    private final Vector3f position;
    private final Vector3f up;
    private final Vector3f direction;
    private final Vector3f right;
    
    //rotation
    private double yav;
    private double pitch;
    
    //view matrix
    private final Matrix4f view;
    //projection matrix
    private final Matrix4f projection;

    public Camera(float fov, float apect,
            float zNear, float zFar, Vector3f position, 
            Vector3f up, Vector3f direction) {
        this.position = position;
        this.wup = up;
        this.direction = direction;
        this.right = new Vector3f();
        this.up = new Vector3f();
        //setup matrixes
        this.view = new Matrix4f();
        this.projection = new Matrix4f()
                .perspective(fov, apect, zNear, zFar);     
        updateVectors();
    }
    
    private void updateVectors() {
        new Vector3f().set(
                (float)(Math.cos(this.pitch) * Math.cos(this.yav)), 
                (float)Math.sin(this.pitch),
                (float)(Math.cos(this.pitch) * Math.sin(this.yav)))
                .normalize(direction);
        direction.cross(wup, right);
        right.cross(direction, up);        
    }
    
    public void moveForward(float speed) {
        Vector3f moved = new Vector3f(direction)
                .mul(speed);
        position.add(moved);
    }
    
    public void moveBackward(float speed) {
        Vector3f moved = new Vector3f(direction)
                .mul(speed);
        position.sub(moved);
    }
    
    public void moveLeft(float speed) {
        Vector3f moved = new Vector3f(right)
                .mul(speed);
        position.sub(moved);
    }
    
    public void moveRight(float speed) {
        Vector3f moved = new Vector3f(right)
                .mul(speed);
        position.add(moved);        
    }
    
    public void rotate(double yav, double pitch) {
        this.yav =  Math.toRadians(yav);
        this.pitch = Math.toRadians(pitch);
        updateVectors();
    }
        
    
    public void apply(ProgramGLSL program) {
                
        Vector3f center = new Vector3f(position)
                .add(direction);        
        view.identity()
            .setLookAt(position, center, up);
        
        program.setParam("projectionMatrix", projection)
               .setParam("viewMatrix", view)
               .setParam("viewPos", position);
    }
    
    public void applySkybox(ProgramGLSL program) {
                
        Vector3f center = new Vector3f(position)
                .add(direction);        
        view.identity()
            .setLookAt(position, center, up);
        Matrix3f notMovedView = new Matrix3f();
        view.get3x3(notMovedView);
        program.setParam("projectionMatrix", projection)
               .setParam("viewMatrix", new Matrix4f(notMovedView));
    }

    @Override
    public String toString() {
        return "Camera{" + "position=" + position + ", up=" + up + ", direction=" + direction + ", dirUpNormal=" + right + ", view=" + view + ", projection=" + projection + '}';
    }
    
    
}


