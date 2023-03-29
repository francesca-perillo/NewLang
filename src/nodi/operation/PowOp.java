package nodi.operation;

import nodi.ExprOp;
import nodi.Visitor;

public class PowOp extends Operation{
    public PowOp(ExprOp e1, ExprOp e2) {
        super(e1, e2);
    }

    public Object accept(Visitor v) throws Exception {
        return v.visit(this);
    }

    @Override
    public String toString() {
        return super.getE1().toString() + super.getE2().toString();
    }
}
