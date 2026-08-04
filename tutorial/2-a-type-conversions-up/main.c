#include <stdio.h>

#include "test.h"

int main(){
	struct testS ts;
	int failures = 0;

	ts.x = -21;

	test(&ts);

	fprintf(stderr, "a = %d\n", ts.a);
	fprintf(stderr, "b = %f\n", ts.b);
	fprintf(stderr, "test(%d) = %f\n", ts.x, ts.y);
	
	if(ts.y == -21.0){

	}else{
		failures = 1;
	}
	
	printf("%d\n", failures);

	return 0;
}
