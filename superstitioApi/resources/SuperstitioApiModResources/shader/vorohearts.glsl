#ifdef GL_ES
precision highp float;
#else
precision highp float;
#endif

varying vec2 v_texCoords;
uniform float u_time;
uniform float u_whRate;//长宽比例

#define t (u_time+12.0)*.5

vec2 rotate(vec2 p, float a)
{
    float s = sin(a), c = cos(a);

    return p * mat2(c,s,-s,c);
}

vec2 hash22(vec2 p)
{
    return fract(sin(vec2(dot(p,vec2(65.1375,23.7513)),dot(p,vec2(37.97844, 17.9753))))*47354.71168);
}

// The MIT License
// Copyright © 2021 Inigo Quilez
// (Added scale)
float sdHeart(vec2 p, float s)
{
    #define dot2(x) dot(x, x)
    p.y += s * 0.5833333;
    p.x = abs(p.x);
    if (p.y + p.x > s) return sqrt(dot2(p - vec2(0.25, 0.75) * s)) - s * sqrt(2.0) / 4.0;
    return sqrt(min(dot2(p - vec2(0.0, s)), dot2(p - 0.5 * max(p.y + p.x, 0.0)))) * sign(p.x - p.y);
}


#define SIZE_MIN 0.08
#define SIZE_RANGE 0.08

float vorohearts(vec2 uv)
{
    #define scale 0.53
    vec2 i = floor(uv / scale);
    vec2 p = mod(uv, scale);
	 float md = 1e1;
    for(int x = -1; x <= 1; x++)
	{
    	for(int y = -1; y <= 1; y++)
    	{
			vec2 ro = hash22((i + vec2(x, y)) * scale);
			float rnd = ro.x + ro.y;
			vec2 rp = p - ((sin(ro * 7.0 + t * 0.3) * 0.5 + 0.5) + vec2(x, y)) * scale;
			rp = rotate(rp, 0.5 * t * (rnd - 1.0));
			rp.x *= 1.0 + sin(0.5 + sin(6.0 * (t+rnd))) * 0.03;
			rp.y *= 0.9 + sin(1.5 + sin(3.0 * (t+rnd))) * 0.1;
			float d = sdHeart(rp, SIZE_MIN + rnd * SIZE_RANGE);//这里的两个参数决定大小
			if (d < md)
            md = d;
    	}
    }
    #undef scale
    return md;
}

void main()
{
    vec2 uv = v_texCoords - 0.5;
    uv.x *= u_whRate;

	uv.y += t * 0.1;
	uv /= 0.13;
    uv.y *= 0.9;

	float d = vorohearts(uv);
	vec4 col = (abs(0.01 / min(-d, 0.0)) + d) *vec4(1.);//* vec3(0.900,0.061,0.825);
 	vec4 c_blank = vec4(1.);
    // col = smoothstep(1.,10.8,col);
	col = step(.9,col);
    col *= vec4(1.000,0.364,0.841,0.900);

    gl_FragColor = vec4(col);
}