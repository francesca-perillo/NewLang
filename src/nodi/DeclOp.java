package nodi;


/**
 * DeclOp è una classe che gestisce il nodo DeclList
 * con oggetti sia FunDeclOp che VarDeclOp.
 *
 */
public class DeclOp {

    private FunDeclOp funDeclOp;
    private VarDeclOp varDeclOp;

    /**
     * Costruttore per FunDeclOp
     * @param funDeclOp oggetto di tipo FunDeclOp
     */
    public DeclOp(FunDeclOp funDeclOp) {
        this.funDeclOp = funDeclOp;
    }

    /**
     * Costruttore per VarDeclOp
     * @param varDeclOp oggetto di tipo VarDeclOp
     */
    public DeclOp(VarDeclOp varDeclOp) {
        this.varDeclOp = varDeclOp;
    }

    public Object accept(Visitor v) throws Exception {
        return v.visit(this);
    }

    public FunDeclOp getFunDeclOp() {
        return funDeclOp;
    }

    public VarDeclOp getVarDeclOp() {
        return varDeclOp;
    }

    @Override
    public String toString() {
        String toString = "DeclOp: ";

        if(funDeclOp != null)
            toString+=funDeclOp;

        if(varDeclOp != null)
            toString+=varDeclOp;

        return toString;
    }


}
