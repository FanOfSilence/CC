package src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import src.Absyn.*;
import src.Absyn.Void;
import src.TypeChecker.BlkRet;
import src.TypeChecker.EvalExpr;
import src.TypeChecker.StmtRet;

public class LLVMCodeGenerator {
	private StringBuilder outputString;
	private int label;
	private int var;
	private int globalVar;
	private LLVMEnv env;
	private String getLLVMTypeFromType(Type t) {
		if (t.equals(new Int())) {
			return "i32";
		} else if (t.equals(new Doub())) {
			return "double";
		} else if (t.equals(new Bool())){
			return "i1";
		} else if (t.equals(new StringLit())) { // StringLit
			return "i8*";
		} else {
			return "void";
		}
	}
	
	private String defaultReturnValue(Type t) {
		if (t.equals(new Int())) {
			return "i32 0";
		} else if (t.equals(new Doub())) {
			return "double 0.0";
		} else if (t.equals(new Bool())){
			return "i1 false";
		} else if (t.equals(new StringLit())) { // StringLit
			return "i8* _";
		} else {
			return "void";
		}
	}
	
	private String getCmpFromType(Type t) {
		if (t.equals(new Int()) || t.equals(new Bool())) {
			return "icmp";
		} else if (t.equals(new Doub())) {
			return "fcmp";
		}
		System.out.print("Can't find cmp for type " + t.toString());
//		System.exit(1);
		return "";
	}
	private String sp(String s) {
		return " " + s + " ";
	}
	
	private String spL(String s) {
		return " " + s;
	}
	
	private String spR(String s) {
		return s + " ";
	}
	
	private String getLabel(int offset) {
		int val = label + offset;
		return "label_" + val;
	}
	
	private String newLabel() {
		label++;
		return "label_" + label;
	}
	
	private void newLine() {
		outputString.append("\n");
	}
	
	private String newLocalVar() {
		var++;
		return "%var_" + var;
	}
	
	private String newGlobalVar() {
		globalVar++;
		return "@globalVar_" + globalVar;
	}
	
	private String newLocalPointer(String s) {
		return s + "_p";
	}
	
	private String newLocalPointer() {
		return newLocalVar() + "_p";
	}

	private String javaletteVarToPointer(String jlVar) {
		return "%" + jlVar + "_p";
	}
	
	private String store(Type t1, String valToStore, String to) {
		String stringType = getLLVMTypeFromType(t1);
		return "\n" + "store " + stringType + spL(valToStore) + ", " + stringType + "*" + spL(to) + "\n";
	}
	
	private Tuple<String, String> alloca(Type t) {
		String varName = newLocalVar();
		String pointerName = newLocalPointer(varName);
		String stringType = getLLVMTypeFromType(t);
		String allocInstruction = "\n" + pointerName + " = alloca " + stringType + "\n"; 
		return new Tuple<String, String>(pointerName, allocInstruction);
	}
	
	private String alloca(String jlVarName, Type t) {
		String pointerName = javaletteVarToPointer(jlVarName);
		String stringType = getLLVMTypeFromType(t);
		String allocInstruction = "\n" + pointerName + " = alloca " + stringType + "\n";
		return allocInstruction;
	}
	
	
	private Tuple<String, String> load(Type t, String fromPointer) {
		String varName = newLocalVar();
		String stringType = getLLVMTypeFromType(t);
		String loadInstruction = "\n" + varName +  " = load " + stringType + ", " + stringType + "* " + fromPointer + "\n";
		return new Tuple<String, String>(varName, loadInstruction);
	}
	
	private String br1(String label) {
		return "\nbr label %" + label + "\n";
	}
	
	private String br2(String expr, String label1, String label2) {
		return "\nbr i1 " + expr + ", label %" + label1 + ", label %" + label2;
	}
	
	private Tuple<String, String> call3(Type t, LLVMFunType fun, List<String> arguments) {
		String var = newLocalVar();
		String callInstruction = "\n" + var + " = call " + spR(getLLVMTypeFromType(t)) + fun.name + "(";
		if (!fun.argTypes.isEmpty()) {
			callInstruction += getLLVMTypeFromType(fun.argTypes.get(0)) + spL(arguments.get(0));
		}
		for (int i = 1; i < arguments.size(); i++) {
			callInstruction += " ,";
			callInstruction += spR(getLLVMTypeFromType(fun.argTypes.get(i)));
			callInstruction += arguments.get(i);
		}
		callInstruction += ")";
		return new Tuple<String, String>(var, callInstruction);
	}
	
	private String call2(LLVMFunType fun, List<String> arguments) {
		String callInstruction = "\ncall " + spR(getLLVMTypeFromType(fun.type)) + fun.name + "(";
		if (!fun.argTypes.isEmpty()) {
			callInstruction += getLLVMTypeFromType(fun.argTypes.get(0)) + spL(arguments.get(0));
		}
		for (int i = 1; i < arguments.size(); i++) {
			callInstruction += " ,";
			callInstruction += spR(getLLVMTypeFromType(fun.argTypes.get(i)));
			callInstruction += arguments.get(i);
		}
		callInstruction += ")";
		return callInstruction;
	}	
	
	
	private String ret(Type t, String expr) {
		return "\nret " + getLLVMTypeFromType(t) + spL(expr) + "\n";
	}
	
	private String vRet() {
		return "\nret void\n";
	}
	
	private Tuple<String, String> addMul(Type t, String op1, String op2, String operation) {
		String varName = newLocalVar();
		String instruction = "\n" + varName + " = " + sp(spR(operation) + getLLVMTypeFromType(t)) + " " + op1 + ", " + op2 + "\n";
		return new Tuple<String, String>(varName, instruction);
	}
	
	private Tuple<String, String> andOr(String op1, String op2, String operation) {
		String var = newLocalVar();
		String instruction = "\n" + var + " = " + operation + " i1 " + op1 + ", " + op2 + "\n";
		return new Tuple<String, String>(var, instruction);
	}
	
	private Tuple<String, String> not(String expr) {
		String var = newLocalVar();
		return new Tuple<String, String>(var, "\n" + var + " = xor i1 " + expr + ", -1\n");
	}
	
	
	public class ProgVisitor implements Prog.Visitor<String, String> {

		@Override
		public String visit(Program p, String arg) throws TypeException {
			outputString = new StringBuilder();
			label = 0;
			var = 0;
			globalVar = 0;
			env = new LLVMEnv();
			
			//Declare all functions available in linked file
			String printInt = "printInt";
			String llvmPrintInt = "@" + printInt;
			Type returnType = new Void();
			List<Type> argumentTypes = new ArrayList<Type>();
			argumentTypes.add(new Int());
			env.funTypes.put(printInt, new LLVMFunType(llvmPrintInt, argumentTypes, returnType));
			outputString.append("declare void @printInt(i32 %n)\n");

			String printString = "printString";
			String llvmPrintString = "@" + printString;
			List<Type> argumentTypesString = new ArrayList<Type>();
			argumentTypesString.add(new StringLit());
			env.funTypes.put(printString, new LLVMFunType(llvmPrintString, argumentTypesString, returnType));
			outputString.append("declare void @printString(i8* %s)\n");
			
			String printDouble = "printDouble";
			String llvmPrintDouble = "@" + printDouble;
			List<Type> argumentTypesDouble = new ArrayList<Type>();
			argumentTypesDouble.add(new Doub());
			env.funTypes.put(printDouble, new LLVMFunType(llvmPrintDouble, argumentTypesDouble, returnType));
			outputString.append("declare void @printDouble(double %d)\n");
			
			String readInt = "readInt";
			String llvmReadInt = "@" + readInt;
			env.funTypes.put(readInt, new LLVMFunType(llvmReadInt, new ArrayList<Type>(), new Int()));
			outputString.append("declare i32 @readInt()\n");
			
			String readDouble = "readDouble";
			String llvmReadDouble = "@" + readDouble;
			env.funTypes.put(readDouble, new LLVMFunType(llvmReadDouble, new ArrayList<Type>(), new Doub()));
			outputString.append("declare double @readDouble()\n");
			
			
			for (TopDef topDef : p.listtopdef_) {
				topDef.accept(new AddTopDef(), arg);
			}
			for (TopDef topDef : p.listtopdef_) {
				topDef.accept(new OutputTopDef(), null);
			}
			return outputString.toString();
		}
	}
	
	public class AddTopDef implements TopDef.Visitor<String, String> {

		@Override
		public String visit(FnDef p, String arg) throws TypeException {
			String name = p.ident_;
			String llvmName = "@" + name;
			List<Type> types = new ArrayList<Type>();
			for (Arg argument: p.listarg_) {
				types.add(argument.accept(new ArgumentType(), arg));
			}
			Type returnType = p.type_;
			env.funTypes.put(name, new LLVMFunType(llvmName, types, returnType));
			return null;
		}
	}
	
	public class ArgumentType implements Arg.Visitor<Type, String> {

		@Override
		public Type visit(Argument p, String arg) {
			return p.type_;
		}
	}
	
	public class ArgumentName implements Arg.Visitor<String, String> {

		@Override
		public String visit(Argument p, String arg) {
			return p.ident_;
		}
	}
	
	
	public class OutputTopDef implements TopDef.Visitor<String, String> {

		@Override
		public String visit(FnDef p, String arg) throws TypeException {
			String llvmFunName = env.funTypes.get(p.ident_).name;
			outputString.append("define " + spR(getLLVMTypeFromType(p.type_)) + llvmFunName + "(");
			//Add first argument
			
			if (!p.listarg_.isEmpty()) {
				Arg firstArg = p.listarg_.get(0);
				String argType0 = getLLVMTypeFromType(firstArg.accept(new ArgumentType(), arg));
				String argName0 = firstArg.accept(new ArgumentName(), arg);
				outputString.append(argType0 + spL("%" + argName0));
			}
			
			for (int i = 1; i < p.listarg_.size(); i++) {
				Arg argument = p.listarg_.get(i);
				String argType = getLLVMTypeFromType(argument.accept(new ArgumentType(), arg));
				String argName = argument.accept(new ArgumentName(), arg);
				outputString.append(", " + spR(argType) + "%" + argName);
			}
			outputString.append(") {");
			newLine();
			outputString.append("entry:\n");
			
			//Store all parameters to pointers
			for (int i = 0; i < p.listarg_.size(); i++) {
				Type t = p.listarg_.get(i).accept(new ArgumentType(), arg);
				String name = p.listarg_.get(i).accept(new ArgumentName(), arg);
				String allocaInstruction = alloca(name, t);
				outputString.append(allocaInstruction);
				newLine();
				String storeInstruction = store(t, "%" + name, javaletteVarToPointer(name));
				outputString.append(storeInstruction);
				newLine();
			}
			
			Boolean blkReturns = p.blk_.accept(new OutputBlk(), null);
			if (!blkReturns) {
				newLine();
				outputString.append("ret " + defaultReturnValue(p.type_));
			}
			newLine();
			outputString.append("}");
			newLine();
			return null;
		}
	}
	
	public class OutputItem implements Item.Visitor<String, Type> {

		@Override
		public String visit(NoInit p, Type t) {
			env.varTypes.put(p.ident_, t);
			
			String pointerName = javaletteVarToPointer(p.ident_);

			newLine();
			outputString.append("; Declaring " + p.ident_);
			
			String allocInstruction = alloca(p.ident_, t);
			outputString.append(allocInstruction);
			String val = "0";
			if (t.equals(new Doub())) {
				val = "0.0";
			}
			String storeInstruction = store(t, val, pointerName);
			outputString.append(storeInstruction);
			
			return pointerName;
		}

		@Override
		public String visit(Init p, Type t) throws TypeException {
			env.varTypes.put(p.ident_, t);
			
			String pointerName = javaletteVarToPointer(p.ident_);
			String expression = p.expr_.accept(new OutputExpr(), t);
			
			newLine();
			outputString.append("; Initializing " + p.ident_);
			
			String allocInstruction = alloca(p.ident_, t);
			outputString.append(allocInstruction);
			
			String storeInstruction = store(t, expression, pointerName);
			outputString.append(storeInstruction);
			
			return pointerName;
		}
		
	}
	
	public class OutputBlk implements Blk.Visitor<Boolean, String> {

		@Override
		public Boolean visit(Block p, String label) throws TypeException {
			//Will output a block AND check if the function has a return
			Boolean returns = false;
			for (Stmt stmt : p.liststmt_) {
				returns = stmt.accept(new OutputStmt(), label);
			}
			return returns;
		}
	}
	
	// On block level, makes sure that a block returns
	public class BlkRet implements Blk.Visitor<Boolean, String> {

		@Override
		public Boolean visit(Block p, String arg) throws TypeException {
			StmtRet stmtRetVisitor = new StmtRet();
			Boolean returns = false;
			for (Stmt stmt : p.liststmt_) {
				returns = stmt.accept(stmtRetVisitor, arg);
				if (returns) {
					break;
				}
			}
			return returns;
		}
	}
	
	public class OutputStmt implements Stmt.Visitor<Boolean, String> {

		@Override
		public Boolean visit(Empty p, String label) {
			String brInstruction = br1(label);
			outputString.append(brInstruction);
			return false;
		}

		@Override
		public Boolean visit(BStmt p, String label) throws TypeException {
			Boolean returns = p.blk_.accept(new OutputBlk(), label);
			if (!returns && label != null) {
				outputString.append(br1(label));
			}
			return returns;
		}

		@Override
		public Boolean visit(Decl p, String label) throws TypeException {
			for (Item item : p.listitem_) {
				item.accept(new OutputItem(), p.type_);
			}
			return false;
		}

		@Override
		public Boolean visit(Ass p, String label) throws TypeException {
			newLine();
			outputString.append("; Assigning to " + p.ident_);
			newLine();
			String expr = p.expr_.accept(new OutputExpr(), null);
			Type t =  p.expr_.accept(new ExprTypeVisitor(), null);
			
			String pointerName = javaletteVarToPointer(p.ident_);
			
			String storeInstruction = store(t, expr, pointerName);
			outputString.append(storeInstruction);
			
			return false;
		}

		@Override
		public Boolean visit(Incr p, String label) throws TypeException {
			Type t = env.varTypes.get(p.ident_);
			String pointer = javaletteVarToPointer(p.ident_);
			Tuple<String, String> loadTuple = load(t, pointer);
			String loadVar = loadTuple.x;
			String loadInstruction = loadTuple.y;
			outputString.append(loadInstruction);
			
			Tuple<String, String> addTuple = addMul(t, loadVar, "1", "add");
			String addInstruction = addTuple.y;
			String addVar = addTuple.x;
			outputString.append(addInstruction);
			
			String storeInstruction = store(t, addVar, pointer);
			outputString.append(storeInstruction);
			return false;
		}

		@Override
		public Boolean visit(Decr p, String label) throws TypeException {
			Type t = env.varTypes.get(p.ident_);
			String pointer = javaletteVarToPointer(p.ident_);
			Tuple<String, String> loadTuple = load(t, pointer);
			String loadVar = loadTuple.x;
			String loadInstruction = loadTuple.y;
			outputString.append(loadInstruction);
			
			Tuple<String, String> addTuple = addMul(t, loadVar, "1", "sub");
			String addInstruction = addTuple.y;
			String addVar = addTuple.x;
			outputString.append(addInstruction);
			
			String storeInstruction = store(t, addVar, pointer);
			outputString.append(storeInstruction);
			return false;
		}

		@Override
		public Boolean visit(Ret p, String label) throws TypeException {
			newLine();
			String expr = p.expr_.accept(new OutputExpr(), null);
			Type t = p.expr_.accept(new ExprTypeVisitor(), null);
			outputString.append(ret(t, expr));
			return true;
		}

		@Override
		public Boolean visit(VRet p, String label) throws TypeException {
			newLine();
			outputString.append(vRet());
			return true;
		}

		@Override
		public Boolean visit(Cond p, String label) throws TypeException {
			newLine();
			String expr = p.expr_.accept(new OutputExpr(), null);
			String trueLabel = newLabel();
			Boolean stmtReturns = p.stmt_.accept(new StmtRet(), null);
			String endLabel = newLabel();
			String brInstruction = br2(expr, trueLabel, endLabel);
			outputString.append(brInstruction);
			newLine();
			outputString.append("; trueLabel");
			newLine();
			outputString.append(trueLabel + ":");
			newLine();
			p.stmt_.accept(this, endLabel);
			if (!stmtReturns) {
				newLine();
				outputString.append(br1(endLabel));
			}
			newLine();
			outputString.append("; endLabel");
			newLine();
			outputString.append(endLabel + ":");
			return false;
		}

		@Override
		public Boolean visit(CondElse p, String label) throws TypeException {
			newLine();
			String expr = p.expr_.accept(new OutputExpr(), null);
			String trueLabel = newLabel();
			String falseLabel = newLabel();
			String endLabel = null;
			Boolean stmt1Returns = p.stmt_1.accept(new StmtRet(), null);
			Boolean stmt2Returns = p.stmt_2.accept(new StmtRet(), null);
			if (!(stmt1Returns && stmt2Returns)) {
				endLabel = newLabel();
			}
			String brInstruction = br2(expr, trueLabel, falseLabel);
			outputString.append(brInstruction);
			newLine();
			outputString.append("; trueLabel");
			newLine();
			outputString.append(trueLabel + ":");
			newLine();
			Boolean firstReturns = p.stmt_1.accept(this, endLabel);
			newLine();
			if (!stmt1Returns) {
				outputString.append(br1(endLabel));
				newLine();
			}
			outputString.append("; falseLabel");
			newLine();
			outputString.append(falseLabel + ":");
			newLine();
			Boolean secondReturns = p.stmt_2.accept(this, endLabel);
			if (!stmt2Returns) {
				newLine();
				outputString.append(br1(endLabel));
				newLine();
			}
			if (!stmt1Returns || !stmt2Returns) {
				outputString.append("; endLabel");
				newLine();
				outputString.append(endLabel + ":");
				newLine();
			}
			return firstReturns && secondReturns;
		}

		@Override
		public Boolean visit(While p, String label) throws TypeException {
			newLine();
			String expr = p.expr_.accept(new OutputExpr(), null);
			String trueLabel = newLabel();
			String endLabel = newLabel();
			String brInstruction = br2(expr, trueLabel, endLabel);
			outputString.append(brInstruction);
			newLine();
			outputString.append("; trueLabel");
			newLine();
			outputString.append(trueLabel + ":");
			Boolean returns = p.stmt_.accept(this, null);
			newLine();
			String newExpr = p.expr_.accept(new OutputExpr(), null);
			String brInstruction2 = br2(newExpr, trueLabel, endLabel);
			outputString.append(brInstruction2);
			newLine();
			outputString.append("; endLabel");
			newLine();
			outputString.append(endLabel + ":");
			return returns;
		}

		@Override
		public Boolean visit(SExp p, String label) throws TypeException {
			newLine();
			p.expr_.accept(new OutputExpr(), null);
			return false;
		}
		
	}
	
	// Class that checks that a statement contains a return in all possible paths
		public class StmtRet implements Stmt.Visitor<Boolean, String> {

			@Override
			public Boolean visit(Empty p, String arg) {
				return false;
			}

			@Override
			public Boolean visit(BStmt p, String arg) throws TypeException {
				return p.blk_.accept(new BlkRet(), arg);
			}

			@Override
			public Boolean visit(Decl p, String arg) throws TypeException {
				return false;
			}

			@Override
			public Boolean visit(Ass p, String arg) throws TypeException {
				return false;
			}

			@Override
			public Boolean visit(Incr p, String arg) throws TypeException {
				return false;
			}

			@Override
			public Boolean visit(Decr p, String arg) throws TypeException {
				return false;
			}

			@Override
			public Boolean visit(Ret p, String arg) throws TypeException {
				return true;
			}

			@Override
			public Boolean visit(VRet p, String arg) throws TypeException {
				return true;
			}

			@Override
			public Boolean visit(Cond p, String arg) throws TypeException {
				Boolean ifReturns = p.stmt_.accept(this, arg);
				return ifReturns;
			}

			@Override
			public Boolean visit(CondElse p, String arg) throws TypeException {
				Boolean ifReturns = p.stmt_1.accept(this, arg);
				Boolean elseReturns = p.stmt_2.accept(this, arg);
				return ifReturns && elseReturns;
			}

			@Override
			public Boolean visit(While p, String arg) throws TypeException {
				Boolean whileReturns = p.stmt_.accept(this, arg);
				return whileReturns;
			}

			@Override
			public Boolean visit(SExp p, String arg) throws TypeException {
				return false;
			}
			
		}
	
	public class OutputExpr implements Expr.Visitor<String, Type> {
		
		@Override
		public String visit(EVar p, Type arg) throws TypeException {
			String pointer = javaletteVarToPointer(p.ident_);
			Tuple<String, String> loadTuple = load(arg, pointer);
			String loadVar = loadTuple.x;
			String loadInstruction = loadTuple.y;
			newLine();
			outputString.append("; load var " + p.ident_);
			outputString.append(loadInstruction);
			return loadVar;
		}

		@Override
		public String visit(ELitInt p, Type arg) {
			return p.integer_.toString();
		}

		@Override
		public String visit(ELitDoub p, Type arg) {
			return p.double_.toString();
		}

		@Override
		public String visit(ELitTrue p, Type arg) {
			return "true";
		}

		@Override
		public String visit(ELitFalse p, Type arg) {
			return "false";
		}

		@Override
		public String visit(EApp p, Type arg) throws TypeException {
			LLVMFunType function = env.funTypes.get(p.ident_);
			Type t = function.type;
			List<String> exprs = new ArrayList<String>();
			for (Expr expr : p.listexpr_) {
				exprs.add(expr.accept(this, arg));
			}
			String callInstruction;
			String callVar = null;
			if (t.equals(new Void())) {
				callInstruction = call2(function, exprs);
			} else {
				Tuple<String, String> callTuple = call3(t, function, exprs);
				callVar = callTuple.x;
				callInstruction = callTuple.y;
			}
			outputString.append(callInstruction);
			newLine();
			return callVar;
		}

		@Override
		public String visit(EString p, Type arg) {
			// Add string to beginning of outputString
			String varName = newGlobalVar();
			int stringLength = p.string_.length() + 1;
			outputString.insert(0, "\n" + varName + " = internal constant [" + stringLength + " x i8] c\"" + p.string_ + "\\00\"" + "\n");
			String localVar = newLocalVar();
			outputString.append(localVar + " = getelementptr [" + stringLength + " x i8], [" + stringLength + " x i8]* " + varName + ", i32 0, i32 0");
			return localVar;
		}

		@Override
		public String visit(Neg p, Type t) throws TypeException {
			String var = p.expr_.accept(this, t);
			String typeString = getLLVMTypeFromType(t);
			String negVar = newLocalVar();
			String subOp = "sub";
			String val = "0";
			if (t.equals(new Doub())) {
				subOp = "fsub";
				val = "0.0";
			}
			
			outputString.append(negVar + " = " + spR(subOp) + typeString + spL(val) + ", " + var);
			return negVar;
		}

		@Override
		public String visit(Not p, Type t) throws TypeException {
			Type t1 = p.expr_.accept(new ExprTypeVisitor(), null);
			String expr = p.expr_.accept(this, t1);
			Tuple<String, String> notTuple = not(expr);
			String varName = notTuple.x;
			String notInstruction = notTuple.y;
			newLine();
			outputString.append(notInstruction);
			newLine();
			return varName;
		}

		@Override
		public String visit(EMul p, Type t) throws TypeException {
			String op1 = p.expr_1.accept(this, t);
			String op2 = p.expr_2.accept(this, t);
			String varName = newLocalVar();
			Type type = p.expr_1.accept(new ExprTypeVisitor(), null);
			String mulOp = p.mulop_.accept(new MulExpr(), type);
			outputString.append(varName + " = " + sp(spR(mulOp) + getLLVMTypeFromType(t)) + " " + op1 + ", " + op2);
			return varName;
		}

		@Override
		public String visit(EAdd p, Type t) throws TypeException {
			String op1 = p.expr_1.accept(this, t);
			String op2 = p.expr_2.accept(this, t);
			String varName = newLocalVar();
			Type type = p.expr_1.accept(new ExprTypeVisitor(), null);
			//Temporary solution. 
			if (type == null) {
				type = t;
			}
			String addOp = p.addop_.accept(new AddExpr(), type);
			outputString.append(varName + " = " + sp(spR(addOp) + getLLVMTypeFromType(t)) + " " + op1 + ", " + op2);
			return varName;
		}

		@Override
		public String visit(ERel p, Type t) throws TypeException {
			Type type = p.expr_1.accept(new ExprTypeVisitor(), null);
			String op1 = p.expr_1.accept(this, t);
			String op2 = p.expr_2.accept(this, t);
			Type t1 = p.expr_1.accept(new ExprTypeVisitor(), null);
			String relOp = p.relop_.accept(new RelExpr(), t1);
			String cmp = getCmpFromType(type);
			String typeString = getLLVMTypeFromType(type);
			String relVar = newLocalVar();
			newLine();
			outputString.append(relVar + sp("=") + sp(cmp)+ sp(relOp) + sp(typeString) + sp(op1) + sp(",") + sp(op2));
			newLine();
			return relVar;
		}

		@Override
		public String visit(EAnd p, Type t) throws TypeException {
			String lazyTrueLabel = newLabel();
			String lazyFalseLabel = newLabel();
			
			// Used for setting the value of the expression
			Tuple<String, String> allocTuple = alloca(new Bool());
			String allocVar = allocTuple.x;
			String allocInstruction = allocTuple.y;
			outputString.append(allocInstruction);
			//Set default value
			String storeFalseInstruction = store(new Bool(), "false", allocVar);
			outputString.append(storeFalseInstruction);
			
			
			String expr1 = p.expr_1.accept(this, t);
			Tuple<String, String> andTupleExpr1 = andOr(expr1, "1", "and");
			String andVarExpr1 = andTupleExpr1.x;
			String andInstructionExpr1 = andTupleExpr1.y;
			outputString.append(andInstructionExpr1);
			
			String lazyBranchInstruction = br2(andVarExpr1, lazyTrueLabel, lazyFalseLabel);
			outputString.append(lazyBranchInstruction);
			newLine();
			outputString.append(";lazyTrueLabel\n");
			outputString.append(lazyTrueLabel + ":");
			newLine();
			
			String expr2 = p.expr_2.accept(this, t);
			Tuple<String, String> andTupleExpr2 = andOr("1", expr2, "and");
			String andVarExpr2 = andTupleExpr2.x;
			String andInstructionExpr2 = andTupleExpr2.y;
			outputString.append(andInstructionExpr2);
			
			String storeTrueInstruction = store(new Bool(), andVarExpr2, allocVar);
			outputString.append(storeTrueInstruction);
			
			String alwaysBranchInstruction = br1(lazyFalseLabel);
			outputString.append(alwaysBranchInstruction);
			newLine();
			outputString.append("; lazyFalseLabel\n");
			outputString.append(lazyFalseLabel + ":");
			
			//Read back value stored to a register
			Tuple<String, String> loadTuple = load(new Bool(), allocVar);
			String loadVar = loadTuple.x;
			String loadInstruction = loadTuple.y;
			outputString.append(loadInstruction);
			
			return loadVar;
		}

		@Override
		public String visit(EOr p, Type t) throws TypeException {
			String lazyTrueLabel = newLabel();
			String lazyFalseLabel = newLabel();
			
			// Used for setting the value of the expression
			Tuple<String, String> allocTuple = alloca(new Bool());
			String allocVar = allocTuple.x;
			String allocInstruction = allocTuple.y;
			outputString.append(allocInstruction);
			//Set default value
			String storeTrueInstruction = store(new Bool(), "true", allocVar);
			outputString.append(storeTrueInstruction);
			
			
			String expr1 = p.expr_1.accept(this, t);
			Tuple<String, String> orTupleExpr1 = andOr(expr1, "0", "or");
			String orVarExpr1 = orTupleExpr1.x;
			String orInstructionExpr1 = orTupleExpr1.y;
			outputString.append(orInstructionExpr1);
			
			String lazyBranchInstruction = br2(orVarExpr1, lazyTrueLabel, lazyFalseLabel);
			outputString.append(lazyBranchInstruction);
			newLine();
			outputString.append(";lazyFalseLabel\n");
			outputString.append(lazyFalseLabel + ":");
			newLine();
			
			String expr2 = p.expr_2.accept(this, t);
			Tuple<String, String> orTupleExpr2 = andOr("0", expr2, "or");
			String orVarExpr2 = orTupleExpr2.x;
			String orInstructionExpr2 = orTupleExpr2.y;
			outputString.append(orInstructionExpr2);
			
			String storeFalseInstruction = store(new Bool(), orVarExpr2, allocVar);
			outputString.append(storeFalseInstruction);
			
			String alwaysBranchInstruction = br1(lazyTrueLabel);
			outputString.append(alwaysBranchInstruction);
			newLine();
			outputString.append("; lazyTrueLabel\n");
			outputString.append(lazyTrueLabel + ":");
			
			//Read back value stored to a register
			Tuple<String, String> loadTuple = load(new Bool(), allocVar);
			String loadVar = loadTuple.x;
			String loadInstruction = loadTuple.y;
			outputString.append(loadInstruction);
			
			return loadVar;
		}

		@Override
		public String visit(TypedE p, Type t) throws TypeException {
			return p.expr_.accept(this, p.type_);
		}

	}
	
	public class ExprTypeVisitor implements Expr.Visitor<Type, Int> {

		@Override
		public Type visit(EVar p, Int arg) throws TypeException {
			return null;
		}

		@Override
		public Type visit(ELitInt p, Int arg) {
			return null;
		}

		@Override
		public Type visit(ELitDoub p, Int arg) {
			return null;
		}

		@Override
		public Type visit(ELitTrue p, Int arg) {
			return null;
		}

		@Override
		public Type visit(ELitFalse p, Int arg) {
			return null;
		}

		@Override
		public Type visit(EApp p, Int arg) throws TypeException {
			return null;
		}

		@Override
		public Type visit(EString p, Int arg) {
			return null;
		}

		@Override
		public Type visit(Neg p, Int arg) throws TypeException {
			return null;
		}

		@Override
		public Type visit(Not p, Int arg) throws TypeException {
			return null;
		}

		@Override
		public Type visit(EMul p, Int arg) throws TypeException {
			return null;
		}

		@Override
		public Type visit(EAdd p, Int arg) throws TypeException {
			return null;
		}

		@Override
		public Type visit(ERel p, Int arg) throws TypeException {
			return null;
		}

		@Override
		public Type visit(EAnd p, Int arg) throws TypeException {
			return null;
		}

		@Override
		public Type visit(EOr p, Int arg) throws TypeException {
			return null;
		}

		@Override
		public Type visit(TypedE p, Int arg) throws TypeException {
			return p.type_;
		}
		
	}
	
	public class RelExpr implements RelOp.Visitor<String, Type> {

		@Override
		public String visit(LTH p, Type t) {
			String cmp = "slt";
			if (t.equals(new Doub())) {
				cmp = "olt";
			}
			return cmp;
		}

		@Override
		public String visit(LE p, Type t) {
			String cmp = "sle";
			if (t.equals(new Doub())) {
				cmp = "ole";
			}
			return cmp;
		}

		@Override
		public String visit(GTH p, Type t) {
			String cmp = "sgt";
			if (t.equals(new Doub())) {
				cmp = "ogt";
			}
			return cmp;
		}

		@Override
		public String visit(GE p, Type t) {
			String cmp = "sge";
			if (t.equals(new Doub())) {
				cmp = "oge";
			}
			return cmp;
		}

		@Override
		public String visit(EQU p, Type t) {
			String cmp = "eq";
			if (t.equals(new Doub())) {
				cmp = "oeq";
			}
			return cmp;
		}

		@Override
		public String visit(NE p, Type t) {
			String cmp = "ne";
			if (t.equals(new Doub())) {
				cmp = "one";
			}
			return cmp;
		}
		
	}
	
	public class AddExpr implements AddOp.Visitor<String, Type> {

		@Override
		public String visit(Plus p, Type t) {
			if (t.equals(new Int())) {
				return "add";
			} else if (t.equals(new Doub())) {
				return "fadd";
			}
			System.out.println("Unsupported type " + t.toString() + " for addition");
			System.exit(1);
			return null;
		}

		@Override
		public String visit(Minus p, Type t) {
			if (t.equals(new Int())) {
				return "sub";
			} else if (t.equals(new Doub())) {
				return "fsub";
			}
			System.out.println("Unsupported type " + t.toString() + " for subtraction");
			System.exit(1);
			return null;
		}
	}
	
	public class MulExpr implements MulOp.Visitor<String, Type> {

		@Override
		public String visit(Times p, Type t) {
			if (t.equals(new Int())) {
				return "mul";
			} else if (t.equals(new Doub())) {
				return "fmul";
			}
			System.out.println("Unsupported type " + t.toString() + " for multiplication");
			System.exit(1);
			return null;
		}

		@Override
		public String visit(Div p, Type t) {
			if (t.equals(new Int())) {
				return "sdiv";
			} else if (t.equals(new Doub())) {
				return "fdiv";
			}
			System.out.println("Unsupported type " + t.toString() + " for multiplication");
			System.exit(1);
			return null;
		}

		@Override
		public String visit(Mod p, Type t) {
			if (t.equals(new Int())) {
				return "srem";
			} else if (t.equals(new Doub())) {
				return "frem";
			}
			System.out.println("Unsupported type " + t.toString() + " for multiplication");
			System.exit(1);
			return null;
		}
		
	}
}

class LLVMFunType {
	public String name;
	public List<Type> argTypes;
	public Type type;
	
	public LLVMFunType(String name, List<Type> argTypes, Type type) {
		this.name = name;
		this.argTypes = argTypes;
		this.type = type;
	}
}

class LLVMEnv {
	public LinkedList<HashMap<String, Tuple<Type, Boolean>>> contexts;
	public LinkedList<HashMap<String, Boolean>> labelContexts;
	//Javalette varName, LLVM pointer
	public HashMap<String, String> vars;
	//Javalette function name, function type and arguments
	public HashMap<String, LLVMFunType> funTypes;
	public HashMap<String, Type> varTypes;
	
	public LLVMEnv() {
		emptyEnv();
		vars = new HashMap<String, String>();
		funTypes = new HashMap<String, LLVMFunType>();
		varTypes = new HashMap<String, Type>();
	}
	
	private Tuple<Type, Boolean> lookupVarCurrentContext(String id) {
		return contexts.getFirst().get(id);
	}
	
	public Tuple<Type, Boolean> lookupVar(String id) throws TypeException {
		for (HashMap<String, Tuple<Type, Boolean>> context : contexts) {
			if (context.containsKey(id)) {
				return context.get(id);
			}
		}
		throw new TypeException("Variable " + id + " used before declaration");
	}
	
	public void addVar(String id, Type t, boolean initialized) throws TypeException {
		if (lookupVarCurrentContext(id) != null) {
			throw new TypeException("Var " + id + " already defined in this scope");
		} else {
			 contexts.getFirst().put(id, new Tuple<Type, Boolean>(t, initialized));
		}
	}
	
	public void updateVar(String id) throws TypeException {
		Boolean initialized = true;
		for (HashMap<String, Tuple<Type, Boolean>> context : contexts) {
			if (context.containsKey(id)) {
				Type varType = context.get(id).x;
				context.put(id, new Tuple<Type, Boolean>(varType, initialized));
				return;
			}
		}
		throw new TypeException("Var " + id + " has to be declared before assignment");
	}
	
	
	public void emptyEnv() {
		contexts = new LinkedList<HashMap<String, Tuple<Type, Boolean>>>();
		labelContexts = new LinkedList<HashMap<String,Boolean>>();
	}
	
	public void newBlock() {
		contexts.addFirst(new HashMap<String, Tuple<Type, Boolean>>());
		labelContexts.addFirst(new HashMap<String, Boolean>());
	}
	
	public void endBlock() {
		contexts.removeFirst();
		labelContexts.removeFirst();
	}
}
