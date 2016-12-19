#version 330 core

in vec3 normal;
in vec3 fragmentPos;
in vec2 texCoord;

out vec4 color;

struct Material {
    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
    float shininess;
    bool useBlinn;
}; 

struct Light {
    vec3 position;    
    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
}; 

uniform Material material;
uniform Light light;
uniform mat3 normalMatrix;
uniform vec3 viewPos;

void main(){
    vec3 norm = normalize(normalMatrix * normal);

    //ambient color
    vec3 ambient = light.ambient * material.ambient;

    //diffuse calculation    
    vec3 lightDir = normalize(light.position - fragmentPos);
    float diff = clamp(dot(norm, lightDir), 0.0f, 1.0f);
    vec3 diffuse = diff * light.diffuse * material.diffuse;

    //specular calculation
    vec3 lookDir = normalize(viewPos - fragmentPos);    
    float spec;
    vec3 debug;
    if (material.useBlinn) {
        vec3 halfwayDir = normalize(lightDir + lookDir);  
        spec = pow(max(dot(norm , halfwayDir), 0.0), material.shininess);
    } else {
        vec3 reflectDir = reflect(-lightDir, norm);
        spec = pow(max(dot(lookDir, reflectDir), 0.0f), material.shininess);
    }
    vec3 specular =  light.specular * (spec * material.specular);
    //vec3 specular = vec3(0.3) * spec;

    vec3 mixed = specular + diffuse + ambient;

    color = vec4(mixed, 1.0f);

    /*Debugging*/
    //color.rgb = ;
    //color.a = 1.0f;

}
