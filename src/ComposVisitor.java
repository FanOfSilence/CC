package src;

import src.TypeException;
import src.Absyn.*;

/**
 * BNFC-Generated Composition Visitor
 */

public class ComposVisitor<A> implements src.Absyn.Prog.Visitor<src.Absyn.Prog, A>,
		src.Absyn.TopDef.Visitor<src.Absyn.TopDef, A>, src.Absyn.Arg.Visitor<src.Absyn.Arg, A>,
		src.Absyn.Blk.Visitor<src.Absyn.Blk, A>, src.Absyn.Stmt.Visitor<src.Absyn.Stmt, A>,
		src.Absyn.Item.Visitor<src.Absyn.Item, A>, src.Absyn.Type.Visitor<src.Absyn.Type, A>,
		src.Absyn.Expr.Visitor<src.Absyn.Expr, A>, src.Absyn.AddOp.Visitor<src.Absyn.AddOp, A>,
		src.Absyn.MulOp.Visitor<src.Absyn.MulOp, A>,
		src.Absyn.RelOp.Visitor<src.Absyn.RelOp, A> {
	/* Prog */
	public Prog visit(src.Absyn.Program p, A arg) throws TypeException {
		ListTopDef listtopdef_ = new ListTopDef();
		for (TopDef x : p.listtopdef_) {
			listtopdef_.add(x.accept(this, arg));
		}
		return new src.Absyn.Program(listtopdef_);
	}

	/* TopDef */
	public TopDef visit(src.Absyn.FnDef p, A arg) throws TypeException {
		Type type_ = p.type_.accept(this, arg);
		String ident_ = p.ident_;
		ListArg listarg_ = new ListArg();
		for (Arg x : p.listarg_) {
			listarg_.add(x.accept(this, arg));
		}
		Blk blk_ = p.blk_.accept(this, arg);
		return new src.Absyn.FnDef(type_, ident_, listarg_, blk_);
	}

	/* Arg */
	public Arg visit(src.Absyn.Argument p, A arg) {
		Type type_ = p.type_.accept(this, arg);
		String ident_ = p.ident_;
		return new src.Absyn.Argument(type_, ident_);
	}

	/* Blk */
	public Blk visit(src.Absyn.Block p, A arg) {
		ListStmt liststmt_ = new ListStmt();
		for (Stmt x : p.liststmt_) {
			try {
				liststmt_.add(x.accept(this, arg));
			} catch (TypeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return new src.Absyn.Block(liststmt_);
	}

	/* Stmt */
	public Stmt visit(src.Absyn.Empty p, A arg) {
		return new src.Absyn.Empty();
	}

	public Stmt visit(src.Absyn.BStmt p, A arg) throws TypeException {
		Blk blk_ = p.blk_.accept(this, arg);
		return new src.Absyn.BStmt(blk_);
	}

	public Stmt visit(src.Absyn.Decl p, A arg) throws TypeException {
		Type type_ = p.type_.accept(this, arg);
		ListItem listitem_ = new ListItem();
		for (Item x : p.listitem_) {
			listitem_.add(x.accept(this, arg));
		}
		return new src.Absyn.Decl(type_, listitem_);
	}

	public Stmt visit(src.Absyn.Ass p, A arg) throws TypeException {
		String ident_ = p.ident_;
		Expr expr_ = p.expr_.accept(this, arg);
		return new src.Absyn.Ass(ident_, expr_);
	}

	public Stmt visit(src.Absyn.Incr p, A arg) {
		String ident_ = p.ident_;
		return new src.Absyn.Incr(ident_);
	}

	public Stmt visit(src.Absyn.Decr p, A arg) {
		String ident_ = p.ident_;
		return new src.Absyn.Decr(ident_);
	}

	public Stmt visit(src.Absyn.Ret p, A arg) throws TypeException {
		Expr expr_ = p.expr_.accept(this, arg);
		return new src.Absyn.Ret(expr_);
	}

	public Stmt visit(src.Absyn.VRet p, A arg) {
		return new src.Absyn.VRet();
	}

	public Stmt visit(src.Absyn.Cond p, A arg) throws TypeException {
		Expr expr_ = p.expr_.accept(this, arg);
		Stmt stmt_ = p.stmt_.accept(this, arg);
		return new src.Absyn.Cond(expr_, stmt_);
	}

	public Stmt visit(src.Absyn.CondElse p, A arg) throws TypeException {
		Expr expr_ = p.expr_.accept(this, arg);
		Stmt stmt_1 = p.stmt_1.accept(this, arg);
		Stmt stmt_2 = p.stmt_2.accept(this, arg);
		return new src.Absyn.CondElse(expr_, stmt_1, stmt_2);
	}

	public Stmt visit(src.Absyn.While p, A arg) throws TypeException {
		Expr expr_ = p.expr_.accept(this, arg);
		Stmt stmt_ = p.stmt_.accept(this, arg);
		return new src.Absyn.While(expr_, stmt_);
	}

	public Stmt visit(src.Absyn.SExp p, A arg) throws TypeException {
		Expr expr_ = p.expr_.accept(this, arg);
		return new src.Absyn.SExp(expr_);
	}

	/* Item */
	public Item visit(src.Absyn.NoInit p, A arg) {
		String ident_ = p.ident_;
		return new src.Absyn.NoInit(ident_);
	}

	public Item visit(src.Absyn.Init p, A arg) throws TypeException {
		String ident_ = p.ident_;
		Expr expr_ = p.expr_.accept(this, arg);
		return new src.Absyn.Init(ident_, expr_);
	}

	/* Type */
	public Type visit(src.Absyn.Int p, A arg) {
		return new src.Absyn.Int();
	}

	public Type visit(src.Absyn.Doub p, A arg) {
		return new src.Absyn.Doub();
	}

	public Type visit(src.Absyn.Bool p, A arg) {
		return new src.Absyn.Bool();
	}

	public Type visit(src.Absyn.Void p, A arg) {
		return new src.Absyn.Void();
	}

	public Type visit(src.Absyn.Fun p, A arg) {
		Type type_ = p.type_.accept(this, arg);
		ListType listtype_ = new ListType();
		for (Type x : p.listtype_) {
			listtype_.add(x.accept(this, arg));
		}
		return new src.Absyn.Fun(type_, listtype_);
	}

	/* Expr */
	public Expr visit(src.Absyn.EVar p, A arg) {
		String ident_ = p.ident_;
		return new src.Absyn.EVar(ident_);
	}

	public Expr visit(src.Absyn.ELitInt p, A arg) {
		Integer integer_ = p.integer_;
		return new src.Absyn.ELitInt(integer_);
	}

	public Expr visit(src.Absyn.ELitDoub p, A arg) {
		Double double_ = p.double_;
		return new src.Absyn.ELitDoub(double_);
	}

	public Expr visit(src.Absyn.ELitTrue p, A arg) {
		return new src.Absyn.ELitTrue();
	}

	public Expr visit(src.Absyn.ELitFalse p, A arg) {
		return new src.Absyn.ELitFalse();
	}

	public Expr visit(src.Absyn.EApp p, A arg) throws TypeException {
		String ident_ = p.ident_;
		ListExpr listexpr_ = new ListExpr();
		for (Expr x : p.listexpr_) {
			listexpr_.add(x.accept(this, arg));
		}
		return new src.Absyn.EApp(ident_, listexpr_);
	}

	public Expr visit(src.Absyn.EString p, A arg) {
		String string_ = p.string_;
		return new src.Absyn.EString(string_);
	}

	public Expr visit(src.Absyn.Neg p, A arg) throws TypeException {
		Expr expr_ = p.expr_.accept(this, arg);
		return new src.Absyn.Neg(expr_);
	}

	public Expr visit(src.Absyn.Not p, A arg) throws TypeException {
		Expr expr_ = p.expr_.accept(this, arg);
		return new src.Absyn.Not(expr_);
	}

	public Expr visit(src.Absyn.EMul p, A arg) throws TypeException {
		Expr expr_1 = p.expr_1.accept(this, arg);
		MulOp mulop_ = p.mulop_.accept(this, arg);
		Expr expr_2 = p.expr_2.accept(this, arg);
		return new src.Absyn.EMul(expr_1, mulop_, expr_2);
	}

	public Expr visit(src.Absyn.EAdd p, A arg) throws TypeException {
		Expr expr_1 = p.expr_1.accept(this, arg);
		AddOp addop_ = p.addop_.accept(this, arg);
		Expr expr_2 = p.expr_2.accept(this, arg);
		return new src.Absyn.EAdd(expr_1, addop_, expr_2);
	}

	public Expr visit(src.Absyn.ERel p, A arg) throws TypeException {
		Expr expr_1 = p.expr_1.accept(this, arg);
		RelOp relop_ = p.relop_.accept(this, arg);
		Expr expr_2 = p.expr_2.accept(this, arg);
		return new src.Absyn.ERel(expr_1, relop_, expr_2);
	}

	public Expr visit(src.Absyn.EAnd p, A arg) throws TypeException {
		Expr expr_1 = p.expr_1.accept(this, arg);
		Expr expr_2 = p.expr_2.accept(this, arg);
		return new src.Absyn.EAnd(expr_1, expr_2);
	}

	public Expr visit(src.Absyn.EOr p, A arg) throws TypeException {
		Expr expr_1 = p.expr_1.accept(this, arg);
		Expr expr_2 = p.expr_2.accept(this, arg);
		return new src.Absyn.EOr(expr_1, expr_2);
	}

	/* AddOp */
	public AddOp visit(src.Absyn.Plus p, A arg) {
		return new src.Absyn.Plus();
	}

	public AddOp visit(src.Absyn.Minus p, A arg) {
		return new src.Absyn.Minus();
	}

	/* MulOp */
	public MulOp visit(src.Absyn.Times p, A arg) {
		return new src.Absyn.Times();
	}

	public MulOp visit(src.Absyn.Div p, A arg) {
		return new src.Absyn.Div();
	}

	public MulOp visit(src.Absyn.Mod p, A arg) {
		return new src.Absyn.Mod();
	}

	/* RelOp */
	public RelOp visit(src.Absyn.LTH p, A arg) {
		return new src.Absyn.LTH();
	}

	public RelOp visit(src.Absyn.LE p, A arg) {
		return new src.Absyn.LE();
	}

	public RelOp visit(src.Absyn.GTH p, A arg) {
		return new src.Absyn.GTH();
	}

	public RelOp visit(src.Absyn.GE p, A arg) {
		return new src.Absyn.GE();
	}

	public RelOp visit(src.Absyn.EQU p, A arg) {
		return new src.Absyn.EQU();
	}

	public RelOp visit(src.Absyn.NE p, A arg) {
		return new src.Absyn.NE();
	}
}