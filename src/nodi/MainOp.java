package nodi;

import main.visitor.utility.SymbolTable;

/**
 * Classe per il nodo MainFunDecl
 * (1) MainFunDecl -> MAIN FunDecl
 */
public class MainOp {
    private String MAIN;
    private FunDeclOp funDecl;

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
     * Costruttore per la produzione
     * (1) MainFunDecl -> MAIN FunDecl
     * @param funDecl oggetto di tipo FunDeclOp
     */
    public MainOp(String main, FunDeclOp funDecl) {
        this.MAIN = main;
        this.funDecl = funDecl;
        isFirstVisit = true;
    }

    public Object accept(Visitor v) throws Exception {
        return v.visit(this);
    }

    public FunDeclOp getFunDecl() {
        return funDecl;
    }

    public String getMAIN() {
        return MAIN;
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
        toString+="MainOp: " + funDecl;

        return toString;
    }
}
