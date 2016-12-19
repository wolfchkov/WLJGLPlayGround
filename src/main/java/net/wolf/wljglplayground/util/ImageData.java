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
package net.wolf.wljglplayground.util;

import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

/**
 *
 * @author Volchkov Andrey
 */
public class ImageData implements Deletable, Closeable{
    private final int width;
    private final int height;
    private final int glType;
    private final ByteBuffer data;

    public ImageData(int width, int height, int imageType, ByteBuffer data) {
        this.width = width;
        this.height = height;
        
        this.glType = toGLType(imageType);
        this.data = data;
    }

    @Override
    public String toString() {
        return "ImageData{" + "width=" + width + ", height=" + height + ", glType=" + glType + '}';
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getGlType() {
        return glType;
    }

    public ByteBuffer getData() {
        return data;
    }

    @Override
    public void delete() {
        if (data != null) {
            MemoryUtil.memFree(data);
        }
    }

    private int toGLType(int imageType) {
        switch (imageType) {
            case BufferedImage.TYPE_3BYTE_BGR: 
            case BufferedImage.TYPE_BYTE_GRAY:
                return GL11.GL_RGB;
            case BufferedImage.TYPE_4BYTE_ABGR: 
                return GL11.GL_RGBA;            
        }
        throw new IllegalArgumentException("Type not supported " + imageType);
    }

    @Override
    public void close() {
        delete();
    }
}
