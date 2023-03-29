package nodi;

/**
 * Classe per gestire l'ID
 * non serve a gestire un non terminale.
 */
public class Identifier {
    private String id;
    public TypeOp idType;

    public Identifier(String id){
        this.id = id;
    }

    /**
     * Costruttore che gestisce sia il lessema che il suo tipo.
     * @param lessema lessema considerato
     * @param idType tipo del lessema
     */
    public Identifier(String lessema, TypeOp idType){
        this.id = lessema;
        this.idType = idType;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setIdType(TypeOp type){
        this.idType = type;
    }

    public TypeOp getIdType() {
        return this.idType;
    }

    public Object accept(Visitor v) throws Exception {
        return v.visit(this);
    }

    public String getLessema() {
        return id;
    }

    @Override
    public String toString() {
        return id + ", ";
    }
}
