package nodi;

import main.visitor.utility.SymbolTable;
import main.visitor.utility.Utility;

import java.util.ArrayList;

/**
 * Classe per FunDecl
 *
 * (1) FunDecl -> DEF ID LPAR ParamDeclList RPAR COLON TypeOrVoid Body
 * (2) FunDecl -> DEF ID LPAR RPAR COLON TypeOrVoid Body
 */
public class FunDeclOp {
    private Identifier id;
    private ArrayList<ParDeclOp> parDeclList;
    private TypeOp type;
    private BodyOp body;

    //analisi semantica
    private boolean isFirstVisit;
    private SymbolTable symbolTable;

    public boolean isFirstVisit() {
        return isFirstVisit;
    }

    public void setFirstVisit(boolean firstVisit) {
        isFirstVisit = firstVisit;
    }

    /**
     * Costruttore con parDeclList per la produzione
     * (1) FunDecl -> DEF ID LPAR ParamDeclList RPAR COLON TypeOrVoid Body
     * @param id oggetto di tipo Identifier
     * @param parDeclList array di oggetti di tipo ParDeclOp
     * @param type oggetto di tipo Type
     * @param body oggetto di tipo Body
     */
    public FunDeclOp(Identifier id, ArrayList<ParDeclOp> parDeclList, TypeOp type, BodyOp body) {
        this.id = id;
        this.parDeclList = parDeclList;
        this.type = type;
        this.body = body;
        isFirstVisit = true;
    }

    /**
     * Costruttore senza parDeclList per la produzione
     * (2) FunDecl -> DEF ID LPAR RPAR COLON TypeOrVoid Body
     * @param id oggetto di tipo Identifier
     * @param type oggetto di tipo Type
     * @param body oggetto di tipo Body
     */
    public FunDeclOp(Identifier id, TypeOp type, BodyOp body) {
        this.id = id;
        this.type = type;
        this.body = body;
        isFirstVisit = true;
    }

    public Object accept(Visitor v) throws Exception {
        return v.visit(this);
    }

    public Identifier getId() {
        return id;
    }

    public ArrayList<ParDeclOp> getParDeclList() {
        return parDeclList;
    }

    public TypeOp getType() {
        return type;
    }

    public BodyOp getBody() {
        return body;
    }

    public void add(ParDeclOp parDeclOp){
        parDeclList.add(parDeclOp);
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    public void setSymbolTable(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }

    @Override
    public String toString() {
        String toString = "";

        toString += "FunDeclOp: " + id;

        //controllo parDeclList
        if(!Utility.isNull(parDeclList)){
            toString += "(" + parDeclList +")";
        }else{
            toString += "()";
        }

        toString += ":" + type + "{ " + body + "}";

        return toString;
    }
}
