#pragma once
#include <pthread.h>
#include <common/err.h>

#define STACK_CAPACITY 4096

typedef struct Shared_sumset {
    int ref_count;
    struct Shared_sumset *prev;
    Sumset sumset;
} Shared_sumset;

typedef struct SumsetPool {
    int top;
    Shared_sumset *data[STACK_CAPACITY];
    Shared_sumset mem[STACK_CAPACITY];
} SumsetPool;

typedef struct State {
    Shared_sumset *a, *b;
} State;

typedef struct SynchronizedStack {
    bool all_tasks_done;
    int size, threads_working;
    pthread_mutex_t mutex;
    pthread_cond_t cond;
    SumsetPool memory_pool;
    State data[STACK_CAPACITY];
} SynchronizedStack;

static inline void sync_stack_init(SynchronizedStack *stack, const int num_threads) {
    *stack = (SynchronizedStack){.size = 0, .threads_working = num_threads, .all_tasks_done = false};
    ASSERT_ZERO(pthread_mutex_init(&stack->mutex, NULL));
    ASSERT_ZERO(pthread_cond_init(&stack->cond, NULL));

    for (int i = 0; i < STACK_CAPACITY; ++i)
        stack->memory_pool.data[i] = &stack->memory_pool.mem[i];
    stack->memory_pool.top = STACK_CAPACITY - 1;
}

// Helper, non thread-safe functions that should not be called by threads directly.
static inline Shared_sumset* create_shared_sumset_no_lock(SumsetPool *pool, const Sumset sumset, Shared_sumset *prev) {
    Shared_sumset *shared_sumset = pool->data[pool->top--];
    if (prev != NULL)
        prev->ref_count++;
    *shared_sumset = (Shared_sumset){.ref_count = 1, .prev = prev, .sumset = sumset};
    return shared_sumset;
}

static inline void shared_sumset_free_no_lock(SumsetPool *pool, Shared_sumset *shared_sumset) {
    while (--shared_sumset->ref_count == 0) {
        pool->data[++pool->top] = shared_sumset;
        if (shared_sumset->prev != NULL)
            shared_sumset = shared_sumset->prev;
        else
            break;
    }
}
// Create a shared sumset and push it to the stack.
static inline void create_shared_sumset_and_push(SynchronizedStack *stack, const Sumset a, Shared_sumset *prev, Shared_sumset *b) {
    ASSERT_ZERO(pthread_mutex_lock(&stack->mutex));

    Shared_sumset *shared_sumset = create_shared_sumset_no_lock(&stack->memory_pool, a, prev);
    b->ref_count++;
    stack->data[stack->size++] = (State){shared_sumset, b};
    ASSERT_ZERO(pthread_cond_signal(&stack->cond));

    ASSERT_ZERO(pthread_mutex_unlock(&stack->mutex));
}

// Take a state from the stack. If there are no states, wait for a signal.
static inline void sync_stack_pop(SynchronizedStack *stack, State *state, bool *all_tasks_done) {
    ASSERT_ZERO(pthread_mutex_lock(&stack->mutex));

    // If state is already initialized, it will be freed and replaced with a new one.
    if (state->a != NULL && state->b != NULL) {
        shared_sumset_free_no_lock(&stack->memory_pool, state->a);
        shared_sumset_free_no_lock(&stack->memory_pool, state->b);
    }

    while (stack->size == 0) {
        stack->threads_working--;

        if (stack->threads_working == 0) {
            stack->all_tasks_done = *all_tasks_done = true;
            ASSERT_ZERO(pthread_cond_broadcast(&stack->cond));
            ASSERT_ZERO(pthread_mutex_unlock(&stack->mutex));
            return;
        }

        ASSERT_ZERO(pthread_cond_wait(&stack->cond, &stack->mutex));
        stack->threads_working++;
    }

    // If thread wakes up, either all tasks are done or there is a task to do.
    if (stack->all_tasks_done)
        *all_tasks_done = true;
    else
        *state = stack->data[--stack->size];

    ASSERT_ZERO(pthread_mutex_unlock(&stack->mutex));
}

static inline void sync_stack_destroy(SynchronizedStack *stack) {
    ASSERT_ZERO(pthread_mutex_destroy(&stack->mutex));
    ASSERT_ZERO(pthread_cond_destroy(&stack->cond));
}
