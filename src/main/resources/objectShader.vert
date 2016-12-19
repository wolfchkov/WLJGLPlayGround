#version 330 core
//data from buffer
layout(location = 0) in vec3 vertexPosition;
layout(location = 1) in vec3 vertexNormal;
layout(location = 2) in vec2 vertexTexCoord;

//data for fragment shader
out vec3 fragmentPos;
out vec3 normal;
out vec2 texCoord;

//matrixes
uniform mat4 modelMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;



void main() {
    //for Phong lighting
    fragmentPos = vec3(modelMatrix * vec4(vertexPosition, 1.0f));
    
    normal = vertexNormal;
    texCoord = vertexTexCoord;

    gl_Position = projectionMatrix * viewMatrix * vec4(fragmentPos, 1.0f);    
}