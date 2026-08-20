
void print_m128_f32(__m128 reg) {
    float *values = (float *)&reg;
    
    printf("f32: %f, %f, %f, %f\n", values[0], values[1], values[2], values[3]);
}

void print_m128_f64(__m128 reg) {
    double *values = (double *)&reg;
    
    printf("f64: %f, %f\n", values[0], values[1]);
}

void print_m256_f64(__m256 reg) {
    double *values = (double *)&reg;
    
    printf("f64: %f, %f, %f, %f\n", values[0], values[1], values[2], values[3]);
}

void print_m512_f64(__m512 reg) {
    double *values = (double *)&reg;
    
    printf("f64: %f, %f, %f, %f, %f, %f, %f, %f\n", values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7]);
}

void print_m128_u8(__m128 reg) {
    uint8_t *values = (uint8_t *)&reg;
    
    printf("u8: %u, %u, %u, %u, %u, %u, %u, %u, %u, %u, %u, %u, %u, %u, %u, %u\n", values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15]);
}

void print_m128_u16(__m128 reg) {
    uint16_t *values = (uint16_t *)&reg;

    printf("u16: %u, %u, %u, %u, %u, %u, %u, %u\n", values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7]);
}

void print_m128_u32(__m128 reg) {
    uint32_t *values = (uint32_t *)&reg;

    printf("u32: %u, %u, %u, %u\n", values[0], values[1], values[2], values[3]);
}

void print_m128_u64(__m128 reg) {
    uint64_t *values = (uint64_t *)&reg;
    
    printf("u64: %lu, %lu\n", values[0], values[1]);
}

void print_m128_s8(__m128 reg) {
    int8_t *values = (int8_t *)&reg;
    
    printf("s8: %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d\n", values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15]);
}

void print_m128_s16(__m128 reg) {
    int16_t *values = (int16_t *)&reg;

    printf("s16: %d, %d, %d, %d, %d, %d, %d, %d\n", values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7]);
}

void print_m128_s32(__m128 reg) {
    int32_t *values = (int32_t *)&reg;

    printf("s32: %d, %d, %d, %d\n", values[0], values[1], values[2], values[3]);
}

void print_m128_b8(__m128 reg) {
    uint8_t *values = (uint8_t *)&reg;
    
    printf("b8: %.2x, %.2x, %.2x, %.2x, %.2x, %.2x, %.2x, %.2x, %.2x, %.2x, %.2x, %.2x, %.2x, %.2x, %.2x, %.2x\n", values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15]);
}

void print_m128_c8(__m128 reg) {
    uint8_t *values = (uint8_t *)&reg;
    
    printf("b8: %c, %c, %c, %c, %c, %c, %c, %c, %c, %c, %c, %c, %c, %c, %c, %c\n", values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15]);
}

void print_m256_b8(__m256 reg) {
    uint8_t *values = (uint8_t *)&reg;
    
    printf("b8: ");
    for(int i = 0; i < 32; i++){
      printf("%.2x", values[i]);
      if(i + 1 != 32){
        printf(", ");
      }
    }
    printf("\n");
    
    printf("b8: %c, %c, %c, %c, %c, %c, %c, %c, %c, %c, %c, %c, %c, %c, %c, %c\n", values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15]);
}

void print_m256_c8(__m256 reg) {
    uint8_t *values = (uint8_t *)&reg;
    
    printf("b8: ");
    for(int i = 0; i < 32; i++){
      printf("%c", values[i]);
      if(i + 1 != 32){
        printf(", ");
      }
    }
    printf("\n");
    
    printf("b8: %c, %c, %c, %c, %c, %c, %c, %c, %c, %c, %c, %c, %c, %c, %c, %c\n", values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15]);
}

void print_m128_b32(__m128 reg) {
    uint32_t *values = (uint32_t *)&reg;
    
    printf("b32: %.8x, %.8x, %.8x, %.8x\n", values[0], values[1], values[2], values[3]);
}

void print_m128_b64(__m128 reg) {
    uint64_t *values = (uint64_t *)&reg;
    
    printf("b64: %.16lx, %.16lx\n", values[0], values[1]);
}

void print_m128_b128(__m128 reg) {
    uint8_t *values = (uint8_t *)&reg;
    
    printf("b8 values: %.2x%.2x%.2x%.2x%.2x%.2x%.2x%.2x%.2x%.2x%.2x%.2x%.2x%.2x%.2x%.2x\n", values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15]);
}

