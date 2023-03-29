package nodi.statement;

import nodi.ExprOp;
import nodi.Visitor;

import java.util.ArrayList;

/**
 * Classe che gestisce il nodo e la produzione
 *
 * (1) WriteStat -> LPAR ExprList RPAR WRITE
 * (2) WriteStat -> LPAR ExprList RPAR WRITELN
 */
public class WriteStatement extends Statement{
    public enum Mode {
        write, //no ritorno a capo
        writeln //ritorno a capo
    }
    private Mode mode;

    private ArrayList<ExprOp> exprList;

    public WriteStatement(Mode mode, ArrayList<ExprOp> exprList) {
        this.mode = mode;
        this.exprList = exprList;
    }

    public Object accept(Visitor v) throws Exception {
        return v.visit(this);
    }

    public Mode getMode() {
        return mode;
    }

    public ArrayList<ExprOp> getExprList() {
        return exprList;
    }

    @Override
    public String toString() {
        return "WriteStat{" +
                "mode=" + mode +
                ", exprList=" + exprList +
                '}';
    }
}
