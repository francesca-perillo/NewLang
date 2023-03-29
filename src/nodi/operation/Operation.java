package nodi.operation;

import main.visitor.utility.CompatibilityTables.TypeOperation;
import nodi.ExprOp;
import nodi.Visitor;

public class Operation {
    private ExprOp e1, e2;
    private TypeOperation typeOperation; //in CompatibilityTables

    public Operation(ExprOp e1, ExprOp e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    public Operation(ExprOp e1) {
        this.e1 = e1;
    }

    public Object accept(Visitor v) throws Exception {
        return v.visit(this);
    }

    public ExprOp getE1() {
        return e1;
    }

    public ExprOp getE2() {
        return e2;
    }

    public void setTypeOperation(TypeOperation typeOperation) {
        this.typeOperation = typeOperation;
    }

    public TypeOperation getTypeOperation() {
        return typeOperation;
    }

    @Override
    public String toString() {
        if(!e2.equals("")){
            return e1.toString() + ", " + e2.toString();
        } else {
            return e1.toString();
        }
    }
}
