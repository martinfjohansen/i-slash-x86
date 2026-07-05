#include <stdio.h>
#include <stdlib.h>

#include "test.h"

int main(){
	struct testS ts;

	ts.x = malloc(sizeof(struct C *) * 100);
	ts.x[50] = malloc(sizeof(struct C));
	ts.x[50]->x = 1.234567;

	test(&ts);

	printf("test(...) = %f\n", ts.y);

	return 0;
}
