package src.Absyn; // Java Package generated by the BNF Converter.

import src.TypeException;

public abstract class Item implements java.io.Serializable {
  public abstract <R,A> R accept(Item.Visitor<R,A> v, A arg) throws TypeException;
  public interface Visitor <R,A> {
    public R visit(src.Absyn.NoInit p, A arg);
    public R visit(src.Absyn.Init p, A arg) throws TypeException;

  }

}
