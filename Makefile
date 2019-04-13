JAVAC=javac
JAVAC_FLAGS=-sourcepath .
JAVA=java
JAVA_FLAGS=
CUP=java_cup.Main
CUPFLAGS=-nopositions -expect 100
JLEX=JLex.Main
all: clean jlc

jlc: absyn src/Yylex.class src/PrettyPrinter.class src/jlc.class src/ComposVisitor.class src/AbstractVisitor.class src/FoldVisitor.class src/AllVisitor.class src/parser.class src/sym.class src/jlc.class

.PHONY: absyn

%.class: %.java
	${JAVAC} ${JAVAC_FLAGS} $^

absyn: src/Absyn/Prog.java src/Absyn/Program.java src/Absyn/TopDef.java src/Absyn/FnDef.java src/Absyn/ListTopDef.java src/Absyn/Arg.java src/Absyn/Argument.java src/Absyn/ListArg.java src/Absyn/Blk.java src/Absyn/Block.java src/Absyn/ListStmt.java src/Absyn/Stmt.java src/Absyn/Empty.java src/Absyn/BStmt.java src/Absyn/Decl.java src/Absyn/Ass.java src/Absyn/Incr.java src/Absyn/Decr.java src/Absyn/Ret.java src/Absyn/VRet.java src/Absyn/Cond.java src/Absyn/CondElse.java src/Absyn/While.java src/Absyn/SExp.java src/Absyn/Item.java src/Absyn/NoInit.java src/Absyn/Init.java src/Absyn/ListItem.java src/Absyn/Type.java src/Absyn/Int.java src/Absyn/Doub.java src/Absyn/Bool.java src/Absyn/Void.java src/Absyn/Fun.java src/Absyn/ListType.java src/Absyn/Expr.java src/Absyn/EVar.java src/Absyn/ELitInt.java src/Absyn/ELitDoub.java src/Absyn/ELitTrue.java src/Absyn/ELitFalse.java src/Absyn/EApp.java src/Absyn/EString.java src/Absyn/Neg.java src/Absyn/Not.java src/Absyn/EMul.java src/Absyn/EAdd.java src/Absyn/ERel.java src/Absyn/EAnd.java src/Absyn/EOr.java src/Absyn/ListExpr.java src/Absyn/AddOp.java src/Absyn/Plus.java src/Absyn/Minus.java src/Absyn/MulOp.java src/Absyn/Times.java src/Absyn/Div.java src/Absyn/Mod.java src/Absyn/RelOp.java src/Absyn/LTH.java src/Absyn/LE.java src/Absyn/GTH.java src/Absyn/GE.java src/Absyn/EQU.java src/Absyn/NE.java src/Absyn/TypedE.java
	${JAVAC} ${JAVAC_FLAGS} $^

src/Yylex.java: src/Yylex
	${JAVA} ${JAVA_FLAGS} ${JLEX} src/Yylex

src/sym.java src/parser.java: src/Javalette.cup
	${JAVA} ${JAVA_FLAGS} ${CUP} ${CUPFLAGS} src/Javalette.cup
	mv sym.java parser.java src/

src/Yylex.class: src/Yylex.java src/sym.java

src/sym.class: src/sym.java

src/parser.class: src/parser.java src/sym.java

src/PrettyPrinter.class: src/PrettyPrinter.java

clean:
	rm -f src/Absyn/*.class src/*.class

distclean: vclean

vclean:
	 rm -f src/Absyn/Prog.java src/Absyn/Program.java src/Absyn/TopDef.java src/Absyn/FnDef.java src/Absyn/ListTopDef.java src/Absyn/Arg.java src/Absyn/Argument.java src/Absyn/ListArg.java src/Absyn/Blk.java src/Absyn/Block.java src/Absyn/ListStmt.java src/Absyn/Stmt.java src/Absyn/Empty.java src/Absyn/BStmt.java src/Absyn/Decl.java src/Absyn/Ass.java src/Absyn/Incr.java src/Absyn/Decr.java src/Absyn/Ret.java src/Absyn/VRet.java src/Absyn/Cond.java src/Absyn/CondElse.java src/Absyn/While.java src/Absyn/SExp.java src/Absyn/Item.java src/Absyn/NoInit.java src/Absyn/Init.java src/Absyn/ListItem.java src/Absyn/Type.java src/Absyn/Int.java src/Absyn/Doub.java src/Absyn/Bool.java src/Absyn/Void.java src/Absyn/Fun.java src/Absyn/ListType.java src/Absyn/Expr.java src/Absyn/EVar.java src/Absyn/ELitInt.java src/Absyn/ELitDoub.java src/Absyn/ELitTrue.java src/Absyn/ELitFalse.java src/Absyn/EApp.java src/Absyn/EString.java src/Absyn/Neg.java src/Absyn/Not.java src/Absyn/EMul.java src/Absyn/EAdd.java src/Absyn/ERel.java src/Absyn/EAnd.java src/Absyn/EOr.java src/Absyn/ListExpr.java src/Absyn/AddOp.java src/Absyn/Plus.java src/Absyn/Minus.java src/Absyn/MulOp.java src/Absyn/Times.java src/Absyn/Div.java src/Absyn/Mod.java src/Absyn/RelOp.java src/Absyn/LTH.java src/Absyn/LE.java src/Absyn/GTH.java src/Absyn/GE.java src/Absyn/EQU.java src/Absyn/NE.java src/Absyn/Prog.class src/Absyn/Program.class src/Absyn/TopDef.class src/Absyn/FnDef.class src/Absyn/ListTopDef.class src/Absyn/Arg.class src/Absyn/Argument.class src/Absyn/ListArg.class src/Absyn/Blk.class src/Absyn/Block.class src/Absyn/ListStmt.class src/Absyn/Stmt.class src/Absyn/Empty.class src/Absyn/BStmt.class src/Absyn/Decl.class src/Absyn/Ass.class src/Absyn/Incr.class src/Absyn/Decr.class src/Absyn/Ret.class src/Absyn/VRet.class src/Absyn/Cond.class src/Absyn/CondElse.class src/Absyn/While.class src/Absyn/SExp.class src/Absyn/Item.class src/Absyn/NoInit.class src/Absyn/Init.class src/Absyn/ListItem.class src/Absyn/Type.class src/Absyn/Int.class src/Absyn/Doub.class src/Absyn/Bool.class src/Absyn/Void.class src/Absyn/Fun.class src/Absyn/ListType.class src/Absyn/Expr.class src/Absyn/EVar.class src/Absyn/ELitInt.class src/Absyn/ELitDoub.class src/Absyn/ELitTrue.class src/Absyn/ELitFalse.class src/Absyn/EApp.class src/Absyn/EString.class src/Absyn/Neg.class src/Absyn/Not.class src/Absyn/EMul.class src/Absyn/EAdd.class src/Absyn/ERel.class src/Absyn/EAnd.class src/Absyn/EOr.class src/Absyn/ListExpr.class src/Absyn/AddOp.class src/Absyn/Plus.class src/Absyn/Minus.class src/Absyn/MulOp.class src/Absyn/Times.class src/Absyn/Div.class src/Absyn/Mod.class src/Absyn/RelOp.class src/Absyn/LTH.class src/Absyn/LE.class src/Absyn/GTH.class src/Absyn/GE.class src/Absyn/EQU.class src/Absyn/NE.class
	 rm -f src/Absyn/*.class
	 rmdir src/Absyn/
	 rm -f src/Yylex src/Javalette.cup src/Yylex.java src/VisitSkel.java src/ComposVisitor.java src/AbstractVisitor.java src/FoldVisitor.java src/AllVisitor.java src/PrettyPrinter.java src/Skeleton.java src/jlc.java src/sym.java src/parser.java src/*.class
	rm -f Makefile
	rmdir -p src/

