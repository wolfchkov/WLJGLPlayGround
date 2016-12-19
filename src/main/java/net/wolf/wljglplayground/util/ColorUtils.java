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

import org.joml.Vector3f;

/**
 * Util methods for color
 * @author Volchkov Andrey
 */
public class ColorUtils {
    
    private static final float COLOR_COEF = 1.0f / 256.0f;
    
    public static Vector3f parseRGBColor(String color) {
        if (!color.startsWith("#")) {
            throw new IllegalArgumentException("Color must start with '#'!");
        }
        if (color.length() != 7) {
            throw new IllegalArgumentException("Color must start with '#' and 6 hex numbers!");
        }
        int intColor = Integer.parseInt(color.substring(1), 16);
        
        return new Vector3f(
                (float)((intColor >> 16) & 0x000000FF) * COLOR_COEF,
                (float)((intColor >> 8) & 0x000000FF) * COLOR_COEF,
                (float)(intColor & 0x000000FF) * COLOR_COEF                
        );
        
    }
}
