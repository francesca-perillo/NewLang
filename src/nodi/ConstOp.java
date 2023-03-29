package nodi;

import java.io.IOException;

/**
 * Classe che gestisce il nodo e la produzione Const
 * Prende il valore solo dalle variabili integer_const, real_const, string_const, char_const.
 * NON prende valore per true e false.
 */
public class ConstOp {
    public enum Type {
        integer,
        real,
        string,
        character,
        trueConst,
        falseConst,
        bool //serve per VisitorTypeChecking.java
    }

    private Type type;
    private Object constValue;

    /**
     * Con valori come true e false viene utilizzato questo costruttore
     * TYPE.trueConst or TYPE.falseConst
     * @param type
     */
    public ConstOp(Type type){
        this.type = type;
    }

    /**
     * Con valori interi, reali e stringhe viene utilizzato questo costruttore
     * TYPE.integer or TYPE.real or TYPE.string or TYPE.character
     * @param type
     * @param constValue valore della costante
     */
    public ConstOp(Type type, Object constValue) {
        this.type = type;
        this.constValue = constValue;
    }

    public Object accept(Visitor v) throws IOException {
        return v.visit(this);
    }

    public Type getType() {
        return type;
    }

    public Object getConstValue() {
        return constValue;
    }

    @Override
    public String toString() {
        return type.toString();
    }
}