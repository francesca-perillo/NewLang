package nodi;

import nodi.statement.AssignStatement;

/**
 * Classe per gestire IdInitObbl
 *
 * Il nodo IdInitObblList viene gestito da un array di IdInitObbl.
 * Serve un costruttore per gestire oggetti IdInitObbl del tipo
 * id << costante
 *
 */
public class IdInitObblOp {
    private AssignStatement assignStatement;

    /**
     * Costruttore per la oggetti del tipo
     * id << valore
     * @param assignStatement oggetto di tipo AssignStat (Identifier, Const)
     */
    public IdInitObblOp(AssignStatement assignStatement) {
        this.assignStatement = assignStatement;
    }

    public Object accept(Visitor v) throws Exception {
        return v.visit(this);
    }

    public AssignStatement getAssignStat() {
        return assignStatement;
    }

    @Override
    public String toString() {
        return "IdInitObblOp{" +
                "assignStat=" + assignStatement +
                '}';
    }
}
