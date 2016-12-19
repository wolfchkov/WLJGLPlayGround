/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.wolf.wljglplayground.input;

import net.wolf.wljglplayground.object.Camera;
import org.lwjgl.glfw.GLFW;

/**
 *
 * @author Volchkov Andrey
 */
public class CameraKeyHandler implements KeyHandler {
    
    private final boolean keysState[] = new boolean[GLFW.GLFW_KEY_LAST];

    @Override
    public void handleKey(int key, int scancode, KeyAction action) {        
        keysState[key] = action != KeyAction.RELEASE;      
    }
    
    
    public void apply(Camera camera, float speed) {
        if (keysState[GLFW.GLFW_KEY_W]) {
            camera.moveForward(speed);
        }
        if (keysState[GLFW.GLFW_KEY_S]) {
            camera.moveBackward(speed);
        }
        if (keysState[GLFW.GLFW_KEY_A]) {
            camera.moveLeft(speed);
        }
        if (keysState[GLFW.GLFW_KEY_D]) {
            camera.moveRight(speed);
        }
    }
    
}

