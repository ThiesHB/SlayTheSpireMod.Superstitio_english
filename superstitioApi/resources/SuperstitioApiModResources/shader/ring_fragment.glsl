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
    texCoords -= 0.5;
    highp float theta = degrees(atan(texCoords.y,texCoords.x));
    lowp float radius = length(texCoords);
    theta += 180.0;

    radius -= (u_radius-u_halfThick);
    radius /=  u_halfThick;
    radius *= 0.5;

    vec4 shouldDraw = vec4(1.0);
    theta -= u_degreeStart;
    theta = mod(theta,360.0);

    shouldDraw.a *= step(0.0,theta);
    shouldDraw.a *= step(theta,u_degreeLength);
    theta /= u_degreeLength;
    shouldDraw.a *= step(0.0,radius);
    shouldDraw.a *= step(radius,1.0);

    gl_FragColor = v_color * shouldDraw * texture2D(u_texture,vec2(theta,radius));
}



