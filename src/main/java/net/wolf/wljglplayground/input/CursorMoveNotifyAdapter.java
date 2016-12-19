/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.wolf.wljglplayground.input;

import java.util.LinkedList;
import org.lwjgl.glfw.GLFWCursorPosCallback;

/**
 *
 * @author Volchkov Andrey
 */
public class CursorMoveNotifyAdapter extends GLFWCursorPosCallback {

    private final LinkedList<CursorMoveHandler> moveHandlers; 

    public CursorMoveNotifyAdapter() {
        this.moveHandlers = new LinkedList<>();
    }

    @Override
    public void invoke(long window, double xpos, double ypos) {
        notifyCursorMoveHandlers(xpos, ypos);
    }

    
    public void notifyCursorMoveHandlers(double xpos, double ypos) {
        for (CursorMoveHandler moveHandler : moveHandlers) {
            moveHandler.handleMove(xpos, ypos);
        }
    }
    
    public void registerHandler(CursorMoveHandler moveHandler) {
        moveHandlers.add(moveHandler);
    }
    
    public void removeHandler(CursorMoveHandler moveHandler) {
        moveHandlers.remove(moveHandler);
    }        
    
}
