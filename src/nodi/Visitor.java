package nodi;

import nodi.operation.*;
import nodi.statement.*;

import java.io.IOException;

public interface Visitor {

    //Nodi operation
    public Object visit(AndOp andOp) throws Exception;
    public Object visit(DivOp divOp) throws Exception;
    public Object visit(EqOp eqOp) throws Exception;
    public Object visit(GeOp geOp) throws Exception;
    public Object visit(GtOp gtOp) throws Exception;
    public Object visit(LeOp leOp) throws Exception;
    public Object visit(LtOp ltOp) throws Exception;
    public Object visit(MinusOp minusOp) throws Exception;
    public Object visit(NeOp neOp) throws Exception;
    public Object visit(NotOp notOp) throws Exception;
    public Object visit(Operation operation) throws Exception;
    public Object visit(OrOp orOp) throws Exception;
    public Object visit(PlusOp plusOp) throws Exception;
    public Object visit(PowOp powOp) throws Exception;
    public Object visit(StringConcatOp stringConcatOp) throws Exception;
    public Object visit(TimesOp timesOp) throws Exception;

    //Nodi statement
    public Object visit(AssignStatement assignStatement) throws Exception;
    public Object visit(ForStatement forStatement) throws Exception;
    public Object visit(IfStatement ifStatement) throws Exception;
    public Object visit(ReadStatement readStatement) throws Exception;
    public Object visit(ReturnOp returnOp) throws Exception;
    public Object visit(Statement statement) throws Exception;
    public Object visit(WhileStatement whileStatement) throws Exception;
    public Object visit(WriteStatement writeStatement) throws Exception;

    //Altri nodi
    public Object visit(BodyOp bodyOp) throws Exception;
    public Object visit(ConstOp constOp) throws IOException;
    public Object visit(DeclOp declOp) throws Exception;
    public Object visit(ExprOp exprOp) throws Exception;
    public Object visit(FunDeclOp funDeclOp) throws Exception;
    public Object visit(FunCallOp funCallOp) throws Exception;
    public Object visit(Identifier identifier) throws Exception;
    public Object visit(IdInitObblOp idInitObblOp) throws Exception;
    public Object visit(IdInitOp idInitOp) throws Exception;
    public Object visit(MainOp mainOp) throws Exception;
    public Object visit(ParDeclOp parDeclOp) throws Exception;
    public Object visit(ProgramOp programOp) throws Exception;
    public Object visit(TypeOp typeOp) throws IOException;
    public Object visit(VarDeclOp varDeclOp) throws Exception;
}
