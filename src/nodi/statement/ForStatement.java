package nodi.statement;

import main.visitor.utility.SymbolTable;
import nodi.BodyOp;
import nodi.ConstOp;
import nodi.Visitor;

/**
 * Classe per gestir il nodo per il For
 *
 * (1) ForStat -> FOR ID ASSIGN INTEGER_CONST TO INTEGER_CONST LOOP Body
 */
public class ForStatement extends Statement{

    private AssignStatement assign;
    private ConstOp integerConstOp;
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
     * Costruttore per la produzione
     * (1) ForStat -> FOR ID ASSIGN INTEGER_CONST TO INTEGER_CONST LOOP Body
     * @param assign oggetto di tipo AssignStat (Integer, Const)
     * @param integerConstOp oggetto di tipo Const.
     * @param body Oggetto di tipo BodyOp
     */
    public ForStatement(AssignStatement assign, ConstOp integerConstOp, BodyOp body) {
        this.assign = assign;
        this.integerConstOp = integerConstOp;
        this.body = body;
        isFirstVisit = true;
    }

    public Object accept(Visitor v) throws Exception{
        return v.visit(this);
    }

    public AssignStatement getAssign() {
        return assign;
    }

    public ConstOp getIntegerConst() {
        return integerConstOp;
    }

    public BodyOp getBody() {
        return body;
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    public void setSymbolTable(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }

    @Override
    public String toString() {
        return "ForStat{" +
                "assign=" + assign +
                ", integerConst=" + integerConstOp +
                ", body=" + body +
                '}';
    }
}
