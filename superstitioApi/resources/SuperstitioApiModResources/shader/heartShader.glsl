// Distance estimation: thanks to https://iquilezles.org/articles/distance

/* Tweaks to try:
 - fill in the hearts (where heart(p) < 0)  With what texture?
*/

// My daughter requested stripes.
// #define STRIPES 1
// #define SOLID_HEARTS 1

// heart(p) = 0 is a heart-shaped curve.
#ifdef GL_ES
precision lowp float;
#else
precision lowp float;
#endif
varying vec2 v_texCoords;
uniform float u_time;
const vec3 u_color = vec3(0.5);

#define EQUATION 2

#if EQUATION == 1

// See https://math.stackexchange.com/a/438161
// This one has burrs/discontinuities on the sides.
float heart(vec2 p) {
    float k = dot(p, p) - 1.;
    return k * k * k - p.x * p.x * p.y * p.y * p.y;
}

// Gradient of heart function, analytical version, thanks to IQ.
vec2 grad(vec2 p) {
    float k = dot(p, p) - 1.;
    return p * (6.0 * k * k - p.yx * p.yx * p.y * vec2(2.0, 3.0));
}

#else

// Heart curve suggested by IQ, improved by Dave_Hoskins.
float heart(vec2 p) {
    // Center it more, vertically:
    p.y += .6;
    // This offset reduces artifacts on the center vertical axis.
    const float offset = .3;
    // (x^2+(1.2*y-sqrt(abs(x)))^2−1)
    float k = 1.2 * p.y - sqrt(abs(p.x) + offset);
    return p.x * p.x + k * k - 1.;
}

// Gradient of heart function.
// TODO: make it analytic.
// For now, use central differences method.
vec2 grad(vec2 p) {
    vec2 h = vec2(0.001, 0.0);
    return vec2(heart(p + h.xy) - heart(p - h.xy),
                heart(p + h.yx) - heart(p - h.yx)) / (2.0 * h.x);
}

#endif
#define SOLID_HEART
// Return 0-1 scalar based on abs distance from heart line.
float color(vec2 p, float vol) {
    float v = heart(p);
#ifdef SOLID_HEARTS
    if (v < 0.) return 0.;
#endif
    vec2  g = grad(p);
    float de = abs(v) / length(g);
    // Thickness: vary with volume
    float eps = (15. + vol * 3.)/u_resolution.x;
    return smoothstep(1.0 * eps, 2.0 * eps, de);
}

// from https://github.com/hughsk/glsl-hsv2rgb/blob/master/index.glsl
vec3 hsv2rgb(vec3 c) {
  vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);
  vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);
  return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);
}

const float pi = 3.14159;

vec3 heart1(vec2 p, float size, float vol) {
    vec2 offset = vec2(0.);
    float scale = size;
    float a;
    vec3 hue;
    p-=0.5;
    hue = u_color;
   	a = pi * (float(0) - 2.) * (2. / 5.) + u_time * .3;
	 offset = vec2(cos(a), sin(a)) * .02;

    vec2 q = (p + offset) / (scale * (1.0 + vol * .3) * .8);
    float brightness = 1. - color(q * 10., vol);
    return brightness * hue;
}

const int nLevels = 32;

// Adapted from https://www.shadertoy.com/view/4tjBRz
// TO DO: would like to use buffers to make the effects of volume
// ease in and out, instead of sudden spikes.
float soundVolume() {
    float total = 0.0, maxVol = 0.0;
    for( int i = 0; i < nLevels; i++ )
    {
        float v = 0.;//texelFetch(iChannel0, ivec2((i*256)/nLevels, 1), 0).x-0.5;
        maxVol = max(maxVol, v*v);
        total += v*v;
    }
    // Split the difference between average and max.
    float vol = (total / float(nLevels) + maxVol) * 0.5;
    // Attempt to smooth out the maximum.
    return pow(vol, 0.1);
}
// 2D Random
float random (in vec2 st) {
    return fract(sin(dot(st.xy,
                         vec2(12.9898,78.233)))-0.5
                 * 43758.5453123);
}

// 2D Noise based on Morgan McGuire @morgan3d
// https://www.shadertoy.com/view/4dS3Wd
float noise (in vec2 st) {
    vec2 i = floor(st);
    vec2 f = fract(st);

    // Four corners in 2D of a tile
    float a = random(i);
    float b = random(i + vec2(1.0, 0.0));
    float c = random(i + vec2(0.0, 1.0));
    float d = random(i + vec2(1.0, 1.0));

    // Smooth Interpolation

    // Cubic Hermine Curve.  Same as SmoothStep()
    vec2 u = f*f*(3.0-2.0*f);
    // u = smoothstep(0.,1.,f);

    // Mix 4 coorners percentages
    return mix(a, b, u.x) +
            (c - a)* u.y * (1.0 - u.x) +
            (d - b) * u.x * u.y;
}

vec2 randomMove(vec2 pos){
    vec2 npos = vec2(0.);
        // Properties
    const int octaves = 1;
    float lacunarity = 2.0;
    float gain = 0.5;
    //
    // Initial values
    float amplitude = 0.5;
    float frequency = 1.;
    //
    // Loop of octaves
    for (int i = 0; i < octaves; i++) {
        npos += amplitude * noise(frequency*pos);
        frequency *= lacunarity;
        amplitude *= gain;
    }
    return npos;
}

mat2 rotate2d(float angle){
    return mat2(cos(angle),-sin(angle),
                sin(angle),cos(angle));
}

void main()
{
    // Normalized pixel coordinates
   	vec2 uv =  gl_FragCoord.xy/u_resolution.xx;
    uv *= 1.;
    uv = fract(uv);
    float powf_start = 1.;
    float powf = 1.* cos(3.*randomMove(vec2(u_time)).x);
    // powf = 0.9;
    powf +=powf_start;
    uv = powf*pow(uv,vec2(powf));
  	 // uv = rotate2d(noise(vec2(u_time))) * uv;
    uv += 0.;


    float vol = soundVolume();
    vec3 col = vec3(0.);
    col += heart1(uv,4., vol);


#define STRIPE
#ifdef STRIPES
    float y = floor(mod(uv.y, .2) * 5. * 3.) / 3.;
    vec3 background = vec3(1., y, y);
#else
    vec3 background = mix(vec3(.1), vec3(1., 0., 0.), vol * .2);
#endif

    gl_FragColor = vec4(col + background, 1.0);
}
