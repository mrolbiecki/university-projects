add_executable(parallel main.c
        synchronized_stack.h)
target_link_libraries(parallel io err)
