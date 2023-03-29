package nodi;

import main.visitor.utility.Utility;
import nodi.statement.AssignStatement;

import java.util.ArrayList;

/**
 * Classe per gestire IdInit
 *
 * Il nodo IdInitList viene gestito da un array di IdInit.
 * Servono due costruttori per gestire oggetti IdInit del tipo
 * id   oppure   id << espressione
 * questi verranno poi aggiunti alla lista IdInitList
 *
 * nonterminal ArrayList<IdInitOp> idInitList;
 */
public class IdInitOp {
    private Identifier id;

    //variabile per IdInitList -> IdInitList COMMA ID ASSIGN Expr
    private ArrayList<Identifier> idList;

    private AssignStatement assignment;

    /**
     * Costruttore per oggetti del tipo
     * id
     * @param id oggetto di tipo Identifier
     */
    public IdInitOp(Identifier id) {
        this.id = id;
    }

    /**
     * Costruttore per la oggetti del tipo
     * id << espressione
     * @param assignment oggetto di tipo AssignStat (Identifier, ExprOp)
     */
    public IdInitOp(AssignStatement assignment) {
        this.assignment = assignment;
    }

    public Object accept(Visitor v) throws Exception {
        return v.visit(this);
    }

    public Identifier getId() {
        return id;
    }

    public AssignStatement getAssignment() {
        return assignment;
    }

    @Override
    public String toString() {
        String toString = "";

        if(assignment != null){
            toString += assignment;
        } else {
            toString += id;
        }

        return toString;
    }
}
