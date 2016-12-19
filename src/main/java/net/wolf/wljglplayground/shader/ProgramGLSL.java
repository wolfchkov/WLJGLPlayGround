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
package net.wolf.wljglplayground.shader;

import java.io.IOException;
import java.nio.FloatBuffer;
import net.wolf.wljglplayground.util.Deletable;

import org.joml.Matrix3fc;
import org.joml.Matrix4fc;
import org.joml.Vector2fc;
import org.joml.Vector2ic;
import org.joml.Vector3fc;
import org.joml.Vector3ic;
import org.joml.Vector4fc;
import org.joml.Vector4ic;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.system.MemoryUtil.memAllocFloat;
import static org.lwjgl.system.MemoryUtil.memFree;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

/**
 * GLSL program linker and binder
 * @author Volchkov Andrey
 */
public class ProgramGLSL implements Deletable {
    
    private static final Logger LOG = LoggerFactory.getLogger(ProgramGLSL.class);
    
    private int programId;
    
    private final ShaderGLSL[] shaders;
    
    private final FloatBuffer matrixBuf3;
    private final FloatBuffer matrixBuf4;

    
    public ProgramGLSL(ShaderGLSL ... shader) {
        this.shaders = shader;
        this.matrixBuf3 = memAllocFloat(3*3);
        this.matrixBuf4 = memAllocFloat(4*4);
    }

    
    public void link() throws IOException {
        if (programId != 0) {
            throw new IllegalStateException("Program already linked. ID => " + programId);
        }
        
        for (ShaderGLSL shader : shaders) {
            shader.load();
        }
        
        LOG.debug("Linking program...");
	programId = glCreateProgram();
        
        for (ShaderGLSL shader : shaders) {
            LOG.debug("Shader ID => {} ", shader.getShaderId());
            glAttachShader(programId, shader.getShaderId());
        }
        
	glLinkProgram(programId);
        
        String programInfoLog = glGetProgramInfoLog(programId);
        
        if (!programInfoLog.isEmpty()) {
            LOG.debug("Linking log {}", programInfoLog);
        }
        
        for (ShaderGLSL shader : shaders) {
            glDetachShader(programId, shader.getShaderId());
            shader.delete();
        }
    }
    
    private int getUniformLocation(String name) {
        return glGetUniformLocation(programId, name);
    }
    
    public ProgramGLSL setParam(String name, Vector2fc vec) {
        glUniform2f(getUniformLocation(name), vec.x(), vec.y());
        return this;
    }
    
    public ProgramGLSL setParam(String name, Vector3fc vec) {
        glUniform3f(getUniformLocation(name), vec.x(), vec.y(), vec.z());
        return this;
    }
    
    public ProgramGLSL setParam(String name, Vector4fc vec) {
        glUniform4f(getUniformLocation(name), vec.x(), vec.y(), vec.z(), vec.w());
        return this;
    }
    
    public ProgramGLSL setParam(String name, Vector2ic vec) {
        glUniform2i(getUniformLocation(name), vec.x(), vec.y());
        return this;
    }
    
    public ProgramGLSL setParam(String name, Vector3ic vec) {
        glUniform3i(getUniformLocation(name), vec.x(), vec.y(), vec.z());
        return this;
    }
    
    public ProgramGLSL setParam(String name, Vector4ic vec) {
        glUniform4i(getUniformLocation(name), vec.x(), vec.y(), vec.z(), vec.w());
        return this;
    }

    public ProgramGLSL setParam(String name, Matrix3fc mat) {
        glUniformMatrix3fv(getUniformLocation(name), false, mat.get(matrixBuf3));
        return this;
    }    

    public ProgramGLSL setParam(String name, Matrix4fc mat) {
        glUniformMatrix4fv(getUniformLocation(name), false, mat.get(matrixBuf4));
        return this;
    } 
    
    public ProgramGLSL setParam(String name, int val) {
        glUniform1i(getUniformLocation(name), val);
        return this;
    } 
    
    public ProgramGLSL setParam(String name, float val) {
        glUniform1f(getUniformLocation(name), val);
        return this;
    }      
    
    public ProgramGLSL use() {
        glUseProgram(programId);
        return this;
    }
    
    @Override
    public void delete() {
        
        if (programId != 0) {            
            
            glDeleteProgram(programId);
            programId = 0;
            
        }
        
        memFree(matrixBuf3);
        memFree(matrixBuf4);
    };
}

