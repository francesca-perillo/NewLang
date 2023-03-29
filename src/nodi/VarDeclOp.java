package nodi;

import main.visitor.utility.Utility;

import java.util.ArrayList;

/**
 * VarDeclOp serve per gestire il nodo VarDecl.
 *
 * (1) VarDecl -> Type IdInitList SEMI
 * (2) VarDecl -> VAR IdInitObblList SEMI
 */
public class VarDeclOp {
    private TypeOp type; // per Type (1) e VAR (2).
    public enum Var {var};
    private Var var;
    private ArrayList<IdInitOp> idInitOpList;
    private ArrayList<IdInitObblOp> idInitObblOpList;


    /**
     *
     * (2) VarDecl -> VAR IdInitObblList SEMI
     * @param type oggetto di tipo TypeOp (che include anche il VAR)
     * @param idInitList_idInitObblList array di oggetti di tipo IdInitOp oppure IdInitObblOp
     */

    /**
     * Costruttore per la produzione
     * (1) VarDecl -> Type IdInitList SEMI
     * @param type oggetto enum nella classe TypeOp: TypeOp.Type.xxx
     * @param idInitOpList
     */
    public VarDeclOp(TypeOp type, ArrayList<IdInitOp> idInitOpList) {
        this.type = type;
        this.idInitOpList = idInitOpList;
    }

    /**
     * Costruttore per la produzione
     * (2) VarDecl -> VAR IdInitObblList SEMI
     * @param var oggetto enum nella classe VarDeclOp: VarDeclOp.Var.var
     * @param idInitObblOpList lista di oggetti IdInitObblOp
     */
    public VarDeclOp(Var var, ArrayList<IdInitObblOp> idInitObblOpList) {
        this.var = var;
        this.idInitObblOpList = idInitObblOpList;
    }

    public Object accept(Visitor v) throws Exception {
        return v.visit(this);
    }

    public TypeOp getType() {
        return type;
    }

    public Var getVar() {
        return var;
    }

    public ArrayList<IdInitOp> getIdInitOpList() {
        return idInitOpList;
    }

    public ArrayList<IdInitObblOp> getIdInitObblOpList() {
        return idInitObblOpList;
    }

    /**
     * Metodo per verificare se idInitList è vuota
     * @return true se idInitObblList è vuota, false altrimenti
     */
    public boolean isIdInitListEmpty(){
        return idInitOpList.isEmpty();
    }

    /**
     * Metodo per verificare se idInitObblList è vuota
     * @return true se idInitObblList è vuota, false altrimenti
     */
    public boolean isIdInitObblListEmpty(){
        return idInitObblOpList.isEmpty();
    }


    @Override
    public String toString() {
        String toString = "";

        //(1) VarDecl -> Type IdInitList SEMI
        if (!Utility.isNull(idInitOpList)) {
            toString += "Type: " + type;
            toString += "\nidInitOpList: " +idInitOpList;
        }

        // (2) VarDecl -> VAR IdInitObblList SEMI
        if(!Utility.isNull(idInitObblOpList)){
            toString += "\n\nVAR ";
            toString += "\nidInitObblList: " + idInitObblOpList;
        }


        return toString;
    }
}
