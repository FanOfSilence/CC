package src.Absyn; // Java Package generated by the BNF Converter.

public class NE extends RelOp {
  public NE() { }

  public <R,A> R accept(src.Absyn.RelOp.Visitor<R,A> v, A arg) { return v.visit(this, arg); }

  public boolean equals(Object o) {
    if (this == o) return true;
    if (o instanceof src.Absyn.NE) {
      return true;
    }
    return false;
  }

  public int hashCode() {
    return 37;
  }


}
