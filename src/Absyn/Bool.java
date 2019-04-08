package src.Absyn; // Java Package generated by the BNF Converter.

public class Bool extends Type {
  public Bool() { }

  public <R,A> R accept(src.Absyn.Type.Visitor<R,A> v, A arg) { return v.visit(this, arg); }

  public boolean equals(Object o) {
    if (this == o) return true;
    if (o instanceof src.Absyn.Bool) {
      return true;
    }
    return false;
  }

  public int hashCode() {
    return 37;
  }


}