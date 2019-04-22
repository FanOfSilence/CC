package src;

import src.Absyn.Type;
import src.Absyn.Type.Visitor;

// Internal type for string literals that can be used in printString
public class StringLit extends Type {
	@Override
	public <R, A> R accept(Visitor<R, A> v, A arg) {
		return null;
	}
	
	  public boolean equals(Object o) {
		    if (this == o) return true;
		    if (o instanceof StringLit) {
		      return true;
		    }
		    return false;
		  }
	
	  public int hashCode() {
	    return 37;
	  }
}
