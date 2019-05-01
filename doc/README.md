In the frontend there are several smaller "passes" in the typechecking.
There is the BlkVisitor that calls CheckStmt. CheckStmt makes sure all statements are correctly typed. It calls InferExprType, which in turn infers the type of every expression.
At the level of InferExprType we know the types of the expression, so instead of doing yet another pass and infering the types again, here is also where the new abstract syntax tree will be annotated with the types.

There is another pass for checking that all paths have a "return" if they have to have it. This is done from the block level also with BlkRet. It does this by calling StmtRet that in turn calls EvalExpr when lazy evaluation of conditional expressions is needed to determine if a program returns on all paths that are reachable.

Up on the TopDef level there is the aforementioned pass called in TopDefVisitor. Both this and AddDefVisitor are called in ProgVisitor. AddDefVisitor is a separate pass to add all the functions to the env.


For the llvm backend two more passes have been added. First there is the alpha renaming pass, that goes through the abstract syntax tree and for each local variable and function parameter it grabs a new name for the declarations and parameters and then replaces all instances of the same variable with the new name. After this pass, no variable will overshadow another one.

The second pass for llvm is the code generation. A string builder is used as a global object that most of the visitors append to. For example, OutputExpr appends a new instruction (or adds it to the start of the string build for strings) and then returns the local register variable that was on the left side of the equality sign in the instruction. 
OutputStmt similarly updates the string builder (and also calls OutputExpr) but it instead returns if the statement has a return value. There is also a convenient class called StmtRet that also checks if a statement returns but without updating the string builder object. Similarly there is also another Expr visitor called ExprTypeVisitor that returns the type of an expression without changing the state of the string builder. This is used to find the nested type of an expression (like for example if there is a less than expression that expression will have type bool but the subexpressions will not). 
Further up there is the block level where OutputBlk updates the string builder and checks if there is a return and RetBlk only checks if there is a return. 
There is the OutputTopDef that appends the string builder a definition of a function. It also checks if the block returns and if it doesn't it returns a default value at the end of the function. Although semantically incorrect this does not actually affect the behavior of the program (it just gets it to compile by not having the function "fall off the end") since in the typechecker we have made sure that all functions return correctly. With a different design on the block and statement level this could probably have been avoided but I didn't feel it was necessary since that part of the llvm program will not be executed anyway. 
On the same level there is also the AddTopDef that does a pass before and adds all functions to the environment. 
At the top level, the functions linked in runtime.ll are also declared.
The environment keeps track of the types for functions and variables. For the variables this is needed for decrementing and incrementing a variable since those are statements and don't have a typed expression to find the type in. There is also a bunch of convenience functions for most of the instructions.

