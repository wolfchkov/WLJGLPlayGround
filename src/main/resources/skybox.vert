#version 330 core
layout (location = 0) in vec3 vertexPosition;
out vec3 texSkyCoords;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;


void main()
{
    gl_Position = projectionMatrix * viewMatrix * vec4(vertexPosition, 1.0);  
    texSkyCoords = vertexPosition;
}  
