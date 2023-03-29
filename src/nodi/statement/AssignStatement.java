package nodi.statement;

import main.visitor.utility.Utility;
import nodi.ConstOp;
import nodi.ExprOp;
import nodi.Identifier;
import nodi.Visitor;

import java.util.ArrayList;

/**
 * Classe per gestire il nodo per l'assegnazione
 */
public class AssignStatement extends Statement {
    //variabili per la produzione AssignStat -> IdList ASSIGN ExprList
    private ArrayList<Identifier> idList;
    private ArrayList<ExprOp> exprList;

    //variabili per la produzione ForStat -> FOR ID ASSIGN INTEGER_CONST TO INTEGER_CONST LOOP Body
    // e per la produzione IdInitObblList -> ID ASSIGN Const
    private Identifier id;
    private ConstOp integerConstOp;

    /**
     * variabile enum che permette di capire dove sto facendo l'assegnazione
     */
    public enum KindAssign {
        forAssign,
        idInitObblAssign
    }
    private KindAssign kinkAssign;

    //variabili per la produzione IdInitList IdInitList -> ID ASSIGN Expr
    private ExprOp e;

    /**
     * Costruttore per la produzione AssignStat -> IdList ASSIGN ExprList
     * @param id ArrayList di oggetti Identifier
     * @param expr ArrayList di oggetti ExprOp
     */
    public AssignStatement(ArrayList<Identifier> id, ArrayList<ExprOp> expr) {
        this.idList = id;
        this.exprList = expr;
    }

    /**
     * Costruttore per la produzione ForStat -> FOR ID ASSIGN INTEGER_CONST TO INTEGER_CONST LOOP Body
     * e per la produzione IdInitObblList -> ID ASSIGN Const
     * @param id oggetto di tipo Identifier
     * @param integerConstOp oggetto di tipo Const
     */
    public AssignStatement(Identifier id, ConstOp integerConstOp) {
        this.id = id;
        this.integerConstOp = integerConstOp;
    }

    /**
     * Costruttore per la produzione ForStat -> FOR ID ASSIGN INTEGER_CONST TO INTEGER_CONST LOOP Body
     * e per la produzione IdInitObblList -> ID ASSIGN Const
     * @param id oggetto di tipo Identifier
     * @param integerConstOp oggetto di tipo Const
     * @param kind tipo di assegnazione - forAssign - oppure - idInitObblAssign.
     */
    public AssignStatement(Identifier id, ConstOp integerConstOp, KindAssign kind) {
        this.id = id;
        this.integerConstOp = integerConstOp;
        this.kinkAssign = kind;
    }

    /**
     * Costruttore per la produzione IdInitList -> ID ASSIGN Expr
     * @param id oggetto di tipo Identifier
     * @param e oggetto di tipo Expr
     */
    public AssignStatement(Identifier id, ExprOp e) {
        this.id = id;
        this.e = e;
    }

    public ExprOp getE() {
        return e;
    }

    public Object accept(Visitor v) throws Exception {
        return v.visit(this);
    }

    public ArrayList<Identifier> getArrayListId() {
        return idList;
    }

    public Identifier getId(){
        return id;
    }

    public ArrayList<ExprOp> getExpr() {
        return exprList;
    }

    public ConstOp getConst() {
        return integerConstOp;
    }


    public void addId (Identifier id){
        idList.add(id);
    }

    public void addExpr (ExprOp e){
        exprList.add(e);
    }

    @Override
    public String toString() {
        String toString = "";

        //ForStat -> FOR ID ASSIGN INTEGER_CONST TO INTEGER_CONST LOOP Body
        if(id != null && integerConstOp != null)
            toString += id + " << " + integerConstOp;

        //AssignStat -> IdList ASSIGN ExprList
        if(!Utility.isNull(idList) && !Utility.isNull(exprList))
            toString += idList + " << " + exprList;

        //IdInitList IdInitList -> ID ASSIGN Expr
        if(id != null && e!=null)
            toString += id + " << " + e;

        return toString;
    }
}
