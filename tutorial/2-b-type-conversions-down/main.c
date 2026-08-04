#include <stdio.h>

#include "test.h"

int main(){
	struct testS ts;
	int failures = 0;

	ts.x = -21.0;

	test(&ts);

	fprintf(stderr, "x = %f\n", ts.x);
	fprintf(stderr, "a = %f\n", ts.a);
	fprintf(stderr, "b = %d\n", ts.b);
	fprintf(stderr, "c = %d\n", ts.c);
	fprintf(stderr, "y = %d\n", ts.y);
	fprintf(stderr, "test(%f) = %d\n", ts.x, ts.y);
	
	if(ts.y == -21){

	}else{
		failures = 1;
	}
	
	printf("%d\n", failures);

	return 0;
}
