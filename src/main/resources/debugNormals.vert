#version 330 core
//data from buffer
layout(location = 0) in vec3 vertexPosition;
layout(location = 1) in vec3 vertexNormal;

//data for fragment shader
out vs_data {
    vec3 normal;
} normal_out;

//matrixes
uniform mat4 modelMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;
uniform mat3 normalMatrix;


void main(){

    gl_Position = projectionMatrix * viewMatrix * modelMatrix * vec4(vertexPosition, 1.0f); 

    mat3 nMatrix = mat3(transpose(inverse(viewMatrix))) * normalMatrix;
    
    normal_out.normal = vec3(projectionMatrix * vec4(nMatrix * vertexNormal, 1.0)); 
}
