package nodi.operation;

import nodi.ExprOp;
import nodi.Visitor;

public class MinusOp extends Operation {
    public MinusOp(ExprOp e1, ExprOp e2) {
        super(e1, e2);
    }

    public MinusOp(ExprOp e1) {
        super(e1);
    }

    public Object accept(Visitor v) throws Exception {
        return v.visit(this);
    }

    @Override
    public String toString() {
        if(super.getE2() == null){
            return super.getE1().toString();
        } else
            return super.getE1().toString() + "," + super.getE2().toString();
    }
}
