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
import java.nio.ByteBuffer;
import org.lwjgl.system.MemoryUtil;

/**
 *
 * @author Volchkov Andrey
 */
abstract class AbstractImageDataFetcher implements ImageDataFetcher {

    @Override
    public ByteBuffer fetch(BufferedImage image) {
        ByteBuffer imgData = MemoryUtil.memAlloc(getDataSize(image));
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                setData(image, x, y, imgData);
            }
        }
        imgData.flip();
        return imgData;
    }

    protected int getDataSize(BufferedImage image) {
        int components = image.getColorModel()
                .getColorSpace()
                .getNumComponents();
        if (components < 3) {
            components = 3; // force to RGB 
        }
        return components * image.getHeight() * image.getWidth();
    }

    protected abstract void setData(BufferedImage image, int x, int y, ByteBuffer imgData);

}
