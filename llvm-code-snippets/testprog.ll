declare void @printInt(i32 %n)
define i32 @main() {
; Declaring x
%x_p = alloca i32

store i32 0, i32* %x_p

; Initializing y
%y_p = alloca i32

store i32 56, i32* %y_p







; load var y
%var_1 = load i32, i32* %y_p
%mulVar =  mul i32  %var_1, 45
%relVar =  icmp  sge  i32  %mulVar  ,  200 




br i1 %relVar, label %label_1, label %label_2
; trueLabel
label_1:

; Assigning to x
store i32 1, i32* %x_p


; falseLabel
label_2:

; Assigning to x
store i32 2, i32* %x_p


; load var x
%var_2 = load i32, i32* %x_p

call void@printInt(i32%var_2)
ret i32 0
}
