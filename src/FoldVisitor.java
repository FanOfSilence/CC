package src;

import src.TypeException;
import src.Absyn.*;

/** BNFC-Generated Fold Visitor */
public abstract class FoldVisitor<R,A> implements AllVisitor<R,A> {
    public abstract R leaf(A arg);
    public abstract R combine(R x, R y, A arg);

/* Prog */
    public R visit(src.Absyn.Program p, A arg) throws TypeException {
      R r = leaf(arg);
      for (TopDef x : p.listtopdef_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      return r;
    }

/* TopDef */
    public R visit(src.Absyn.FnDef p, A arg) throws TypeException {
      R r = leaf(arg);
      r = combine(p.type_.accept(this, arg), r, arg);
      for (Arg x : p.listarg_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      r = combine(p.blk_.accept(this, arg), r, arg);
      return r;
    }

/* Arg */
    public R visit(src.Absyn.Argument p, A arg) {
      R r = leaf(arg);
      r = combine(p.type_.accept(this, arg), r, arg);
      return r;
    }

/* Blk */
    public R visit(src.Absyn.Block p, A arg) throws TypeException {
      R r = leaf(arg);
      for (Stmt x : p.liststmt_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      return r;
    }

/* Stmt */
    public R visit(src.Absyn.Empty p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(src.Absyn.BStmt p, A arg) throws TypeException {
      R r = leaf(arg);
      r = combine(p.blk_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(src.Absyn.Decl p, A arg) throws TypeException {
      R r = leaf(arg);
      r = combine(p.type_.accept(this, arg), r, arg);
      for (Item x : p.listitem_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      return r;
    }
    public R visit(src.Absyn.Ass p, A arg) throws TypeException {
      R r = leaf(arg);
      r = combine(p.expr_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(src.Absyn.Incr p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(src.Absyn.Decr p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(src.Absyn.Ret p, A arg) throws TypeException {
      R r = leaf(arg);
      r = combine(p.expr_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(src.Absyn.VRet p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(src.Absyn.Cond p, A arg) throws TypeException {
      R r = leaf(arg);
      r = combine(p.expr_.accept(this, arg), r, arg);
      r = combine(p.stmt_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(src.Absyn.CondElse p, A arg) throws TypeException {
      R r = leaf(arg);
      r = combine(p.expr_.accept(this, arg), r, arg);
      r = combine(p.stmt_1.accept(this, arg), r, arg);
      r = combine(p.stmt_2.accept(this, arg), r, arg);
      return r;
    }
    public R visit(src.Absyn.While p, A arg) throws TypeException {
      R r = leaf(arg);
      r = combine(p.expr_.accept(this, arg), r, arg);
      r = combine(p.stmt_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(src.Absyn.SExp p, A arg) throws TypeException {
      R r = leaf(arg);
      r = combine(p.expr_.accept(this, arg), r, arg);
      return r;
    }

/* Item */
    public R visit(src.Absyn.NoInit p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(src.Absyn.Init p, A arg) throws TypeException {
      R r = leaf(arg);
      r = combine(p.expr_.accept(this, arg), r, arg);
      return r;
    }

/* Type */
    public R visit(src.Absyn.Int p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(src.Absyn.Doub p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(src.Absyn.Bool p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(src.Absyn.Void p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(src.Absyn.Fun p, A arg) {
      R r = leaf(arg);
      r = combine(p.type_.accept(this, arg), r, arg);
      for (Type x : p.listtype_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      return r;
    }

/* Expr */
    public R visit(src.Absyn.EVar p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(src.Absyn.ELitInt p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(src.Absyn.ELitDoub p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(src.Absyn.ELitTrue p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(src.Absyn.ELitFalse p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(src.Absyn.EApp p, A arg) throws TypeException {
      R r = leaf(arg);
      for (Expr x : p.listexpr_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      return r;
    }
    public R visit(src.Absyn.EString p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(src.Absyn.Neg p, A arg) throws TypeException {
      R r = leaf(arg);
      r = combine(p.expr_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(src.Absyn.Not p, A arg) throws TypeException {
      R r = leaf(arg);
      r = combine(p.expr_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(src.Absyn.EMul p, A arg) throws TypeException {
      R r = leaf(arg);
      r = combine(p.expr_1.accept(this, arg), r, arg);
      r = combine(p.mulop_.accept(this, arg), r, arg);
      r = combine(p.expr_2.accept(this, arg), r, arg);
      return r;
    }
    public R visit(src.Absyn.EAdd p, A arg) throws TypeException {
      R r = leaf(arg);
      r = combine(p.expr_1.accept(this, arg), r, arg);
      r = combine(p.addop_.accept(this, arg), r, arg);
      r = combine(p.expr_2.accept(this, arg), r, arg);
      return r;
    }
    public R visit(src.Absyn.ERel p, A arg) throws TypeException {
      R r = leaf(arg);
      r = combine(p.expr_1.accept(this, arg), r, arg);
      r = combine(p.relop_.accept(this, arg), r, arg);
      r = combine(p.expr_2.accept(this, arg), r, arg);
      return r;
    }
    public R visit(src.Absyn.EAnd p, A arg) throws TypeException {
      R r = leaf(arg);
      r = combine(p.expr_1.accept(this, arg), r, arg);
      r = combine(p.expr_2.accept(this, arg), r, arg);
      return r;
    }
    public R visit(src.Absyn.EOr p, A arg) throws TypeException {
      R r = leaf(arg);
      r = combine(p.expr_1.accept(this, arg), r, arg);
      r = combine(p.expr_2.accept(this, arg), r, arg);
      return r;
    }

/* AddOp */
    public R visit(src.Absyn.Plus p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(src.Absyn.Minus p, A arg) {
      R r = leaf(arg);
      return r;
    }

/* MulOp */
    public R visit(src.Absyn.Times p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(src.Absyn.Div p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(src.Absyn.Mod p, A arg) {
      R r = leaf(arg);
      return r;
    }

/* RelOp */
    public R visit(src.Absyn.LTH p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(src.Absyn.LE p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(src.Absyn.GTH p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(src.Absyn.GE p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(src.Absyn.EQU p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(src.Absyn.NE p, A arg) {
      R r = leaf(arg);
      return r;
    }


}
