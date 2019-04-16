define i1 @main() {
;add instruction variable  %result
;entry: %result = add i32 10, 20
;       ret i32 %result

;entry: ret i1 false

;need to use alloca for assignments
; %result is pointer and need to deallocate
;entry: %result = alloca i1
;       store i1 true, i1* %result
;       %res = load i1, i1* %result
;       ret i1 %res
entry: %res_p = alloca i32
       store i32 -12, i32* %res_p
       ret i1 true
}