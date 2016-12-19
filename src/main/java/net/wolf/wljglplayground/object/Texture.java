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
package net.wolf.wljglplayground.object;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import net.wolf.wljglplayground.util.Deletable;
import net.wolf.wljglplayground.util.ImageData;
import net.wolf.wljglplayground.util.TextureDataLoader;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_REPEAT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Volchkov Andrey
 */
public class Texture implements Deletable {
    private static final Logger LOG = LoggerFactory.getLogger(Texture.class);
    
    private static final Map<String, Texture> TEXTURE_CACHE = 
            new HashMap<>();

    private final String texFile;
    private int glId;
    
    public static Texture create(String texFile) {
        String texName = texFile.trim();
        Texture tex = TEXTURE_CACHE.get(texName);
        if (tex == null) {
            try {
                tex = new Texture(texName);
                tex.init();
                TEXTURE_CACHE.put(texName, tex);
            } catch (IOException ex) {
                LOG.error("Error loading texture " + texFile, ex);
                return null;
            }
        }
        return tex;
    }

    private Texture(String texFile) {
        this.texFile = Texture.class.getResource(texFile).getFile();
    }

    public void init() throws IOException {
        File imgFile = new File(texFile);

        if (imgFile.exists() && imgFile.isFile()) {
            try (ImageData imageData = TextureDataLoader.getInstance()
                    .loadImageData(imgFile);) {
                

                //generate texture object
                glId = glGenTextures();                
                glBindTexture(GL_TEXTURE_2D, glId);
                //texture parameters
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
                //texture filtering
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
                //load texture data
                glTexImage2D(GL_TEXTURE_2D, 0, imageData.getGlType(),
                        imageData.getWidth(), imageData.getHeight(), 0, imageData.getGlType(),
                        GL_UNSIGNED_BYTE, imageData.getData());
                //generate mipmaps
                glGenerateMipmap(GL_TEXTURE_2D);
                glBindTexture(GL_TEXTURE_2D, 0);
            }

        } else {
            throw new IllegalStateException("Bad image file " + texFile);
        }

    }

    public int getGLId() {
        return glId;
    }

    @Override
    public void delete() {
        if (glId != 0) {
            GL11.glDeleteTextures(glId);
        }
    }


}

