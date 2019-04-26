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
	
	public class ProgAlpha implements Prog.Visitor<Prog, Object> {

		@Override
		public Prog visit(Program p, Object obj) throws TypeException {
			arg = 0;
			localVar = 0;
			AlphaEnv env = new AlphaEnv();
			ListTopDef listTopDef = new ListTopDef();
			for (TopDef topDef : p.listtopdef_) {
				listTopDef.add(topDef.accept(new TopDefAlpha(), env));
			}
			return new Program(listTopDef);
		}
	}
	
	public class TopDefAlpha implements TopDef.Visitor<TopDef, AlphaEnv> {

		@Override
		public TopDef visit(FnDef p, AlphaEnv env) throws TypeException {
			env.emptyEnv();
			env.newBlock();
			ListArg listArg = new ListArg();
			for (Arg arg : p.listarg_) {
				listArg.add(arg.accept(new ArgAlpha(), env));
			}
			Blk blk = p.blk_.accept(new BlkAlpha(), env);
			return new FnDef(p.type_, p.ident_, listArg, blk);
		}
	}
	
	public class ArgAlpha implements Arg.Visitor<Arg, AlphaEnv> {

		@Override
		public Arg visit(Argument p, AlphaEnv env) {
			String newArg = newArg();
			try {
				env.addVar(p.ident_, newArg);
			} catch (TypeException e) {
				e.printStackTrace();
			}
			return new Argument(p.type_, newArg);
		}
	}
	
	public class BlkAlpha implements Blk.Visitor<Blk, AlphaEnv> {

		@Override
		public Blk visit(Block p, AlphaEnv env) throws TypeException {
			env.newBlock();
			ListStmt listStmt = new ListStmt();
			for (Stmt stmt : p.liststmt_) {
				listStmt.add(stmt.accept(new StmtAlpha(), env));
			}
			env.endBlock();
			return new Block(listStmt);
		}
	}
	
	public class ItemAlpha implements Item.Visitor<Item, AlphaEnv> {

		@Override
		public Item visit(NoInit p, AlphaEnv env) {
			String newVar = newLocalVar();
			try {
				env.addVar(p.ident_, newVar);
			} catch (TypeException e) {
				e.printStackTrace();
			}
			return new NoInit(newVar);
		}

		@Override
		public Item visit(Init p, AlphaEnv env) throws TypeException {
			String newVar = newLocalVar();
			Expr expr = p.expr_.accept(new ExprAlpha(), env);
			env.addVar(p.ident_, newVar);
			return new Init(newVar, expr);
		}
	}
	
	public class StmtAlpha implements Stmt.Visitor<Stmt, AlphaEnv> {

		@Override
		public Stmt visit(Empty p, AlphaEnv arg) {
			return p;
		}

		@Override
		public Stmt visit(BStmt p, AlphaEnv env) throws TypeException {
			Blk blk = p.blk_.accept(new BlkAlpha(), env);
			return new BStmt(blk);
		}

		@Override
		public Stmt visit(Decl p, AlphaEnv env) throws TypeException {
			ListItem listItem = new ListItem();
			for (Item item : p.listitem_) {
				listItem.add(item.accept(new ItemAlpha(), env));
			}
			return new Decl(p.type_, listItem);
		}

		@Override
		public Stmt visit(Ass p, AlphaEnv env) throws TypeException {
			String newId = env.lookupVar(p.ident_);
			Expr expr = p.expr_.accept(new ExprAlpha(), env);
			return new Ass(newId, expr);
		}

		@Override
		public Stmt visit(Incr p, AlphaEnv env) throws TypeException {
			String newId = env.lookupVar(p.ident_);
			return new Incr(newId);
		}

		@Override
		public Stmt visit(Decr p, AlphaEnv env) throws TypeException {
			String newId = env.lookupVar(p.ident_);
			return new Decr(newId);
		}

		@Override
		public Stmt visit(Ret p, AlphaEnv env) throws TypeException {
			Expr expr = p.expr_.accept(new ExprAlpha(), env);
			return new Ret(expr);
		}

		@Override
		public Stmt visit(VRet p, AlphaEnv arg) throws TypeException {
			return p;
		}

		@Override
		public Stmt visit(Cond p, AlphaEnv env) throws TypeException {
			Expr expr = p.expr_.accept(new ExprAlpha(), env);
			Stmt stmt = p.stmt_.accept(this, env);
			return new Cond(expr, stmt);
		}

		@Override
		public Stmt visit(CondElse p, AlphaEnv env) throws TypeException {
			Expr expr = p.expr_.accept(new ExprAlpha(), env);
			Stmt stmt1 = p.stmt_1.accept(this, env);
			Stmt stmt2 = p.stmt_2.accept(this, env);
			return new CondElse(expr, stmt1, stmt2);
		}

		@Override
		public Stmt visit(While p, AlphaEnv env) throws TypeException {
			Expr expr = p.expr_.accept(new ExprAlpha(), env);
			Stmt stmt = p.stmt_.accept(this, env);
			return new While(expr, stmt);
		}

		@Override
		public Stmt visit(SExp p, AlphaEnv env) throws TypeException {
			Expr expr = p.expr_.accept(new ExprAlpha(), env);
			return new SExp(expr);
		}
		
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
			if (contexts.size() == 0) {
				
			}
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
