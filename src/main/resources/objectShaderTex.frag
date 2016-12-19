#version 330 core

in vec3 normal;
in vec3 fragmentPos;
in vec2 texCoord;

out vec4 color;

struct Light {
    vec3 position;    
    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
}; 

uniform Light light;
uniform mat3 normalMatrix;
uniform vec3 viewPos;
uniform float shininess;
uniform sampler2D textureDiffuse;
uniform sampler2D textureSpecular;

void main(){
    vec3 norm = normalize(normalMatrix * normal);

    vec3 texDiffuse = vec3(texture(textureDiffuse, texCoord));
    vec3 texSpecular = vec3(texture(textureSpecular, texCoord));

    //ambient color
    vec3 ambient = 0.2f * light.ambient * texDiffuse;

    //diffuse calculation    
    vec3 lightDir = normalize(light.position - fragmentPos);
    float diff = clamp(dot(norm, lightDir), 0.0f, 1.0f);
    vec3 diffuse = diff * light.diffuse * texDiffuse;

    //specular calculation
    vec3 lookDir = normalize(viewPos - fragmentPos);    
    vec3 reflectDir = reflect(-lightDir, norm);
    float spec = pow(clamp(dot(lookDir, reflectDir), 0.0f, 1.0f), shininess);
    
    vec3 specular =  light.specular * spec * texSpecular;

    vec3 mixed = specular + diffuse + ambient;

    color = vec4(mixed, 1.0f);

    /*Debugging*/
    //color.rgb = vec3(spec, spec, spec);
    //color.a = 1.0f;

}

