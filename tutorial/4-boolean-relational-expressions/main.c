#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>

#include "test.h"

int main(){
	struct testS ts;
	int failures = 0;

	ts.a = 75;

	test(&ts);

	fprintf(stderr, "test(...) = %d\n", ts.y);
	
	if(ts.y == true){

	}else{
		failures = 1;
	}
	
	printf("%d\n", failures);

	return 0;
}
