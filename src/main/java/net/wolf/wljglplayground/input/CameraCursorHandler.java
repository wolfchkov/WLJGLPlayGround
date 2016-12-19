/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.wolf.wljglplayground.input;

import net.wolf.wljglplayground.object.Camera;

/**
 *
 * @author Volchkov Andrey
 */
public class CameraCursorHandler implements CursorMoveHandler {
    private double sens;
    
    private boolean firstMove = true;
    
    private double prevXPos;
    private double prevYPos;
    private double yaw = -90 ;
    private double pitch = 0;

    public CameraCursorHandler(double sens) {
        this.sens = sens;
    }

    public void setSens(double sens) {
        this.sens = sens;
    }

    @Override
    public void handleMove(double xpos, double ypos) {
        if (firstMove) {
            prevXPos = xpos;
            prevYPos = ypos;
            firstMove = false;
            return;
        }
        
        double xOffset = xpos - prevXPos;
        double yOffset = prevYPos - ypos;
        prevXPos = xpos;
        prevYPos = ypos;
        
        yaw += xOffset * sens;
        pitch += yOffset * sens;   
        
        if (pitch > 89) {
            pitch = 89;
        }
        if (pitch < -89) {
            pitch = -89;
        }
    }
    
    public void apply(Camera camera) {
        camera.rotate(yaw, pitch);
    }
    
}


