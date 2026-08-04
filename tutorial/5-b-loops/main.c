#include <stdio.h>
#include <stdlib.h>

#include "test.h"

int main(){
	struct testS ts;
	int failures = 0;

	ts.x = 10;

	test(&ts);

	fprintf(stderr, "test(...) = %ld\n", ts.y);
	
	if(ts.y == 45){

	}else{
		failures = 1;
	}
	
	printf("%d\n", failures);

	return 0;
}
