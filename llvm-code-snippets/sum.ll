declare void @printInt(i32 %n)

define i32 @main() {
entry: %t1 = call i32 @sum(i32 100)
       call void @printInt(i32 %t1)
       ret i32 0
}

define i32 @sum (i32 %n) {
entry: %sum = alloca i32
       store i32 0, i32* %sum
       %i = alloca i32
       store i32 0, i32* %i
       br label %lab1
lab1:  %t1 = load i32, i32* %i
       %t2 = add i32 %t1, 1
       %t3 = load i32, i32* %sum
       %t4 = add i32 %t2, %t3
       store i32 %t2, i32* %i
       store i32 %t4, i32* %sum
       %t5 = icmp eq i32 %t2, %n
       br i1 %t5, label %end,
                  label %lab1
end:   ret i32 %t4
}
