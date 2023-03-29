package nodi;

import main.visitor.utility.SymbolTable;
import main.visitor.utility.Utility;

import java.util.ArrayList;

/**
 * Classe per il nodo Program
 *
 * (1) Program -> DeclList MainFunDecl DeclList
 * (2) Program -> MainFunDecl DeclList
 * (3) Program -> MainFunDecl
 */
public class ProgramOp {
    private ArrayList<DeclOp> DeclList1;
    private ArrayList<DeclOp> DeclList2;
    private MainOp mainOp;

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
     * (1) Program -> DeclList MainFunDecl DeclList
     * @param DeclList1 array di oggetti di tipo DeclList
     * @param main oggetto di tipo MainOp
     * @param DeclList2 array di oggetti di tipo DeclList
     */
    public ProgramOp(ArrayList<DeclOp> DeclList1, MainOp main, ArrayList<DeclOp> DeclList2) {
        this.DeclList1 = DeclList1;
        this.mainOp = main;
        this.DeclList2 = DeclList2;
        isFirstVisit = true;
    }

    /**
     * Costruttore per la produzione
     * (2) Program -> MainFunDecl DeclList2
     * @param main oggetto di tipo MainOp
     * @param DeclList1 array di oggetti di tipo DeclList
     */
    public ProgramOp( MainOp main, ArrayList<DeclOp> DeclList2) {
        this.mainOp = main;
        this.DeclList2 = DeclList2;
        isFirstVisit = true;
    }

    /**
     * Costruttore per la produzione
     * (3) Program -> DeclList1 MainFunDecl
     * @param DeclList1 array di oggetti di tipo DeclList
     * @param main oggetto di tipo MainOp
     */
    public ProgramOp(ArrayList<DeclOp> DeclList1, MainOp main) {
        this.mainOp = main;
        this.DeclList1 = DeclList1;
        isFirstVisit = true;
    }

    /**
     * Costruttore per la produzione
     * (4) Program -> MainFunDecl
     * @param main oggetto di tipo MainOp
     */
    public ProgramOp(MainOp main) {
        this.mainOp = main;
        isFirstVisit = true;
    }

    public Object accept(Visitor v) throws Exception {
        return v.visit(this);
    }

    public ArrayList<DeclOp> getDeclList1() {
        return DeclList1;
    }

    public ArrayList<DeclOp> getDeclList2() {
        return DeclList2;
    }

    public MainOp getMain() {
        return mainOp;
    }

    public void add (DeclOp object){
        DeclList1.add(object);
    }

    public void add2 (DeclOp object){
        DeclList2.add(object);
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
        if(!Utility.isNull(DeclList1))
            toString+=DeclList1;

        toString+= mainOp.toString();

        if(!Utility.isNull(DeclList2))
            toString+=DeclList2;

        return toString;
    }
}
