declare void @printInt(i32 %n)

; int main()
define i32 @main() {

; int x;
entry: %x_p = alloca i32
       store i32 0, i32* %x_p
       %x = load i32, i32* %x_p

; 1 <= 2
       %t2 = icmp sle i32 3, 2

; if (1 <= 2)
       br i1 %t2, label %True,
                  label %False

; { x = 1;}
       True:
;           %x1_p = alloca i32
           store i32 1, i32* %x_p
	   br label %End
;           %x1 = load i32, i32* %x1_p

; {x = 2;};
       False:
;	   %x2_p = alloca i32
           store i32 2, i32* %x_p
	   br label %End
;           %x2 = load i32, i32* %x2_p

; printInt(x);
; return 0;
       End:
	   %new_x = load i32, i32* %x_p
	   call void @printInt(i32 %new_x)
	   ret i32 0
}
	