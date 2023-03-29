package nodi.statement;

import main.visitor.utility.SymbolTable;
import nodi.BodyOp;
import nodi.ExprOp;
import nodi.Visitor;
/**
 * Classe per il nodo IfStat
 *
 * (1) IfStat -> IF Expr THEN Body Else
 * (2) IfStat -> IF Expr THEN Body
 */
public class IfStatement extends Statement {
    private ExprOp expr;
    private BodyOp bodyOp1, bodyOp2;

    //analisi semantica
    private boolean isFirstVisit;
    private SymbolTable symbolTable;

    public boolean isFirstVisit() {
        return isFirstVisit;
    }

    public void setFirstVisit(boolean firstVisit) {
        isFirstVisit = firstVisit;
    }

    /**
     * Costruttore per la produzione
     * (1) IfStat -> IF Expr THEN Body Else
     * @param expr oggetto di tipo ExprOp
     * @param bodyOp1 oggetto di tipo BodyOp
     * @param bodyOp2 oggetto di tipo BodyOp
     */
    public IfStatement(ExprOp expr, BodyOp bodyOp1, BodyOp bodyOp2) {
        this.expr = expr;
        this.bodyOp1 = bodyOp1;
        this.bodyOp2 = bodyOp2;
        isFirstVisit = true;
    }

    /**
     * Costruttore per la produzione
     * (2) IfStat -> IF Expr THEN Body
     * @param expr oggetto di tipo ExprOp
     * @param bodyOp1 oggetto di tipo BodyOp
     */
    public IfStatement(ExprOp expr, BodyOp bodyOp1) {
        this.expr = expr;
        this.bodyOp1 = bodyOp1;
        isFirstVisit = true;
    }

    public Object accept(Visitor v) throws Exception {
        return v.visit(this);
    }

    public ExprOp getExpr() {
        return expr;
    }

    public BodyOp getBody1() {
        return bodyOp1;
    }

    public BodyOp getBody2() {
        return bodyOp2;
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    public void setSymbolTable(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }

    @Override
    public String toString() {
        return "IfStat{" +
                "expr=" + expr +
                ", bodyOp1=" + bodyOp1 +
                ", bodyOp2=" + bodyOp2 +
                '}';
    }
}
