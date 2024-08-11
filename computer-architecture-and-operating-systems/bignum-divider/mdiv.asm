; Marcin Rolbiecki
global mdiv
mdiv:
    mov r8, rdx
    lea r11, [rdi + (rsi-1) * 8]
    xor dil, dil
    ; Use dil register to store true/false information:
    ; r9d & 1 - remainder is negative
    ; r9d & 2 - result is negative
    ; r9d & 4 - division has already been done
    ; Some registers do not change:
    ; rsi - size of the array, r8 - divisor, r11 - pointer to the last element of the array
    xor qword [r11], 0 ; if the dividend is positive
    jns control ; skip changing the sign
    xor dil, 3 ; if the dividend is negative, then the remainder and the result are negative (this can be changed later)

changeSign: ; change the sign of the array by negating it and adding one
    mov rcx, rsi ; rcx - loop counter
    mov r10, r11 ; r10 - points to the last element
L1: ; negation
    not qword [r10]
    sub r10, 8
    loop L1
L2: ; addition
    add r10, 8 ; after the previous loop r10 is set to first element - 8
    add qword [r10], 1
    jnz control ; if the result of the addition is not zero, then the carry bit is off, and we can continue
    inc rcx
    cmp rcx, rsi
    jne L2

control:
    test dil, 4 ; check whether it's the first or the second time the sign has been changed
    jnz end
; checking the sign of the divisor
    test r8, r8
    jns divide
    neg r8 ; if the divisor is negative, make it positive
    xor dil, 2 ; and flip the sign of the result

divide:
    mov rcx, rsi ; rcx - loop counter
    mov r10, r11 ; r10 - points to the last element
    xor edx, edx ; rdx must be zero because it is used by div
L3:
    mov rax, qword [r10] ; common long division algorithm
    div r8
    mov [r10], rax
    sub r10, 8
    loop L3

    mov rax, rdx ; move remainder to rax, because it should be returned as the result
    test dil, 1 ; check the sign of the remainder
    jz L4 ; if it should be positive, we don't have to do anything
    neg rax ; if it should be negative, flip the sign
L4:
    xor dil, 4 ; store the information about the division
    test dil, 2 ; check the sign of the result
    jnz changeSign ; if it should be negative, flip the sign
                         ; negative result never causes overflow
    xor qword [r11], 0 ; if it should be positive, check the sign of the result
    js overflow ; if it's on, then we know that an overflow happened

end:
    ret

overflow:
    xor eax, eax
    div eax ; division by zero causes SIGFPE
