@globalVar_1 = internal constant [5 x i8] c"foo\A0\00"
declare void @printInt(i32 %n)
declare void @printString(i8* %n)
define i32 @main() {
entry:


call void @foo()
ret i32 0
}
define void @foo() {
entry:

%var_1 = getelementptr [5 x i8], [5 x i8]* @globalVar_1, i32 0, i32 0
call void @printString(i8* %var_1)
ret void
}
