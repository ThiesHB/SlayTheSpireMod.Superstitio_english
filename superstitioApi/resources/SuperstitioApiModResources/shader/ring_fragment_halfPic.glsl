#version 100
#ifdef GL_ES
precision lowp float;
#endif
varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform float u_degreeStart;
uniform float u_degreeLength;
uniform float u_radius;
uniform float u_halfThick;

void main(){
    vec2 texCoords = v_texCoords ;
    texCoords -= 0.5f;
    highp float theta = degrees(atan(texCoords.y,texCoords.x));
    lowp float radius = length(texCoords);
    theta += 180.0f;

    radius -= (u_radius-u_halfThick);
    radius /=  u_halfThick;
    radius *= 0.5f;
    radius -= 0.5f;
    radius = abs(radius);
    radius += 0.5f;

    vec4 shouldDraw = vec4(1.0f);
    theta -= u_degreeStart;
    theta = mod(theta,360.0f);

    shouldDraw.a *= step(0.0f,theta);
    shouldDraw.a *= step(theta,u_degreeLength);
    theta /= u_degreeLength;
    shouldDraw.a *= step(0.0f,radius);
    shouldDraw.a *= step(radius,1.0f);

    gl_FragColor = v_color * shouldDraw * texture2D(u_texture,vec2(theta,radius));
}



