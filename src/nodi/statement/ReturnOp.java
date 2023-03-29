package nodi.statement;
import nodi.Visitor;
import nodi.ExprOp;

/**
 * Questa classe non gestisce alcun nodo,
 * serve per le produzioni
 *
 * (1) Stat -> RETURN Expr SEMI
 * (2) Stat -> RETURN SEMI
 */
public class ReturnOp extends Statement {
    private ExprOp exprOp;

    /**
     * Costruttore con espressione per la produzione
     * (1) Stat -> RETURN Expr SEMI
     * @param exprOp oggetto di tipo ExprOp
     */
    public ReturnOp(ExprOp exprOp) {
        this.exprOp = exprOp;
    }

    /**
     * Costruttore vuoto per la produzione
     * (2) Stat -> RETURN SEMI
     */
    public ReturnOp() {
    }

    public Object accept(Visitor v) throws Exception {
        return v.visit(this);
    }

    public ExprOp getExprOp() {
        return exprOp;
    }
}
