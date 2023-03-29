package nodi;

import main.visitor.utility.SymbolTable;
import main.visitor.utility.Utility;
import nodi.statement.Statement;

import java.util.ArrayList;

public class BodyOp {
    private ArrayList<VarDeclOp> varDeclOps;
    private ArrayList<Statement> statList;

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
     * Costruttore per le produzioni
     *
     * (1) Body -> LBRAC VarDeclList StatList RBRAC
     * (2) Body -> LBRAC StatList RBRAC
     * (3) Body -> LBRAC VarDeclList RBRAC
     *
     * @param varDeclOps può essere anche formato da un empty ArrayList di VarDeclOp
     * @param statList può essere anche formato da un empty ArrayList di StatList
     *
     */
    public BodyOp(ArrayList<VarDeclOp> varDeclOps, ArrayList<Statement> statList) {
        this.varDeclOps = varDeclOps;
        this.statList = statList;
        isFirstVisit = true;
    }

    /**
     * Costruttore vuoto per la produzione
     * (4) Body -> LBRAC RBRAC
     */
    public BodyOp() {
        isFirstVisit = true;
    }

    public Object accept(Visitor v) throws Exception {
        return v.visit(this);
    }

    public ArrayList<VarDeclOp> getVarDeclOps() {
        return varDeclOps;
    }

    public ArrayList<Statement> getStatList() {
        return statList;
    }

    public void add(VarDeclOp element){
        varDeclOps.add(element);
    }

    public void add(Statement element){
        statList.add(element);
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

        if(Utility.isNull(varDeclOps))
            toString+=varDeclOps;

        if(Utility.isNull(statList))
            toString+=statList;

        return toString;
    }
}
