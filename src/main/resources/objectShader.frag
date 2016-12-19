#version 330 core

in vec3 normal;
in vec3 fragmentPos;

out vec4 color;

uniform mat3 normalMatrix;
uniform vec3 modelColor;
uniform vec3 lightPos;
uniform vec3 lightColor;
uniform vec3 viewPos;

const float kPi = 3.14159265;
const float kShininess = 16.0;

void main(){
    vec3 norm = normalize(normalMatrix * normal);

    //ambient const
    float ambientPower = 0.1f;
    vec3 ambient = ambientPower * modelColor;

    //diffuse calculation    
    vec3 lightDir = normalize(lightPos - fragmentPos);
    float diff = clamp(dot(norm, lightDir), 0.0f, 1.0f);
    vec3 diffuse = diff * lightColor * modelColor;

    //specular calculation
    vec3 lookDir = normalize(viewPos - fragmentPos);    

/*
    const float kEnergyConservation = ( 8.0 + kShininess ) / ( 8.0 * kPi ); 
    vec3 halfwayDir = normalize(lightDir + lookDir);  
    float spec = kEnergyConservation * pow(max(dot(normal, halfwayDir), 0.0), 32.0f);
*/  
    const float kEnergyConservation = ( 2.0 + kShininess ) / ( 2.0 * kPi ); 
    vec3 reflectDir = reflect(-lightDir, norm);
    float spec = pow(clamp(dot(lookDir, reflectDir), 0.0f, 1.0f), 32.0f);
    

    float specularPower = 0.8f;
    vec3 specular = specularPower * spec * lightColor;

    vec3 mixed = specular + diffuse + ambient;

    color = vec4(mixed, 1.0f);

    /*Debugging*/
    //color.rgb = vec3(spec, spec, spec);
    //color.a = 1.0f;

}
