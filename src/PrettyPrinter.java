package src;
import src.Absyn.*;

public class PrettyPrinter
{
  //For certain applications increasing the initial size of the buffer may improve performance.
  private static final int INITIAL_BUFFER_SIZE = 128;
  private static final int INDENT_WIDTH = 2;
  //You may wish to change the parentheses used in precedence.
  private static final String _L_PAREN = new String("(");
  private static final String _R_PAREN = new String(")");
  //You may wish to change render
  private static void render(String s)
  {
    if (s.equals("{"))
    {
       buf_.append("\n");
       indent();
       buf_.append(s);
       _n_ = _n_ + INDENT_WIDTH;
       buf_.append("\n");
       indent();
    }
    else if (s.equals("(") || s.equals("["))
       buf_.append(s);
    else if (s.equals(")") || s.equals("]"))
    {
       backup();
       buf_.append(s);
       buf_.append(" ");
    }
    else if (s.equals("}"))
    {
       int t;
       _n_ = _n_ - INDENT_WIDTH;
       for(t=0; t<INDENT_WIDTH; t++) {
         backup();
       }
       buf_.append(s);
       buf_.append("\n");
       indent();
    }
    else if (s.equals(","))
    {
       backup();
       buf_.append(s);
       buf_.append(" ");
    }
    else if (s.equals(";"))
    {
       backup();
       buf_.append(s);
       buf_.append("\n");
       indent();
    }
    else if (s.equals("")) return;
    else
    {
       buf_.append(s);
       buf_.append(" ");
    }
  }


  //  print and show methods are defined for each category.
  public static String print(src.Absyn.Prog foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(src.Absyn.Prog foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(src.Absyn.TopDef foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(src.Absyn.TopDef foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(src.Absyn.ListTopDef foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(src.Absyn.ListTopDef foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(src.Absyn.Arg foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(src.Absyn.Arg foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(src.Absyn.ListArg foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(src.Absyn.ListArg foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(src.Absyn.Blk foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(src.Absyn.Blk foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(src.Absyn.ListStmt foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(src.Absyn.ListStmt foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(src.Absyn.Stmt foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(src.Absyn.Stmt foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(src.Absyn.Item foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(src.Absyn.Item foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(src.Absyn.ListItem foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(src.Absyn.ListItem foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(src.Absyn.Type foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(src.Absyn.Type foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(src.Absyn.ListType foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(src.Absyn.ListType foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(src.Absyn.Expr foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(src.Absyn.Expr foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(src.Absyn.ListExpr foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(src.Absyn.ListExpr foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(src.Absyn.AddOp foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(src.Absyn.AddOp foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(src.Absyn.MulOp foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(src.Absyn.MulOp foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(src.Absyn.RelOp foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(src.Absyn.RelOp foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  /***   You shouldn't need to change anything beyond this point.   ***/

  private static void pp(src.Absyn.Prog foo, int _i_)
  {
    if (foo instanceof src.Absyn.Program)
    {
       src.Absyn.Program _program = (src.Absyn.Program) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_program.listtopdef_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(src.Absyn.TopDef foo, int _i_)
  {
    if (foo instanceof src.Absyn.FnDef)
    {
       src.Absyn.FnDef _fndef = (src.Absyn.FnDef) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_fndef.type_, 0);
       pp(_fndef.ident_, 0);
       render("(");
       pp(_fndef.listarg_, 0);
       render(")");
       pp(_fndef.blk_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(src.Absyn.ListTopDef foo, int _i_)
  {
     for (java.util.Iterator<TopDef> it = foo.iterator(); it.hasNext();)
     {
       pp(it.next(), _i_);
       if (it.hasNext()) {
         render("");
       } else {
         render("");
       }
     }  }

  private static void pp(src.Absyn.Arg foo, int _i_)
  {
    if (foo instanceof src.Absyn.Argument)
    {
       src.Absyn.Argument _argument = (src.Absyn.Argument) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_argument.type_, 0);
       pp(_argument.ident_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(src.Absyn.ListArg foo, int _i_)
  {
     for (java.util.Iterator<Arg> it = foo.iterator(); it.hasNext();)
     {
       pp(it.next(), _i_);
       if (it.hasNext()) {
         render(",");
       } else {
         render("");
       }
     }  }

  private static void pp(src.Absyn.Blk foo, int _i_)
  {
    if (foo instanceof src.Absyn.Block)
    {
       src.Absyn.Block _block = (src.Absyn.Block) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("{");
       pp(_block.liststmt_, 0);
       render("}");
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(src.Absyn.ListStmt foo, int _i_)
  {
     for (java.util.Iterator<Stmt> it = foo.iterator(); it.hasNext();)
     {
       pp(it.next(), _i_);
       if (it.hasNext()) {
         render("");
       } else {
         render("");
       }
     }  }

  private static void pp(src.Absyn.Stmt foo, int _i_)
  {
    if (foo instanceof src.Absyn.Empty)
    {
       src.Absyn.Empty _empty = (src.Absyn.Empty) foo;
       if (_i_ > 0) render(_L_PAREN);
       render(";");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof src.Absyn.BStmt)
    {
       src.Absyn.BStmt _bstmt = (src.Absyn.BStmt) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_bstmt.blk_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof src.Absyn.Decl)
    {
       src.Absyn.Decl _decl = (src.Absyn.Decl) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_decl.type_, 0);
       pp(_decl.listitem_, 0);
       render(";");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof src.Absyn.Ass)
    {
       src.Absyn.Ass _ass = (src.Absyn.Ass) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_ass.ident_, 0);
       render("=");
       pp(_ass.expr_, 0);
       render(";");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof src.Absyn.Incr)
    {
       src.Absyn.Incr _incr = (src.Absyn.Incr) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_incr.ident_, 0);
       render("++");
       render(";");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof src.Absyn.Decr)
    {
       src.Absyn.Decr _decr = (src.Absyn.Decr) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_decr.ident_, 0);
       render("--");
       render(";");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof src.Absyn.Ret)
    {
       src.Absyn.Ret _ret = (src.Absyn.Ret) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("return");
       pp(_ret.expr_, 0);
       render(";");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof src.Absyn.VRet)
    {
       src.Absyn.VRet _vret = (src.Absyn.VRet) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("return");
       render(";");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof src.Absyn.Cond)
    {
       src.Absyn.Cond _cond = (src.Absyn.Cond) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("if");
       render("(");
       pp(_cond.expr_, 0);
       render(")");
       pp(_cond.stmt_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof src.Absyn.CondElse)
    {
       src.Absyn.CondElse _condelse = (src.Absyn.CondElse) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("if");
       render("(");
       pp(_condelse.expr_, 0);
       render(")");
       pp(_condelse.stmt_1, 0);
       render("else");
       pp(_condelse.stmt_2, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof src.Absyn.While)
    {
       src.Absyn.While _while = (src.Absyn.While) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("while");
       render("(");
       pp(_while.expr_, 0);
       render(")");
       pp(_while.stmt_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof src.Absyn.SExp)
    {
       src.Absyn.SExp _sexp = (src.Absyn.SExp) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_sexp.expr_, 0);
       render(";");
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(src.Absyn.Item foo, int _i_)
  {
    if (foo instanceof src.Absyn.NoInit)
    {
       src.Absyn.NoInit _noinit = (src.Absyn.NoInit) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_noinit.ident_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof src.Absyn.Init)
    {
       src.Absyn.Init _init = (src.Absyn.Init) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_init.ident_, 0);
       render("=");
       pp(_init.expr_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(src.Absyn.ListItem foo, int _i_)
  {
     for (java.util.Iterator<Item> it = foo.iterator(); it.hasNext();)
     {
       pp(it.next(), _i_);
       if (it.hasNext()) {
         render(",");
       } else {
         render("");
       }
     }  }

  private static void pp(src.Absyn.Type foo, int _i_)
  {
    if (foo instanceof src.Absyn.Int)
    {
       src.Absyn.Int _int = (src.Absyn.Int) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("int");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof src.Absyn.Doub)
    {
       src.Absyn.Doub _doub = (src.Absyn.Doub) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("double");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof src.Absyn.Bool)
    {
       src.Absyn.Bool _bool = (src.Absyn.Bool) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("boolean");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof src.Absyn.Void)
    {
       src.Absyn.Void _void = (src.Absyn.Void) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("void");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof src.Absyn.Fun)
    {
       src.Absyn.Fun _fun = (src.Absyn.Fun) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_fun.type_, 0);
       render("(");
       pp(_fun.listtype_, 0);
       render(")");
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(src.Absyn.ListType foo, int _i_)
  {
     for (java.util.Iterator<Type> it = foo.iterator(); it.hasNext();)
     {
       pp(it.next(), _i_);
       if (it.hasNext()) {
         render(",");
       } else {
         render("");
       }
     }  }

  private static void pp(src.Absyn.Expr foo, int _i_)
  {
    if (foo instanceof src.Absyn.EVar)
    {
       src.Absyn.EVar _evar = (src.Absyn.EVar) foo;
       if (_i_ > 6) render(_L_PAREN);
       pp(_evar.ident_, 0);
       if (_i_ > 6) render(_R_PAREN);
    }
    else     if (foo instanceof src.Absyn.ELitInt)
    {
       src.Absyn.ELitInt _elitint = (src.Absyn.ELitInt) foo;
       if (_i_ > 6) render(_L_PAREN);
       pp(_elitint.integer_, 0);
       if (_i_ > 6) render(_R_PAREN);
    }
    else     if (foo instanceof src.Absyn.ELitDoub)
    {
       src.Absyn.ELitDoub _elitdoub = (src.Absyn.ELitDoub) foo;
       if (_i_ > 6) render(_L_PAREN);
       pp(_elitdoub.double_, 0);
       if (_i_ > 6) render(_R_PAREN);
    }
    else     if (foo instanceof src.Absyn.ELitTrue)
    {
       src.Absyn.ELitTrue _elittrue = (src.Absyn.ELitTrue) foo;
       if (_i_ > 6) render(_L_PAREN);
       render("true");
       if (_i_ > 6) render(_R_PAREN);
    }
    else     if (foo instanceof src.Absyn.ELitFalse)
    {
       src.Absyn.ELitFalse _elitfalse = (src.Absyn.ELitFalse) foo;
       if (_i_ > 6) render(_L_PAREN);
       render("false");
       if (_i_ > 6) render(_R_PAREN);
    }
    else     if (foo instanceof src.Absyn.EApp)
    {
       src.Absyn.EApp _eapp = (src.Absyn.EApp) foo;
       if (_i_ > 6) render(_L_PAREN);
       pp(_eapp.ident_, 0);
       render("(");
       pp(_eapp.listexpr_, 0);
       render(")");
       if (_i_ > 6) render(_R_PAREN);
    }
    else     if (foo instanceof src.Absyn.EString)
    {
       src.Absyn.EString _estring = (src.Absyn.EString) foo;
       if (_i_ > 6) render(_L_PAREN);
       pp(_estring.string_, 0);
       if (_i_ > 6) render(_R_PAREN);
    }
    else     if (foo instanceof src.Absyn.Neg)
    {
       src.Absyn.Neg _neg = (src.Absyn.Neg) foo;
       if (_i_ > 5) render(_L_PAREN);
       render("-");
       pp(_neg.expr_, 6);
       if (_i_ > 5) render(_R_PAREN);
    }
    else     if (foo instanceof src.Absyn.Not)
    {
       src.Absyn.Not _not = (src.Absyn.Not) foo;
       if (_i_ > 5) render(_L_PAREN);
       render("!");
       pp(_not.expr_, 6);
       if (_i_ > 5) render(_R_PAREN);
    }
    else     if (foo instanceof src.Absyn.EMul)
    {
       src.Absyn.EMul _emul = (src.Absyn.EMul) foo;
       if (_i_ > 4) render(_L_PAREN);
       pp(_emul.expr_1, 4);
       pp(_emul.mulop_, 0);
       pp(_emul.expr_2, 5);
       if (_i_ > 4) render(_R_PAREN);
    }
    else     if (foo instanceof src.Absyn.EAdd)
    {
       src.Absyn.EAdd _eadd = (src.Absyn.EAdd) foo;
       if (_i_ > 3) render(_L_PAREN);
       pp(_eadd.expr_1, 3);
       pp(_eadd.addop_, 0);
       pp(_eadd.expr_2, 4);
       if (_i_ > 3) render(_R_PAREN);
    }
    else     if (foo instanceof src.Absyn.ERel)
    {
       src.Absyn.ERel _erel = (src.Absyn.ERel) foo;
       if (_i_ > 2) render(_L_PAREN);
       pp(_erel.expr_1, 2);
       pp(_erel.relop_, 0);
       pp(_erel.expr_2, 3);
       if (_i_ > 2) render(_R_PAREN);
    }
    else     if (foo instanceof src.Absyn.EAnd)
    {
       src.Absyn.EAnd _eand = (src.Absyn.EAnd) foo;
       if (_i_ > 1) render(_L_PAREN);
       pp(_eand.expr_1, 2);
       render("&&");
       pp(_eand.expr_2, 1);
       if (_i_ > 1) render(_R_PAREN);
    }
    else     if (foo instanceof src.Absyn.EOr)
    {
       src.Absyn.EOr _eor = (src.Absyn.EOr) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_eor.expr_1, 1);
       render("||");
       pp(_eor.expr_2, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(src.Absyn.ListExpr foo, int _i_)
  {
     for (java.util.Iterator<Expr> it = foo.iterator(); it.hasNext();)
     {
       pp(it.next(), _i_);
       if (it.hasNext()) {
         render(",");
       } else {
         render("");
       }
     }  }

  private static void pp(src.Absyn.AddOp foo, int _i_)
  {
    if (foo instanceof src.Absyn.Plus)
    {
       src.Absyn.Plus _plus = (src.Absyn.Plus) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("+");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof src.Absyn.Minus)
    {
       src.Absyn.Minus _minus = (src.Absyn.Minus) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("-");
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(src.Absyn.MulOp foo, int _i_)
  {
    if (foo instanceof src.Absyn.Times)
    {
       src.Absyn.Times _times = (src.Absyn.Times) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("*");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof src.Absyn.Div)
    {
       src.Absyn.Div _div = (src.Absyn.Div) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("/");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof src.Absyn.Mod)
    {
       src.Absyn.Mod _mod = (src.Absyn.Mod) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("%");
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(src.Absyn.RelOp foo, int _i_)
  {
    if (foo instanceof src.Absyn.LTH)
    {
       src.Absyn.LTH _lth = (src.Absyn.LTH) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("<");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof src.Absyn.LE)
    {
       src.Absyn.LE _le = (src.Absyn.LE) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("<=");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof src.Absyn.GTH)
    {
       src.Absyn.GTH _gth = (src.Absyn.GTH) foo;
       if (_i_ > 0) render(_L_PAREN);
       render(">");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof src.Absyn.GE)
    {
       src.Absyn.GE _ge = (src.Absyn.GE) foo;
       if (_i_ > 0) render(_L_PAREN);
       render(">=");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof src.Absyn.EQU)
    {
       src.Absyn.EQU _equ = (src.Absyn.EQU) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("==");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof src.Absyn.NE)
    {
       src.Absyn.NE _ne = (src.Absyn.NE) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("!=");
       if (_i_ > 0) render(_R_PAREN);
    }
  }


  private static void sh(src.Absyn.Prog foo)
  {
    if (foo instanceof src.Absyn.Program)
    {
       src.Absyn.Program _program = (src.Absyn.Program) foo;
       render("(");
       render("Program");
       render("[");
       sh(_program.listtopdef_);
       render("]");
       render(")");
    }
  }

  private static void sh(src.Absyn.TopDef foo)
  {
    if (foo instanceof src.Absyn.FnDef)
    {
       src.Absyn.FnDef _fndef = (src.Absyn.FnDef) foo;
       render("(");
       render("FnDef");
       sh(_fndef.type_);
       sh(_fndef.ident_);
       render("[");
       sh(_fndef.listarg_);
       render("]");
       sh(_fndef.blk_);
       render(")");
    }
  }

  private static void sh(src.Absyn.ListTopDef foo)
  {
     for (java.util.Iterator<TopDef> it = foo.iterator(); it.hasNext();)
     {
       sh(it.next());
       if (it.hasNext())
         render(",");
     }
  }

  private static void sh(src.Absyn.Arg foo)
  {
    if (foo instanceof src.Absyn.Argument)
    {
       src.Absyn.Argument _argument = (src.Absyn.Argument) foo;
       render("(");
       render("Argument");
       sh(_argument.type_);
       sh(_argument.ident_);
       render(")");
    }
  }

  private static void sh(src.Absyn.ListArg foo)
  {
     for (java.util.Iterator<Arg> it = foo.iterator(); it.hasNext();)
     {
       sh(it.next());
       if (it.hasNext())
         render(",");
     }
  }

  private static void sh(src.Absyn.Blk foo)
  {
    if (foo instanceof src.Absyn.Block)
    {
       src.Absyn.Block _block = (src.Absyn.Block) foo;
       render("(");
       render("Block");
       render("[");
       sh(_block.liststmt_);
       render("]");
       render(")");
    }
  }

  private static void sh(src.Absyn.ListStmt foo)
  {
     for (java.util.Iterator<Stmt> it = foo.iterator(); it.hasNext();)
     {
       sh(it.next());
       if (it.hasNext())
         render(",");
     }
  }

  private static void sh(src.Absyn.Stmt foo)
  {
    if (foo instanceof src.Absyn.Empty)
    {
       src.Absyn.Empty _empty = (src.Absyn.Empty) foo;
       render("Empty");
    }
    if (foo instanceof src.Absyn.BStmt)
    {
       src.Absyn.BStmt _bstmt = (src.Absyn.BStmt) foo;
       render("(");
       render("BStmt");
       sh(_bstmt.blk_);
       render(")");
    }
    if (foo instanceof src.Absyn.Decl)
    {
       src.Absyn.Decl _decl = (src.Absyn.Decl) foo;
       render("(");
       render("Decl");
       sh(_decl.type_);
       render("[");
       sh(_decl.listitem_);
       render("]");
       render(")");
    }
    if (foo instanceof src.Absyn.Ass)
    {
       src.Absyn.Ass _ass = (src.Absyn.Ass) foo;
       render("(");
       render("Ass");
       sh(_ass.ident_);
       sh(_ass.expr_);
       render(")");
    }
    if (foo instanceof src.Absyn.Incr)
    {
       src.Absyn.Incr _incr = (src.Absyn.Incr) foo;
       render("(");
       render("Incr");
       sh(_incr.ident_);
       render(")");
    }
    if (foo instanceof src.Absyn.Decr)
    {
       src.Absyn.Decr _decr = (src.Absyn.Decr) foo;
       render("(");
       render("Decr");
       sh(_decr.ident_);
       render(")");
    }
    if (foo instanceof src.Absyn.Ret)
    {
       src.Absyn.Ret _ret = (src.Absyn.Ret) foo;
       render("(");
       render("Ret");
       sh(_ret.expr_);
       render(")");
    }
    if (foo instanceof src.Absyn.VRet)
    {
       src.Absyn.VRet _vret = (src.Absyn.VRet) foo;
       render("VRet");
    }
    if (foo instanceof src.Absyn.Cond)
    {
       src.Absyn.Cond _cond = (src.Absyn.Cond) foo;
       render("(");
       render("Cond");
       sh(_cond.expr_);
       sh(_cond.stmt_);
       render(")");
    }
    if (foo instanceof src.Absyn.CondElse)
    {
       src.Absyn.CondElse _condelse = (src.Absyn.CondElse) foo;
       render("(");
       render("CondElse");
       sh(_condelse.expr_);
       sh(_condelse.stmt_1);
       sh(_condelse.stmt_2);
       render(")");
    }
    if (foo instanceof src.Absyn.While)
    {
       src.Absyn.While _while = (src.Absyn.While) foo;
       render("(");
       render("While");
       sh(_while.expr_);
       sh(_while.stmt_);
       render(")");
    }
    if (foo instanceof src.Absyn.SExp)
    {
       src.Absyn.SExp _sexp = (src.Absyn.SExp) foo;
       render("(");
       render("SExp");
       sh(_sexp.expr_);
       render(")");
    }
  }

  private static void sh(src.Absyn.Item foo)
  {
    if (foo instanceof src.Absyn.NoInit)
    {
       src.Absyn.NoInit _noinit = (src.Absyn.NoInit) foo;
       render("(");
       render("NoInit");
       sh(_noinit.ident_);
       render(")");
    }
    if (foo instanceof src.Absyn.Init)
    {
       src.Absyn.Init _init = (src.Absyn.Init) foo;
       render("(");
       render("Init");
       sh(_init.ident_);
       sh(_init.expr_);
       render(")");
    }
  }

  private static void sh(src.Absyn.ListItem foo)
  {
     for (java.util.Iterator<Item> it = foo.iterator(); it.hasNext();)
     {
       sh(it.next());
       if (it.hasNext())
         render(",");
     }
  }

  private static void sh(src.Absyn.Type foo)
  {
    if (foo instanceof src.Absyn.Int)
    {
       src.Absyn.Int _int = (src.Absyn.Int) foo;
       render("Int");
    }
    if (foo instanceof src.Absyn.Doub)
    {
       src.Absyn.Doub _doub = (src.Absyn.Doub) foo;
       render("Doub");
    }
    if (foo instanceof src.Absyn.Bool)
    {
       src.Absyn.Bool _bool = (src.Absyn.Bool) foo;
       render("Bool");
    }
    if (foo instanceof src.Absyn.Void)
    {
       src.Absyn.Void _void = (src.Absyn.Void) foo;
       render("Void");
    }
    if (foo instanceof src.Absyn.Fun)
    {
       src.Absyn.Fun _fun = (src.Absyn.Fun) foo;
       render("(");
       render("Fun");
       sh(_fun.type_);
       render("[");
       sh(_fun.listtype_);
       render("]");
       render(")");
    }
  }

  private static void sh(src.Absyn.ListType foo)
  {
     for (java.util.Iterator<Type> it = foo.iterator(); it.hasNext();)
     {
       sh(it.next());
       if (it.hasNext())
         render(",");
     }
  }

  private static void sh(src.Absyn.Expr foo)
  {
    if (foo instanceof src.Absyn.EVar)
    {
       src.Absyn.EVar _evar = (src.Absyn.EVar) foo;
       render("(");
       render("EVar");
       sh(_evar.ident_);
       render(")");
    }
    if (foo instanceof src.Absyn.ELitInt)
    {
       src.Absyn.ELitInt _elitint = (src.Absyn.ELitInt) foo;
       render("(");
       render("ELitInt");
       sh(_elitint.integer_);
       render(")");
    }
    if (foo instanceof src.Absyn.ELitDoub)
    {
       src.Absyn.ELitDoub _elitdoub = (src.Absyn.ELitDoub) foo;
       render("(");
       render("ELitDoub");
       sh(_elitdoub.double_);
       render(")");
    }
    if (foo instanceof src.Absyn.ELitTrue)
    {
       src.Absyn.ELitTrue _elittrue = (src.Absyn.ELitTrue) foo;
       render("ELitTrue");
    }
    if (foo instanceof src.Absyn.ELitFalse)
    {
       src.Absyn.ELitFalse _elitfalse = (src.Absyn.ELitFalse) foo;
       render("ELitFalse");
    }
    if (foo instanceof src.Absyn.EApp)
    {
       src.Absyn.EApp _eapp = (src.Absyn.EApp) foo;
       render("(");
       render("EApp");
       sh(_eapp.ident_);
       render("[");
       sh(_eapp.listexpr_);
       render("]");
       render(")");
    }
    if (foo instanceof src.Absyn.EString)
    {
       src.Absyn.EString _estring = (src.Absyn.EString) foo;
       render("(");
       render("EString");
       sh(_estring.string_);
       render(")");
    }
    if (foo instanceof src.Absyn.Neg)
    {
       src.Absyn.Neg _neg = (src.Absyn.Neg) foo;
       render("(");
       render("Neg");
       sh(_neg.expr_);
       render(")");
    }
    if (foo instanceof src.Absyn.Not)
    {
       src.Absyn.Not _not = (src.Absyn.Not) foo;
       render("(");
       render("Not");
       sh(_not.expr_);
       render(")");
    }
    if (foo instanceof src.Absyn.EMul)
    {
       src.Absyn.EMul _emul = (src.Absyn.EMul) foo;
       render("(");
       render("EMul");
       sh(_emul.expr_1);
       sh(_emul.mulop_);
       sh(_emul.expr_2);
       render(")");
    }
    if (foo instanceof src.Absyn.EAdd)
    {
       src.Absyn.EAdd _eadd = (src.Absyn.EAdd) foo;
       render("(");
       render("EAdd");
       sh(_eadd.expr_1);
       sh(_eadd.addop_);
       sh(_eadd.expr_2);
       render(")");
    }
    if (foo instanceof src.Absyn.ERel)
    {
       src.Absyn.ERel _erel = (src.Absyn.ERel) foo;
       render("(");
       render("ERel");
       sh(_erel.expr_1);
       sh(_erel.relop_);
       sh(_erel.expr_2);
       render(")");
    }
    if (foo instanceof src.Absyn.EAnd)
    {
       src.Absyn.EAnd _eand = (src.Absyn.EAnd) foo;
       render("(");
       render("EAnd");
       sh(_eand.expr_1);
       sh(_eand.expr_2);
       render(")");
    }
    if (foo instanceof src.Absyn.EOr)
    {
       src.Absyn.EOr _eor = (src.Absyn.EOr) foo;
       render("(");
       render("EOr");
       sh(_eor.expr_1);
       sh(_eor.expr_2);
       render(")");
    }
  }

  private static void sh(src.Absyn.ListExpr foo)
  {
     for (java.util.Iterator<Expr> it = foo.iterator(); it.hasNext();)
     {
       sh(it.next());
       if (it.hasNext())
         render(",");
     }
  }

  private static void sh(src.Absyn.AddOp foo)
  {
    if (foo instanceof src.Absyn.Plus)
    {
       src.Absyn.Plus _plus = (src.Absyn.Plus) foo;
       render("Plus");
    }
    if (foo instanceof src.Absyn.Minus)
    {
       src.Absyn.Minus _minus = (src.Absyn.Minus) foo;
       render("Minus");
    }
  }

  private static void sh(src.Absyn.MulOp foo)
  {
    if (foo instanceof src.Absyn.Times)
    {
       src.Absyn.Times _times = (src.Absyn.Times) foo;
       render("Times");
    }
    if (foo instanceof src.Absyn.Div)
    {
       src.Absyn.Div _div = (src.Absyn.Div) foo;
       render("Div");
    }
    if (foo instanceof src.Absyn.Mod)
    {
       src.Absyn.Mod _mod = (src.Absyn.Mod) foo;
       render("Mod");
    }
  }

  private static void sh(src.Absyn.RelOp foo)
  {
    if (foo instanceof src.Absyn.LTH)
    {
       src.Absyn.LTH _lth = (src.Absyn.LTH) foo;
       render("LTH");
    }
    if (foo instanceof src.Absyn.LE)
    {
       src.Absyn.LE _le = (src.Absyn.LE) foo;
       render("LE");
    }
    if (foo instanceof src.Absyn.GTH)
    {
       src.Absyn.GTH _gth = (src.Absyn.GTH) foo;
       render("GTH");
    }
    if (foo instanceof src.Absyn.GE)
    {
       src.Absyn.GE _ge = (src.Absyn.GE) foo;
       render("GE");
    }
    if (foo instanceof src.Absyn.EQU)
    {
       src.Absyn.EQU _equ = (src.Absyn.EQU) foo;
       render("EQU");
    }
    if (foo instanceof src.Absyn.NE)
    {
       src.Absyn.NE _ne = (src.Absyn.NE) foo;
       render("NE");
    }
  }


  private static void pp(Integer n, int _i_) { buf_.append(n); buf_.append(" "); }
  private static void pp(Double d, int _i_) { buf_.append(d); buf_.append(" "); }
  private static void pp(String s, int _i_) { buf_.append(s); buf_.append(" "); }
  private static void pp(Character c, int _i_) { buf_.append("'" + c.toString() + "'"); buf_.append(" "); }
  private static void sh(Integer n) { render(n.toString()); }
  private static void sh(Double d) { render(d.toString()); }
  private static void sh(Character c) { render(c.toString()); }
  private static void sh(String s) { printQuoted(s); }
  private static void printQuoted(String s) { render("\"" + s + "\""); }
  private static void indent()
  {
    int n = _n_;
    while (n > 0)
    {
      buf_.append(" ");
      n--;
    }
  }
  private static void backup()
  {
     if (buf_.charAt(buf_.length() - 1) == ' ') {
      buf_.setLength(buf_.length() - 1);
    }
  }
  private static void trim()
  {
     while (buf_.length() > 0 && buf_.charAt(0) == ' ')
        buf_.deleteCharAt(0); 
    while (buf_.length() > 0 && buf_.charAt(buf_.length()-1) == ' ')
        buf_.deleteCharAt(buf_.length()-1);
  }
  private static int _n_ = 0;
  private static StringBuilder buf_ = new StringBuilder(INITIAL_BUFFER_SIZE);
}

