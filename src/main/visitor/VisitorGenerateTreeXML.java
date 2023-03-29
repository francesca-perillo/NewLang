package main.visitor;

import main.visitor.utility.Utility;
import nodi.*;
import nodi.operation.*;
import nodi.statement.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Visitor per la generazione dell'AST.
 */
public class VisitorGenerateTreeXML implements Visitor {
    private Document document;

    public VisitorGenerateTreeXML() throws ParserConfigurationException {
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
        this.document = documentBuilder.newDocument();
    }

    public void saveTo(String filepath) throws TransformerException {
        Transformer t = TransformerFactory.newInstance().newTransformer();
        t.setOutputProperty(OutputKeys.INDENT, "yes");
        t.transform(new DOMSource((Node) document), new StreamResult(new File(filepath)));
    }

    /**
     * Expr -> Expr AND Expr
     * @param andOp lessema: and
     * @return
     */
    @Override
    public Object visit(AndOp andOp) throws Exception {
        Element andOpExpr = document.createElement("Expr");
        andOpExpr.setAttribute("typeExpr", "AND");

        Element expr1 = (Element) andOp.getE1().accept(this);
        andOpExpr.appendChild(expr1);

        Element expr2 = (Element) andOp.getE2().accept(this);
        andOpExpr.appendChild(expr2);

        return andOpExpr;
    }

    /**
     * Expr -> Expr DIV Expr
     * @param divOp lessama: /
     * @return
     */
    @Override
    public Object visit(DivOp divOp) throws Exception {
        Element divOpExpr = document.createElement("Expr");
        divOpExpr.setAttribute("typeExpr", "DIV");

        Element expr1 = (Element) divOp.getE1().accept(this);
        divOpExpr.appendChild(expr1);

        Element expr2 = (Element) divOp.getE2().accept(this);
        divOpExpr.appendChild(expr2);

        return divOpExpr;
    }

    /**
     * Expr -> Expr EQ Expr
     * @param eqOp lessama: =
     * @return
     */
    @Override
    public Object visit(EqOp eqOp) throws Exception {
        Element eqOpExpr = document.createElement("Expr");
        eqOpExpr.setAttribute("typeExpr", "EQ");

        Element expr1 = (Element)  eqOp.getE1().accept(this);
        eqOpExpr.appendChild(expr1);

        Element expr2 = (Element) eqOp.getE2().accept(this);
        eqOpExpr.appendChild(expr2);

        return eqOpExpr;
    }

    /**
     * Expr -> Expr GE Expr
     * @param geOp lessema: >=
     * @return
     */
    @Override
    public Object visit(GeOp geOp) throws Exception {
        Element geOpExpr = document.createElement("Expr");
        geOpExpr.setAttribute("typeExpr", "GE");

        Element expr1 = (Element) geOp.getE1().accept(this);
        geOpExpr.appendChild(expr1);

        Element expr2 = (Element) geOp.getE2().accept(this);
        geOpExpr.appendChild(expr2);

        return geOpExpr;
    }

    /**
     * Expr -> Expr GT Expr
     * @param gtOp lessema: >
     * @return
     */
    @Override
    public Object visit(GtOp gtOp) throws Exception {
        Element gtOpExpr = document.createElement("Expr");
        gtOpExpr.setAttribute("typeExpr", "GT");

        Element expr1 = (Element) gtOp.getE1().accept(this);
        gtOpExpr.appendChild(expr1);

        Element expr2 = (Element) gtOp.getE2().accept(this);
        gtOpExpr.appendChild(expr2);

        return gtOpExpr;
    }

    /**
     * Expr -> Expr LE Expr
     * @param leOp lessema: >
     * @return
     */
    @Override
    public Object visit(LeOp leOp) throws Exception {
        Element leOpExpr = document.createElement("Expr");
        leOpExpr.setAttribute("typeExpr", "LE");

        Element expr1 = (Element) leOp.getE1().accept(this);
        leOpExpr.appendChild(expr1);

        Element expr2 = (Element) leOp.getE2().accept(this);
        leOpExpr.appendChild(expr2);

        return leOpExpr;
    }

    /**
     * Expr -> Expr LT Expr
     * @param ltOp lessema: <
     * @return
     */
    @Override
    public Object visit(LtOp ltOp) throws Exception {
        Element ltOpExpr = document.createElement("Expr");
        ltOpExpr.setAttribute("typeExpr", "LT");

        Element expr1 = (Element) ltOp.getE1().accept(this);
        ltOpExpr.appendChild(expr1);

        Element expr2 = (Element) ltOp.getE2().accept(this);
        ltOpExpr.appendChild(expr2);

        return ltOpExpr;
    }

    /**
     * (1) Expr -> Expr MINUS Expr
     * (2) Expr -> MINUS Expr
     * @param minusOp lessema: -
     * @return
     */
    @Override
    public Object visit(MinusOp minusOp) throws Exception {
        Element minusOpExpr = document.createElement("Expr");

        Element expr1 = (Element) minusOp.getE1().accept(this);
        minusOpExpr.appendChild(expr1);

        // (2) Expr -> MINUS Expr
        if(minusOp.getE2() == null){
            minusOpExpr.setAttribute("typeExpr", "minus");
            return minusOpExpr;
        } else { // (1) Expr -> Expr MINUS Expr
            minusOp.getE2().accept(this);
            Element expr2 = (Element) minusOp.getE2().accept(this);
            minusOpExpr.appendChild(expr2);
            minusOpExpr.setAttribute("typeExpr", "-");
            return minusOpExpr;
        }
    }

    /**
     * Expr -> Expr NE Expr
     * @param neOp lessema: <> or !=
     * @return
     */
    @Override
    public Object visit(NeOp neOp) throws Exception {
        Element neOpExpr = document.createElement("Expr");
        neOpExpr.setAttribute("typeExpr", "NE");

        Element expr1 = (Element) neOp.getE1().accept(this);
        neOpExpr.appendChild(expr1);

        Element expr2 = (Element) neOp.getE2().accept(this);
        neOpExpr.appendChild(expr2);

        return neOpExpr;
    }

    /**
     * Expr -> NOT Expr
     * @param notOp lessema: not
     * @return
     */
    @Override
    public Object visit(NotOp notOp) throws Exception {
        Element notOpExpr = document.createElement("Expr");
        notOpExpr.setAttribute("typeExpr", "NOT");

        Element expr = (Element) notOp.getE1().accept(this);
        notOpExpr.appendChild(expr);

        return notOpExpr;
    }

    /**
     * Classe che gestisce le operazioni. Contiene i costruttori
     * per le operazioni binarie e unarie.
     * @param operation
     * @return
     */
    @Override
    public Object visit(Operation operation) throws Exception {
        Element op = document.createElement("Operation");

        //operazione unaria
        Element expr1 = (Element) operation.getE1().accept(this);
        op.appendChild(expr1);

        //operazione binaria, se expr2 è presente
        Element expr2 = (Element) operation.getE2().accept(this);
        if(expr2 != null)
            op.appendChild(expr2);

        return op;
    }

    /**
     * Expr -> Expr OR Expr
     * @param orOp lessema: or
     * @return
     */
    @Override
    public Object visit(OrOp orOp) throws Exception {
        Element orOpExpr = document.createElement("Expr");
        orOpExpr.setAttribute("typeExpr", "OR");

        Element expr1 = (Element) orOp.getE1().accept(this);
        orOpExpr.appendChild(expr1);

        Element expr2 = (Element) orOp.getE2().accept(this);
        orOpExpr.appendChild(expr2);

        return orOpExpr;
    }

    /**
     * Expr -> Expr PLUS Expr
     * @param plusOp lessema: +
     * @return
     */
    @Override
    public Object visit(PlusOp plusOp) throws Exception {
        Element plusOpExpr = document.createElement("Expr");
        plusOpExpr.setAttribute("typeExpr", "PLUS");

        Element expr1 = (Element) plusOp.getE1().accept(this);
        plusOpExpr.appendChild(expr1);

        Element expr2 = (Element) plusOp.getE2().accept(this);
        plusOpExpr.appendChild(expr2);

        return plusOpExpr;
    }

    /**
     * Expr -> Expr POW Expr
     * @param powOp lessema: ^
     * @return
     */
    @Override
    public Object visit(PowOp powOp) throws Exception {
        Element powOpExpr = document.createElement("Expr");
        powOpExpr.setAttribute("typeExpr", "POW");

        Element expr1 = (Element) powOp.getE1().accept(this);
        powOpExpr.appendChild(expr1);

        Element expr2 = (Element) powOp.getE2().accept(this);
        powOpExpr.appendChild(expr2);

        return powOpExpr;
    }

    /**
     * Expr -> Expr STR_CONCAT Expr
     * @param stringConcatOp lessema: &
     * @return
     */
    @Override
    public Object visit(StringConcatOp stringConcatOp) throws Exception {
        Element stringConcatOpExpr = document.createElement("Expr");
        stringConcatOpExpr.setAttribute("typeExpr","STRING_CONCAT");

        Element expr1 = (Element) stringConcatOp.getE1().accept(this);
        stringConcatOpExpr.appendChild(expr1);

        Element expr2 = (Element) stringConcatOp.getE2().accept(this);
        stringConcatOpExpr.appendChild(expr2);

        return stringConcatOpExpr;
    }

    /**
     * Expr -> Expr TIMES Expr
     * @param timesOp lessema: *
     * @return
     */
    @Override
    public Object visit(TimesOp timesOp) throws Exception {
        Element timesOpExpr = document.createElement("Expr");
        timesOpExpr.setAttribute("typeExpr", "TIMES");

        Element expr1 = (Element) timesOp.getE1().accept(this);
        timesOpExpr.appendChild(expr1);

        Element expr2 = (Element) timesOp.getE2().accept(this);
        timesOpExpr.appendChild(expr2);

        return timesOpExpr;
    }

    /**
     * (1) AssignStat -> IdList ASSIGN ExprList
     * -----
     * Possiamo trovare l'operatore ASSIGN anche nelle produzioni:
     * (2) ForStat -> FOR ID ASSIGN INTEGER_CONST TO INTEGER_CONST LOOP Body
     * (3) IdInitList -> ID ASSIGN Expr
     * @param assignStatement lessema: <<
     * @return
     */
    @Override
    public Object visit(AssignStatement assignStatement) throws Exception {
        Element assignStatOp = document.createElement("AssignStat");

        ArrayList<Identifier> idList = assignStatement.getArrayListId();
        ArrayList<ExprOp> exprList = assignStatement.getExpr();

        // (2) ForStat -> FOR ID ASSIGN INTEGER_CONST TO INTEGER_CONST LOOP Body
        if(assignStatement.getId() != null & assignStatement.getConst() != null){

            Element id = (Element) assignStatement.getId().accept(this);
            assignStatOp.appendChild(id);

            Element integerConst = (Element) assignStatement.getConst().accept(this);
            assignStatOp.appendChild(integerConst);

            // (3) IdInitList -> ID ASSIGN Expr
        } else if (assignStatement.getId() != null & assignStatement.getE() != null) {

            Element id = (Element) assignStatement.getId().accept(this);
            assignStatOp.appendChild(id);

            Element expr = (Element) assignStatement.getE().accept(this);
            assignStatOp.appendChild(expr);

            // (1) AssignStat -> IdList ASSIGN ExprList
        }else if(!isNull(idList) & !isNull(exprList)){

            Collections.reverse(idList);
            for (Identifier i : idList){
                Element singleId = (Element) i.accept(this);
                assignStatOp.appendChild(singleId);
            }

            Collections.reverse(exprList);
            for(ExprOp e : exprList){
                Element singleExpr = (Element) e.accept(this);
                assignStatOp.appendChild(singleExpr);
            }
        }

        return assignStatOp;
    }

    /**
     * ForStat -> FOR ID ASSIGN INTEGER_CONST TO INTEGER_CONST LOOP Body
     * @param forStatement
     * @return
     */
    @Override
    public Object visit(ForStatement forStatement) throws Exception {
        Element forStat = document.createElement("ForStat");

        Element assignStat = (Element) forStatement.getAssign().accept(this);
        forStat.appendChild(assignStat);

        Element integerConst = (Element) forStatement.getIntegerConst().accept(this);
        forStat.appendChild(integerConst);

        Element body = (Element) forStatement.getBody().accept(this);
        forStat.appendChild(body);

        return forStat;
    }

    /**
     * (1) IfStat -> IF Expr THEN Body Else
     * (2) IfStat -> IF Expr THEN Body
     * @param ifStatement
     * @return
     */
    @Override
    public Object visit(IfStatement ifStatement) throws Exception {
        Element ifStat = document.createElement("IfStat");

        //(2) IfStat -> IF Expr THEN Body
        Element expr = (Element) ifStatement.getExpr().accept(this);
        ifStat.appendChild(expr);

        Element body = (Element) ifStatement.getBody1().accept(this);
        ifStat.appendChild(body);

        //(1) IfStat -> IF Expr THEN Body Else
        if(ifStatement.getBody2() != null){
            Element body2 = (Element) ifStatement.getBody2().accept(this);
            ifStat.appendChild(body2);
        }

        return ifStat;
    }

    /**
     * (1) ReadStat -> IdList READ STRING_CONST
     * (2) ReadStat -> IdList READ
     * @param readStatement
     * @return
     */
    @Override
    public Object visit(ReadStatement readStatement) throws Exception {
        Element readStat = document.createElement("ReadStat");
        ArrayList<Identifier> idList = readStatement.getIdList();
        Collections.reverse(idList);

        // (2) ReadStat -> IdList READ
        for(Identifier i : idList){
            Element id = (Element) i.accept(this);
            readStat.appendChild(id);
        }

        if (readStatement.getStringConst() != null) {
            // (1) ReadStat -> IdList READ STRING_CONST
            Element stringConst = (Element) readStatement.getStringConst().accept(this);
            readStat.appendChild(stringConst);
        }
        return readStat;
    }

    /**
     * (1) Stat -> RETURN Expr SEMI
     * (2) Stat -> RETURN SEMI
     * @param returnOp
     * @return
     */
    @Override
    public Object visit(ReturnOp returnOp) throws Exception {
        Element returnStat = document.createElement("ReturnOp");

        //(1) Stat -> RETURN Expr SEMI
        if(returnOp.getExprOp() != null) {
            Element expr = (Element) returnOp.getExprOp().accept(this);
            returnStat.appendChild(expr);
        }

        // (1) and (2) Stat -> RETURN SEMI
        return returnStat;
    }

    /**
     * La classe Statement contiene un metodo per ogni Statement.
     * node: oggetto Object che identifica il nodo
     * type: tipo del nodo di tipi TypeStmt
     * @param statement
     * @return
     */
    @Override
    public Object visit(Statement statement) throws Exception {
        Element stmt = document.createElement("Statement");

        Element e1 = null;

        switch (statement.getType()) {
            case forStm:
                e1 = (Element) ((ForStatement) statement.getNode()).accept(this);
                break;
            case ifStm:
                e1 = (Element) ((IfStatement) statement.getNode()).accept(this);
                break;
            case readStm:
                e1 = (Element) ((ReadStatement) statement.getNode()).accept(this);
                break;
            case writeStm:
                e1 = (Element) ((WriteStatement) statement.getNode()).accept(this);
                break;
            case assignStm:
                e1 = (Element) ((AssignStatement) statement.getNode()).accept(this);
                break;
            case whileStm:
                e1 = (Element) ((WhileStatement) statement.getNode()).accept(this);
                break;
            case funCall:
                e1 = (Element) ((FunCallOp) statement.getNode()).accept(this);
                break;
        }

        if (e1 != null)
            stmt.appendChild(e1);

        return stmt;
    }

    /**
     * WhileStat -> WHILE Expr LOOP Body
     * @param whileStatement
     * @return
     */
    @Override
    public Object visit(WhileStatement whileStatement) throws Exception {
        Element whileStmt = document.createElement("WhileStat");

        Element expr = (Element) whileStatement.getExpr().accept(this);
        whileStmt.appendChild(expr);

        Element body = (Element) whileStatement.getBody().accept(this);
        whileStmt.appendChild(body);

        return whileStmt;
    }

    /**
     * (1) WriteStat -> LPAR ExprList RPAR WRITE
     * (2) WriteStat -> LPAR ExprList RPAR WRITELN
     * @param writeStatement
     * @return
     */
    @Override
    public Object visit(WriteStatement writeStatement) throws Exception {
        Element writeStmt = document.createElement("WriteStat");

        //mode definisce se stiamo considerando (1) o (2)
        Element writeOp = document.createElement("WriteMode");
        writeOp.setAttribute("mode", writeStatement.getMode().toString());
        writeStmt.appendChild(writeOp);

        ArrayList<ExprOp> exprList = writeStatement.getExprList();
        Collections.reverse(exprList);
        for(ExprOp e : exprList){
            Element expr = (Element) e.accept(this);
            writeStmt.appendChild(expr);
        }

        return writeStmt;
    }

    /**
     * (1) Body -> LBRAC VarDeclList StatList RBRAC
     * (2) Body -> LBRAC StatList RBRAC
     * (3) Body -> LBRAC VarDeclList RBRAC
     * (4) Body -> LBRAC RBRAC
     * @param bodyOp
     * @return
     */
    @Override
    public Object visit(BodyOp bodyOp) throws Exception {
        Element body = document.createElement("Body");

        ArrayList<VarDeclOp> varDeclList = null;
        ArrayList<Statement> statList = null;

        if(!Utility.isNull(bodyOp.getVarDeclOps())){
            varDeclList = bodyOp.getVarDeclOps();
            Collections.reverse(varDeclList);
        }

        if(!Utility.isNull(bodyOp.getStatList())){
            statList = bodyOp.getStatList();
            Collections.reverse(statList);
        }

        // (1) Body -> LBRAC VarDeclList StatList RBRAC
        // (3) Body -> LBRAC VarDeclList RBRAC
        if(!isNull(varDeclList)){
            for (VarDeclOp v : varDeclList) {
                Element varDecl = (Element) v.accept(this);
                body.appendChild(varDecl);
            }
        }

        // (1) Body -> LBRAC VarDeclList StatList RBRAC
        // (2) Body -> LBRAC StatList RBRAC
        if(!isNull(statList)) {
            for(Statement s : statList) {
               Element stmt = (Element) s.accept(this);
               body.appendChild(stmt);
            }
        }

        return body;
    }

    /**
     *
     * @param constOp
     * @return
     */
    @Override
    public Object visit(ConstOp constOp) {
        Element constValue = document.createElement("ConstOp");

        Element type = document.createElement("Type");
        type.setAttribute("type", constOp.getType().toString());
        constValue.appendChild(type);

        if(constOp.getConstValue() != null){
            Element value = document.createElement("Value");
            value.setAttribute("value", constOp.getConstValue().toString());
            constValue.appendChild(value);
        }

        return constValue;
    }

    /**
     * DeclOp gestisce i DeclList
     * con oggetti sia FunDeclOp che VarDeclOp.
     * @param declOp
     * @return
     */
    @Override
    public Object visit(DeclOp declOp) throws Exception {
        Element declList = document.createElement("DeclList");

        if(declOp.getFunDeclOp() != null){
            Element funDeclOp = (Element) declOp.getFunDeclOp().accept(this);
            declList.appendChild(funDeclOp);
        } else if (declOp.getVarDeclOp() != null){
            Element varDeclOp = (Element) declOp.getVarDeclOp().accept(this);
            declList.appendChild(varDeclOp);
        }

        return declList;
    }

    /**
     * ExprOp gestisce tutte le operazioni aritmetiche e logiche.
     * @param exprOp
     * @return
     */
    @Override
    public Object visit(ExprOp exprOp) throws Exception {
        Element expr = document.createElement("ExprOp");

        if(exprOp.getConstType() != null){
            Element constType = (Element) exprOp.getConstType().accept(this);
            expr.appendChild(constType);
        }

        if(exprOp.getId() != null){
            Element id = (Element) exprOp.getId().accept(this);
            expr.appendChild(id);
        }

        if(exprOp.getOperation() != null){
            Element op = (Element) exprOp.getOperation().accept(this);
            expr.appendChild(op);
        }

        if(exprOp.getFunCallOp() != null){
            Element funCall = (Element) exprOp.getFunCallOp().accept(this);
            expr.appendChild(funCall);
        }

        return expr;
    }

    /**
     * (1) FunDecl -> DEF ID LPAR RPAR COLON TypeOrVoid Body
     * (2) FunDecl -> DEF ID LPAR ParamDeclList RPAR COLON TypeOrVoid Body
     * @param funDeclOp
     * @return
     */
    @Override
    public Object visit(FunDeclOp funDeclOp) throws Exception {
        Element funDecl = document.createElement("FunDeclOp");

        //(1) FunDecl -> DEF ID LPAR RPAR COLON TypeOrVoid Body
        Element id = (Element) funDeclOp.getId().accept(this);
        funDecl.appendChild(id);

        ArrayList<ParDeclOp> parDeclList = funDeclOp.getParDeclList();

        //(2) FunDecl -> DEF ID LPAR ParamDeclList RPAR COLON TypeOrVoid Body
        if(!isNull(parDeclList)){
            ArrayList<ParDeclOp> parDeclOps = funDeclOp.getParDeclList();
            Collections.reverse(parDeclOps);
            for (ParDeclOp par: parDeclOps) {
                Element param = (Element) par.accept(this);
                funDecl.appendChild(param);
            }
        }

        //(1) FunDecl -> DEF ID LPAR RPAR COLON TypeOrVoid Body
        Element typeOrVoid = (Element) funDeclOp.getType().accept(this);
        funDecl.appendChild(typeOrVoid);
        Element body = (Element) funDeclOp.getBody().accept(this);
        funDecl.appendChild(body);

        return funDecl;
    }

    /**
     * (1) FunCall -> ID LPAR RPAR
     * (2) FunCall -> ID LPAR ExprList RPAR
     * @param funCallOp
     * @return
     */
    @Override
    public Object visit(FunCallOp funCallOp) throws Exception {
        Element funCall = document.createElement("FunCallOp");

        //FunCall -> ID LPAR RPAR
        Element id = (Element) funCallOp.getId().accept(this);
        funCall.appendChild(id);

        //FunCall -> ID LPAR ExprList RPAR
        if(!isNull(funCallOp.getExpr())){
            ArrayList<ExprOp> exprOps = funCallOp.getExpr();
            Collections.reverse(exprOps);
            for (ExprOp e : exprOps) {
                Element expr = (Element) e.accept(this);
                funCall.appendChild(expr);
            }
        }

        return funCall;
    }

    /**
     * Identifier gestisce l'id settando il suo attributo.
     * @param identifier
     * @return
     */
    @Override
    public Object visit(Identifier identifier) {
        Element id = document.createElement("Identifier");
        id.setAttribute("id", identifier.getLessema());
        return id;
    }

    /**
     * Questo visit gestisce la coppia id << valore
     * @param idInitObblOp
     * @return
     */
    @Override
    public Object visit(IdInitObblOp idInitObblOp) throws Exception {
        Element idInitObbl = document.createElement("idInitObblOp");

        Element assignment = (Element) idInitObblOp.getAssignStat().accept(this);
        idInitObbl.appendChild(assignment);
        return idInitObblOp;
    }

    /**
     * Classe per gestire IdInit
     * id   oppure   id << espressione
     * questi verranno poi aggiunti alla lista IdInitList
     * @param idInitOp
     * @return
     */
    @Override
    public Object visit(IdInitOp idInitOp) throws Exception {
        Element idInit = document.createElement("IdInitOp");

        //serve per gestire il singolo id, senza assegnazione.
        if(idInitOp.getId() != null) {
            Element identifier = (Element) idInitOp.getId().accept(this);
            idInit.appendChild(identifier);
        } //serve per gestire la coppia formata da id / espressione - id<<espressione
        else if(idInitOp.getAssignment() != null){
            Element assignment = (Element) idInitOp.getAssignment().accept(this);
            idInit.appendChild(assignment);
        }
        return idInit;
    }

    /**
     * (1) MainFunDecl -> MAIN FunDecl
     * @param mainOp
     * @return
     */
    @Override
    public Object visit(MainOp mainOp) throws Exception {
        Element main = document.createElement("MainOp");

        Element funDecl = (Element) mainOp.getFunDecl().accept(this);
        main.appendChild(funDecl);
        return main;
    }

    /**
     * (1) ParDecl -> OUT Type IdList
     * (2) ParDecl -> Type IdList
     * @param parDeclOp
     * @return
     */
    @Override
    public Object visit(ParDeclOp parDeclOp) throws Exception {
        Element parDecl = document.createElement("ParDeclOp");

        ArrayList<Identifier> identifiers = parDeclOp.getIdList();
        Collections.reverse(identifiers);

        // (1) ParDecl -> OUT Type IdList
        if(parDeclOp.getParamType()){
            Element out = document.createElement("out");
            out.setAttribute("out", "out");
            parDecl.appendChild(out);
        }

        // (2) ParDecl -> Type IdList
        Element type = (Element) parDeclOp.getType().accept(this);
        parDecl.appendChild(type);

        for (Identifier id : identifiers) {
            Element expr = (Element) id.accept(this);
            parDecl.appendChild(expr);
        }

        return parDecl;
    }

    /**
     * (1) Program -> DeclList1 MainFunDecl DeclList2
     * (2) Program -> MainFunDecl DeclList1
     * (2) Program -> DeclList1 MainFunDecl
     * (3) Program -> MainFunDecl
     * @param programOp
     * @return
     */
    @Override
    public Object visit(ProgramOp programOp) throws Exception {
        Element program = document.createElement("ProgramOp");
        ArrayList<DeclOp> declList1 = programOp.getDeclList1(); //(1), (2) e (3)
        ArrayList<DeclOp> declList2 = programOp.getDeclList2(); //(1)

        // (1) Program -> DeclList1 MainFunDecl DeclList2
        if( !isNull(declList1) && !isNull(declList2)){
            //inserisco DeclList1 come figlio
            Collections.reverse(declList1);
            for (DeclOp d : declList1) {
                Element decl1 = (Element) d.accept(this);
                program.appendChild(decl1);
            }

            Element mainFunDecl = (Element) programOp.getMain().accept(this);
            program.appendChild(mainFunDecl);

            Collections.reverse(declList2);
            for (DeclOp d : declList2) {
                Element decl2 = (Element) d.accept(this);
                program.appendChild(decl2);
            }
        } // (2) Program -> MainFunDecl DeclList2
        else if(isNull(declList1) && !isNull(declList2)){

            Element mainFunDecl = (Element) programOp.getMain().accept(this);
            program.appendChild(mainFunDecl);

            ArrayList<DeclOp> declOps2 = programOp.getDeclList2();
            Collections.reverse(declOps2);
            for (DeclOp d : declOps2) {
                Element decl1 = (Element) d.accept(this);
                program.appendChild(decl1);
            }
        } // (2) Program -> DeclList1 MainFunDecl
        else if(!isNull(declList1) && isNull(declList2)){

            Element mainFunDecl = (Element) programOp.getMain().accept(this);

            ArrayList<DeclOp> declOps1 = programOp.getDeclList1();
            Collections.reverse(declOps1);
            for (DeclOp d : declOps1) {
                Element decl1 = (Element) d.accept(this);
                program.appendChild(decl1);
            }

            program.appendChild(mainFunDecl);
        } // (3) Program -> MainFunDecl
        else {
            Element mainFunDecl = (Element) programOp.getMain().accept(this);
            program.appendChild(mainFunDecl);
        }

        document.appendChild(program);
        return document;
    }

    @Override
    public Object visit(TypeOp typeOp) {
        Element t = document.createElement("Type");
        t.setAttribute("type",typeOp.getType().toString());
        return t;
    }

    /**
     * (1) VarDecl -> VAR IdInitObblList SEMI
     * (2) VarDecl -> Type IdInitList SEMI
     * @param varDeclOp
     * @return
     */
    @Override
    public Object visit(VarDeclOp varDeclOp) throws Exception {
        Element varDecl = document.createElement("VarDeclOp");

        //(1) VarDecl -> VAR IdInitObblList SEMI
        ArrayList<IdInitObblOp> idInitObbl = varDeclOp.getIdInitObblOpList();
        if(varDeclOp.getVar() != null && !isNull(idInitObbl)){
            Element var = document.createElement("VAR");
            varDecl.appendChild(var);

            Collections.reverse(idInitObbl);
            for(IdInitObblOp i : idInitObbl){
                Element element = (Element) i.getAssignStat().accept(this);
                varDecl.appendChild(element);
            }
        }

        // (2) VarDecl -> Type IdInitList SEMI
        ArrayList<IdInitOp> idInitOp = varDeclOp.getIdInitOpList();
        if (varDeclOp.getType() != null && !isNull(idInitOp)){
            Element type = (Element) varDeclOp.getType().accept(this);
            varDecl.appendChild(type);

            Collections.reverse(idInitOp);
            for(IdInitOp i : idInitOp){
                Element id = (Element) i.accept(this);
                varDecl.appendChild(id);
            }
        }

        return varDecl;
    }

    private static Boolean isNull(ArrayList arrList) {
        if(arrList != null)
            for(Object a : arrList)
                if(a != null) return false;
        return true;
    }
}
