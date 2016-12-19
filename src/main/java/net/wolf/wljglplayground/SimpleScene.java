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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import net.wolf.wljglplayground.object.BoxMesh;
import net.wolf.wljglplayground.object.Camera;
import net.wolf.wljglplayground.object.ColorMaterial;
import net.wolf.wljglplayground.object.Light3d;
import net.wolf.wljglplayground.object.Mesh3d;
import net.wolf.wljglplayground.object.PlaneMesh;
import net.wolf.wljglplayground.object.Simple3dObject;
import net.wolf.wljglplayground.object.Skybox;
import net.wolf.wljglplayground.object.SphereMesh;
import net.wolf.wljglplayground.object.TexMaterial;
import net.wolf.wljglplayground.object.Texture;
import net.wolf.wljglplayground.shader.ProgramGLSL;
import net.wolf.wljglplayground.shader.ShaderGLSL;
import net.wolf.wljglplayground.util.ColorUtils;
import net.wolf.wljglplayground.util.Deletable;
import org.joml.Vector3f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Volchkov Andrey
 */
public class SimpleScene implements PlayScene {
    
    private static final Logger LOG = LoggerFactory.getLogger(PlayRenderer.class);

    private static final Vector3f[] cubePositionsColors = {
        new Vector3f(1.5f, 0.5f, 2f),   ColorUtils.parseRGBColor("#00ffff"),
        new Vector3f(-1.5f, -0.5f, 1f), ColorUtils.parseRGBColor("#ff00ff"),
        new Vector3f(-1f, 1f, 0f),      ColorUtils.parseRGBColor("#6600cc"),
        new Vector3f(1f, -1f, -1f),     ColorUtils.parseRGBColor("#66ff33"),
        new Vector3f(1f, 1f, 1f),       ColorUtils.parseRGBColor("#663300")};

    private Camera camera;

    private ProgramGLSL skyboxProgram;
    private ProgramGLSL objectsProgram;
    private ProgramGLSL objectsTexProgram;
    private ProgramGLSL lightProgram;
    //private ProgramGLSL normalsDebug;

    //for delete all objects when destroy scene
    private final List<Deletable> objToDelete = new ArrayList<>();
    private Light3d light;

    private Skybox skybox;

    private Simple3dObject plane;
    private List<Simple3dObject> cubes;
    private Simple3dObject sphere3d;



    /**
     * Load shaders and link program
     *
     * @param shaderFiles - args of shader files
     * @return ProgramGLSL
     */
    private ProgramGLSL loadProgram(String... shaderFiles) {
        try {
            ShaderGLSL[] shaders = new ShaderGLSL[shaderFiles.length];

            for (int i = 0; i < shaderFiles.length; ++i) {
                shaders[i] = new ShaderGLSL(shaderFiles[i]);
            }

            ProgramGLSL program = new ProgramGLSL(shaders);
            program.link();

            objToDelete.add(program);
            return program;

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private List<Simple3dObject> createCubes(final Mesh3d cubeMesh) {
        Vector3f specular = new Vector3f(1.0f); //white
        return IntStream.range(0, cubePositionsColors.length / 2)
                .mapToObj((i) -> {
                    Vector3f c = cubePositionsColors[i * 2 + 1];
                    Simple3dObject cube3d = new Simple3dObject(cubeMesh,
                            cubePositionsColors[i * 2],
                            new ColorMaterial(c.mul(0.3f), c, specular, 128, (i%2 != 0)));
                    cube3d.init();
                    objToDelete.add(cube3d);
                    return cube3d;
                }).collect(Collectors.toList());
    }

    @Override
    public void initScene(int width, int height) {
        //create camera
        camera = new Camera(45.f, (float) width / (float) height, 0.1f, 100.f,
                new Vector3f(0.f, 0.f, 5.f),
                new Vector3f(0.f, 1.f, 0.f),
                new Vector3f(0.f, 0.f, -1.f));
        
        //create all shaders
        skyboxProgram = loadProgram("/skybox.vert", "/skybox.frag");        
        objectsProgram = loadProgram("/objectShader.vert", "/objectShaderM.frag");
        objectsTexProgram = loadProgram("/objectShader.vert", "/objectShaderTex.frag");
        lightProgram = loadProgram("/objectShader.vert", "/lightShader.frag");
        //normalsDebug = loadProgram("/debugNormals.vert", "/debugNormals.frag",
        //       "/debugNormals.geom");

        
        //create skybox
        skybox = new Skybox(
                "/textures/skybox/land/px.png",
                "/textures/skybox/land/nx.png",
                "/textures/skybox/land/py.png",
                "/textures/skybox/land/ny.png",                
                "/textures/skybox/land/pz.png",
                "/textures/skybox/land/nz.png"                
        );
        try {
            skybox.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        
        //create gound-plane
        final PlaneMesh planeMesh = new PlaneMesh(20.0f, 15.0f);
        planeMesh.init();
        plane = new Simple3dObject(planeMesh,
                new Vector3f(0.f, -2.f, 0.f),
                new TexMaterial(
                        Texture.create("/textures/flour_diffuse.jpg"),
                        Texture.create("/textures/flour_specular.jpg"), 64.0f));
        objToDelete.add(plane);

        //create cubes
        final BoxMesh cubeMesh = new BoxMesh();
        cubeMesh.init();
        cubes = createCubes(cubeMesh);
        
        //light
        final SphereMesh lsphere = new SphereMesh(0.1f, 20, 20);
        lsphere.init();
        light = new Light3d(lsphere, new Vector3f(-3.f, 3.f, 3.f),
                "#FFFFFF", 0.5f, 1.0f, 1.0f);
        objToDelete.add(light);

        //create sphere
        final SphereMesh sphereMesh = new SphereMesh(0.5f, 30, 30);
        sphereMesh.init();
        sphere3d = new Simple3dObject(sphereMesh,
                new Vector3f(0.f, 0.f, 0.f),
                new ColorMaterial("#0F004D", "#3300FF", "#FFFFFF", 128, true));
        objToDelete.add(sphere3d);
    }

    @Override
    public void drawFrame(float deltaTime, float currTime) {

        //draw skybox
        skyboxProgram.use();
        camera.applySkybox(skyboxProgram);            
        skybox.draw(skyboxProgram);
            
        //draw light
        lightProgram.use();
        light.setX((float) (-3.0f - Math.sin(currTime) * 3));
        camera.apply(lightProgram);
        light.draw(lightProgram);

        //draw cubes
        objectsProgram.use();
        camera.apply(objectsProgram);
        light.apply(objectsProgram);
        for (Simple3dObject cube : cubes) {
            cube.draw(objectsProgram);
        }        
        //draw shpere
        sphere3d.draw(objectsProgram);

        //draw plane
        objectsTexProgram.use();
        camera.apply(objectsTexProgram);
        light.apply(objectsTexProgram);
        plane.draw(objectsTexProgram);

    }

    @Override
    public Camera getCamera() {
        return camera;
    }

    @Override
    public void destroyScene() {
        objToDelete.stream()
                .forEach(Deletable::delete);
    }

    public static void main(String[] args) {

        try {
            PlayScene scene = new SimpleScene();
            PlayWindow playWindow = PlayWindow.builder(scene)
                    .withWidth(1650)
                    .withHeight(1050)
                    .withAntialiasing(4)
                    .build();
            
            playWindow.init();
            playWindow.start();
        } catch (Exception e) {
            LOG.error("Scene error", e);
        }
        System.out.println("Destory");
        //Runtime.getRuntime().halt(0);
    }
}
