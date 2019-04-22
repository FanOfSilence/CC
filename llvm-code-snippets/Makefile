even: runtime.ll even.ll
	llvm-link runtime.ll even.ll -o out.bc
	llc out.bc -o out.s
	gcc out.s -o even
	$(RM) out.bc out.s
sum: runtime.ll sum.ll
	llvm-link runtime.ll sum.ll -o out.bc
	llc out.bc -o out.s
	gcc out.s -no-pie -o sum
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
orTest: runtime.ll orTest.ll
	llvm-link runtime.ll orTest.ll -o out.bc
	llc out.bc -o out.s
	gcc out.s -o orTest
	$(RM) out.bc out.s
orAgain: runtime.ll orAgain.ll
	llvm-link runtime.ll orAgain.ll -o out.bc
	llc out.bc -o out.s
	gcc out.s -o orAgain
	$(RM) out.bc out.s
testProg: runtime.ll testProg.ll
	llvm-link runtime.ll testProg.ll -o out.bc
	llc out.bc -o out.s
	gcc out.s -no-pie -o testProg
	$(RM) out.bc out.s
clean:
	$(RM) even sum test newTest negTest orTest orAgain testProg
