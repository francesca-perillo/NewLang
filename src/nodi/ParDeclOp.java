package nodi;

import java.util.ArrayList;

/**
 * Gestisce la produzione ParDecl
 *
 * (1) ParDecl -> Type IdList
 * (2) ParDecl -> OUT Type IdList
 */
public class ParDeclOp {
    private final Boolean paramOut;
    private final TypeOp type;
    private final ArrayList<Identifier> idList;

    /**
     * Costruttore per la produzione
     * (1) ParDecl -> Type IdList
     * (2) ParDecl -> OUT Type IdList
     * @param paramOut oggetto boolean che specifica se è tipo out
     * @param type oggetto di tipo TypeOp
     * @param idList array di oggetti di tipo IdList
     */
    public ParDeclOp(Boolean paramOut, TypeOp type, ArrayList<Identifier> idList) {
        this.paramOut = paramOut;
        this.type = type;
        this.idList = idList;
    }


    public Object accept(Visitor v) throws Exception {
        return v.visit(this);
    }

    public boolean getParamType() {
        return paramOut;
    }

    public TypeOp getType() {
        return type;
    }

    public ArrayList<Identifier> getIdList() {
        return idList;
    }

    public void add(Identifier id) {
        idList.add(id);
    }

    @Override
    public String toString() {
        String toString = "";

        if(paramOut)
            toString = "\n- parametro out";

        toString += "\n-tipo: " + type + "\n-valore:" + idList + "\n";

        return toString;
    }
}
