#include <cmath>
#include <stdlib.h>
#include <stdio.h>

int main() {
    float param = 1;
    float sigval;
    while (param >= -10) {
        scanf("%f", &param);
        sigval = 1.0f/ (1.0f + exp(-1.0f*param));
        printf("%f\n", sigval);
    }
    return 0;
}
