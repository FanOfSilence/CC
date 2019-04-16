declare void @printInt(i32 %n)

define i32 @main() {
entry: %x_p = alloca i32
       store i32 2, i32* %x_p
       %x = load i32, i32* %x_p

       %n_x = sub i32 0, %x
       call void @printInt(i32 %n_x)
;       %t = add i32 -(%x), 2
       ret i32 0
}