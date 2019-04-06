package src;

import src.Absyn.*;

/** BNFC-Generated All Visitor */
public interface AllVisitor<R,A> extends
  src.Absyn.Prog.Visitor<R,A>,
  src.Absyn.TopDef.Visitor<R,A>,
  src.Absyn.Arg.Visitor<R,A>,
  src.Absyn.Blk.Visitor<R,A>,
  src.Absyn.Stmt.Visitor<R,A>,
  src.Absyn.Item.Visitor<R,A>,
  src.Absyn.Type.Visitor<R,A>,
  src.Absyn.Expr.Visitor<R,A>,
  src.Absyn.AddOp.Visitor<R,A>,
  src.Absyn.MulOp.Visitor<R,A>,
  src.Absyn.RelOp.Visitor<R,A>
{}
