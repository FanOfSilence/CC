declare void @printString(i8* %s)
declare i32 @readInt()

@evenString = internal constant [7 x i8] c"Even!\0A\00"
@oddString  = internal constant [6 x i8] c"Odd!\0A\00"

define i32 @main() {
entry: %t0 = call i32 @readInt()
       %t1 = call i1 @even(i32 %t0)
       br i1 %t1, label %lab1,
                  label %lab2
lab1:  %t2 = getelementptr [7 x i8], [7 x i8]* @evenString, i32 0, i32 0
       call void @printString(i8* %t2)
       br label %lab3
lab2:  %t3 = getelementptr [6 x i8], [6 x i8]* @oddString, i32 0, i32 0
       call void @printString(i8* %t3)
       br label %lab3
lab3:  ret i32 0
}

define i1 @even(i32 %n) {
entry: %t1 = icmp eq i32 %n, 0
       br i1 %t1, label %lab1,
                  label %lab2
lab1:  ret i1 true
lab2:  %t2 = sub i32 %n, 1
       %t3 = tail call i1 @odd(i32 %t2)
       ret i1 %t3
}

define i1 @odd(i32 %n) {
entry: %t1 = icmp eq i32 %n, 0
       br i1 %t1, label %lab1,
                  label %lab2
lab1:  ret i1 false
lab2:  %t2 = sub i32 %n, 1
       %t3 = tail call i1 @even(i32 %t2)
       ret i1 %t3
}
