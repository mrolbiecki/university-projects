#pragma once
#include "common/sumset.h"

#define STACK_CAPACITY 1024

// type 0: free sumset, type 1: process sumset
typedef struct Task {
    int type;
    Sumset *a, *b;
} Task;

typedef struct Stack {
    int size;
    Task data[STACK_CAPACITY];
} Stack;

static inline void stack_init(Stack *stack) {
    stack->size = 0;
}

static inline void stack_push(Stack *stack, const Task task) {
    stack->data[stack->size++] = task;
}

static inline void stack_pop(Stack *stack, Task *task) {
    *task = stack->data[--stack->size];
}