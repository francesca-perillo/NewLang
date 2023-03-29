package nodi.statement;

import nodi.ConstOp;
import nodi.Identifier;
import nodi.Visitor;

import java.util.ArrayList;

/**
 * Classe che gestiste nodi ReadStat
 *
 * (1) ReadStat -> IdList READ STRING_CONST
 * (2) ReadStat -> IdList READ
 */
public class ReadStatement extends Statement{
    private ArrayList<Identifier> idList;
    private ConstOp stringConstOp;

    /**
     * Costruttore di ReadStat per la produzione
     * (1) ReadStat -> IdList READ STRING_CONST
     *
     * @param idList lista di oggetti Identifier
     * @param stringConstOp oggetto Const(Const, String)
     */
    public ReadStatement(ArrayList<Identifier> idList, ConstOp stringConstOp) {
        this.idList = idList;
        this.stringConstOp = stringConstOp;
    }

    /**
     * Costruttore di ReadStat per la produzione
     * (2) ReadStat -> IdList READ
     *
     * @param idList lista di oggetti Identifier
     */
    public ReadStatement(ArrayList<Identifier> idList) {
        this.idList = idList;
    }

    public Object accept(Visitor v) throws Exception {
        return v.visit(this);
    }

    public ArrayList<Identifier> getIdList() {
        return idList;
    }

    public ConstOp getStringConst() {
        return stringConstOp;
    }

    @Override
    public String toString() {
        return "ReadStatOp{" +
                "idList=" + idList +
                ", stringConst=" + stringConstOp +
                '}';
    }
}
