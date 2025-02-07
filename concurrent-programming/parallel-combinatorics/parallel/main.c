#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include "common/io.h"
#include "common/sumset.h"
#include "synchronized_stack.h"

SynchronizedStack task_pool;
InputData input_data;
Solution best_solution;

//processes one task and pushes all new tasks to the stack
static inline void solve_producer(Shared_sumset *a, Shared_sumset *b, Solution *local_best_solution) {
    if (a->sumset.sum > b->sumset.sum)
        return solve_producer(b, a, local_best_solution);

    if (is_sumset_intersection_trivial(&a->sumset, &b->sumset)) {
        for (int i = a->sumset.last; i <= input_data.d; ++i) {
            if (!does_sumset_contain(&b->sumset, i)) {
                Sumset a_with_i;
                sumset_add(&a_with_i, &a->sumset, i);
                create_shared_sumset_and_push(&task_pool, a_with_i, a, b);
            }
        }
    } else if ((a->sumset.sum == b->sumset.sum) &&
               (get_sumset_intersection_size(&a->sumset, &b->sumset) == 2)) {
        if (b->sumset.sum > local_best_solution->sum)
            solution_build(local_best_solution, &input_data, &a->sumset, &b->sumset);
    }
}

//processes one task and all its subtasks without synchronization
static void solve_consumer(Sumset *a, Sumset *b, Solution *local_best_solution) {
    if (a->sum > b->sum)
        return solve_consumer(b, a, local_best_solution);

    if (is_sumset_intersection_trivial(a, b)) {
        for (int i = a->last; i <= input_data.d; ++i) {
            if (!does_sumset_contain(b, i)) {
                Sumset a_with_i;
                sumset_add(&a_with_i, a, i);
                solve_consumer(&a_with_i, b, local_best_solution);
            }
        }
    } else if ((a->sum == b->sum) &&
               (get_sumset_intersection_size(a, b) == 2)) {
        if (b->sum > local_best_solution->sum)
            solution_build(local_best_solution, &input_data, a, b);
    }
}

void *thread_function(void *ptr) {
    Solution local_best_solution;
    solution_init(&local_best_solution);

    State state = {NULL, NULL};
    bool all_tasks_done = false;

    while (1) {
        sync_stack_pop(&task_pool, &state, &all_tasks_done);
        if (all_tasks_done)
            break;

        //if there are few tasks, produce more
        if (task_pool.size < 2 * input_data.t)
            solve_producer(state.a, state.b, &local_best_solution);
        else
            solve_consumer(&state.a->sumset, &state.b->sumset, &local_best_solution);
    }

    ASSERT_ZERO(pthread_mutex_lock(&task_pool.mutex));
    if (local_best_solution.sum > best_solution.sum)
        best_solution = local_best_solution;
    ASSERT_ZERO(pthread_mutex_unlock(&task_pool.mutex));

    return NULL;
}

int main(void) {
    input_data_read(&input_data);
    solution_init(&best_solution);
    sync_stack_init(&task_pool, input_data.t);
    Shared_sumset *b = create_shared_sumset_no_lock(&task_pool.memory_pool, input_data.b_start, NULL);
    create_shared_sumset_and_push(&task_pool, input_data.a_start, NULL, b);

    pthread_t threads[input_data.t];
    for (int i = 0; i < input_data.t; i++)
        ASSERT_ZERO(pthread_create(&threads[i], NULL, thread_function, NULL));

    for (int i = 0; i < input_data.t; i++)
        ASSERT_ZERO(pthread_join(threads[i], NULL));

    sync_stack_destroy(&task_pool);
    solution_print(&best_solution);
    return 0;
}