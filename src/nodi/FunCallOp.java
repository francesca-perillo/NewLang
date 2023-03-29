package nodi;
import main.visitor.utility.Utility;

import java.util.ArrayList;

/**
 * Classe per il nodo FanCall
 *
 * (1) FunCall -> ID LPAR ExprList RPAR
 * (2) FunCall -> ID LPAR RPAR
 */
public class FunCallOp {
    private Identifier id;
    private ArrayList<ExprOp> exprList;

    /**
     * Costruttore per la produzione
     * (1) FunCall -> ID LPAR ExprList RPAR
     * @param id oggetto di tipo Identifier
     * @param expr array di oggetti di tipo ExprOp
     */
    public FunCallOp(Identifier id, ArrayList<ExprOp> expr) {
        this.id = id;
        this.exprList = expr;
    }

    /**
     * Costruttore per la produzione
     * (2) FunCall -> ID LPAR RPAR
     * @param id oggetto di tipo Identifier
     */
    public FunCallOp(Identifier id) {
        this.id = id;
    }

    public Object accept(Visitor v) throws Exception {
        return v.visit(this);
    }

    public Identifier getId() {
        return id;
    }

    public ArrayList<ExprOp> getExpr() {
        return exprList;
    }

    public void add (ExprOp e){
        exprList.add(e);
    }

    @Override
    public String toString() {
        String  toString = "";

        toString += id + "{";

        if(!Utility.isNull(exprList))
            toString += exprList;

        toString += "}";

        return toString;
    }
}
