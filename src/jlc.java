package src;
import java_cup.runtime.*;
import src.*;
import src.Absyn.*;
import src.VisitSkel.ProgVisitor;

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
      System.out.println();
      System.out.println("Parse Succesful!");
      System.out.println();
      System.out.println("[Abstract Syntax]");
      System.out.println();
      System.out.println(PrettyPrinter.show(parse_tree));
      System.out.println();
      System.out.println("[Linearized Tree]");
      System.out.println();
      System.out.println(PrettyPrinter.print(parse_tree));
      VisitSkel skel = new VisitSkel();
      VisitSkel.ProgVisitor progVis = skel.new ProgVisitor();
      parse_tree.accept(progVis, null);
      System.out.println("Program accepted without any type errors!");
      System.err.println("OK");
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
