#include <stdio.h>
#include <stdlib.h>

#include "test.h"

int main(){
	struct testS ts;
	int failures = 0;

	ts.c = malloc(sizeof(double) * 100);
	ts.c->x = 1.234567;

	test(&ts);

	fprintf(stderr, "test(...) = %f\n", ts.y);
	
	if(ts.y == 1.234567){

	}else{
		failures = 1;
	}
	
	printf("%d\n", failures);

	return 0;
}
