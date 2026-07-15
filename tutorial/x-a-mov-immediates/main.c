#include <stdio.h>

#include "test.h"
#include "../simdlib.c"

int main(){
	struct testS ts;

	test(&ts);

	printf("u8: %x\n", ts.a);
	printf("u16: %x\n", ts.b);
	printf("u32: %x\n", ts.c);
	printf("u64: %lx\n", ts.d);
	
	printf("s8: %d\n", ts.e);
	printf("s16: %d\n", ts.f);
	printf("s32: %d\n", ts.g);
	printf("s64: %ld\n", ts.h);
	
	printf("f32: %.7f\n", ts.i);
	printf("f64: %.15f\n", ts.j);
	
	print_m128_u16(ts.k);
	//print_m256_u16(ts.l);

	return 0;
}
