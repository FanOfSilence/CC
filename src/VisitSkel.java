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
	
public class VisitSkel {
	public class ProgVisitor implements Prog.Visitor<Env, String> {
		public Env visit(src.Absyn.Program p, String arg) throws TypeException { /* Code For Program Goes Here */
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
			
			for (TopDef x : p.listtopdef_) {
				x.accept(new TopDefVisitor(), env);
			}
			FunType mainFun = env.lookupFun("main");
			if (mainFun == null || !mainFun.val.equals(new Int()) || mainFun.args.size() != 0) {
				throw new TypeException("Function int main() has to be defined");
			}
			return env;
		}
	}

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
	public class TopDefVisitor implements TopDef.Visitor<Env, Env> {
		public Env visit(src.Absyn.FnDef p, Env env) throws TypeException { /* Code For FnDef Goes Here */
			Type t = p.type_;
			String id = p.ident_;
			env.emptyEnv();
			env.currentSignature = id;
			p.blk_.accept(new BlkVisitor(), env);
			Boolean returns = p.blk_.accept(new BlkRet(), env);
			if (!returns && !t.equals(new Void())) {
				throw new TypeException("Function " + id + " has to return a value in all paths");
			}
			return env;
		}
	}

	public class ArgVisitor implements Arg.Visitor<Tuple<String, Type>, Env> {
		public Tuple<String, Type> visit(src.Absyn.Argument p, Env arg) { /* Code For Argument Goes Here */
			return new Tuple<String, Type>(p.ident_, p.type_);
		}
	}

	public class BlkVisitor implements Blk.Visitor<Env, Env> {
		public Env visit(src.Absyn.Block p, Env env) throws TypeException { /* Code For Block Goes Here */
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
			for (Stmt x : p.liststmt_) {
				x.accept(new CheckStmt(), env);
			}
			env.endBlock();
			return env;
		}
	}
	
	public class BlkRet implements Blk.Visitor<Boolean, Env> {

		@Override
		public Boolean visit(Block p, Env env) throws TypeException {
			StmtRet stmtRetVisitor = new StmtRet();
			Boolean returns = false;
			for (Stmt stmt : p.liststmt_) {
				returns = stmt.accept(stmtRetVisitor, env);
				if (returns && p.liststmt_.indexOf(stmt) != p.liststmt_.size() - 1) {
					break;
//					throw new TypeException("Block cannot contain statement after return statement");
				}
			}
			return returns;
		}
	}



	public class ItemVisitor implements Item.Visitor<Triple<String, Type, Boolean>, Env> {

		@Override
		public Triple<String, src.Absyn.Type, Boolean> visit(NoInit p, Env arg) {
			Triple<String, Type, Boolean> retTriple = new Triple<String, Type, Boolean>(p.ident_, null, false);
			return retTriple;
		}

		@Override
		public Triple<String, src.Absyn.Type, Boolean> visit(Init p, Env env) throws TypeException {
			Type t = p.expr_.accept(new InferExprType(), env);
			Triple<String, Type, Boolean> retTriple = new Triple<String, Type, Boolean>(p.ident_, t, true);
			return retTriple;
		}
	}

	public class CheckStmt implements Stmt.Visitor<Env, Env> {

		@Override
		public Env visit(Empty p, Env env) {
			return env;
		}

		@Override
		public Env visit(BStmt p, Env env) throws TypeException {
			p.blk_.accept(new BlkVisitor(), env);
			return env;
		}

		@Override
		public Env visit(Decl p, Env env) throws TypeException {
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
			return env;
		}

		@Override
		public Env visit(Ass p, Env env) throws TypeException {
			Tuple<src.Absyn.Type, Boolean> varTuple  = env.lookupVar(p.ident_);
			if (varTuple == null) {
				throw new TypeException("Can't assign to var " + p.ident_ + " that has not been declared");
			}
			Type varType = varTuple.x;
			Type exprType = p.expr_.accept(new InferExprType(), env);
			if (exprType.equals(varType)) {
				env.updateVar(p.ident_);
				return env;
			}
			throw new TypeException("Can't assign expression of type " + exprType.toString() + " to var of type " + varType.toString());
		}

		@Override
		public Env visit(Incr p, Env env) throws TypeException {
			Tuple<src.Absyn.Type, Boolean> t = env.lookupVar(p.ident_);
			if (t.x.equals(new Doub()) || t.x.equals(new Int())) {
				return env;
			}
			throw new TypeException("Can only increment int or double");
		}

		@Override
		public Env visit(Decr p, Env env) throws TypeException {
			Tuple<src.Absyn.Type, Boolean> t = env.lookupVar(p.ident_);
			if (t.x.equals(new Doub()) || t.x.equals(new Int())) {
				return env;
			}
			throw new TypeException("Can only increment int or double");
		}

		@Override
		public Env visit(Ret p, Env env) throws TypeException {
			Type t = p.expr_.accept(new InferExprType(), env);
			String currentFunId = env.currentSignature;
			Type funType = env.lookupFun(currentFunId).val;
			if (!t.equals(funType)) {
				throw new TypeException("Function of type " + funType.toString() + " cannot return value of type " + t.toString());
			}
			return env;
		}

		@Override
		public Env visit(VRet p, Env env) throws TypeException {
			String currentFunId = env.currentSignature;
			
			Type funType = env.lookupFun(currentFunId).val;
			if (!funType.equals(new Void())) {
				throw new TypeException("Function of type " + funType.toString() + " has to return a value of that type");
			}
			return env;
		}

		@Override
		public Env visit(Cond p, Env env) throws TypeException {
			Type t = p.expr_.accept(new InferExprType(), env);
			if (t.equals(new Bool())) {
				p.stmt_.accept(this, env);
				return env;
			}
			throw new TypeException("Conditional expression has to be of type bool");
		}

		@Override
		public Env visit(CondElse p, Env env) throws TypeException {
			Type t = p.expr_.accept(new InferExprType(), env);
			if (t.equals(new Bool())) {
				p.stmt_1.accept(this, env);
				p.stmt_2.accept(this, env);
				return env;
			}
			throw new TypeException("Conditional expression has to be of type bool");
		}

		@Override
		public Env visit(While p, Env env) throws TypeException {
			Type exprType = p.expr_.accept(new InferExprType(), env);
			if (exprType.equals(new Bool())) {
				p.stmt_.accept(this, env);
				return env;
			}
			throw new TypeException("Conditional expression has to be of type bool");
		}

		@Override
		public Env visit(SExp p, Env env) throws TypeException {
			if (!p.expr_.accept(new NakedExpr(), env)) {
				throw new TypeException("Naked expression on top level");
			}
			p.expr_.accept(new InferExprType(), env);
			return env;
		}
		
	}
	
	public class InferExprType implements Expr.Visitor<Type, Env>  {

		@Override
		public Type visit(EVar p, Env env) throws TypeException {
			Tuple<Type, Boolean> varTuple = env.lookupVar(p.ident_);
			if (!varTuple.y) {
				throw new TypeException("Variable " + p.ident_ + " not initialized before being used in expression");
			}
			return varTuple.x;
		}

		@Override
		public Type visit(ELitInt p, Env env) {
			return new Int();
		}

		@Override
		public Type visit(ELitDoub p, Env env) {
			return new Doub();
		}

		@Override
		public Type visit(ELitTrue p, Env env) {
			return new Bool();
		}

		@Override
		public Type visit(ELitFalse p, Env env) {
			return new Bool();
		}

		@Override
		public Type visit(EApp p, Env env) throws TypeException {
			FunType f = env.lookupFun(p.ident_);
			if (f == null) {
				throw new TypeException("Function of name " + p.ident_ + " is used but it's not defined");
			}
			for (int i = 0; i < p.listexpr_.size(); i++) {
				Expr e = p.listexpr_.get(i);
				Type argumentType = e.accept(this, env);
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
			}
			if (p.listexpr_.size() != f.args.size()) {
				throw new TypeException("Function " + p.ident_ + " is being applied with " + p.listexpr_.size() + " args while the functions has " + f.args.size() + " parameters");
			}
			return f.val;
		}

		@Override
		public Type visit(EString p, Env env) {
			return new StringLit();
		}

		@Override
		public Type visit(Neg p, Env arg) throws TypeException {
			Type t = p.expr_.accept(this, arg);
			if (t.equals(new Doub()) || t.equals(new Int())) {
				return t;
			}
			throw new TypeException("Operand to - must be int our double");
			
		}

		@Override
		public Type visit(Not p, Env env) throws TypeException {
			Type t = p.expr_.accept(this, env);
			if (t.equals(new Bool())) {
				return t;
			}
			throw new TypeException("Operand to ! must be boolean");
		}

		@Override
		public Type visit(EMul p, Env env) throws TypeException {
			Type t1 = p.expr_1.accept(this, env);
			Type t2 = p.expr_2.accept(this, env);
			if (t1.equals(t2)) {
				if (t1.equals(new Int())) {
					return new Int();
				} else if (t1.equals(new Doub())) {
					return new Doub();
				}
				throw new TypeException("Operands to * must be int or double");
			}
			throw new TypeException("Operands to * must be of the same type");
		}

		@Override
		public Type visit(EAdd p, Env env) throws TypeException {
			Type t1 = p.expr_1.accept(this, env);
			Type t2 = p.expr_2.accept(this, env);
			if (t1.equals(t2)) {
				if (t1.equals(new Int())) {
					return new Int();
				} else if (t1.equals(new Doub())) {
					return new Doub();
				}
				throw new TypeException("Operands to + must be int or double");
			}
			throw new TypeException("Operands to + must be of the same type");
		}

		@Override
		public Type visit(ERel p, Env env) throws TypeException {
			Type t1 = p.expr_1.accept(this, env);
			Type t2 = p.expr_2.accept(this, env);
			if (t1.equals(t2)) {
				return new Bool();
			}
			throw new TypeException("Operands in relational expression must be of the same type");
		}

		@Override
		public Type visit(EAnd p, Env env) throws TypeException {
			Type t1 = p.expr_1.accept(this, env);
			Type t2 = p.expr_2.accept(this, env);
			if (t1.equals(new Bool()) && t2.equals(new Bool())) {
				return new Bool();
			}
			throw new TypeException("Operands to && must be boolean");
		}

		@Override
		public Type visit(EOr p, Env env) throws TypeException {
			Type t1 = p.expr_1.accept(this, env);
			Type t2 = p.expr_2.accept(this, env);
			if (t1.equals(new Bool()) && t2.equals(new Bool())) {
				return new Bool();
			}
			throw new TypeException("Operands to && must be boolean");
		}
	}
	
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
		
	}
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
	
	// Evaluate expression (even though only some expression can actually be evaluateda at this point)
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
		
	}
	
	public class EvalAddOpVisitor<R, A> implements AddOp.Visitor<R, A> {
		public R visit(src.Absyn.Plus p, A arg) { /* Code For Plus Goes Here */
			return null;
		}

		public R visit(src.Absyn.Minus p, A arg) { /* Code For Minus Goes Here */
			return null;
		}
	}
	
	class MulOpVisitor<R, A> implements MulOp.Visitor<R, A> {
		public R visit(src.Absyn.Times p, A arg) { /* Code For Times Goes Here */
			return null;
		}

		public R visit(src.Absyn.Div p, A arg) { /* Code For Div Goes Here */
			return null;
		}

		public R visit(src.Absyn.Mod p, A arg) { /* Code For Mod Goes Here */
			return null;
		}
	}

	class RelOpVisitor<R, A> implements RelOp.Visitor<R, A> {
		public R visit(src.Absyn.LTH p, A arg) { /* Code For LTH Goes Here */
			return null;
		}

		public R visit(src.Absyn.LE p, A arg) { /* Code For LE Goes Here */
			return null;
		}

		public R visit(src.Absyn.GTH p, A arg) { /* Code For GTH Goes Here */
			return null;
		}

		public R visit(src.Absyn.GE p, A arg) { /* Code For GE Goes Here */
			return null;
		}

		public R visit(src.Absyn.EQU p, A arg) { /* Code For EQU Goes Here */
			return null;
		}

		public R visit(src.Absyn.NE p, A arg) { /* Code For NE Goes Here */
			return null;
		}
	}

	
//	public class EvalType implements 
	
	
	public class StringLit extends Type {
		@Override
		public <R, A> R accept(Visitor<R, A> v, A arg) {
			return null;
		}
		
		  public boolean equals(Object o) {
			    if (this == o) return true;
			    if (o instanceof StringLit) {
			      return true;
			    }
			    return false;
			  }
		
		  public int hashCode() {
		    return 37;
		  }
		
	}
	
}

class FunType {
	public FunType(LinkedList<Tuple<String, Type>> args, Type val) {
		this.args = args;
		this.val = val;
	}
	public LinkedList<Tuple<String, Type>> args;
	public Type val;
}

class Env {
	public HashMap<String, FunType> signature;
	public String currentSignature;
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

class Tuple<X, Y> { 
	public final X x; 
	public final Y y; 
	public Tuple(X x, Y y) { 
		this.x = x; 
		this.y = y; 
	} 
}

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












class TypeVisitor<R, A> implements Type.Visitor<R, A> {
	public R visit(src.Absyn.Int p, A arg) { /* Code For Int Goes Here */
		return null;
	}

	public R visit(src.Absyn.Doub p, A arg) { /* Code For Doub Goes Here */
		return null;
	}

	public R visit(src.Absyn.Bool p, A arg) { /* Code For Bool Goes Here */
		return null;
	}

	public R visit(src.Absyn.Void p, A arg) { /* Code For Void Goes Here */
		return null;
	}

	public R visit(src.Absyn.Fun p, A arg) { /* Code For Fun Goes Here */
		p.type_.accept(new TypeVisitor<R, A>(), arg);
		for (Type x : p.listtype_) {
			/* ... */ }
		return null;
	}
}

class AddOpVisitor<R, A> implements AddOp.Visitor<R, A> {
	public R visit(src.Absyn.Plus p, A arg) { /* Code For Plus Goes Here */
		
		return null;
	}

	public R visit(src.Absyn.Minus p, A arg) { /* Code For Minus Goes Here */
		return null;
	}
}

class MulOpVisitor<R, A> implements MulOp.Visitor<R, A> {
	public R visit(src.Absyn.Times p, A arg) { /* Code For Times Goes Here */
		return null;
	}

	public R visit(src.Absyn.Div p, A arg) { /* Code For Div Goes Here */
		return null;
	}

	public R visit(src.Absyn.Mod p, A arg) { /* Code For Mod Goes Here */
		return null;
	}
}

class RelOpVisitor<R, A> implements RelOp.Visitor<R, A> {
	public R visit(src.Absyn.LTH p, A arg) { /* Code For LTH Goes Here */
		return null;
	}

	public R visit(src.Absyn.LE p, A arg) { /* Code For LE Goes Here */
		return null;
	}

	public R visit(src.Absyn.GTH p, A arg) { /* Code For GTH Goes Here */
		return null;
	}

	public R visit(src.Absyn.GE p, A arg) { /* Code For GE Goes Here */
		return null;
	}

	public R visit(src.Absyn.EQU p, A arg) { /* Code For EQU Goes Here */
		return null;
	}

	public R visit(src.Absyn.NE p, A arg) { /* Code For NE Goes Here */
		return null;
	}
}
