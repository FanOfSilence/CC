define i32 @fun(i32 %y) {
entry: %x_p = alloca i32
       store i32 0, i32* %x_p
       %x = load i32, i32* %x_p

       %t1 = icmp sge i32 %y, 2

       br i1 %t1, label %True, label %False

       True:
           %x1_p = alloca i32
	   store i32 3, i32* %x1_p
	   %x1 = load i32, i32* %x1_p
	   br label %EndTrue
       False:
           %x2_p = alloca i32
	   store i32 4, i32* %x2_p
	   %x2 = load i32, i32* %x2_p
	   br label %EndFalse

       EndTrue:
              printInt(%x1)
       EndFalse:
              printInt(%x2)
	      