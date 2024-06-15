// Distance estimation: thanks to https://iquilezles.org/articles/distance

/* Tweaks to try:
 - fill in the hearts (where heart(p) < 0)  With what texture?
*/

// My daughter requested stripes.
// #define STRIPES 1
// #define SOLID_HEARTS 1

// heart(p) = 0 is a heart-shaped curve.
#version 100
#ifdef GL_ES
precision lowp float;
#else
precision mediump float;
#endif
#define RANDOMSEED 114.514

varying vec2 v_texCoords;
uniform float u_time;
uniform float u_speed;//速度
uniform vec2 u_tileTimes;//径向和轴向的爱心个数
uniform float u_startTime;//初始时间，使得不同的种子错位，也作为随机数种子使用
uniform float u_density;//密度，越小密度越大
uniform float u_whRate;//长宽比例
uniform vec2 u_offset;//偏移量

// const vec3 u_color = vec3(0.5);

vec2 noise(vec2 p) {
    return fract(RANDOMSEED * sin(RANDOMSEED * (fract(RANDOMSEED * p) + p.yx)));
}

float heart(vec2 p, float s) {
    p /= s;
    vec2 q = p;
    q.x *= 0.5 + .5 * q.y;
    q.y -= abs(p.x) * .63;
    return (length(q) - .7) * s;
}

#define PI 3.14159
#define TESTMODS
vec4 hearts(vec2 polar, float time, vec2 tileTimes,float randomSeed) {
    float l = clamp(polar.y, 0., 1.);
    vec2 tiling = 0.5/PI * tileTimes;
    float radiusWithOutTime = polar.y * tiling.y;
    polar.y -= time;
    vec2 polarID = floor(polar * tiling);
    float radiusInner = -fract(polar.y * tiling.y) + radiusWithOutTime + tileTimes.y * 0.6;

    float radiusOuter = fract(polar.y * tiling.y) - radiusWithOutTime -1.5;


    polar.x = polar.x + polarID.y * .03;
    polar.x = mod(polar.x + PI * 2., PI * 2.);
    polarID = floor(polar * tiling);
    polar = fract(polar * tiling);
    polar = polar * 2. - 1.;
    polar.x *= max(tileTimes.y,tileTimes.x)/tileTimes.x;
    polar.y *= max(tileTimes.y,tileTimes.x)/tileTimes.y;

    radiusOuter /= tileTimes.y * (1.+ 0.3 * floor(2.*noise(polarID + .5+ randomSeed).x+0.5));
    radiusInner /= tileTimes.y * (1.+ 0.2 * floor(2.*noise(polarID + .6+ randomSeed).x+0.5));
    float alpha;
    alpha = radiusInner * radiusOuter*5.;
    polar *= 0.9;

    //这个n调控心形的大小
    vec2 n = noise(polarID +0.1+ randomSeed) * .95 + .25;
    // vec2 n = vec2(1.);
    vec2 n2 = 2. * noise(polarID+ randomSeed) - 1.;
    vec2 offset = (1. - n.y) * n2;
    float heartDist = heart(polar + offset, n.y * .6);
    // float heartDist = heart(polar + offset, n.y * 100.);

    float a = smoothstep(.0, 0.25, n.x*n.x);
    vec4 heartGlow = smoothstep(0.1, -.05, heartDist) * .85 * a * vec4(0.900,0.568,0.884,1.0) + smoothstep(0.12, -0.02, heartDist) * .75 * vec4(0.950,0.234,0.494,1.0);
    vec4 heartCol = smoothstep(0., -.05, heartDist) * .25 * a *vec4(0.900,0.186,0.856,1.0) + heartGlow;
    // vec3 totalCol = vec3(0.15 + l / 2., .0, 0.);

    // float heartGlowInner = smoothstep(0.2, -.05, heartDist) * .5 * a;
    // float heartGlowOuter = smoothstep(0.1, -0.05 , heartDist) * .75;
    // vec3 heartCol = vec3(smoothstep(0., -.15, heartDist))*vec3(1.0,0.5,0.3) * a + (heartGlowInner+heartGlowOuter) * vec3(.9, .5, .7);
#ifdef TESTMOD
    return vec4(vec4(0.15 + l / 2., .0, 0.,1.0) * 11.+ heartCol)*alpha;
#else
    vec4 totalCol = heartCol * step(u_density, noise(polarID + .4+ randomSeed).x);
    return vec4(totalCol*alpha*4.);
    // 这个step使得不是每个tile里面都有东西
#endif
}
uniform float u_spawnRemoveTimer;
void main()
{
    vec2 uv = v_texCoords - 0.5;
    uv += u_offset;
    uv.x *= u_whRate;
    float alpha = smoothstep(u_spawnRemoveTimer,u_spawnRemoveTimer-0.1,length(uv));
    vec2 polar = vec2(atan(uv.y, uv.x), log(length(uv)));
    vec4 h = hearts(polar, u_time * u_speed + u_startTime,u_tileTimes,u_startTime);
    h.a *= alpha;
    gl_FragColor = vec4(h);
}
