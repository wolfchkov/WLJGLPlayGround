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
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import javax.imageio.ImageIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Loader for image data
 * @author Volchkov Andrey
 */
public class TextureDataLoader {
    
    private static final Logger LOG = LoggerFactory.getLogger(TextureDataLoader.class);
    
    private static final TextureDataLoader INSTANCE  = new TextureDataLoader();
    
    public static TextureDataLoader getInstance() {
        return INSTANCE;
    }

    public ImageData loadImageData(File imageFile) throws IOException {

        LOG.debug("Load texture " + imageFile);
        BufferedImage image = ImageIO.read(imageFile);
        LOG.debug("Fetching data ...");
        ImageDataFetcher fetcher = getFetcher(image.getType()); 
        ByteBuffer data = fetcher.fetch(image);
        ImageData imageData = new ImageData(image.getWidth(), image.getHeight(), 
                image.getType(), data);
        LOG.debug("Image data {}", imageData);
        return imageData;
                
    }

    private ImageDataFetcher getFetcher(int imageType) {
        switch (imageType) {
            case BufferedImage.TYPE_3BYTE_BGR:
            case BufferedImage.TYPE_BYTE_GRAY:
                return new AbstractImageDataFetcher() { 

                    @Override
                    protected void setData(BufferedImage image, int x, int y, ByteBuffer imgData) {
                        int rgb = image.getRGB(x, y);
                        imgData.put((byte) ((rgb >> 16) & 0xFF));    //red component
                        imgData.put((byte) ((rgb >> 8) & 0xFF));     //green component
                        imgData.put((byte) (rgb & 0xFF));            //blue component
                    }
                };
            case BufferedImage.TYPE_4BYTE_ABGR:
                return new AbstractImageDataFetcher() { 

                    @Override
                    protected void setData(BufferedImage image, int x, int y, ByteBuffer imgData) {
                        int rgba = image.getRGB(x, y);
                        imgData.put((byte) ((rgba >> 16) & 0xFF));    //red component
                        imgData.put((byte) ((rgba >> 8) & 0xFF));     //green component
                        imgData.put((byte) (rgba & 0xFF));            //blue component
                        imgData.put((byte) ((rgba >> 24) & 0xFF));    //alpha component
                    }
                };
            default:
                throw new IllegalArgumentException("Image type " + imageType 
                        + " not supported!");
        }
    }
}
