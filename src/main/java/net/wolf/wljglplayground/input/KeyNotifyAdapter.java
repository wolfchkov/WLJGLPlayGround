/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.wolf.wljglplayground.input;

import java.util.LinkedList;
import org.lwjgl.glfw.GLFWKeyCallback;

/**
 * Callback for keyboard input processing
 * @author Volchkov Andrey
 */
public class KeyNotifyAdapter extends GLFWKeyCallback {
    
    private final LinkedList<KeyHandler> keyHandlers; 

    public KeyNotifyAdapter() {
        keyHandlers = new LinkedList<>();
    }
    
    @Override
    public void invoke(long window, int key, int scancode, int action, int mods) {
        notifyKeyHandlers(key, scancode, action, mods);
    }
    
    
    public void notifyKeyHandlers(int key, int scancode, int action, int mods) {
        for (KeyHandler keyHandler : keyHandlers) {
            keyHandler.handleKey(key, scancode, KeyAction.getFor(action));
        }
    }
    
    public void registerHandler(KeyHandler keyHandler) {
        keyHandlers.add(keyHandler);
    }
    
    public void removeHandler(KeyHandler keyHandler) {
        keyHandlers.remove(keyHandler);
    }    
}
