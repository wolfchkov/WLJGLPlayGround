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

import net.wolf.wljglplayground.shader.ProgramGLSL;
import net.wolf.wljglplayground.util.ColorUtils;
import org.joml.Vector3f;
import org.joml.Vector3fc;

/**
 *
 * @author Volchkov Andrey
 */
public class ColorMaterial implements Material {
       
    private final Vector3f ambient;
    private final Vector3f diffuse;
    private final Vector3f specular;
    private final float shininess;
    private final int useBlinn;        

    public ColorMaterial(Vector3f ambient, Vector3f diffuse, Vector3f specular, 
            int shininess, boolean useBlinn) {
        this.ambient = ambient;
        this.diffuse = diffuse;
        this.specular = specular;
        this.shininess = (float) nearestPow2(shininess);
        this.useBlinn = useBlinn ? 1 : 0;
    }

    public ColorMaterial(String ambient, String diffuse, String specular, 
            int shininess, boolean useBlinn) {
        this.ambient = ColorUtils.parseRGBColor(ambient);
        this.diffuse = ColorUtils.parseRGBColor(diffuse);
        this.specular = ColorUtils.parseRGBColor(specular);
        this.shininess = (float) nearestPow2(shininess);
        this.useBlinn = useBlinn ? 1 : 0;
    }
    
    @Override
    public void apply(ProgramGLSL program) {
        program.setParam("material.ambient", ambient);
        program.setParam("material.diffuse", diffuse);
        program.setParam("material.specular", specular);
        program.setParam("material.shininess", shininess);
        program.setParam("material.useBlinn", useBlinn);
        
    }

    public Vector3fc getAmbient() {
        return ambient.toImmutable();
    }

    public Vector3fc getDiffuse() {
        return diffuse.toImmutable();
    }

    public Vector3fc getSpecular() {
        return specular.toImmutable();
    }

    public float getShininess() {
        return shininess;
    }

    public int getUseBlinn() {
        return useBlinn;
    }
    
    
    private static int nearestPow2(int value) {
        int pow = 32 - Integer.numberOfLeadingZeros(value - 1);
        int rez = 1;
        for (int i = 0; i < pow; ++i) {
            rez <<= 1;
        }
        return rez;
        
    }

    @Override
    public void use() {
        //do nothing
    }

    @Override
    public void delete() {
     
    }

    
}
