package nodi.statement;
import nodi.FunCallOp;
import nodi.TypeOp;
import nodi.Visitor;

public class Statement {

    private Object node;

    public enum TypeStmt{
        assignStm,
        forStm,
        ifStm,
        readStm,
        whileStm,
        writeStm,
        funCall,
        returnStm,
    } private TypeStmt type;

    public Statement(AssignStatement node) {
        this.type = TypeStmt.assignStm;
        this.node = node;
    }

    public Statement(ForStatement node) {
        this.type = TypeStmt.forStm;
        this.node = node;
    }

    public Statement(IfStatement node) {
        this.type = TypeStmt.ifStm;
        this.node = node;
    }

    public Statement(ReadStatement node) {
        this.type = TypeStmt.readStm;
        this.node = node;
    }

    public Statement(WhileStatement node) {
        this.type = TypeStmt.whileStm;
        this.node = node;
    }

    public Statement(WriteStatement node) {
        this.type = TypeStmt.writeStm;
        this.node = node;
    }

    public Statement(FunCallOp node){
        this.type = TypeStmt.funCall;
        this.node = node;
    }

    public Statement(ReturnOp node){
        this.type = TypeStmt.returnStm;
        this.node = node;
    }

    public Statement(){}

    public Object getNode() {
        return node;
    }

    public TypeStmt getType() {
        return type;
    }

    public Object accept(Visitor v) throws Exception {
        return v.visit(this);
    }
}
