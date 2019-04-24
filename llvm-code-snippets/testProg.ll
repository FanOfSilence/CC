@globalVar_2 = internal constant [10 x i8] c"/* world\A0\00"

@globalVar_1 = internal constant [10 x i8] c"hello */\A0\00"
declare void @printInt(i32 %n)
declare void @printString(i8* %s)
declare void @printDouble(double %d)
declare i32 @readInt()
define i32 @main() {
entry:

%var_1

call void @printInt(i32 
%var_1 = call i32 @fac(i32 10))

%var_2

call void @printInt(i32 
%var_2 = call i32 @rfac(i32 10))

%var_3

call void @printInt(i32 
%var_3 = call i32 @mfac(i32 10))

%var_4

call void @printInt(i32 
%var_4 = call i32 @ifac(i32 10))

; Declaring r
%r_p = alloca d32

store d32 0, d32* %r_p

; Initializing n
%n_p = alloca i32

store i32 10, i32* %n_p

; Initializing r
%r_p = alloca i32

store i32 1, i32* %r_p


; load var n
%var_5 = load i32, i32* %n_p

%var_6 =  icmp  sgt  i32  %var_5  ,  0 

br i1 %var_6, label %label_1, label %label_2
; trueLabel
label_1:
; Assigning to r

; load var r
%var_7 = load i32, i32* %r_p

; load var n
%var_8 = load i32, i32* %n_p
%var_9 =  mul i32  %var_7, %var_8
store i32 %var_9, i32* %r_p

%var_10 = load i32, i32* %n_p

%var_11 =  sub i32  %var_10, 1

store i32 %var_11, i32* %n_p


; load var n
%var_12 = load i32, i32* %n_p

%var_13 =  icmp  sgt  i32  %var_12  ,  0 

br i1 %var_13, label %label_1, label %label_2
; endLabel
label_2:

; load var r
%var_14 = load i32, i32* %r_p

call void @printInt(i32 %var_14)

%var_15

call void @printDouble(d32 
%var_15 = call d32 @dfac(d32 10.0))

%var_16 = getelementptr [10 x i8], [10 x i8]* @globalVar_1, i32 0, i32 0
call void @printString(i8* %var_16)

%var_17 = getelementptr [10 x i8], [10 x i8]* @globalVar_2, i32 0, i32 0
call void @printString(i8* %var_17)


ret i32 0

}
define i32 @fac(i32 a) {
entry:

; Declaring r
%r_p = alloca i32

store i32 0, i32* %r_p

; Declaring n
%n_p = alloca i32

store i32 0, i32* %n_p

; Assigning to r

store i32 1, i32* %r_p

; Assigning to n

; load var a
%var_18 = load i32, i32* %a_p

store i32 %var_18, i32* %n_p


; load var n
%var_19 = load i32, i32* %n_p

%var_20 =  icmp  sgt  i32  %var_19  ,  0 

br i1 %var_20, label %label_3, label %label_4
; trueLabel
label_3:
; Assigning to r

; load var r
%var_21 = load i32, i32* %r_p

; load var n
%var_22 = load i32, i32* %n_p
%var_23 =  mul i32  %var_21, %var_22
store i32 %var_23, i32* %r_p

; Assigning to n

; load var n
%var_24 = load i32, i32* %n_p
%var_25 =  sub i32  %var_24, 1
store i32 %var_25, i32* %n_p


; load var n
%var_26 = load i32, i32* %n_p

%var_27 =  icmp  sgt  i32  %var_26  ,  0 

br i1 %var_27, label %label_3, label %label_4
; endLabel
label_4:

; load var r
%var_28 = load i32, i32* %r_p

ret i32 %var_28

}
define i32 @rfac(i32 n) {
entry:


; load var n
%var_29 = load i32, i32* %n_p

%var_30 =  icmp  eq  i32  %var_29  ,  0 

br i1 %var_30, label %label_5, label %label_6
; trueLabel
label_5:


ret i32 1

; falseLabel
label_6:


; load var n
%var_31 = load i32, i32* %n_p

; load var n
%var_32 = load i32, i32* %n_p
%var_33 =  sub i32  %var_32, 1%var_34
%var_35 =  mul i32  %var_31, 
%var_34 = call i32 @rfac(i32 %var_33)
ret i32 %var_35

}
define i32 @mfac(i32 n) {
entry:


; load var n
%var_36 = load i32, i32* %n_p

%var_37 =  icmp  eq  i32  %var_36  ,  0 

br i1 %var_37, label %label_7, label %label_8
; trueLabel
label_7:


ret i32 1

; falseLabel
label_8:


; load var n
%var_38 = load i32, i32* %n_p

; load var n
%var_39 = load i32, i32* %n_p
%var_40 =  sub i32  %var_39, 1%var_41
%var_42 =  mul i32  %var_38, 
%var_41 = call i32 @nfac(i32 %var_40)
ret i32 %var_42

}
define i32 @nfac(i32 n) {
entry:


; load var n
%var_43 = load i32, i32* %n_p

%var_44 =  icmp  ne  i32  %var_43  ,  0 

br i1 %var_44, label %label_9, label %label_10
; trueLabel
label_9:


; load var n
%var_45 = load i32, i32* %n_p
%var_46 =  sub i32  %var_45, 1%var_47

; load var n
%var_48 = load i32, i32* %n_p
%var_49 =  mul i32  
%var_47 = call i32 @mfac(i32 %var_46), %var_48
ret i32 %var_49

; falseLabel
label_10:


ret i32 1

}
define d32 @dfac(d32 n) {
entry:


; load var n
%var_50 = load d32, d32* %n_p

%var_51 =  fcmp  eq  d32  %var_50  ,  0.0 

br i1 %var_51, label %label_11, label %label_12
; trueLabel
label_11:


ret d32 1.0

; falseLabel
label_12:


; load var n
%var_52 = load d32, d32* %n_p

; load var n
%var_53 = load d32, d32* %n_p
%var_54 =  fsub d32  %var_53, 1.0%var_55
%var_56 =  fmul d32  %var_52, 
%var_55 = call d32 @dfac(d32 %var_54)
ret d32 %var_56

}
define i32 @ifac(i32 n) {
entry:


; load var n
%var_57 = load i32, i32* %n_p
%var_58

ret i32 
%var_58 = call i32 @ifac2f(i32 1 ,i32 %var_57)

}
define i32 @ifac2f(i32 l, i32 h) {
entry:


; load var l
%var_59 = load i32, i32* %l_p

; load var h
%var_60 = load i32, i32* %h_p

%var_61 =  icmp  eq  i32  %var_59  ,  %var_60 

br label %label_13

; trueLabel
label_13:


; load var l
%var_62 = load i32, i32* %l_p

ret i32 %var_62


; load var l
%var_63 = load i32, i32* %l_p

; load var h
%var_64 = load i32, i32* %h_p

%var_65 =  icmp  sgt  i32  %var_63  ,  %var_64 

br label %label_14

; trueLabel
label_14:


ret i32 1

; Declaring m
%m_p = alloca i32

store i32 0, i32* %m_p

; Assigning to m

; load var l
%var_66 = load i32, i32* %l_p

; load var h
%var_67 = load i32, i32* %h_p
%var_68 =  add i32  %var_66, %var_67%var_69 =  sdiv i32  %var_68, 2
store i32 %var_69, i32* %m_p


; load var l
%var_70 = load i32, i32* %l_p

; load var m
%var_71 = load i32, i32* %m_p
%var_72

; load var m
%var_73 = load i32, i32* %m_p
%var_74 =  add i32  %var_73, 1
; load var h
%var_75 = load i32, i32* %h_p
%var_76
%var_77 =  mul i32  
%var_72 = call i32 @ifac2f(i32 %var_70 ,i32 %var_71), 
%var_76 = call i32 @ifac2f(i32 %var_74 ,i32 %var_75)
ret i32 %var_77

}