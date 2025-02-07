#pragma once
#include "common/sumset.h"

#define SUMSET_POOL_CAPACITY 1024

typedef struct SumsetPool {
    int size;
    Sumset *pool[SUMSET_POOL_CAPACITY];
    Sumset data[SUMSET_POOL_CAPACITY];
} SumsetPool;

static inline void memory_pool_init(SumsetPool *pool) {
    pool->size = SUMSET_POOL_CAPACITY - 1;
    for (int i = 0; i < SUMSET_POOL_CAPACITY; ++i)
        pool->pool[i] = &pool->data[i];
}

static inline Sumset *pool_malloc(SumsetPool *pool) {
    return pool->pool[pool->size--];
}

static inline void pool_free(SumsetPool *pool, Sumset *ptr) {
    pool->pool[++pool->size] = ptr;
}