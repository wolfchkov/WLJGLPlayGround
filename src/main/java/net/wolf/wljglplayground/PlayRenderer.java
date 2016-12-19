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
package net.wolf.wljglplayground;

import java.io.IOException;
import java.util.List;
import net.wolf.wljglplayground.shader.ShaderGLSL;
import net.wolf.wljglplayground.shader.ProgramGLSL;
import java.util.Objects;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import net.wolf.wljglplayground.input.CameraCursorHandler;
import net.wolf.wljglplayground.input.CameraKeyHandler;
import net.wolf.wljglplayground.object.Camera;
import net.wolf.wljglplayground.object.BoxMesh;
import net.wolf.wljglplayground.object.ColorMaterial;
import net.wolf.wljglplayground.object.Light3d;
import net.wolf.wljglplayground.object.Mesh3d;
import net.wolf.wljglplayground.object.PlaneMesh;
import net.wolf.wljglplayground.object.Simple3dObject;
import net.wolf.wljglplayground.object.Skybox;
import net.wolf.wljglplayground.object.SphereMesh;
import net.wolf.wljglplayground.object.TexMaterial;
import net.wolf.wljglplayground.object.Texture;
import net.wolf.wljglplayground.util.Timer;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLUtil;
import org.lwjgl.system.Callback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Volchkov Andrey
 */
public class PlayRenderer implements Runnable {
    
    private static final Logger LOG = LoggerFactory.getLogger(PlayRenderer.class);
    

    private final long window;
    private final Object lock;
    private final PlayScene scene;
    
    private volatile boolean destroyed;    
    private Callback debugProc;
     
    private int height;
    private int width;
    
    private CameraKeyHandler cameraMover;
    private CameraCursorHandler cameraCursorHandler;
    

    public PlayRenderer(long window, Object lock, PlayScene scene) {
        this.window = window;
        this.lock = lock;
        this.scene = scene;
    }
    
    public void setViewPortSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void setCameraMover(CameraKeyHandler cameraMover) {
        this.cameraMover = cameraMover;
    }

    public void setCameraCursorHandler(CameraCursorHandler cameraCursorHandler) {
        this.cameraCursorHandler = cameraCursorHandler;
    }
    
    public void destroy() {
        LOG.debug("Renderer destroying");
        this.destroyed = true;
    }
        
    public void init() {
        glfwMakeContextCurrent(window);
        
        GL.createCapabilities();
        
        debugProc = GLUtil.setupDebugMessageCallback();
     
        String glVersionString = glGetString(GL_VERSION);
        LOG.info(glVersionString);        
        
        glfwSwapInterval(0);
        
        glEnable(GL_DEPTH_TEST);
        glViewport(0, 0, width, height);
        
        scene.initScene(width, height);
    }
        
    
    @Override
    public void run() {
        init();        

        
        Timer timer = Timer.getTimer();
        timer.start();
        while (!destroyed) {
            float deltaTime = timer.elapsedTime();
            cameraMover.apply(scene.getCamera(), 1.0f * deltaTime);
            cameraCursorHandler.apply(scene.getCamera());

            //set viewport
            glViewport(0, 0, width, height);
            //clear buffers
            glClearColor(0.3f, 0.3f, 0.3f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            
            //draw scene
            scene.drawFrame(deltaTime, timer.currentTime());
            
            synchronized (lock) {
                if (!destroyed) {
                    glfwSwapBuffers(window);
                }
            }
            //ts = System.nanoTime() - ts;
            //LOG.debug("Frame time {}", TimeUnit.NANOSECONDS.toMicros(ts));
        }
        
        scene.destroyScene();
    }



     public void close() {
         if (Objects. nonNull(debugProc)) {
             debugProc.free();
         }
     }


}


