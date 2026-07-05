#include <stdio.h>
#include <stdlib.h>

#include "test.h"

int main(){
	struct testS ts;

	ts.a = 50;

	test(&ts);

	printf("test(...) = %ld\n", ts.y);

	return 0;
}
