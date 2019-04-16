even: runtime.ll even.ll
	llvm-link runtime.ll even.ll -o out.bc
	llc out.bc -o out.s
	gcc out.s -o even
	$(RM) out.bc out.s
sum: runtime.ll sum.ll
	llvm-link runtime.ll sum.ll -o out.bc
	llc out.bc -o out.s
	gcc out.s -o sum
	$(RM) out.bc out.s
test: runtime.ll test.ll
	llvm-link runtime.ll test.ll -o out.bc
	llc out.bc -o out.s
	gcc out.s -o test
	$(RM) out.bc out.s
newTest: runtime.ll newTest.ll
	llvm-link runtime.ll newTest.ll -o out.bc
	llc out.bc -o out.s
	gcc out.s -o newTest
	$(RM) out.bc out.s
negTest: runtime.ll negTest.ll
	llvm-link runtime.ll negTest.ll -o out.bc
	llc out.bc -o out.s
	gcc out.s -o negTest
	$(RM) out.bc out.s
clean:
	$(RM) even sum test newTest negTest
