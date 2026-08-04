#include <stdio.h>

#include "test.h"

int main(){
	struct testS ts;
	int failures = 0;

	ts.x = 21;

	test(&ts);

	fprintf(stderr, "test(%ld) = %ld\n", ts.x, ts.y);
	
	if(ts.y == -40){

	}else{
		failures = 1;
	}
	
	printf("%d\n", failures);

	return 0;
}
