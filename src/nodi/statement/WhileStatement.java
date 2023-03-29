package nodi.statement;

import main.visitor.utility.SymbolTable;
import nodi.BodyOp;
import nodi.ExprOp;
import nodi.Visitor;

/**
 * Classe per gestire i nodi WhileStat
 *
 * (1) WhileStat -> WHILE Expr LOOP Body
 */
public class WhileStatement extends Statement {
    private ExprOp expr;
    private BodyOp bodyOp;

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
     * (1) WhileStat -> WHILE Expr LOOP Body
     * @param expr oggetto di tipo ExprOp
     * @param bodyOp oggetto di tipo BodyOp
     */
    public WhileStatement(ExprOp expr, BodyOp bodyOp) {
        this.expr = expr;
        this.bodyOp = bodyOp;
        isFirstVisit = true;
    }

    public Object accept(Visitor v) throws Exception {
        return v.visit(this);
    }

    public ExprOp getExpr() {
        return expr;
    }

    public BodyOp getBody() {
        return bodyOp;
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    public void setSymbolTable(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }



    @Override
    public String toString() {
        return expr.toString() + "," + bodyOp.toString();
    }
}
