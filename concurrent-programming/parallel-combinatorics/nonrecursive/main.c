#include "common/io.h"
#include "common/sumset.h"
#include "stack.h"
#include "sumset_pool.h"

#include <stdio.h>

int main(void) {
    InputData input_data;
    input_data_read(&input_data);

    Solution best_solution;
    solution_init(&best_solution);

    Stack stack;
    stack_init(&stack);
    SumsetPool sumset_pool;
    memory_pool_init(&sumset_pool);

    Sumset *a = pool_malloc(&sumset_pool);
    Sumset *b = pool_malloc(&sumset_pool);
    *a = input_data.a_start;
    *b = input_data.b_start;

    stack_push(&stack, (Task){.type = 0, .a = a, .b = NULL});
    stack_push(&stack, (Task){.type = 0, .a = b, .b = NULL});
    stack_push(&stack, (Task){.type = 1, .a = a, .b = b});

    while (stack.size > 0) {
        Task task;
        stack_pop(&stack, &task);

        if (task.type == 0) {
            pool_free(&sumset_pool, task.a);
            continue;
        }

        if (task.a->sum > task.b->sum) {
            // swap a and b
            Sumset *tmp = task.a;
            task.a = task.b;
            task.b = tmp;
        }

        if (is_sumset_intersection_trivial(task.a, task.b)) {
            for (int i = task.a->last; i <= input_data.d; ++i) {
                if (!does_sumset_contain(task.b, i)) {
                    Sumset *a_with_i = pool_malloc(&sumset_pool);
                    sumset_add(a_with_i, task.a, i);
                    // after all tasks are processed, a_with_i will be freed
                    stack_push(&stack, (Task){.type = 0, .a = a_with_i, .b = NULL});
                    stack_push(&stack, (Task){.type = 1, .a = a_with_i, .b = task.b});
                }
            }
        } else if (task.a->sum == task.b->sum && get_sumset_intersection_size(task.a, task.b) == 2) {
            if (task.b->sum > best_solution.sum) {
                solution_build(&best_solution, &input_data, task.a, task.b);
            }
        }
    }

    solution_print(&best_solution);
    return 0;
}