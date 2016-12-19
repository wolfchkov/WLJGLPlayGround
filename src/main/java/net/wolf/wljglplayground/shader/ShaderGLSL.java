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
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.lwjgl.opengl.GL20.*;
import org.lwjgl.opengl.GL32;
import org.lwjgl.opengl.GL40;

/**
 * GLSL shader loader and compiler
 * @author Volchkov Andrey
 */
public class ShaderGLSL {
    
    private static final Logger LOG = LoggerFactory.getLogger(ShaderGLSL.class);
    
    private int shaderId = 0;
    private final URI shaderFile;
    private final ShaderType shaderType;

    public ShaderGLSL(String shaderFile) throws URISyntaxException {
        URL resource = this.getClass().getResource(shaderFile);
        if (Objects.isNull(resource)) {
            throw new IllegalArgumentException("Shader file " + shaderFile + " not found!");
        }
        this.shaderFile = this.getClass().getResource(shaderFile).toURI();
        this.shaderType = getTypeByExt(shaderFile);
    }

    public void load() throws IOException {
        if (shaderId != 0) {
            throw new IllegalStateException("Shader already loaded!");
        }

        shaderId = glCreateShader(shaderType.getGLType());

        String shader = Files
                .lines(Paths.get(shaderFile))
                .collect(Collectors.joining("\n"));

        // Compile Vertex Shader
        LOG.debug("Compiling shader {}", shaderFile);
        glShaderSource(shaderId, shader);
        glCompileShader(shaderId);

        // Check shader compilation on errors
        String shaderInfoLog = glGetShaderInfoLog(shaderId);
        if (!shaderInfoLog.isEmpty()) {
            LOG.error("Shader log: {}", shaderInfoLog);
            throw new IllegalStateException(shaderInfoLog);
        }
    }

    public int getShaderId() {
        return shaderId;
    }

    public URI getShaderFile() {
        return shaderFile;
    }

    public ShaderType getShaderType() {
        return shaderType;
    }
    
    public void delete() {
        if (shaderId != -1) {
            glDeleteShader(shaderId);
            shaderId = -1;
        }
    }

    private ShaderType getTypeByExt(String shaderFile) {
        int pointIndex = shaderFile.lastIndexOf('.');
        if (pointIndex < 0) {
            throw new IllegalArgumentException("Can't determine the type of shader " + shaderFile);
        }
        //get extension of file 
        String ext = shaderFile.substring(pointIndex + 1);
        
        switch (ext.toLowerCase().trim()) {
            case "vert": return ShaderType.VERTEX;
            case "frag": return ShaderType.FRAGMENT;            
            case "geom": return ShaderType.GEOMETRY;            
            case "tesc": return ShaderType.TESS_CONTROLL;            
            case "tese": return ShaderType.TESS_EVALUATION;            
        }
        
        throw new IllegalArgumentException("Can't determine the type of shader."
                + " Unknown extension " + ext);
    }
    

    /**
     * Shaders types
     */
    public enum ShaderType {
        VERTEX(GL_VERTEX_SHADER),
        FRAGMENT(GL_FRAGMENT_SHADER),
        GEOMETRY(GL32.GL_GEOMETRY_SHADER),
        TESS_CONTROLL(GL40.GL_TESS_CONTROL_SHADER),
        TESS_EVALUATION(GL40.GL_TESS_EVALUATION_SHADER);

        /**
         * OpenGL shader identifier
         */
        private final int glType;

        private ShaderType(int glType) {
            this.glType = glType;
        }

        int getGLType() {
            return glType;
        }

    }

}

