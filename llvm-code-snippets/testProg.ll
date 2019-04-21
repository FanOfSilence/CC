@string = internal constant [5 x i8] c"foo\A0\00"
declare void @printInt(i32 %n)
declare void @printString(i8* %n)
define i32 @main() {
entry:


call void @foo()
ret i32 0
}
define void @foo() {
entry:


call void @printString(i8* @string)
ret void
}