package src.Absyn; // Java Package generated by the BNF Converter.

import src.TypeException;

public class Block extends Blk {
  public final ListStmt liststmt_;
  public Block(ListStmt p1) { liststmt_ = p1; }

  public <R,A> R accept(src.Absyn.Blk.Visitor<R,A> v, A arg) throws TypeException { return v.visit(this, arg); }

  public boolean equals(Object o) {
    if (this == o) return true;
    if (o instanceof src.Absyn.Block) {
      src.Absyn.Block x = (src.Absyn.Block)o;
      return this.liststmt_.equals(x.liststmt_);
    }
    return false;
  }

  public int hashCode() {
    return this.liststmt_.hashCode();
  }


}
