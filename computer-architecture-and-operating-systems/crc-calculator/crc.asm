global _start

; system functions and parameters
SYS_EXIT equ 60
SYS_WRITE equ 1
STDOUT equ 1
SYS_CLOSE equ 3
SYS_OPEN equ 2
READ_ONLY equ 0
SYS_READ equ 0
LSEEK equ 8
SEEK_CUR equ 1

; my constants
BUFF_SIZE equ 65536 ; this buffer size allows me to read maximum possible
                    ; fragments of data at once (65535 = 2^16)
MAX_POLY_LEN equ 64
NO_BYTES equ 256 ; number of all possible bytes - for lookup table
NULL_TERMINATOR equ 0

section .bss
    buffer resb BUFF_SIZE ; buffer to store file contents
    lookup resq NO_BYTES ; lookup table for calculating CRC
    data_len resw 1 ; length of data in fragment
    offset resd 1 ; offset to next fragment
    poly_len resb 1 ; polynomial length
    fd resq 1 ; file descriptor
    crc resq 1 ; CRC value
    ans resb MAX_POLY_LEN + 1 ; buffer to store output string (including newline)

section .text

_start:
    ; check if the number of arguments is correct
    cmp qword [rsp], 3
    jne error_exit

    ; load the address of the CRC polynomial string
    mov rsi, [rsp+24]
    
    ; calculate the length of the polynomial string and convert it to a number
    xor rcx, rcx ; rcx - string length counter
    xor r11, r11 ; r11 - polynomial in binary

find_len:
    mov al, [rsi + rcx] ; load single char to al
    cmp al, NULL_TERMINATOR ; check if it's the end of the string
    je done_len
    shl r11, 1 ; shift polynomial left to make room for the next bit
    sub al, '0' ; convert from a character to a number
    cmp al, 1 ; if different from 0 or 1, exit
    ja error_exit
    movzx rax, al ; extend to 64-bit for addition
    add r11, rax ; add the next bit
    inc rcx ; increment string length
    jmp find_len

done_len:
    ; now rcx contains polynomial length 
    ; and r11 contains polynomial in binary
    cmp rcx, MAX_POLY_LEN ; if polynomial is too long, exit
    jg error_exit
    test rcx, rcx ; if polynomial is empty (constant), exit
    jz error_exit
    mov [poly_len], cl ; save polynomial length in poly_len
    
    mov cl, MAX_POLY_LEN ; fix the polynomial to size 64
    sub cl, [poly_len]
    shl r11, cl ; shift as far left as possible

    ; polynomial in r11 will be used to calculate lookup table
calculate_lookup_table:
    mov rdx, 1
    shl rdx, 63 ; rdx = 2^63 for checking the msb
    mov rax, rdx ; rax - CRC value for powers of two
                 ; initially 2^63 to pass the first test
    mov rcx, 1   ; rcx - power of two counter

    ; calculating CRC value for all powers of two from 1 to 128
power_two_loop:
    test rax, rdx ; if msb is not set
    jz just_shift ; then just shift the number left

xor_with_poly:
    shl rax, 1 ; if msb is set, shift left and xor with polynomial
    xor rax, r11  ; just like in standard CRC algorithm
    jmp other_bytes_loop_prep 

just_shift:
    shl rax, 1

    ; for bytes different than powers of two
    ; utilize this property: lookup[i ^ j] = lookup[i] ^ lookup[j]
    ; lookup table for numbers other than powers of two is calculated as a
    ; xor of the biggest possible power of two and smaller, previously calculated
    ; value

    ; for example: lookup[1011] = lookup[1000] ^ lookup[11]
    ; where rcx = 1000 and r9 = 11

other_bytes_loop_prep:
    xor r9, r9 ; r9 - counter for smaller values used to xor with the power of two

other_bytes_loop:
    mov r8, rax ; r8 - temporary CRC value
    xor r8, [lookup + r9 * 8] ; xor with lookup table
    lea rdi, [rcx + r9] ; calculate destination index
                        ; here xor is the same as addition
    mov [lookup + rdi * 8], r8 ; store calculated CRC value
    inc r9
    cmp r9, rcx ; compare smaller value with current power of two
    jb other_bytes_loop
    shl rcx, 1 ; next power of two
    cmp rcx, NO_BYTES
    jb power_two_loop
    ; end of calculating lookup table
    ; polynomial in r11 is no longer needed
    
open_file:
    mov rdi, [rsp+16] ; address of the file name
    mov rax, SYS_OPEN
    mov rsi, READ_ONLY
    syscall
    test rax, rax ; check if file has been opened properly
    js error_exit
    mov [fd], rax ; store file descriptor

    xor r10, r10 ; initialize CRC to 0
                 ; r10 is the CRC value for the rest of the program

read_data_length:
    mov rax, SYS_READ
    mov rdi, [fd]
    mov rsi, data_len ; data length is stored in [data_len]
    mov rdx, 2
    syscall
    cmp rax, 2 ; check if reading was successful
    jne error_exit_and_close

read_data:
    movzx edx, word [data_len] ; get data length
    mov rax, SYS_READ
    mov rdi, [fd]
    mov rsi, buffer ; normal data is stored in the buffer
    syscall
    cmp rax, rdx ; check if reading was successful
    jne error_exit_and_close
    test rax, rax ; update CRC only if the data size is nonzero
    jz read_offset

crc_update:
    xor rcx, rcx ; initialize buffer offset

crc_update_loop:
    mov rdx, r10 ; get current CRC value
    shr rdx, 56 ; shift to get the most significant byte
    xor dl, [buffer + rcx] ; xor with the current byte from buffer
    movzx rdx, dl ; extend to 64-bit
    shl r10, 8 ; shift CRC left by one byte
    xor r10, [lookup + rdx * 8] ; xor with lookup table value
    inc rcx ; increment buffer offset
    cmp cx, word [data_len] ; compare with data length
    jb crc_update_loop ; if less, repeat loop

read_offset:
    mov rax, SYS_READ
    mov rdi, [fd]
    mov rsi, offset ; offset is stored in [offset]
    mov rdx, 4
    syscall
    cmp rax, 4 ; check if reading was successful
    jne error_exit_and_close

    ; check if offset points to the fragment itself
    mov ax, [data_len]
    movzx eax, ax
    neg eax
    sub eax, 6 ; eax = -data_len - 6, because data length and the offset
               ; take up 6 bytes together
    mov edx, [offset]
    cmp edx, eax ; if offset points to the fragment itself, skip the lseek
    je fix_crc

    mov rax, LSEEK
    mov rdi, [fd]
    mov esi, [offset]
    movsx rsi, esi
    mov rdx, SEEK_CUR ; it means that the offset is counted from the current position
    syscall
    test rax, rax
    js error_exit_and_close
    jmp read_data_length ; loop

fix_crc:
    ; shift CRC to its previous size
    mov cl, MAX_POLY_LEN
    sub cl, [poly_len]
    shr r10, cl

convert_to_string:
    xor rcx, rcx ; initialize string length counter
    mov cl, [poly_len] ; get polynomial length
    mov rdi, ans ; set answer buffer as destination
    xor rdx, rdx ; initialize answer buffer offset

convert_loop:
    test cl, cl ; check if counter is zero
    jz write_answer
    dec cl
    mov rax, r10 ; get current CRC value
    shr rax, cl ; shift right by counter value
    and rax, 1 ; mask to get least significant bit
    add rax, '0' ; convert bit to character
    mov [rdi + rdx], al ; store character in answer buffer
    inc rdx ; increment answer buffer offset
    jmp convert_loop

write_answer:
    mov byte [rdi + rdx], 0x0A ; add newline at the end
    inc rdx ; increment answer buffer offset because of the newline added

    mov rax, SYS_WRITE
    mov rdi, STDOUT
    mov rsi, ans
    mov rdx, rdx ; buffer offset is the number of bytes to write
    syscall
    jmp normal_exit

error_exit_and_close:
    mov rax, SYS_CLOSE
    mov rdi, [fd]
    syscall

error_exit:
    mov rax, SYS_EXIT
    mov rdi, 1
    syscall

normal_exit:
    mov rax, SYS_EXIT
    xor rdi, rdi
    syscall
