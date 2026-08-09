#include <stdio.h>
#include <stdlib.h>
#include <immintrin.h>

#include "test.h"

#include "../simdlib.c"

int main(){
	struct testS ts;

	test(&ts);

	print_m128_f32(ts.x);
	print_m128_f32(ts.y);
	print_m128_f32(ts.z);
	
	print_m128_u32(ts.a);
	print_m128_u32(ts.b);
	print_m128_u32(ts.c);
	
	print_m512_f64(ts.g);

	/*if(ts.y == 0x6add1e80){
		printf("success\n");
	}else{
		printf("fail\n");
	}*/

	return 0;
}
