declare void @printInt(i32 %n)

; int main()
define i32 @main() {

; int x;
entry: %x_p = alloca i32
       store i32 0, i32* %x_p
       %x = load i32, i32* %x_p

; int y = 56;
       %y_p = alloca i32
       store i32 56, i32* %y_p
       %y = load i32, i32* %y_p

; y + 45
       %t1 = add i32 %y, 45

; y + 45 <= 2
       %t2 = icmp sle i32 %t1, 2

; if (y + 45 <= 2)
       br i1 %t2, label %True,
                  label %False

; { x = 1;}
       True:
           %x1_p = alloca i32
           store i32 2, i32* %x1_p
           %x1 = load i32, i32* %x1_p

; printInt(x);
; return 0;
	   call void @printInt(i32 %x1)
	   ret i32 0

; {x = 2;};
       False:
	   %x2_p = alloca i32
           store i32 2, i32* %x2_p
           %x2 = load i32, i32* %x2_p

; printInt(x);
; return 0;
	   call void @printInt(i32 %x2)
	   ret i32 0
}
		
       