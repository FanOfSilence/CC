package src;


import java.util.HashMap;
import java.util.LinkedList;

import src.TypeException;
import src.Absyn.*;
import src.Absyn.Void;

/*** BNFC-Generated Visitor Design Pattern Skeleton. ***/
/*
 * This implements the common visitor design pattern. Tests show it to be
 * slightly less efficient than the instanceof method, but easier to use.
 * Replace the R and A parameters with the desired return and context types.
 */
	
public class TypeChecker {
	
	// Top level visitor that adds the available functions, calls AddDefVisitor and TopDefVisitor, and makes sure int main() is defined
	public class ProgVisitor implements Prog.Visitor<Prog, String> {
		public Prog visit(src.Absyn.Program p, String arg) throws TypeException { /* Code For Program Goes Here */
			System.out.println("Starting prog visitor");
			Env env = new Env();

			String argumentName = "s";
			
			LinkedList<Tuple<String, Type>> printStringArgs = new LinkedList<Tuple<String, Type>>();
			printStringArgs.add(new Tuple<String, Type>(argumentName, new StringLit()));
			FunType printString = new FunType(printStringArgs, new Void());
			env.updateFun("printString", printString);
			
			LinkedList<Tuple<String, Type>> printIntArgs = new LinkedList<Tuple<String, Type>>();
			printIntArgs.add(new Tuple<String, Type>(argumentName, new Int()));
			FunType printInt = new FunType(printIntArgs, new Void());
			env.updateFun("printInt", printInt);
			
			LinkedList<Tuple<String, Type>> printDoubleArgs = new LinkedList<Tuple<String, Type>>();
			printDoubleArgs.add(new Tuple<String, Type>(argumentName, new Doub()));
			FunType printDoub = new FunType(printDoubleArgs, new Void());
			env.updateFun("printDouble", printDoub);
			
			LinkedList<Tuple<String, Type>> readIntArgs = new LinkedList<Tuple<String, Type>>();
			FunType readInt = new FunType(readIntArgs, new Int());
			env.updateFun("readInt", readInt);
			
			LinkedList<Tuple<String, Type>> readDoubleArgs = new LinkedList<Tuple<String, Type>>();
			FunType readDouble = new FunType(readDoubleArgs, new Doub());
			env.updateFun("readDouble", readDouble);
			
			for (TopDef def : p.listtopdef_) {
				def.accept(new AddDefVisitor(), env);
			}
			
			ListTopDef listTypedTopDef = new ListTopDef();
			for (TopDef x : p.listtopdef_) {
				listTypedTopDef.add(x.accept(new TopDefVisitor(), env));
			}
			FunType mainFun = env.lookupFun("main");
			if (mainFun == null || !mainFun.val.equals(new Int()) || mainFun.args.size() != 0) {
				throw new TypeException("Function int main() has to be defined");
			}
			return new Program(listTypedTopDef);
		}
	}

	// Adds all functions to the environment
	public class AddDefVisitor implements TopDef.Visitor<Env, Env> {

		@Override
		public Env visit(FnDef p, Env env) throws TypeException {
			Type t = p.type_;
			LinkedList<Tuple<String, Type>> funArgs = new LinkedList<Tuple<String, Type>>();
			for (Arg x : p.listarg_) {
				funArgs.add(x.accept(new ArgVisitor(), env));
				/* ... */ }
			FunType f = new FunType(funArgs, t);
			String id = p.ident_;
			env.updateFun(id, f);
			return env;
		}
		
	}
	
	// Calls BlkVisitor and BlkRet to typecheck a function
	public class TopDefVisitor implements TopDef.Visitor<TopDef, Env> {
		public TopDef visit(src.Absyn.FnDef p, Env env) throws TypeException { /* Code For FnDef Goes Here */
			Type t = p.type_;
			String id = p.ident_;
			env.emptyEnv();
			env.currentSignature = id;
			Block typedBlock = p.blk_.accept(new BlkVisitor(), env);
			Boolean returns = p.blk_.accept(new BlkRet(), env);
			if (!returns && !t.equals(new Void())) {
				throw new TypeException("Function " + id + " has to return a value in all paths");
			}
			return new FnDef(t, id, p.listarg_, typedBlock);
		}
	}

	// Returns the name and type of an argument
	public class ArgVisitor implements Arg.Visitor<Tuple<String, Type>, Env> {
		public Tuple<String, Type> visit(Argument p, Env arg) { /* Code For Argument Goes Here */
			return new Tuple<String, Type>(p.ident_, p.type_);
		}
	}

	// On block level, that calls CheckStmt for all statements to type check them
	public class BlkVisitor implements Blk.Visitor<Block, Env> {
		public Block visit(Block p, Env env) throws TypeException { /* Code For Block Goes Here */
			env.newBlock();
			FunType currentFun = env.lookupFun(env.currentSignature);
			for (Tuple<String, Type> tupleArg : currentFun.args) {
				String argName = tupleArg.x;
				Type argType = tupleArg.y;
				try {
					env.addVar(argName, argType, true);
				} catch (TypeException e) {
					System.out.println("Trying to add var for function " + env.currentSignature + " that has already been added");
				}
			}
			ListStmt listTypedStmt = new ListStmt();
			for (Stmt x : p.liststmt_) {
				listTypedStmt.add(x.accept(new CheckStmt(), env));
			}
			env.endBlock();
			return new Block(listTypedStmt);
		}
	}
	
	// On block level, makes sure that a block returns
	public class BlkRet implements Blk.Visitor<Boolean, Env> {

		@Override
		public Boolean visit(Block p, Env env) throws TypeException {
			StmtRet stmtRetVisitor = new StmtRet();
			Boolean returns = false;
			for (Stmt stmt : p.liststmt_) {
				returns = stmt.accept(stmtRetVisitor, env);
				if (returns) {
					break;
				}
//				if (returns && p.liststmt_.indexOf(stmt) != p.liststmt_.size() - 1) {
//					throw new TypeException("Block cannot contain statement after return statement");
//				}
			}
			return returns;
		}
	}


	// Visitor for items that returns enough information to typecheck the statements they are used in
	public class ItemVisitor implements Item.Visitor<Triple<String, Type, Boolean>, Env> {

		@Override
		public Triple<String, src.Absyn.Type, Boolean> visit(NoInit p, Env arg) {
			Triple<String, Type, Boolean> retTriple = new Triple<String, Type, Boolean>(p.ident_, null, false);
			return retTriple;
		}

		@Override
		public Triple<String, src.Absyn.Type, Boolean> visit(Init p, Env env) throws TypeException {
			Tuple<Type, TypedE> initTuple = p.expr_.accept(new InferExprType(), env);
			Type t = initTuple.x;
			Triple<String, Type, Boolean> retTriple = new Triple<String, Type, Boolean>(p.ident_, t, true);
			return retTriple;
		}
	}

	// Calls InferExprType for each expression to check the types in every statement
	public class CheckStmt implements Stmt.Visitor<Stmt, Env> {

		@Override
		public Stmt visit(Empty p, Env env) {
			return p;
		}

		@Override
		public Stmt visit(BStmt p, Env env) throws TypeException {
			Block typedBlock = p.blk_.accept(new BlkVisitor(), env);
			return new BStmt(typedBlock);
		}

		@Override
		public Stmt visit(Decl p, Env env) throws TypeException {
			Type initType = p.type_;
			for (Item item : p.listitem_) {
				Triple<String, Type, Boolean> declTriple = item.accept(new ItemVisitor(), env);
				String declName = declTriple.x;
				Type exprType = declTriple.y;
				Boolean initiated = declTriple.z;
				if (exprType == null || exprType.equals(initType)) {
					env.addVar(declName, initType, true /*initiated*/);
				} else {
					throw new TypeException("Can't assign epxression of type " + exprType.toString() + " to type " + initType.toString());
				}
			}
			//TODO: Need type information here? Not sure...
			return p;
		}

		@Override
		public Stmt visit(Ass p, Env env) throws TypeException {
			Tuple<src.Absyn.Type, Boolean> varTuple  = env.lookupVar(p.ident_);
			if (varTuple == null) {
				throw new TypeException("Can't assign to var " + p.ident_ + " that has not been declared");
			}
			Type varType = varTuple.x;
			Tuple<Type, TypedE> assignmentTuple = p.expr_.accept(new InferExprType(), env);
			Type exprType = assignmentTuple.x;
			TypedE typedExpr = assignmentTuple.y;
			if (exprType.equals(varType)) {
				env.updateVar(p.ident_);
				return new Ass(p.ident_, typedExpr);
			}
			throw new TypeException("Can't assign expression of type " + exprType.toString() + " to var of type " + varType.toString());
		}

		@Override
		public Stmt visit(Incr p, Env env) throws TypeException {
			Tuple<src.Absyn.Type, Boolean> t = env.lookupVar(p.ident_);
			if (t.x.equals(new Doub()) || t.x.equals(new Int())) {
				//TODO: Same as below
				return p;
			}
			throw new TypeException("Can only increment int or double");
		}

		@Override
		public Stmt visit(Decr p, Env env) throws TypeException {
			Tuple<src.Absyn.Type, Boolean> t = env.lookupVar(p.ident_);
			if (t.x.equals(new Doub()) || t.x.equals(new Int())) {
				//TODO: Need type information here I guess???
				return p;
			}
			throw new TypeException("Can only increment int or double");
		}

		@Override
		public Stmt visit(Ret p, Env env) throws TypeException {
			Tuple<Type, TypedE> retTuple = p.expr_.accept(new InferExprType(), env);
			Type t = retTuple.x;
			TypedE typedExpr = retTuple.y;
			String currentFunId = env.currentSignature;
			Type funType = env.lookupFun(currentFunId).val;
			if (!t.equals(funType)) {
				throw new TypeException("Function of type " + funType.toString() + " cannot return value of type " + t.toString());
			}
			return new Ret(typedExpr);
		}

		@Override
		public Stmt visit(VRet p, Env env) throws TypeException {
			String currentFunId = env.currentSignature;
			
			Type funType = env.lookupFun(currentFunId).val;
			if (!funType.equals(new Void())) {
				throw new TypeException("Function of type " + funType.toString() + " has to return a value of that type");
			}
			return p;
		}

		@Override
		public Stmt visit(Cond p, Env env) throws TypeException {
			Tuple<Type, TypedE> condTuple = p.expr_.accept(new InferExprType(), env);
			Type t = condTuple.x;
			TypedE typedExpr = condTuple.y;
			if (t.equals(new Bool())) {
				Stmt typedStmt = p.stmt_.accept(this, env);
				return new Cond(typedExpr, typedStmt);
			}
			throw new TypeException("Conditional expression has to be of type bool");
		}

		@Override
		public Stmt visit(CondElse p, Env env) throws TypeException {
			Tuple<Type, TypedE> elseTuple = p.expr_.accept(new InferExprType(), env);
			Type t = elseTuple.x;
			TypedE typedExpr = elseTuple.y;
			if (t.equals(new Bool())) {
				Stmt typedStmt1 = p.stmt_1.accept(this, env);
				Stmt typedStmt2 = p.stmt_2.accept(this, env);
				return new CondElse(typedExpr, typedStmt1, typedStmt2);
			}
			throw new TypeException("Conditional expression has to be of type bool");
		}

		@Override
		public Stmt visit(While p, Env env) throws TypeException {
			Tuple<Type, TypedE> exprTuple = p.expr_.accept(new InferExprType(), env);
			Type exprType = exprTuple.x;
			TypedE typedExpr = exprTuple.y;
			if (exprType.equals(new Bool())) {
				Stmt typedStmt = p.stmt_.accept(this, env);
				return new While(typedExpr, typedStmt);
			}
			throw new TypeException("Conditional expression has to be of type bool");
		}

		@Override
		public Stmt visit(SExp p, Env env) throws TypeException {
			if (!p.expr_.accept(new NakedExpr(), env)) {
				throw new TypeException("Naked expression on top level");
			}
			Tuple<Type, TypedE> expTuple = p.expr_.accept(new InferExprType(), env);
			TypedE typedExp = expTuple.y;
			return new SExp(typedExp);
		}
		
	}
	
	// Infer the type of an expression and through the types of its subexpressions
	public class InferExprType implements Expr.Visitor<Tuple<Type, TypedE>, Env>  {

		@Override
		public Tuple<Type, TypedE> visit(EVar p, Env env) throws TypeException {
			Tuple<Type, Boolean> varTuple = env.lookupVar(p.ident_);
			Type varType = varTuple.x;
			Boolean initialized = varTuple.y;
			if (!initialized) {
				throw new TypeException("Variable " + p.ident_ + " not initialized before being used in expression");
			}
			return new Tuple<Type, TypedE>(varType, new TypedE(varType, p));
		}

		@Override
		public Tuple<Type, TypedE> visit(ELitInt p, Env env) {
			return new Tuple<Type, TypedE>(new Int(), new TypedE(new Int(), p));
		}

		@Override
		public Tuple<Type, TypedE> visit(ELitDoub p, Env env) {
			return new Tuple<Type, TypedE>(new Doub(), new TypedE(new Doub(), p));
		}

		@Override
		public Tuple<Type, TypedE> visit(ELitTrue p, Env env) {
			return new Tuple<Type, TypedE>(new Bool(), new TypedE(new Bool(), p));
		}

		@Override
		public Tuple<Type, TypedE> visit(ELitFalse p, Env env) {
			return new Tuple<Type, TypedE>(new Bool(), new TypedE(new Bool(), p));
		}

		@Override
		public Tuple<Type, TypedE> visit(EApp p, Env env) throws TypeException {
			FunType f = env.lookupFun(p.ident_);
			if (f == null) {
				throw new TypeException("Function of name " + p.ident_ + " is used but it's not defined");
			}
			ListExpr listTypedE = new ListExpr();
			for (int i = 0; i < p.listexpr_.size(); i++) {
				Expr e = p.listexpr_.get(i);
				Tuple<Type, TypedE> argumentTuple = e.accept(this, env);
				Type argumentType = argumentTuple.x;
				if (i >= f.args.size()) {
					throw new TypeException("Function " + p.ident_ + " cannot be applied with " + p.listexpr_.size() + " arguments");
				}
				Tuple<String, Type> parameterTuple = f.args.get(i);
				String parameterName = parameterTuple.x;
				Type parameterType = parameterTuple.y;
				if (!argumentType.equals(parameterType)) {
					throw new TypeException("Parameter " + parameterName + " of function " + p.ident_+ " is of type " + parameterType.toString()
					 + " while argument is of type " + argumentType.toString());
				}
				TypedE argumentTypedE = argumentTuple.y;
				listTypedE.add(argumentTypedE);
			}
			if (p.listexpr_.size() != f.args.size()) {
				throw new TypeException("Function " + p.ident_ + " is being applied with " + p.listexpr_.size() + " args while the functions has " + f.args.size() + " parameters");
			}
			return new Tuple<Type, TypedE>(f.val, new TypedE(f.val, new EApp(p.ident_, listTypedE)));
		}

		@Override
		public Tuple<Type, TypedE> visit(EString p, Env env) {
			return new Tuple<Type, TypedE>(new StringLit(), new TypedE(new StringLit(), p));
		}

		@Override
		public Tuple<Type, TypedE> visit(Neg p, Env arg) throws TypeException {
			Tuple<Type, TypedE> negTuple = p.expr_.accept(this, arg);
			Type t = negTuple.x;
			TypedE typedExpr = negTuple.y;
			if (t.equals(new Doub()) || t.equals(new Int())) {
				return new Tuple<Type, TypedE>(t, new TypedE(t, p /*typedExpr*/));
			}
			throw new TypeException("Operand to - must be int our double");
		}

		@Override
		public Tuple<Type, TypedE> visit(Not p, Env env) throws TypeException {
			Tuple<Type, TypedE> notTuple = p.expr_.accept(this, env);
			Type t = notTuple.x;
			TypedE typedExpr = notTuple.y;
			if (t.equals(new Bool())) {
				return new Tuple<Type, TypedE>(t, new TypedE(t, typedExpr));
			}
			throw new TypeException("Operand to ! must be boolean");
		}

		@Override
		public Tuple<Type, TypedE> visit(EMul p, Env env) throws TypeException {
			Tuple<Type, TypedE> mulTuple1 = p.expr_1.accept(this, env);
			Tuple<Type, TypedE> mulTuple2 = p.expr_2.accept(this, env);
			Type t1 = mulTuple1.x;
			Type t2 = mulTuple2.x;
			TypedE tE1 = mulTuple1.y;
			TypedE tE2 = mulTuple2.y;
			if (t1.equals(t2)) {
				if (t1.equals(new Int()) || t1.equals(new Doub())) {
					return new Tuple<Type, TypedE>(t1, new TypedE(t1, new EMul(tE1, p.mulop_, tE2)));
				}
				throw new TypeException("Operands to * must be int or double");
			}
			throw new TypeException("Operands to * must be of the same type");
		}

		@Override
		public Tuple<Type, TypedE> visit(EAdd p, Env env) throws TypeException {
			Tuple<Type, TypedE> addTuple1 = p.expr_1.accept(this, env);
			Tuple<Type, TypedE> addTuple2 = p.expr_2.accept(this, env);
			Type t1 = addTuple1.x;
			Type t2 = addTuple2.x;
			TypedE tE1 = addTuple1.y;
			TypedE tE2 = addTuple2.y;
			if (t1.equals(t2)) {
				if (t1.equals(new Int()) || t1.equals(new Doub())) {
					return new Tuple<Type, TypedE>(t1, new TypedE(t1, new EAdd(tE1, p.addop_, tE2)));
				}
				throw new TypeException("Operands to addition operation must be int or double");
			}
			throw new TypeException("Operands to addition operation must be of the same type");
		}

		@Override
		public Tuple<Type, TypedE> visit(ERel p, Env env) throws TypeException {
			Tuple<Type, TypedE> relTuple1 = p.expr_1.accept(this, env);
			Tuple<Type, TypedE> relTuple2 = p.expr_2.accept(this, env);
			Type t1 = relTuple1.x;
			Type t2 = relTuple2.x;
			TypedE tE1 = relTuple1.y;
			TypedE tE2 = relTuple2.y;
			if (t1.equals(t2)) {
				return new Tuple<Type, TypedE>(new Bool(), new TypedE(new Bool(), new ERel(tE1, p.relop_, tE2)));
			}
			throw new TypeException("Operands in relational expression must be of the same type");
		}

		@Override
		public Tuple<Type, TypedE> visit(EAnd p, Env env) throws TypeException {
			Tuple<Type, TypedE> andTuple1 = p.expr_1.accept(this, env);
			Tuple<Type, TypedE> andTuple2 = p.expr_2.accept(this, env);
			Type t1 = andTuple1.x;
			Type t2 = andTuple2.x;
			TypedE tE1 = andTuple1.y;
			TypedE tE2 = andTuple2.y;
			if (t1.equals(new Bool()) && t2.equals(new Bool())) {
				return new Tuple<Type, TypedE>(new Bool(), new TypedE(new Bool(), new EAnd(tE1, tE2)));
			}
			throw new TypeException("Operands to && must be boolean");
		}

		@Override
		public Tuple<Type, TypedE> visit(EOr p, Env env) throws TypeException {
			Tuple<Type, TypedE> andTuple1 = p.expr_1.accept(this, env);
			Tuple<Type, TypedE> andTuple2 = p.expr_2.accept(this, env);
			Type t1 = andTuple1.x;
			Type t2 = andTuple2.x;
			TypedE tE1 = andTuple1.y;
			TypedE tE2 = andTuple2.y;
			if (t1.equals(new Bool()) && t2.equals(new Bool())) {
				return new Tuple<Type, TypedE>(new Bool(), new TypedE(new Bool(), new EOr(tE1, tE2)));
			}
			throw new TypeException("Operands to || must be boolean");
		}

		@Override
		public Tuple<Type, TypedE> visit(TypedE p, Env arg) throws TypeException {
			throw new TypeException("Wasn't expecting typed expression");
		}
	}
	
	// Makes sure that there are no illegal expressions on statement level
	public class NakedExpr implements Expr.Visitor<Boolean, Env> {

		@Override
		public Boolean visit(EVar p, Env arg) throws TypeException {
			return false;
		}

		@Override
		public Boolean visit(ELitInt p, Env arg) {
			return false;
		}

		@Override
		public Boolean visit(ELitDoub p, Env arg) {
			return false;
		}

		@Override
		public Boolean visit(ELitTrue p, Env arg) {
			return false;
		}

		@Override
		public Boolean visit(ELitFalse p, Env arg) {
			return false;
		}

		@Override
		public Boolean visit(EApp p, Env arg) throws TypeException {
			return true;
		}

		@Override
		public Boolean visit(EString p, Env arg) {
			return false;
		}

		@Override
		public Boolean visit(Neg p, Env arg) throws TypeException {
			return p.expr_.accept(this, arg);
		}

		@Override
		public Boolean visit(Not p, Env arg) throws TypeException {
			return p.expr_.accept(this, arg);
		}

		@Override
		public Boolean visit(EMul p, Env arg) throws TypeException {
			return false;
		}

		@Override
		public Boolean visit(EAdd p, Env arg) throws TypeException {
			return false;
		}

		@Override
		public Boolean visit(ERel p, Env arg) throws TypeException {
			return false;
		}

		@Override
		public Boolean visit(EAnd p, Env arg) throws TypeException {
			return false;
		}

		@Override
		public Boolean visit(EOr p, Env arg) throws TypeException {
			return false;
		}

		@Override
		public Boolean visit(TypedE p, Env arg) throws TypeException {
			// TypedE is internal so not this code won't be called
			return null;
		}
		
	}
	
	// Class that checks that a statement contains a return in all possible paths
	public class StmtRet implements Stmt.Visitor<Boolean, Env> {

		@Override
		public Boolean visit(Empty p, Env arg) {
			return false;
		}

		@Override
		public Boolean visit(BStmt p, Env arg) throws TypeException {
			return p.blk_.accept(new BlkRet(), arg);
		}

		@Override
		public Boolean visit(Decl p, Env arg) throws TypeException {
			return false;
		}

		@Override
		public Boolean visit(Ass p, Env arg) throws TypeException {
			return false;
		}

		@Override
		public Boolean visit(Incr p, Env arg) throws TypeException {
			return false;
		}

		@Override
		public Boolean visit(Decr p, Env arg) throws TypeException {
			return false;
		}

		@Override
		public Boolean visit(Ret p, Env arg) throws TypeException {
			return true;
		}

		@Override
		public Boolean visit(VRet p, Env arg) throws TypeException {
			return true;
		}

		@Override
		public Boolean visit(Cond p, Env arg) throws TypeException {
			Boolean ifReturns = p.stmt_.accept(this, arg);
			Double exprAlwaysTrue = p.expr_.accept(new EvalExpr(), arg);
			if (exprAlwaysTrue == null || exprAlwaysTrue != 1) {
				return false;
			}
			//exprAlwaysTrue == 1
			return ifReturns;
		}

		@Override
		public Boolean visit(CondElse p, Env arg) throws TypeException {
			Boolean ifReturns = p.stmt_1.accept(this, arg);
			Boolean elseReturns = p.stmt_2.accept(this, arg);
			Double exprAlwaysTrue = p.expr_.accept(new EvalExpr(), arg);
			
			if (exprAlwaysTrue == null || (exprAlwaysTrue != 0 && exprAlwaysTrue != 1)) {
				return ifReturns && elseReturns;
			}
			if (exprAlwaysTrue == 1) {
				return ifReturns;
			} else { // exprAlwaysTrue == 0
				return elseReturns;
			}
		}

		@Override
		public Boolean visit(While p, Env arg) throws TypeException {
			Boolean whileReturns = p.stmt_.accept(this, arg);
			Double exprAlwaysTrue = p.expr_.accept(new EvalExpr(), arg);
			if (exprAlwaysTrue == null || exprAlwaysTrue != 1) {
				return false;
			}
			//exprAlwaysTrue == 1
			return whileReturns;
		}

		@Override
		public Boolean visit(SExp p, Env arg) throws TypeException {
			return false;
		}
		
	}
	
	// Evaluate expression (even though only some expression can actually be evaluated at this point)
	// Only the simplest expressions are evaluated
	public class EvalExpr implements Expr.Visitor<Double, Env> {

		@Override
		public Double visit(EVar p, Env arg) throws TypeException {
			System.out.println("Cannot evaluate variable");
			return null;
		}

		@Override
		public Double visit(ELitInt p, Env arg) {
			return (double) p.integer_;
		}

		@Override
		public Double visit(ELitDoub p, Env arg) {
			return p.double_;
		}

		@Override
		public Double visit(ELitTrue p, Env arg) {
			return 1.0;
		}

		@Override
		public Double visit(ELitFalse p, Env arg) {
			return 0.0;
		}

		@Override
		public Double visit(EApp p, Env arg) throws TypeException {
			System.out.println("Cannot evaluate application");
			return null;
		}

		@Override
		public Double visit(EString p, Env arg) {
			System.out.println("Cannot evaluate String");
			return null;
		}

		@Override
		public Double visit(Neg p, Env arg) throws TypeException {
			Double val = p.expr_.accept(this, arg);
			if (val == null) {
				return null;
			}
			return - val;
		}

		@Override
		public Double visit(Not p, Env arg) throws TypeException {
			Double val = p.expr_.accept(this, arg);
			if (val == null) {
				return null;
			}
			if (val == 0) {
				return 1.0;
			}
			return 0.0;
		}

		@Override
		public Double visit(EMul p, Env arg) throws TypeException {
			System.out.println("Cannot evaluate EMul");
			return null;
		}

		@Override
		public Double visit(EAdd p, Env arg) throws TypeException {
			System.out.println("Cannot evaluate EAdd");
			return null;
		}

		@Override
		public Double visit(ERel p, Env arg) throws TypeException {
			System.out.println("Cannot evaluate ERel");
			return null;
		}

		@Override
		public Double visit(EAnd p, Env arg) throws TypeException {
			Double leftVal = p.expr_1.accept(this, arg);
			if (leftVal == null) {
				return null;
			}
			if (leftVal == 0) {
				return 0.0;
			} else {
				return p.expr_2.accept(this, arg);
			}
		}

		@Override
		public Double visit(EOr p, Env arg) throws TypeException {
			Double leftVal = p.expr_1.accept(this, arg);
			if (leftVal == null) {
				return null;
			}
			if (leftVal > 0) {
				return leftVal;
			} else {
				return p.expr_2.accept(this, arg);
			}
		}

		@Override
		public Double visit(TypedE p, Env arg) throws TypeException {
			// TypedE is internal so not this code won't be called
			return null;
		}
		
	}
}

// Definition of a function
class FunType {
	public FunType(LinkedList<Tuple<String, Type>> args, Type val) {
		this.args = args;
		this.val = val;
	}
	public LinkedList<Tuple<String, Type>> args;
	public Type val;
}

class Env {
	// All function signatures
	public HashMap<String, FunType> signature;
	
	// Current signature that is being checked
	public String currentSignature;
	
	//Context with all variables initialized in this context
	public LinkedList<HashMap<String, Tuple<Type, Boolean>>> contexts;
	
	public Env() {
		signature = new HashMap<String, FunType>();
		emptyEnv();
	}
	
	private Tuple<Type, Boolean> lookupVarCurrentContext(String id) {
		return contexts.getFirst().get(id);
	}
	
	public Tuple<Type, Boolean> lookupVar(String id) throws TypeException {
		for (HashMap<String, Tuple<Type, Boolean>> context : contexts) {
			if (context.containsKey(id)) {
				return context.get(id);
			}
		}
		throw new TypeException("Variable " + id + " used before declaration");
	}
	
	public FunType lookupFun(String id) {
		return signature.get(id);
	}
	
	public void addVar(String id, Type t, boolean initialized) throws TypeException {
		if (lookupVarCurrentContext(id) != null) {
			throw new TypeException("Var " + id + " already defined in this scope");
		} else {
			 contexts.getFirst().put(id, new Tuple<Type, Boolean>(t, initialized));
		}
	}
	
	public void updateVar(String id) throws TypeException {
		Boolean initialized = true;
		for (HashMap<String, Tuple<Type, Boolean>> context : contexts) {
			if (context.containsKey(id)) {
				Type varType = context.get(id).x;
				context.put(id, new Tuple<Type, Boolean>(varType, initialized));
				return;
			}
		}
		throw new TypeException("Var " + id + " has to be declared before assignment");
	}
	
	public void updateFun(String id, FunType f) throws TypeException {
		if (signature.containsKey(id)) {
			throw new TypeException("Function " + id + " already defined");
		} else {
			signature.put(id, f);
		}
	}
	
	public void emptyEnv() {
		contexts = new LinkedList<HashMap<String, Tuple<Type, Boolean>>>();
	}
	
	public void newBlock() {
		contexts.addFirst(new HashMap<String, Tuple<Type, Boolean>>());
	}
	
	public void endBlock() {
		contexts.removeFirst();
	}
}

// Classes for creating tuples



class Triple<X, Y, Z> { 
	public final X x; 
	public final Y y;
	public final Z z;
	public Triple(X x, Y y, Z z) { 
		this.x = x;
		this.y = y;
		this.z = z;
	} 
}
