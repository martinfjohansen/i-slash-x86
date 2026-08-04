#include <stdio.h>
#include <stdlib.h>

#include "test.h"

int main(){
	struct testS ts;
	int failures = 0;

	ts.d0 = 0xAB;
	ts.d1 = 0xCD;
	ts.d2 = 0xEF;

	test(&ts);

	fprintf(stderr, "test(...) = 0x%X\n", ts.y);
	
	if(ts.y == 0xabcdef){

	}else{
		failures = 1;
	}
	
	printf("%d\n", failures);

	return 0;
}
