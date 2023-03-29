package nodi;

import nodi.operation.Operation;

/**
 * Classe per nodi di tipo Expr.
 * Le operazioni in Expr vengono gestite dall'Interfaccia Operation.
 * Le costanti in Expr vengono gestite dalla classe Const.
 */
public class ExprOp {
    private ConstOp constOpType;
    private Identifier id;
    private Operation operation;
    private FunCallOp funCallOp;

    public ExprOp(ConstOp constOpType) {
        this.constOpType = constOpType;
    }

    public ExprOp(Identifier id) {
        this.id = id;
    }

    public ExprOp(Operation operation) {
        this.operation = operation;
    }

    public ExprOp(FunCallOp funCallOp) {
        this.funCallOp = funCallOp;
    }

    public Object accept(Visitor v) throws Exception {
        return v.visit(this);
    }

    public ConstOp getConstType() {
        return constOpType;
    }

    public Identifier getId() {
        return id;
    }

    public Operation getOperation() {
        return operation;
    }

    public FunCallOp getFunCallOp() {
        return funCallOp;
    }

    @Override
    public String toString() {
        return
                (constOpType != null ? "const type: "+getConstType().toString()+"\n" : "\n") +
                (id != null ? "lessema: "+getId().toString()+"\n" : "\n ") +
                (operation != null ? "operazione: "+getOperation().toString()+"\n" : "\n") +
                (funCallOp != null ? "chiamata a funzione: "+getFunCallOp()+"\n" : "\n");
    }
}
