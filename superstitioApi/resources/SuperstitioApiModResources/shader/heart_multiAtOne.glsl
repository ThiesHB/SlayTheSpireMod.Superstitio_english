//多个爱心叠加在一起的图像
#ifdef GL_ES
precision lowp float;
#else
precision lowp float;
#endif
uniform float u_time;
uniform vec2 u_resolution;

#define PI 3.1415926
#define TIMER1 u_time
//-2时完全消失
//(sin(u_time*PI))*1.


// vec3 paintHeart(vec3 col, vec3 col1, float x, float y)
// {
// 	float r = x*x + pow((y - pow(x*x, 1.0/3.0)), 2.0);
// 	r -= pow(TIMER1, 10.0);

// 	if (r < 1.5) {
// 		col = col1 * r;
// 	}
// 	return col;
// }

vec4 paintSpecialHeart(vec4 col, vec4 col1, float x, float y)
{
	float r = x*x + pow((y - pow(x*x, 1.0/3.0)), 2.0);
    r -= TIMER1 ;//- 0.5;
		if ((r < 2.0 && r > 1.5) || (r < 1.0 && r > 0.6) || (r < 0.3 && r > 0.0)) {
		col = col1 * r * 0.5*(TIMER1+2.0);
		//col = col1 * r * 3.0;
		}
	return col;
}

#define TIMER2 1.
//abs(sin(u_time*PI))-0.2
void main()
{
	vec2 st =  gl_FragCoord.xy/u_resolution.xy;
    st -= 0.;
	st.y *= max(u_resolution.y,u_resolution.x)/u_resolution.x;
    st.x *= max(u_resolution.y,u_resolution.x)/u_resolution.y;
	st.y *= 1.5;
    st *= 4.0;
	// vec2 p2 = 45.0 * (gl_FragCoord.xy / u_resolution.y);

	vec4 col = vec4(0.0);
	vec4 col1 = mix(vec4(1.0,0.0,0.6,1.0), vec4(1.0,0.0,0.4,1.0), sqrt(st.y));
	// vec3 col2 = mix(vec3(1.0,0.0,0.1), vec3(1.0,0.1,0.0), pow(st.y, 1.3));

	float x = st.x - 2.0;
	float y = st.y - 1.65;

	// if (length(u_mouse.x) > p.x*200.0) {
	// 	col1 = vec3(0.1,0.1,0.5);
	// 	col2 = vec3(0.1,0.9,0.3);
	// }

	col = paintSpecialHeart(col, col1, x, y);
    col *= TIMER2;

	gl_FragColor = vec4(col);
}