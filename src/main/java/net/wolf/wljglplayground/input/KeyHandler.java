/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.wolf.wljglplayground.input;

/**
 *
 * @author Volchkov Andrey
 */
public interface KeyHandler {
    
    
    void handleKey(int key, int scancode, KeyAction action);
}
