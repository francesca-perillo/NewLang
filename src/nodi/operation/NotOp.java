package nodi.operation;


import nodi.ExprOp;
import nodi.Visitor;

public class NotOp extends Operation{

    public NotOp(ExprOp e) {
        super(e);
    }

    public Object accept(Visitor v) throws Exception {
        return v.visit(this);
    }

    @Override
    public String toString() {
        return super.getE1().toString();
    }
}
