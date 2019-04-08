In the frontend there are several smaller "passes" in the typechecking.
There is the BlkVisitor that calls CheckStmt. CheckStmt makes sure all statements are correctly typed. It calls InferExprType, which in turn infers the type of every expression.
At the level of InferExprType we know the types of the expression, so instead of doing yet another pass and infering the types again, here is also where the new abstract syntax tree will be annotated with the types.

There is another pass for checking that all paths have a "return" if they have to have it. This is done from the block level also with BlkRet that makes sure no lines are after a return statement. It does this by calling StmtRet that in turn calls EvalExpr when lazy evaluation of conditional expressions is needed to determine if a program returns on all paths that are reachable.

Up on the TopDef level there is the aforementioned pass called in TopDefVisitor. Both this and AddDefVisitor are called in ProgVisitor. AddDefVisitor is a separate pass to add all the functions to the env.

