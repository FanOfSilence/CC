package src;

import src.Absyn.*;

public class LLVMCodeGenerator {
	private StringBuilder outputString;
	private String getLLVMTypeFromType(Type t) {
		if (t.equals(new Int())) {
			return "i32";
		} else if (t.equals(new Doub())) {
			return "d32";
		} else { // bool
			return "i1";
		}
	}
	private String getOutputString(String s) {
		return " " + s + " ";
	}
	
	public class ProgVisitor implements Prog.Visitor<String, String> {

		@Override
		public String visit(Program p, String arg) throws TypeException {
			outputString = new StringBuilder();
			for (TopDef topDef : p.listtopdef_) {
				topDef.accept(new OutputTopDef(), null);
			}
			return outputString.toString();
		}
		
	}
	
	public class OutputTopDef implements TopDef.Visitor<String, String> {

		@Override
		public String visit(FnDef p, String arg) throws TypeException {
			p.blk_.accept(new OutputBlk(), null);
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	public class OutputItem implements Item.Visitor<String, Type> {

		@Override
		public String visit(NoInit p, Type t) {
			String llvmTypeString = getLLVMTypeFromType(t);
			String llvmPointerString = llvmTypeString + "*";
			String pointerName = "%" + p.ident_ + "_p";
			String varName = "%" + p.ident_;
			outputString.append("\n");
			outputString.append(pointerName + " = alloca " + llvmTypeString);
			outputString.append("\n");
			outputString.append("store " + llvmTypeString + " 0, " + llvmPointerString + " " + pointerName);
			outputString.append("\n");
			outputString.append(varName + " = load " + llvmTypeString + ", " + llvmPointerString + " " + pointerName);
			outputString.append("\n");
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String visit(Init p, Type t) throws TypeException {
			String llvmTypeString = getLLVMTypeFromType(t);
			String llvmPointerString = llvmTypeString + "*";
			String pointerName = "%" + p.ident_ + "_p";
			String varName = "%" + p.ident_;
			String expression = p.expr_.accept(new OutputExpr(), null);
			outputString.append("\n");
			outputString.append(pointerName + " = alloca " + llvmTypeString);
			outputString.append("\n");
			outputString.append("store " + llvmTypeString + ", " + expression + " " + llvmPointerString + " " + pointerName);
			outputString.append("\n");
			outputString.append(varName + " = load " + llvmTypeString + ", " + llvmPointerString + " " + pointerName);
			outputString.append("\n");			
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	public class OutputBlk implements Blk.Visitor<String, String> {

		@Override
		public String visit(Block p, String arg) throws TypeException {
			for (Stmt stmt : p.liststmt_) {
				stmt.accept(new OutputStmt(), null);
			}
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	public class OutputStmt implements Stmt.Visitor<String, String> {

		@Override
		public String visit(Empty p, String arg) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String visit(BStmt p, String arg) throws TypeException {
			p.blk_.accept(new OutputBlk(), arg);
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String visit(Decl p, String arg) throws TypeException {
			for (Item item : p.listitem_) {
				item.accept(new OutputItem(), p.type_);
			}
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String visit(Ass p, String arg) throws TypeException {
			outputString.append("\n");
			p.expr_.accept(new OutputExpr(), null);
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String visit(Incr p, String arg) throws TypeException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String visit(Decr p, String arg) throws TypeException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String visit(Ret p, String arg) throws TypeException {
			outputString.append("\n");
			p.expr_.accept(new OutputExpr(), null);
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String visit(VRet p, String arg) throws TypeException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String visit(Cond p, String arg) throws TypeException {
			outputString.append("\n");
			p.expr_.accept(new OutputExpr(), null);
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String visit(CondElse p, String arg) throws TypeException {
			outputString.append("\n");
			p.expr_.accept(new OutputExpr(), null);
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String visit(While p, String arg) throws TypeException {
			outputString.append("\n");
			p.expr_.accept(new OutputExpr(), null);
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String visit(SExp p, String arg) throws TypeException {
			outputString.append("\n");
			p.expr_.accept(new OutputExpr(), null);
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	public class OutputExpr implements Expr.Visitor<String, Type> {
		
		public void main(String[] args) {
			
		}

		@Override
		public String visit(EVar p, Type arg) throws TypeException {
			// TODO Auto-generated method stub
			return p.ident_;
		}

		@Override
		public String visit(ELitInt p, Type arg) {
//			outputString.append(getOutputString(p.integer_.toString()));
			return p.integer_.toString();
		}

		@Override
		public String visit(ELitDoub p, Type arg) {
//			outputString.append(getOutputString(p.double_.toString()));
			return p.double_.toString();
		}

		@Override
		public String visit(ELitTrue p, Type arg) {
//			outputString.append(getOutputString("true"));
			return "true";
		}

		@Override
		public String visit(ELitFalse p, Type arg) {
//			outputString.append(getOutputString("false"));
			return "false";
		}

		@Override
		public String visit(EApp p, Type arg) throws TypeException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String visit(EString p, Type arg) {
			// TODO: read variable name
			// Add string to beginning
			String varName = "@string";
			outputString.insert(0, "\n" + varName + " = internal constant + [" + p.string_.length() + 2 + " x i8] c\"" + p.string_ + "\\A0\\00\"" + "\n");
			//TODO: add in call?
			return varName;
		}

		@Override
		public String visit(Neg p, Type t) throws TypeException {
			String var = p.expr_.accept(this, t);
			String typeString = getLLVMTypeFromType(t);
			String negVar = var + "_n";
			outputString.append(negVar + " = sub " + typeString + " 0, " + var);
			return negVar;
		}

		@Override
		public String visit(Not p, Type t) throws TypeException {
			outputString.append(getOutputString("!("));
			p.expr_.accept(this, t);
			outputString.append(getOutputString(")"));
			return null;
		}

		@Override
		public String visit(EMul p, Type t) throws TypeException {
			outputString.append(getOutputString("mul " + getLLVMTypeFromType(t)));
			p.expr_1.accept(this, t);
			outputString.append(getOutputString(","));
			p.expr_2.accept(this, t);			
			return null;
		}

		@Override
		public String visit(EAdd p, Type t) throws TypeException {
			String op1 = p.expr_1.accept(this, t);
			String op2 = p.expr_2.accept(this, t);
			String varName = "%addVar";
			outputString.append(varName + " = " + getOutputString("add " + getLLVMTypeFromType(t)) + " " + op1 + ", " + op2);
			return varName;
		}

		@Override
		public String visit(ERel p, Type t) throws TypeException {
			p.expr_1.accept(this, t);
			outputString.append(getOutputString(p.relop_.accept(new RelExpr(), null)));
			p.expr_2.accept(this, t);
			return null;
		}

		@Override
		public String visit(EAnd p, Type t) throws TypeException {
			outputString.append(getOutputString("and " + getLLVMTypeFromType(t)));
			p.expr_1.accept(this, t);
			outputString.append(getOutputString(","));
			p.expr_2.accept(this, t);
			return null;
		}

		@Override
		public String visit(EOr p, Type t) throws TypeException {
			outputString.append(getOutputString("or " + getLLVMTypeFromType(t)));
			p.expr_1.accept(this, t);
			outputString.append(getOutputString(","));
			p.expr_2.accept(this, t);
			return null;
		}

		@Override
		public String visit(TypedE p, Type t) throws TypeException {
			return p.expr_.accept(this, p.type_);
		}

	}
	
	public class RelExpr implements RelOp.Visitor<String, String> {

		@Override
		public String visit(LTH p, String arg) {
			return "slt";
		}

		@Override
		public String visit(LE p, String arg) {
			return "sle";
		}

		@Override
		public String visit(GTH p, String arg) {
			return "sgt";
		}

		@Override
		public String visit(GE p, String arg) {
			return "sge";
		}

		@Override
		public String visit(EQU p, String arg) {
			return "eq";
		}

		@Override
		public String visit(NE p, String arg) {
			return "ne";
		}
		
	}
}
