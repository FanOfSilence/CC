package src;
import src.Absyn.*;
/** BNFC-Generated Abstract Visitor */
public class AbstractVisitor<R,A> implements AllVisitor<R,A> {
/* Prog */
    public R visit(src.Absyn.Program p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(src.Absyn.Prog p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* TopDef */
    public R visit(src.Absyn.FnDef p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(src.Absyn.TopDef p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* Arg */
    public R visit(src.Absyn.Argument p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(src.Absyn.Arg p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* Blk */
    public R visit(src.Absyn.Block p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(src.Absyn.Blk p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* Stmt */
    public R visit(src.Absyn.Empty p, A arg) { return visitDefault(p, arg); }
    public R visit(src.Absyn.BStmt p, A arg) { return visitDefault(p, arg); }
    public R visit(src.Absyn.Decl p, A arg) { return visitDefault(p, arg); }
    public R visit(src.Absyn.Ass p, A arg) { return visitDefault(p, arg); }
    public R visit(src.Absyn.Incr p, A arg) { return visitDefault(p, arg); }
    public R visit(src.Absyn.Decr p, A arg) { return visitDefault(p, arg); }
    public R visit(src.Absyn.Ret p, A arg) { return visitDefault(p, arg); }
    public R visit(src.Absyn.VRet p, A arg) { return visitDefault(p, arg); }
    public R visit(src.Absyn.Cond p, A arg) { return visitDefault(p, arg); }
    public R visit(src.Absyn.CondElse p, A arg) { return visitDefault(p, arg); }
    public R visit(src.Absyn.While p, A arg) { return visitDefault(p, arg); }
    public R visit(src.Absyn.SExp p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(src.Absyn.Stmt p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* Item */
    public R visit(src.Absyn.NoInit p, A arg) { return visitDefault(p, arg); }
    public R visit(src.Absyn.Init p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(src.Absyn.Item p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* Type */
    public R visit(src.Absyn.Int p, A arg) { return visitDefault(p, arg); }
    public R visit(src.Absyn.Doub p, A arg) { return visitDefault(p, arg); }
    public R visit(src.Absyn.Bool p, A arg) { return visitDefault(p, arg); }
    public R visit(src.Absyn.Void p, A arg) { return visitDefault(p, arg); }
    public R visit(src.Absyn.Fun p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(src.Absyn.Type p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* Expr */
    public R visit(src.Absyn.EVar p, A arg) { return visitDefault(p, arg); }
    public R visit(src.Absyn.ELitInt p, A arg) { return visitDefault(p, arg); }
    public R visit(src.Absyn.ELitDoub p, A arg) { return visitDefault(p, arg); }
    public R visit(src.Absyn.ELitTrue p, A arg) { return visitDefault(p, arg); }
    public R visit(src.Absyn.ELitFalse p, A arg) { return visitDefault(p, arg); }
    public R visit(src.Absyn.EApp p, A arg) { return visitDefault(p, arg); }
    public R visit(src.Absyn.EString p, A arg) { return visitDefault(p, arg); }

    public R visit(src.Absyn.Neg p, A arg) { return visitDefault(p, arg); }
    public R visit(src.Absyn.Not p, A arg) { return visitDefault(p, arg); }

    public R visit(src.Absyn.EMul p, A arg) { return visitDefault(p, arg); }

    public R visit(src.Absyn.EAdd p, A arg) { return visitDefault(p, arg); }

    public R visit(src.Absyn.ERel p, A arg) { return visitDefault(p, arg); }

    public R visit(src.Absyn.EAnd p, A arg) { return visitDefault(p, arg); }

    public R visit(src.Absyn.EOr p, A arg) { return visitDefault(p, arg); }

    public R visitDefault(src.Absyn.Expr p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* AddOp */
    public R visit(src.Absyn.Plus p, A arg) { return visitDefault(p, arg); }
    public R visit(src.Absyn.Minus p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(src.Absyn.AddOp p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* MulOp */
    public R visit(src.Absyn.Times p, A arg) { return visitDefault(p, arg); }
    public R visit(src.Absyn.Div p, A arg) { return visitDefault(p, arg); }
    public R visit(src.Absyn.Mod p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(src.Absyn.MulOp p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* RelOp */
    public R visit(src.Absyn.LTH p, A arg) { return visitDefault(p, arg); }
    public R visit(src.Absyn.LE p, A arg) { return visitDefault(p, arg); }
    public R visit(src.Absyn.GTH p, A arg) { return visitDefault(p, arg); }
    public R visit(src.Absyn.GE p, A arg) { return visitDefault(p, arg); }
    public R visit(src.Absyn.EQU p, A arg) { return visitDefault(p, arg); }
    public R visit(src.Absyn.NE p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(src.Absyn.RelOp p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }

}
