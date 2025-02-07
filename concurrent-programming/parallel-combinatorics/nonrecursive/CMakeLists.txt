add_executable(nonrecursive main.c
        stack.h
        sumset_pool.h
)
target_link_libraries(nonrecursive io err)
