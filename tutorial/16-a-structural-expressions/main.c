#include <stdio.h>
#include <stdlib.h>

#include "test.h"

int main(){
	struct testS ts;

	ts.a = 75;
	ts.s = malloc(sizeof(struct s));
	ts.s->a = 88;
	ts.s->t = malloc(sizeof(struct t));
	ts.s->t->a = 11;
	ts.ar = malloc(sizeof(long long) * 10);
	ts.ar[5] = 77;
	ts.s->ar = malloc(sizeof(long long) * 10);
	ts.s->ar[5] = 99;

	test(&ts);

	printf("test(...) = %ld\n", ts.y);

	return 0;
}
