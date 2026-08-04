#include <stdio.h>
#include <stdlib.h>

#include "test.h"

int main(){
	struct testS ts;
	int failures = 0;

	ts.x = malloc(sizeof(double) * 100);
	ts.x[50] = 1.2345;

	test(&ts);

	fprintf(stderr, "test(...) = %f\n", ts.y);
	
	if(ts.y == 1.2345){

	}else{
		failures = 1;
	}
	
	printf("%d\n", failures);

	return 0;
}
