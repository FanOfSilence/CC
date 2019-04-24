package src;

import java.util.HashMap;
import java.util.LinkedList;

import src.Absyn.*;

public class AlphaRenaming {
	private int arg;
	private int localVar;
	
	private String newLocalVar() {
		localVar++;
		return "localVar_" + localVar;
	}
	private String newArg() {
		arg++;
		return "arg_" + arg;
	}
	
	
	public class ExprAlpha implements Expr.Visitor<Expr, AlphaEnv> {

		@Override
		public Expr visit(EVar p, AlphaEnv env) throws TypeException {
			String newId = env.lookupVar(p.ident_);
			return new EVar(newId);
		}

		@Override
		public Expr visit(ELitInt p, AlphaEnv arg) {
			return p;
		}

		@Override
		public Expr visit(ELitDoub p, AlphaEnv arg) {
			return p;
		}

		@Override
		public Expr visit(ELitTrue p, AlphaEnv arg) {
			return p;
		}

		@Override
		public Expr visit(ELitFalse p, AlphaEnv arg) {
			return p;
		}

		@Override
		public Expr visit(EApp p, AlphaEnv env) throws TypeException {
			ListExpr listExpr = new ListExpr();
			for (Expr expr : p.listexpr_) {
				listExpr.add(expr.accept(this, env));
			}
			return new EApp(p.ident_, listExpr);
		}

		@Override
		public Expr visit(EString p, AlphaEnv arg) {
			return p;
		}

		@Override
		public Expr visit(Neg p, AlphaEnv env) throws TypeException {
			Expr expr = p.expr_.accept(this, env);
			return new Neg(expr);
		}

		@Override
		public Expr visit(Not p, AlphaEnv env) throws TypeException {
			Expr expr = p.expr_.accept(this, env);
			return new Not(expr);
		}

		@Override
		public Expr visit(EMul p, AlphaEnv env) throws TypeException {
			Expr expr1 = p.expr_1.accept(this, env);
			Expr expr2 = p.expr_2.accept(this, env);
			return new EMul(expr1, p.mulop_, expr2);
		}

		@Override
		public Expr visit(EAdd p, AlphaEnv env) throws TypeException {
			Expr expr1 = p.expr_1.accept(this, env);
			Expr expr2 = p.expr_2.accept(this, env);
			return new EAdd(expr1, p.addop_, expr2);
		}

		@Override
		public Expr visit(ERel p, AlphaEnv env) throws TypeException {
			Expr expr1 = p.expr_1.accept(this, env);
			Expr expr2 = p.expr_2.accept(this, env);
			return new ERel(expr1, p.relop_, expr2);
		}

		@Override
		public Expr visit(EAnd p, AlphaEnv env) throws TypeException {
			Expr expr1 = p.expr_1.accept(this, env);
			Expr expr2 = p.expr_2.accept(this, env);
			return new EAnd(expr1, expr2);
		}

		@Override
		public Expr visit(EOr p, AlphaEnv env) throws TypeException {
			Expr expr1 = p.expr_1.accept(this, env);
			Expr expr2 = p.expr_2.accept(this, env);
			return new EOr(expr1, expr2);
		}

		@Override
		public Expr visit(TypedE p, AlphaEnv env) throws TypeException {
			Expr expr = p.expr_.accept(this, env);
			return new TypedE(p.type_, expr);
		}
	}
}

class AlphaEnv {

	//Context with all variables initialized in this context mapping to a new name
	public LinkedList<HashMap<String, String>> contexts;
	
	public AlphaEnv() {
		emptyEnv();
	}
	
	private String lookupVarCurrentContext(String id) {
		return contexts.getFirst().get(id);
	}
	
	public String lookupVar(String id) throws TypeException {
		for (HashMap<String, String> context : contexts) {
			if (context.containsKey(id)) {
				return context.get(id);
			}
		}
		throw new TypeException("Variable " + id + " used before declaration");
	}
	
	public void addVar(String id, String newId) throws TypeException {
		if (lookupVarCurrentContext(id) != null) {
			throw new TypeException("Var " + id + " already defined in this scope");
		} else {
			 contexts.getFirst().put(id, newId);
		}
	}
	
//	public void updateVar(String id) throws TypeException {
//		Boolean initialized = true;
//		for (HashMap<String, String> context : contexts) {
//			if (context.containsKey(id)) {
//				Type varType = context.get(id).x;
//				context.put(id, new Tuple<Type, Boolean>(varType, initialized));
//				return;
//			}
//		}
//		throw new TypeException("Var " + id + " has to be declared before assignment");
//	}
	
	public void emptyEnv() {
		contexts = new LinkedList<HashMap<String, String>>();
	}
	
	public void newBlock() {
		contexts.addFirst(new HashMap<String, String>());
	}
	
	public void endBlock() {
		contexts.removeFirst();
	}
}
