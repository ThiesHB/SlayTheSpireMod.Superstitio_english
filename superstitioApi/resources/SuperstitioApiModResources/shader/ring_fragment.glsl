#version 100
#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif
varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform float u_degreeStart;
uniform float u_degreeLength;
uniform float u_radius;
uniform float u_halfThick;

vec2 toRound(vec2 texCoords){
    texCoords -= 0.5f;
    float theta = degrees(atan(texCoords.y,texCoords.x));
    float radius = length(texCoords);
    theta += 180.0f;
    return vec2(theta,radius);
}

void main(){
    vec2 texC = toRound(v_texCoords);
    texC.y -= (u_radius-u_halfThick);
    texC.y /=  u_halfThick;
    texC.y *= 0.5f;

    vec4 shouldDraw = vec4(1.0f,1.0f,1.0f,0.0f);
    texC.x = mod(texC.x,360.0f);


    if ((u_degreeStart < texC.x && texC.x < u_degreeLength + u_degreeStart)||(u_degreeStart < texC.x - 360.0f && texC.x - 360.0f < u_degreeLength + u_degreeStart))
        shouldDraw.a = 1.0f;

    gl_FragColor = v_color * shouldDraw * texture2D(u_texture,texC);
}



