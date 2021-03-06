package src.Absyn; // Java Package generated by the BNF Converter.

public abstract class Type implements java.io.Serializable {
  public abstract <R,A> R accept(Type.Visitor<R,A> v, A arg);
  public interface Visitor <R,A> {
    public R visit(src.Absyn.Int p, A arg);
    public R visit(src.Absyn.Doub p, A arg);
    public R visit(src.Absyn.Bool p, A arg);
    public R visit(src.Absyn.Void p, A arg);
    public R visit(src.Absyn.Fun p, A arg);

  }

}
