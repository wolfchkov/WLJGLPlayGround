/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.wolf.wljglplayground.input;

import org.lwjgl.glfw.GLFW;

/**
 * Enum for key actions
 * @author Volchkov Andrey
 */
public enum KeyAction {
    
    PRESS(GLFW.GLFW_PRESS), 
    RELEASE(GLFW.GLFW_RELEASE), 
    REPEAT(GLFW.GLFW_REPEAT);
    
    
    public static KeyAction getFor(int glfwCode) {
        switch (glfwCode) {
            case GLFW.GLFW_PRESS: return PRESS;
            case GLFW.GLFW_RELEASE: return RELEASE;
            case GLFW.GLFW_REPEAT: return REPEAT;
        }
        throw new IllegalArgumentException("Unknown GLFW Code " + glfwCode);
    }
    
    private final int glfwCode;

    private KeyAction(int glgwCode) {
        this.glfwCode = glgwCode;
    }

    public int getGlgwCode() {
        return glfwCode;
    }
    
    
}
