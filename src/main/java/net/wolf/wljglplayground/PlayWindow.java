/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.wolf.wljglplayground;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.scene.Scene;
import net.wolf.wljglplayground.input.CameraCursorHandler;
import net.wolf.wljglplayground.input.CameraKeyHandler;
import net.wolf.wljglplayground.input.CursorMoveNotifyAdapter;
import net.wolf.wljglplayground.input.KeyNotifyAdapter;
import org.lwjgl.glfw.GLFW;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MAJOR;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MINOR;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_DISABLED;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_SAMPLES;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetFramebufferSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWaitEvents;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWVidMode;
import static org.lwjgl.system.MemoryUtil.NULL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwSetInputMode;

/**
 * Our main window class for playing with OpenGL
 *
 * @author Volchkov Andrey
 */
public class PlayWindow {

    private static final Logger LOG = LoggerFactory.getLogger(PlayWindow.class);

    public static class Builder {
        
        private int width = 1024;
        private int height = 768;
        private int antialiasing = 0;
        private int glMajorVer = 3;
        private int glMinorVer = 3;
        private final PlayScene scene;

        public Builder(PlayScene scene) {
            this.scene = scene;
        }

        public Builder withWidth(int width) {
            this.width = width;
            return this;
        }

        public Builder withHeight(int height) {
            this.height = height;
            return this;
        }

        public Builder withAntialiasing(int antialiasing) {
            this.antialiasing = antialiasing;
            return this;
        }

        public Builder withGLVersion(int glMajorVer, int glMinorVer) {
            this.glMajorVer = glMajorVer;
            this.glMinorVer = glMinorVer;
            return this;
        }

        public PlayWindow build() {
            if (glMajorVer < 3 || glMinorVer < 3) {
                throw new IllegalStateException("Only OpenGL 3.3 or higher!");
            }
            if (glMajorVer > 4 && glMinorVer > 5) {
                throw new IllegalStateException("Illegal version of OpenGL!");
            }
            return new PlayWindow(scene, width, height, antialiasing, glMajorVer, glMinorVer);
        }
    }
    
    public static Builder builder(PlayScene scene) {
        return new Builder(scene);
    }

    //window size
    private int width;
    private int height;
    //over
    private final int antialiasing;
    private final int glMajorVer;
    private final int glMinorVer;
    private final PlayScene scene;

    //window hanlder
    private long window;

    //render    
    private final ExecutorService exec = Executors.newFixedThreadPool(1);
    private PlayRenderer playRenderer;

    //sync
    private final Object lock = new Object();

    //callbacks
    private final KeyNotifyAdapter keyNotifier = new KeyNotifyAdapter();
    private final CursorMoveNotifyAdapter moveNotifyAdapter = new CursorMoveNotifyAdapter();
    private final GLFWErrorCallback errorCallback = GLFWErrorCallback.createPrint();
    private final GLFWFramebufferSizeCallback fsCallback = new GLFWFramebufferSizeCallback() {
        @Override
        public void invoke(long window, int w, int h) {
            if (w > 0 && h > 0) {
                playRenderer.setViewPortSize(w, h);
                width = w;
                height = h;
            }
        }
    };

    public PlayWindow(PlayScene scene, int width, int height, int antialiasing, 
            int glMajorVer, int glMinorVer) {
        this.scene = scene;
        this.width = width;
        this.height = height;
        this.antialiasing = antialiasing;
        this.glMajorVer = glMajorVer;
        this.glMinorVer = glMinorVer;
    }

    /**
     * Initialization GLFW window and creation window
     */
    public void init() {

        LOG.info("Initializing GLFW...");
        glfwSetErrorCallback(errorCallback);

        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_SAMPLES, antialiasing);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, glMajorVer);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, glMinorVer);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

        System.out.println("!!!!!!!!!!!!!!!!!!" + width);
        System.out.println("!!!!!!!!!!!!!!!!!!" + height);
        window = glfwCreateWindow(width, height, "PlayGround ;)", NULL, NULL);
        if (window == NULL) {
            throw new IllegalStateException("Failed to create the GLFW window");
        }

        glfwSetKeyCallback(window, keyNotifier);
        GLFW.glfwSetCursorPosCallback(window, moveNotifyAdapter);
        glfwSetFramebufferSizeCallback(window, fsCallback);

        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(window, (vidmode.width() - width) / 2, (vidmode.height() - height) / 2);
        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);

        CameraKeyHandler cameraKeyHandler = new CameraKeyHandler();
        keyNotifier.registerHandler(cameraKeyHandler);
        CameraCursorHandler cameraCursorHandler = new CameraCursorHandler(0.05);
        moveNotifyAdapter.registerHandler(cameraCursorHandler);

        playRenderer = new PlayRenderer(window, lock, scene);
        playRenderer.setViewPortSize(width, height);
        playRenderer.setCameraMover(cameraKeyHandler);
        playRenderer.setCameraCursorHandler(cameraCursorHandler);

        LOG.info("Show GLFW window.");
        glfwShowWindow(window);

    }

    void start() {
        try {
            LOG.info("Starting renderer thread...");
            exec.execute(playRenderer);

            //dispatch window events
            while (!glfwWindowShouldClose(window)) {
                glfwWaitEvents();
            }
            LOG.info("Shutdown...");
            playRenderer.destroy();

            synchronized (lock) {
                glfwDestroyWindow(window);
            }
            LOG.debug("Main window destroyed...");

            exec.shutdown();
            playRenderer.close();
            LOG.debug("Render destroyed...");

            keyNotifier.free();
            fsCallback.free();
            LOG.debug("Callbacks destroyed...");
        } finally {
            glfwTerminate();
            errorCallback.free();
            LOG.info("All done...");
        }
    }

}
