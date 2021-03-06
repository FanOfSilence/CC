package src.Absyn; // Java Package generated by the BNF Converter.

import src.TypeException;

public abstract class Prog implements java.io.Serializable {
  public abstract <R,A> R accept(Prog.Visitor<R,A> v, A arg) throws TypeException;
  public interface Visitor <R,A> {
    public R visit(src.Absyn.Program p, A arg) throws TypeException;

  }

}
