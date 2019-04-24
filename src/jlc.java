package src;
import java_cup.runtime.*;
import src.*;
import src.Absyn.*;
import src.TypeChecker.ProgVisitor;

import java.io.*;

public class jlc
{
  public static void main(String args[]) throws Exception
  {
    Yylex l = null;
    parser p;
    try
    {
      if (args.length == 0) l = new Yylex(new InputStreamReader(System.in));
      else l = new Yylex(new FileReader(args[0]));
    }
    catch(FileNotFoundException e)
    {
     System.err.println("Error: File not found: " + args[0]);
     System.exit(1);
    }
    p = new parser(l);
    /* The default parser is the first-defined entry point. */
    /* You may want to change this. Other options are: */
    /*  */
    try
    {
      src.Absyn.Prog parse_tree = p.pProg();
//      System.out.println();
//      System.out.println("Parse Succesful!");
//      System.out.println();
//      System.out.println("[Abstract Syntax]");
//      System.out.println();
//      System.out.println(PrettyPrinter.show(parse_tree));
//      System.out.println();
//      System.out.println("[Linearized Tree]");
//      System.out.println();
//      System.out.println(PrettyPrinter.print(parse_tree));
      TypeChecker skel = new TypeChecker();
      TypeChecker.ProgVisitor progVis = skel.new ProgVisitor();
      Prog typedParseTree = parse_tree.accept(progVis, null);
//      System.out.println("Program accepted without any type errors!");
//      System.out.println("[Abstract Syntax with type annotations]");
//      System.out.println();
//      System.out.println(PrettyPrinter.show(typedParseTree));
      System.err.println("OK");
      LLVMCodeGenerator codeGenerator = new LLVMCodeGenerator();
      LLVMCodeGenerator.ProgVisitor outputProg = codeGenerator.new ProgVisitor();
      String outputtedString = typedParseTree.accept(outputProg, null);
      System.out.print(outputtedString);
    }
    catch (TypeException t) {
    	t.printStackTrace();
        System.out.println("At line " + String.valueOf(l.line_num()) + ", near \"" + l.buff() + "\" :");
        System.out.println("     " + t.getMessage());
        System.err.println("ERROR");
        System.exit(1);
    }
    catch(Throwable e)
    {
    	e.printStackTrace();
      System.out.println("At line " + String.valueOf(l.line_num()) + ", near \"" + l.buff() + "\" :");
      System.out.println("     " + e.getMessage());
      System.err.println("ERROR");
      System.exit(1);
    }
  }
}
