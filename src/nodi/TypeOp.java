package nodi;

import java.io.IOException;

/**
 * TypeOp gestisce i nodi: Type e TypeOrVoid
 *
 * (1) TypeOp -> INTEGER | BOOL | REAL | STRING | CHAR
 * (2) TypeOrVoid -> Type | VOID
 */
public class TypeOp {
    /**
     * enum per gestire tutti i tipi, compresi void e var
     */
    public enum Type {
        integer,
        bool,
        real,
        string,
        character,
        voidType,
        notype,
    }
    private Type type;

    //se il lessema è di out
    private int isOut;

    public TypeOp (Type type){
        this.type = type;
    }

    public Object accept(Visitor v) throws IOException {
        return v.visit(this);
    }

    public Type getType() {
        return type;
    }

    public int isOut() {
        return isOut;
    }

    public void setOut(int out) {
        isOut = out;
    }

    @Override
    public String toString() {
        return type.toString();

    }
}
