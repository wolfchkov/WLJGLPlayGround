#version 330 core
layout (triangles) in;
layout (line_strip, max_vertices = 6) out;

in vs_data {
    vec3 normal;
} normals_in[];

out vec3 vcolor;

const float MAGNITUDE = 0.2f;

void genNormalLine(int index, vec3 color)
{
    gl_Position = gl_in[index].gl_Position;
    vcolor = color;
    EmitVertex();
    gl_Position = gl_in[index].gl_Position + vec4(normals_in[index].normal, 0.0f) * MAGNITUDE;
    vcolor = color;
    EmitVertex();
    EndPrimitive();
}

void main()
{
    //1 vertex normal
    genNormalLine(0, vec3(1.0f, 1.0f, 0.0f)); 
    //2 vertex normal
    genNormalLine(1, vec3(1.0f, 1.0f, 0.0f)); 
    //3 vertex normal
    genNormalLine(2, vec3(1.0f, 1.0f, 0.0f)); 
}
