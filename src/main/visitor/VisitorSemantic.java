package main.visitor;

import main.visitor.utility.*;
import nodi.*;
import nodi.operation.*;
import nodi.statement.*;

import javax.swing.*;
import java.awt.desktop.SystemSleepEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Visitor per la gestione dello scoping
 * Viene contestualmente fatto il type cheching.
 */
public class VisitorSemantic implements Visitor {
    ArrayList<String> funNotDecl = new ArrayList<>();
    HashMap<String, String> varNotDeclAndType = new HashMap<>();

    /**
     * Il nodo ProgramOp gestisce le seguenti produzioni.
     * (1) Program -> DeclList MainFunDecl DeclList
     * (2) Program -> MainFunDecl DeclList
     * (3) Program -> DeclList MainFunDecl
     * (4) Program -> MainFunDecl
     * @param p
     * @return
     */
    @Override
    public Object visit(ProgramOp p) throws Exception {

        //alla prima visita creo il nuovo scope.
        if (p.isFirstVisit()){
            p.setFirstVisit(false);
            TypeEnvironment.newScope();
            p.setSymbolTable(TypeEnvironment.getScope());

            //accetto i declList
            if(!Utility.isNull(p.getDeclList1()))
                for(DeclOp d : p.getDeclList1()){
                    if(d.getFunDeclOp()!=null){
                        Utility.recDecl(d.getFunDeclOp());
                    }
                    if(d.getVarDeclOp()!=null){
                        d.getVarDeclOp().accept(this);
                    }
                }

            //accetto i declList
            if(!Utility.isNull(p.getDeclList2()))
                for(DeclOp d : p.getDeclList2()){
                    if(d.getFunDeclOp()!=null) {
                        Utility.recDecl(d.getFunDeclOp());
                    }
                    if(d.getVarDeclOp()!=null){
                        d.getVarDeclOp().accept(this);
                    }
                }

            if(!Utility.isNull(p.getDeclList1()))
                for(DeclOp d : p.getDeclList1()){
                    d.accept(this);
                }

            //accetto i declList
            if(!Utility.isNull(p.getDeclList2()))
                for(DeclOp d : p.getDeclList2()){
                    d.accept(this);
                }

            //accetto il main
            p.getMain().accept(this);

            if(!funNotDecl.isEmpty()){
                for(String s : funNotDecl)
                    throw new Error("Missing Function. \n\nVisitorSemantic - La funzione - " + s + "() - non esiste.\n");
            }

           if(!varNotDeclAndType.isEmpty()){
                for(Map.Entry<String, String> entry : varNotDeclAndType.entrySet())
                        throw new Error("Missing var declaration.\n\nVisitorSemantic - ERROR.\nSi sta tentando di usare l'identificatore:\n" +
                                "- " + entry.getKey() +
                                "\nsenza che questo sia mai stato dichiarato!\n");
            }
        }

        TypeEnvironment.removeScope();
        //ritorno il tipo
        return TypeOp.Type.voidType.toString();
    }

    /**
     * ForStat ::= FOR ID ASSIGN INTEGER_CONST TO INTEGER_CONST LOOP Body
     * @param f
     * @return
     */
    @Override
    public Object visit(ForStatement f) throws Exception {

        String id = f.getAssign().getId().getLessema();
        String start = f.getAssign().getConst().getType().toString();
        String end = f.getIntegerConst().getType().toString();
        if(f.isFirstVisit()){
            TypeEnvironment.newScope();
            f.setSymbolTable(TypeEnvironment.getScope());
            f.setFirstVisit(false);

            if(!start.equals(TypeOp.Type.integer.toString()) && !end.equals(TypeOp.Type.integer.toString())){
                throw new Exception("VisitorSemantic - TypeChecking_ERROR.\n I valori di inizio e fine del for devono essere interi. \n" +
                        "- valore start: "+ start+ "\n" +
                        "- valore end: "+end);
            }

            //se id NON è gia stato dichiarato, lo aggiungo alla tabella del for
            if(!TypeEnvironment.lookupBool(id)){
                ElementVar ev = new ElementVar(id, TypeOp.Type.integer.toString(), false);
                TypeEnvironment.getScope().addVar(ev);
            }

            f.getBody().accept(this);
        }

        TypeEnvironment.removeScope();
        return TypeOp.Type.voidType.toString();
    }


    /**
     *  (1) IfStat -> IF Expr THEN Body Else
     *  (2) IfStat -> IF Expr THEN Body
     * @param i
     * @return
     */
    @Override
    public Object visit(IfStatement i) throws Exception {

        String bodyType = "voidType";

        if(i.isFirstVisit()){
            i.setFirstVisit(false);
            TypeEnvironment.newScope();
            i.setSymbolTable(TypeEnvironment.getScope());

            //recupero il tipo di expr per il controllo
            Object exprReturn = i.getExpr().accept(this);
            String exprType = exprReturn.toString();

            if(!exprType.equals(TypeOp.Type.bool.toString()))
                throw new Exception("VisitorSemantic - TypeChecking_ERROR. La condizione dell'IF deve essere necessariamente booleana, non " + exprType);

           i.getBody1().accept(this);

            //controllo se esiste l'Else (Body2) e lo accetto
            if(i.getBody2() != null)
                i.getBody2().accept(this);

            TypeEnvironment.removeScope();
        }
        return bodyType;
    }

    /**
     *
     * ParamDeclList -> NonEmptyParamDeclList
     * NonEmptyParamDeclList -> ParDecl
     * NonEmptyParamDeclList -> NonEmptyParamDeclList PIPE ParDecl
     * ---------------
     * (1) ParDecl -> Type IdList
     * (2) ParDecl -> OUT Type IdList
     * @param p
     * @return
     */
    @Override
    public Object visit(ParDeclOp p) throws Exception {

        String type = p.getType().toString();
        ElementVar var = null;

        ArrayList<Identifier> parId = p.getIdList();
        Collections.reverse(parId);

        for(Identifier id : parId){
            if(TypeEnvironment.getScope().containElement(id.getLessema())){
                throw new Exception("\n\nVisitorSemantic - ERROR.\nIl parametro "+ parId +" è stato gia' dichiarato.\n");
            } else {
                //p.getParamType è la modalità OUT
                var = new ElementVar(id.getLessema(), type, p.getParamType());
                TypeEnvironment.getScope().addVar(var);

                id.setIdType(p.getType());
            }
        }

        return TypeOp.Type.voidType.toString();
    }

    /**
     * Possono creare un nuovo BodyOp: if, else, while e for.
     * (1) Body -> LBRAC VarDeclList StatList RBRAC
     * (2) Body -> LBRAC StatList RBRAC
     * (3) Body -> LBRAC VarDeclList RBRAC
     * (4) Body -> LBRAC RBRAC
     * @param b
     * @return
     */
    @Override
    public Object visit(BodyOp b) throws Exception {

        b.setSymbolTable(TypeEnvironment.getScope());

        Object typeReturn = null;
        if (!Utility.isNull(b.getVarDeclOps()))
            for (VarDeclOp v : b.getVarDeclOps())
                v.accept(this);

        if (!Utility.isNull(b.getStatList())) {
            for (Statement s : b.getStatList()) {
                if(s instanceof nodi.statement.ReturnOp){
                    return s.accept(this);
                }
                s.accept(this);
            }
        }
        return "noType";
    }

    /**
     * FunDeclOp si occupa del nodo FunDecl:
     * FunDecl -> DEF ID LPAR ParamDeclList RPAR COLON TypeOrVoid Body
     * FunDecl -> DEF ID LPAR RPAR COLON TypeOrVoid Body
     * --------
     * 1. se la funzione non esiste ancora, allora la aggiungo alla tabella dei simboli
     * 2. se la funzione esiste allora ERRORE -> funzione già dichiarata
     * ---------
     * per testare fare riferimento alla cartella test/methodFail
     * @param f
     * @return
     */
    @Override
    public Object visit(FunDeclOp f) throws Exception {
        String funName = f.getId().getLessema();

        funNotDecl.remove(funName);

        //creo il nuovo scope per la funzione corrente
        if(f.isFirstVisit()){
            TypeEnvironment.newScope();
            f.setSymbolTable(TypeEnvironment.getScope());
            f.setFirstVisit(false);

            //faccio l'accept dei singoli par per aggiungerli alla tabella dei siboli
            if(!Utility.isNull(f.getParDeclList())){
                //accept dei singoli pardecl
                for(ParDeclOp p : f.getParDeclList())
                    p.accept(this);
            }

            //Prelevo il valore di ritorno atteso
            String returnOfMethod = f.getType().toString();

            //prelevo il valore di ritorno effettivo
            Object returnValue = f.getBody().accept(this);
            String returnOfBody = returnValue.toString();

            //controllo sul tipo di ritorno della funzione (returnOfMethod)
            if (returnOfMethod.equals(TypeOp.Type.real.toString()) && returnOfBody.equals(TypeOp.Type.integer.toString())) {
                return "inferenza_di_tipo";
            } else if(returnOfBody.equals("noType") && !returnOfMethod.equals(TypeOp.Type.voidType.toString())){
                throw new Error("Missing return. \n\nVisitorSemantic - La funzione " + funName + "() non ha un valore di ritorno.\n");
            } else if (!returnOfBody.equals("noType")) {
                if(!returnOfMethod.equals(returnOfBody))
                    throw new Error("Wrong return type. \n\nVisitorSemantic - Problema con il valore di ritorno della funzione " + funName + "().\n" +
                            "- tipo di ritorno atteso: " + returnOfMethod + "\n" +
                            "- tipo di ritorno effettivo: " + returnOfBody + "\n");
            }

            //creo l'HashMap per salvare il nome della funzione
            Utility.returnFunctionType.put(funName, returnOfBody);
        }

        TypeEnvironment.removeScope();
        //ritorno il tipo di ritorno della funzione.
        return f.getType().toString();
    }

    /**
     * Gestisce il nodo FunCall.
     * FunCall -> ID LPAR ExprList RPAR
     * FunCall -> ID LPAR RPAR
     * ---
     * In questo caso dobbiamo controllare che:
     * 1. controllare che la funzione non esista già
     * 2. devo salvare la firma della funzione che servirà a FunCall nel momento in cui la chiama.
     * ------
     * per testare fare riferimento alla cartella test/methodFail
     * @param f
     * @return
     * @throws Exception
     */
    @Override
    public Object visit(FunCallOp f) throws Exception {
        //identificativo della funzione attuale
        String funName = f.getId().getLessema();

        //nel caso in cui ho FunCall -> ID LPAR RPAR
        int  paramNum_attuale = 0;

        //parametri della funzione attuale
        if(!Utility.isNull(f.getExpr())){
            ArrayList<ExprOp> exprOps = f.getExpr();
            paramNum_attuale = exprOps.size();
        }

        //se il nome della funzione esiste, faccio tutti i controlli
        if(TypeEnvironment.lookupBool(funName)){
            ElementMethod method = (ElementMethod) TypeEnvironment.lookup(funName);

            //prelevo il numero dei parametri dalla funzione nella tabella dei simboli
            assert method != null;
            int paramNum_TE = method.getParamsName().size();

            //controllo sul numero dei parametri della funzione
            if(paramNum_attuale != paramNum_TE){
                throw new Error("Wrong param number.\n\nVisitorSemantic. La funzione "+ funName +"() viene chiamata con un numero diverso di parametri:\n" +
                        "- numero di parametri attesi: "+paramNum_TE+"\n" +
                        "- numero di parametri con cui viene chiamata: "+paramNum_attuale+"\n\n");
            }

            //se hanno un numero uguale di parametri, controllo i tipi
            else{
                //controllo dei tipi
                if(!Utility.isNull(f.getExpr())) {
                    ArrayList<ExprOp> exprOps = f.getExpr();
                    //Collections.reverse(exprOps);
                    ArrayList<TypeOp> type_TE = method.getParamsType();
                    Collections.reverse(type_TE);

                    //salvo i tipi della funzione attuale in un nuovo array
                    ArrayList<String> tipi_attualeS = new ArrayList<>();
                    for(ExprOp e : exprOps) {
                        Object returnE = e.accept(this);
                        tipi_attualeS.add(returnE.toString());
                    }

                    ArrayList<String> tipi_TES = new ArrayList<>();
                    for(TypeOp t : type_TE)
                        tipi_TES.add(t.toString());

                    //rimpiazzo trueConst e falseConst con bool.
                    for(int i = 0; i<tipi_attualeS.size(); i++){
                        if(tipi_attualeS.get(i).equals(ConstOp.Type.trueConst.toString()) || tipi_attualeS.get(i).equals(ConstOp.Type.falseConst.toString())){
                            tipi_attualeS.set(i,TypeOp.Type.bool.toString());
                        }
                    }
                }
            }
            ElementMethod em = (ElementMethod) TypeEnvironment.lookup(f.getId().getLessema());
            return em.getReturnType();
        } else if(!TypeEnvironment.lookupBool(funName)) {
            if(!funNotDecl.contains(funName)) {
                funNotDecl.add(funName);
            }
        }

        return null;
    }

    /**
     * DeclOp gestisce i gestisce il nodo DeclList
     * con oggetti sia FunDeclOp che VarDeclOp.
     * @param d
     * @return
     */
    @Override
    public Object visit(DeclOp d) throws Exception {

        if(d.getFunDeclOp() != null)
            d.getFunDeclOp().accept(this);

        return TypeOp.Type.voidType.toString();
    }

    /**
     * (1) WhileStat -> WHILE Expr LOOP Body
     * @param w
     * @return
     */
    @Override
    public Object visit(WhileStatement w) throws Exception {

        String bodyType = TypeOp.Type.voidType.toString();

        if (w.isFirstVisit()) {
            w.setFirstVisit(false);

            TypeEnvironment.newScope();
            w.setSymbolTable(TypeEnvironment.getScope());

            Object exprReturn = w.getExpr().accept(this);
            String exprType = exprReturn.toString();

            if (!exprType.equals(TypeOp.Type.bool.toString()))
                throw new Exception("VisitorSemantic - TypeChecking_ERROR. Attenzione, il tipo della condizione del while deve essere booleano");

            Object bodyTypeReturn = w.getBody().accept(this);
            bodyType = bodyTypeReturn.toString();

            TypeEnvironment.removeScope();
        }
        return bodyType;
    }

    /**
     * MainFunDecl ::= MAIN FunDecl
     * @param m
     * @return
     */
    @Override
    public Object visit(MainOp m) throws Exception {

        if (m.isFirstVisit()) {
            m.setFirstVisit(false);

            //Il nuovo scope viene già creato da FunDeclOp!
            //TypeEnvironment.newScope();
            //m.setSymbolTable(TypeEnvironment.getScope());

            m.getFunDecl().accept(this);

            //TypeEnvironment.removeScope();
        }

        return TypeOp.Type.voidType.toString();
    }

    //nodi che NON devono gestire lo scope
    @Override
    public Object visit(AndOp andOp) throws Exception {
        CompatibilityTables checkType = new CompatibilityTables();
        CompatibilityTables.TypeOperation resultOperation;

        Object E1Return = andOp.getE1().accept(this);
        Object E2Return = andOp.getE2().accept(this);
        String typeE1 = E1Return.toString();
        String typeE2 = E2Return.toString();

        if(typeE1.equals(ConstOp.Type.character.toString()))
            typeE1 = ConstOp.Type.string.toString();
        if(typeE2.equals(ConstOp.Type.character.toString()))
            typeE2 = ConstOp.Type.string.toString();
        if(typeE1.equals(ConstOp.Type.trueConst.toString()) || typeE1.equals(ConstOp.Type.falseConst.toString()))
            typeE1 = ConstOp.Type.bool.toString();
        if(typeE2.equals(ConstOp.Type.trueConst.toString()) || typeE2.equals(ConstOp.Type.falseConst.toString()))
            typeE2 = ConstOp.Type.bool.toString();

        resultOperation = checkType.optype2(CompatibilityTables.OpType.logicalOperator, typeE1, typeE2);
        if (resultOperation.equals(CompatibilityTables.TypeOperation.erroType))
            throw new Exception("VisitorSemantic - TypeChecking_ERROR. Errore di compatibilita' fra i tipi in" + andOp.getE1() + " AND " +andOp.getE2());
        else{
            //setto il tipo dell'operazione corretto
            andOp.setTypeOperation(resultOperation);
        }
        return andOp.getTypeOperation().toString();
    }

    @Override
    public Object visit(DivOp divOp) throws Exception{
        CompatibilityTables checkType = new CompatibilityTables();
        CompatibilityTables.TypeOperation resultOperation;

        Object E1Return = divOp.getE1().accept(this);
        Object E2Return  = divOp.getE2().accept(this);
        String typeE1 = E1Return.toString();
        String typeE2 = E2Return.toString();

        resultOperation = checkType.optype2(CompatibilityTables.OpType.div, typeE1, typeE2);
        if (resultOperation.equals(CompatibilityTables.TypeOperation.erroType))
            throw new Exception("VisitorSemantic - TypeChecking_ERROR. Errore di compatibilita' fra i tipi in" + divOp.getE1() + " DIV " +divOp.getE2());
        else{
            //setto il tipo dell'operazione corretto
            divOp.setTypeOperation(resultOperation);
        }
        return divOp.getTypeOperation().toString();
    }

    @Override
    public Object visit(EqOp eqOp) throws Exception {
        CompatibilityTables checkType = new CompatibilityTables();
        CompatibilityTables.TypeOperation resultOperation;

        Object E1Return = eqOp.getE1().accept(this);
        Object E2Return = eqOp.getE2().accept(this);
        String typeE1 = E1Return.toString();
        String typeE2 = E2Return.toString();

        resultOperation = checkType.optype2(CompatibilityTables.OpType.comparisonOperation, typeE1, typeE2);
        if (resultOperation.equals(CompatibilityTables.TypeOperation.erroType))
            throw new Exception("VisitorSemantic - TypeChecking_ERROR. Errore di compatibilita' fra i tipi in " + typeE1 + " EQUAL " +typeE2);
        else{
            //setto il tipo dell'operazione corretto
            eqOp.setTypeOperation(resultOperation);
        }
        return eqOp.getTypeOperation().toString();
    }

    @Override
    public Object visit(GeOp geOp) throws Exception {
        CompatibilityTables checkType = new CompatibilityTables();
        CompatibilityTables.TypeOperation resultOperation;

        Object E1Return = geOp.getE1().accept(this);
        Object E2Return = geOp.getE2().accept(this);
        String typeE1 = E1Return.toString();
        String typeE2 = E2Return.toString();

        resultOperation = checkType.optype2(CompatibilityTables.OpType.comparisonOperation, typeE1, typeE2);
        if (resultOperation.equals(CompatibilityTables.TypeOperation.erroType))
            throw new Exception("VisitorSemantic - TypeChecking_ERROR. Errore di compatibilita' fra i tipi in" + geOp.getE1() + " GE " +geOp.getE2());
        else{
            //setto il tipo dell'operazione corretto
            geOp.setTypeOperation(resultOperation);
        }
        return geOp.getTypeOperation().toString();
    }

    @Override
    public Object visit(GtOp gtOp) throws Exception {
        CompatibilityTables checkType = new CompatibilityTables();
        CompatibilityTables.TypeOperation resultOperation;

        Object E1Return = gtOp.getE1().accept(this);
        Object E2Return = gtOp.getE2().accept(this);
        String typeE1 = E1Return.toString();
        String typeE2 = E2Return.toString();

        resultOperation = checkType.optype2(CompatibilityTables.OpType.comparisonOperation, typeE1, typeE2);
        if (resultOperation.equals(CompatibilityTables.TypeOperation.erroType))
            throw new Exception("VisitorSemantic - TypeChecking_ERROR. Errore di compatibilita' fra i tipi in" + gtOp.getE1() + " GT " +gtOp.getE2());
        else{
            //setto il tipo dell'operazione corretto
            gtOp.setTypeOperation(resultOperation);
        }
        return gtOp.getTypeOperation().toString();
    }

    @Override
    public Object visit(LeOp leOp) throws Exception {
        CompatibilityTables checkType = new CompatibilityTables();
        CompatibilityTables.TypeOperation resultOperation;

        Object E1Return = leOp.getE1().accept(this);
        Object E2Return = leOp.getE2().accept(this);
        String typeE1 = E1Return.toString();
        String typeE2 = E2Return.toString();

        if(typeE1.equals(ConstOp.Type.trueConst.toString()) || typeE1.equals(ConstOp.Type.falseConst.toString()))
            typeE1 = ConstOp.Type.bool.toString();
        if(typeE2.equals(ConstOp.Type.trueConst.toString()) || typeE2.equals(ConstOp.Type.falseConst.toString()))
            typeE2 = ConstOp.Type.bool.toString();

        resultOperation = checkType.optype2(CompatibilityTables.OpType.comparisonOperation, typeE1, typeE2);
        if (resultOperation.equals(CompatibilityTables.TypeOperation.erroType))
            throw new Exception("VisitorSemantic - TypeChecking_ERROR. Errore di compatibilita' fra i tipi in " + typeE1 + " LE " +typeE2);
        else{
            //setto il tipo dell'operazione corretto
            leOp.setTypeOperation(resultOperation);
        }
        return leOp.getTypeOperation().toString();
    }

    @Override
    public Object visit(LtOp ltOp) throws Exception {
        CompatibilityTables checkType = new CompatibilityTables();
        CompatibilityTables.TypeOperation resultOperation;

        Object E1Return = ltOp.getE1().accept(this);
        Object E2Return = ltOp.getE2().accept(this);

        String typeE1 = E1Return.toString();
        String typeE2 = E2Return.toString();

        resultOperation = checkType.optype2(CompatibilityTables.OpType.comparisonOperation, typeE1, typeE2);
        if (resultOperation.equals(CompatibilityTables.TypeOperation.erroType))
            throw new Exception("VisitorSemantic - TypeChecking_ERROR. Errore di compatibilita' fra i tipi in" + ltOp.getE1() + " EQUAL " +ltOp.getE2());
        else{
            //setto il tipo dell'operazione corretto
            ltOp.setTypeOperation(resultOperation);
        }
        return ltOp.getTypeOperation().toString();
    }

    @Override
    public Object visit(MinusOp minusOp) throws Exception{

        CompatibilityTables checkType = new CompatibilityTables();
        CompatibilityTables.TypeOperation resultOperation;

        //considero l'operazione binaria
        if(minusOp.getE1() != null && minusOp.getE2() != null){

            Object E1Return = minusOp.getE1().accept(this);
            Object E2Return = minusOp.getE2().accept(this);
            String typeE1 = E1Return.toString();
            String typeE2 = E2Return.toString();

            resultOperation = checkType.optype2(CompatibilityTables.OpType.arithmeticOperation, typeE1, typeE2);
            if (resultOperation.equals(CompatibilityTables.TypeOperation.erroType))
                throw new Exception("VisitorSemantic - TypeChecking_ERROR. Errore di compatibilita' fra i tipi in" + minusOp.getE1() + " BINARY MINUS " +minusOp.getE2());
            else{
                //setto il tipo dell'operazione corretto
                minusOp.setTypeOperation(resultOperation);
            }
            return minusOp.getTypeOperation().toString();
        }

        Object E1Return = minusOp.getE1().accept(this);
        String typeE1 = E1Return.toString();

        resultOperation = checkType.optype1(CompatibilityTables.OpType.unaryMinus, typeE1);
        if (resultOperation.equals(CompatibilityTables.TypeOperation.erroType))
            throw new Exception("VisitorSemantic - TypeChecking_ERROR. Errore di compatibilita' per unminus unario: " + minusOp.getE1());
        else{
            //setto il tipo dell'operazione corretto
            minusOp.setTypeOperation(resultOperation);
        }
        return minusOp.getTypeOperation().toString();
    }

    @Override
    public Object visit(NeOp neOp) throws Exception {
        CompatibilityTables checkType = new CompatibilityTables();
        CompatibilityTables.TypeOperation resultOperation;

        Object E1Return = neOp.getE1().accept(this);
        Object E2Return = neOp.getE2().accept(this);
        String typeE1 = E1Return.toString();
        String typeE2 = E2Return.toString();

        if(typeE1.equals(ConstOp.Type.trueConst.toString()) || typeE1.equals(ConstOp.Type.falseConst.toString()))
            typeE1 = ConstOp.Type.bool.toString();
        if(typeE2.equals(ConstOp.Type.trueConst.toString()) || typeE2.equals(ConstOp.Type.falseConst.toString()))
            typeE2 = ConstOp.Type.bool.toString();

        resultOperation = checkType.optype2(CompatibilityTables.OpType.comparisonOperation, typeE1, typeE2);
        if (resultOperation.equals(CompatibilityTables.TypeOperation.erroType))
            throw new Exception("VisitorSemantic - TypeChecking_ERROR. Errore di compatibilita' fra i tipi in" + neOp.getE1() + " NOT EQUAL " +neOp.getE2());
        else{
            //setto il tipo dell'operazione corretto
            neOp.setTypeOperation(resultOperation);
        }
        return neOp.getTypeOperation().toString();
    }

    @Override
    public Object visit(NotOp notOp) throws Exception {
        CompatibilityTables checkType = new CompatibilityTables();
        CompatibilityTables.TypeOperation resultOperation;

        Object E1Return = notOp.getE1().accept(this);
        String typeE1 = E1Return.toString();

        //trasformo trueConst e falseConst nel tipo bool
        if(typeE1.equals(ConstOp.Type.trueConst.toString()) || typeE1.equals(ConstOp.Type.falseConst.toString()))
            typeE1 = ConstOp.Type.bool.toString();

        resultOperation = checkType.optype1(CompatibilityTables.OpType.unaryNot, typeE1);

        if (resultOperation.equals(CompatibilityTables.TypeOperation.erroType))
            throw new Exception("VisitorSemantic - TypeChecking_ERROR. Errore di compatibilita' per not unario: " + notOp.getE1());
        else{
            //setto il tipo dell'operazione corretto
            notOp.setTypeOperation(resultOperation);
        }
        return notOp.getTypeOperation().toString();
    }

    @Override
    public Object visit(Operation operation) throws Exception {
        if(operation instanceof EqOp){
            EqOp eq = null;
            eq.accept(this);
        }

        if(operation instanceof GeOp) {
            GeOp geOp = null;
            geOp.accept(this);
        }

        if(operation instanceof GtOp){
            GtOp gtOp = null;
            gtOp.accept(this);
        }

        if(operation instanceof LeOp){
            LeOp leOp = null;
            leOp.accept(this);
        }

        if (operation instanceof LtOp){
            LtOp ltOp = null;
            ltOp.accept(this);
        }

        if(operation instanceof MinusOp){
            MinusOp minusOp = null;
            minusOp.accept(this);
        }

        if(operation instanceof NeOp){
            NeOp neOp = null;
            neOp.accept(this);
        }

        if(operation instanceof OrOp){
            OrOp orOp = null;
            orOp.accept(this);
        }

        if (operation instanceof AndOp){
            AndOp andOp = null;
            andOp.accept(this);
        }

        if(operation instanceof DivOp){
            DivOp divOp = null;
            divOp.accept(this);
        }

        if (operation instanceof PlusOp) {
            PlusOp plusOp = null;
            plusOp.accept(this);
        }

        if(operation instanceof PowOp){
            PowOp powOp = null;
            powOp.accept(this);
        }

        if(operation instanceof StringConcatOp){
            StringConcatOp stringConcatOp = null;
            stringConcatOp.accept(this);
        }

        if(operation instanceof TimesOp){
            TimesOp timesOp = null;
            timesOp.accept(this);
        }

        return operation.getTypeOperation();
    }


    @Override
    public Object visit(OrOp orOp) throws Exception{
        CompatibilityTables checkType = new CompatibilityTables();
        CompatibilityTables.TypeOperation resultOperation;

        Object E1Return = orOp.getE1().accept(this);
        Object E2Return = orOp.getE2().accept(this);
        String typeE1 = E1Return.toString();
        String typeE2 = E2Return.toString();

        if(typeE1.equals(ConstOp.Type.character.toString()))
            typeE1 = ConstOp.Type.string.toString();
        if(typeE2.equals(ConstOp.Type.character.toString()))
            typeE2 = ConstOp.Type.string.toString();
        if(typeE1.equals(ConstOp.Type.trueConst.toString()) || typeE1.equals(ConstOp.Type.falseConst.toString()))
            typeE1 = ConstOp.Type.bool.toString();
        if(typeE2.equals(ConstOp.Type.trueConst.toString()) || typeE2.equals(ConstOp.Type.falseConst.toString()))
            typeE2 = ConstOp.Type.bool.toString();

        resultOperation = checkType.optype2(CompatibilityTables.OpType.logicalOperator, typeE1, typeE2);
        if (resultOperation.equals(CompatibilityTables.TypeOperation.erroType))
            throw new Exception("VisitorSemantic - TypeChecking_ERROR. Errore di compatibilita' fra i tipi in" + orOp.getE1() + " OR " +orOp.getE2());
        else{
            //setto il tipo dell'operazione corretto
            orOp.setTypeOperation(resultOperation);
        }
        return orOp.getTypeOperation().toString();
    }

    @Override
    public Object visit(PlusOp plusOp) throws Exception{
        CompatibilityTables checkType = new CompatibilityTables();
        CompatibilityTables.TypeOperation resultOperation;
        plusOp.setTypeOperation(CompatibilityTables.TypeOperation.integer);

        Object E1Return = plusOp.getE1().accept(this);
        Object E2Return = plusOp.getE2().accept(this);

        String typeE1 = E1Return.toString();
        String typeE2 = E2Return.toString();

        resultOperation = checkType.optype2(CompatibilityTables.OpType.arithmeticOperation, typeE1, typeE2);
        if (resultOperation.equals(CompatibilityTables.TypeOperation.erroType))
            throw new Exception("VisitorSemantic - TypeChecking_ERROR.\n" +
                    "Errore di compatibilita' fra i tipi in\n" +
                    "- tipo 1: " + typeE1+ "\n" +
                    "- operazione: PLUS \n" +
                    "- tipo 2: " + typeE2);
        else{
            //setto il tipo dell'operazione corretto
            plusOp.setTypeOperation(resultOperation);
        }

        return plusOp.getTypeOperation().toString();
    }

    @Override
    public Object visit(PowOp powOp) throws Exception {
        CompatibilityTables checkType = new CompatibilityTables();
        CompatibilityTables.TypeOperation resultOperation;

        Object E1Return = powOp.getE1().accept(this);
        Object E2Return = powOp.getE2().accept(this);
        String typeE1 = E1Return.toString();
        String typeE2 = E2Return.toString();

        resultOperation = checkType.optype2(CompatibilityTables.OpType.arithmeticOperation, typeE1, typeE2);
        if (resultOperation.equals(CompatibilityTables.TypeOperation.erroType))
            throw new Exception("VisitorSemantic - TypeChecking_ERROR. Errore di compatibilita' fra i tipi in" + powOp.getE1() + " POW " +powOp.getE2());
        else{
            //setto il tipo dell'operazione corretto
            powOp.setTypeOperation(resultOperation);
        }
        return powOp.getTypeOperation().toString();
    }

    @Override
    public Object visit(StringConcatOp stringConcatOp) throws Exception {
        CompatibilityTables checkType = new CompatibilityTables();
        CompatibilityTables.TypeOperation resultOperation;

        Object E1Return = stringConcatOp.getE1().accept(this);
        Object E2Return = stringConcatOp.getE2().accept(this);
        String typeE1 = E1Return.toString();
        String typeE2 = E2Return.toString();

        if(typeE1.equals(ConstOp.Type.character.toString()))
            typeE1 = ConstOp.Type.string.toString();
        if(typeE2.equals(ConstOp.Type.character.toString()))
            typeE2 = ConstOp.Type.string.toString();


        resultOperation = checkType.optype2(CompatibilityTables.OpType.stringConcat, typeE1, typeE2);

        if (resultOperation.equals(CompatibilityTables.TypeOperation.erroType))
            throw new Exception("\n\nVisitorSemantic - TypeChecking_ERROR.\nErrore di compatibilita' fra i tipi:\n" +
                    "- primo tipo: " + typeE1 + " \n" +
                    "- operazione: STRING_CONCAT \n" +
                    "- secondo tipo: " +typeE2 + "\n");
        else{
            //setto il tipo dell'operazione corretto
            stringConcatOp.setTypeOperation(resultOperation);
        }
        return stringConcatOp.getTypeOperation().toString();
    }

    @Override
    public Object visit(TimesOp timesOp) throws Exception{
        CompatibilityTables checkType = new CompatibilityTables();
        CompatibilityTables.TypeOperation resultOperation;

        Object E1Return = timesOp.getE1().accept(this);
        Object E2Return = timesOp.getE2().accept(this);
        String typeE1 = E1Return.toString();
        String typeE2 = E2Return.toString();

        resultOperation = checkType.optype2(CompatibilityTables.OpType.arithmeticOperation, typeE1, typeE2);
        if (resultOperation.equals(CompatibilityTables.TypeOperation.erroType))
            throw new Exception("VisitorSemantic - TypeChecking_ERROR.\n" +
                    "Errore di compatibilita' fra i tipi in" + timesOp.getE1() + " TIMESOP " +timesOp.getE2());
        else{
            //setto il tipo dell'operazione corretto
            timesOp.setTypeOperation(resultOperation);
        }
        return timesOp.getTypeOperation().toString();
    }

    /**
     * Posso avere una assegnazione fra:
     * 1. lista id << espressoni : il ritorno in questo caso è il tipo di exprop
     *      - controllo del numero di variabili.
     * 2. id << costante : in questo caso il valore di ritorno è constop
     * 3. id << espressione : in questo caso il valore di ritorno è quello di exprop
     * @param a
     * @return
     * @throws Exception
     */
    @Override
    public Object visit(AssignStatement a) throws Exception {

        //ArrayList<Identifier> << ArrayList<ExprOp>
        if(!Utility.isNull(a.getArrayListId()) && !Utility.isNull(a.getExpr())){

            //recupero i valori
            ArrayList<Identifier> identifiers = a.getArrayListId();
            ArrayList<ExprOp> exprOps = a.getExpr();

            //controllo se il numero di elementi alla dx e alla sx è diverso
            if(identifiers.size() != exprOps.size())
                    throw new Error("VisitorSemantic - ERROR.\n Il numero di elementi assegnati e' diverso dal numero delle variabili:\n" +
                            "- numero variabili: " +identifiers.size() + "\n" +
                            "- numero valori: "+exprOps.size());

            for(Identifier i : identifiers){
                for(ExprOp e : exprOps){

                    //se l'Identifier non esiste nel TE allora non è stato dichiarato.
                    if(!TypeEnvironment.getScope().containElement(i.getLessema())){
                        i.accept(this);
                    }
                    //se esiste la variabile allora la recupero dallo scope
                    else{
                        ElementVar varFromScope = (ElementVar) TypeEnvironment.getScope().getElement(i.getLessema());
                        if(e.getConstType() != null){
                            if(!varFromScope.getType().equals(e.getConstType().toString()))
                                throw new Error("VisitorSemantic - ERROR.\nLa variabile "+i.getLessema()+" ha un tipo diverso \n" +
                                        "- tipo atteso: " + varFromScope.getType() +"\n" +
                                        "- tipo effettivo: "+ e.getConstType());
                        }
                    }
                    //accept delle espressioni
                    return e.accept(this);
                }
            }
        }

        //Identifier << ConstOp
        if(a.getId() != null && a.getConst() != null){

            //prelevo i valori
            a.getId().accept(this);

            ConstOp.Type constType = a.getConst().getType();

            //se esiste la variabile allora la recupero dallo scope
            if(TypeEnvironment.getScope().containElement(a.getId().getLessema())){
                ElementVar varFromScope = (ElementVar) TypeEnvironment.getScope().getElement(a.getId().getLessema());
                if(!varFromScope.getType().equals(constType.toString()))
                    throw new Error("VisitorSemantic - ERROR.\nLa variabile "+ a.getId().getLessema() +" ha un tipo diverso.");
            }

            return constType;
        }

        //Identifier << ExprOp
        if(a.getId() != null && a.getE() != null){
            //prelevo i valori
            String identifier = a.getId().getLessema();

            Object returnE = a.getE().accept(this);
            String type = returnE.toString();

            //se esiste la variabile allora la recupero dallo scope
            if(TypeEnvironment.getScope().containElement(identifier)){
                ElementVar varFromScope = (ElementVar) TypeEnvironment.getScope().getElement(identifier);
                if(!varFromScope.getType().equals(type))
                    throw new Error("VisitorSemantic - ERROR.\nLa variabile "+identifier+" ha un tipo diverso.");
            }

            return type;
        }

        throw new Error("VisitorSemantic - ERROR.\n Qualcosa nell'assegnazione " + a + "e' andato storto.");
    }

    /**
     * (1) ReadStat -> IdList READ STRING_CONST
     * (2) ReadStat -> IdList READ
     * @param r
     * @return
     */
    @Override
    public Object visit(ReadStatement r) throws Exception {

        if(!Utility.isNull(r.getIdList())){
            for (Identifier id : r.getIdList()) {
                Element e = TypeEnvironment.lookup(id.getLessema());
                if (e == null)
                    throw new Exception("VisitorSemantic - ERROR. Nessuna dichiarazione della variabile - " + id.getLessema() + " - impossibile l'assegnazione di valore");
                else{
                    if(e.getKindElement().equals(Element.KindElement.var))
                        return VarDeclOp.Var.var;
                }
            }
        }
        return TypeOp.Type.voidType.toString();
    }

    @Override
    public Object visit(ReturnOp r) throws Exception {
        if(r.getExprOp() == null)
            return TypeOp.Type.voidType.toString();
        return r.getExprOp().accept(this);
    }

    @Override
    public Object visit(Statement s) throws Exception {

        return switch (s.getType()) {
            case ifStm -> ((IfStatement) s.getNode()).accept(this);
            case forStm -> ((ForStatement) s.getNode()).accept(this);
            case readStm -> ((ReadStatement) s.getNode()).accept(this);
            case writeStm -> ((WriteStatement) s.getNode()).accept(this);
            case assignStm -> ((AssignStatement) s.getNode()).accept(this);
            case whileStm -> ((WhileStatement) s.getNode()).accept(this);
            case funCall -> ((FunCallOp) s.getNode()).accept(this);
            case returnStm -> ((ReturnOp) s.getNode()).accept(this);
            default -> throw new Exception("VisitorSemantic - TypeChecking_ERROR. Errore di tipo in uno statement. ");
        };

    }

    @Override
    public Object visit(WriteStatement w) throws Exception {

        if(Utility.isNull(w.getExprList()))
            throw new Exception("VisitorSemantic - ERROR. Errore nella scrittura della write");

        for(ExprOp e : w.getExprList())
            e.accept(this);

        return TypeOp.Type.voidType.toString();
    }


    @Override
    public Object visit(ConstOp c) {
        return c.getType().toString();
    }

    /** Classe che gestisce:
     * ConstOp constOpType;
     * Identifier id;
     * Operation operation;
     * FunCallOp funCallOp;
     * @param e
     * @return
     */
    @Override
    public Object visit(ExprOp e) throws Exception {
        if(e.getOperation() != null)
            return e.getOperation().accept(this);
        if(e.getId() != null)
            return e.getId().accept(this);
        if(e.getConstType() != null)
            return e.getConstType().accept(this);
        if(e.getFunCallOp() != null)
            return e.getFunCallOp().accept(this);
        return "ERROR";
    }

    @Override
    public Object visit(Identifier i) throws Exception {
        //controllo se l'identificatore esiste già nelle tabelle dei simboli attive
        Element e = TypeEnvironment.lookup(i.getLessema());

        //controllo se l'elemento da aggiungere è una variabile
        if(e != null) {
            if (e.getKindElement().equals(Element.KindElement.var)) {
                ElementVar var = (ElementVar) e;

                switch (((ElementVar) e).getType()) {
                    case "integer" -> i.setIdType(new TypeOp(TypeOp.Type.integer));
                    case "real" -> i.setIdType(new TypeOp(TypeOp.Type.real));
                    case "string" -> i.setIdType(new TypeOp(TypeOp.Type.string));
                    case "character" -> i.setIdType(new TypeOp(TypeOp.Type.character));
                    case "bool" -> i.setIdType(new TypeOp(TypeOp.Type.bool));
                    default -> i.setIdType(new TypeOp(TypeOp.Type.voidType));
                }

                return var.getType();
            } else if (e.getKindElement().equals(Element.KindElement.method)) {
                //forzo e a ElementMethod e mi prendo il tipo di ritorno
                return ((ElementMethod) e).getReturnType();
            } else throw new Exception("VisitorSemantic - ERROR. L'id non e' specificato nella maniera corretta");
        } else {
            //se non viene trovato allora si tenta di usare id senza che questo sia definito
            if (!varNotDeclAndType.containsKey(i.getLessema())) {
                varNotDeclAndType.put(i.getLessema(), null);
                return i.getLessema();
            }
        }
        return null;
    }

    /**
     * id << valore
     * @param i
     * @return
     */
    @Override
    public Object visit(IdInitObblOp i) throws Exception{
        //scoping
        if (TypeEnvironment.getScope().containElement(i.getAssignStat().getId().getLessema()))
            throw new Error("VisitorSemantic - Scoping ERROR. L'identificativo" + i.getAssignStat().getId().getLessema() + " e' stato gia' dichiarato ");
        else {
            Object assignReturn = i.getAssignStat().accept(this);
            String type = assignReturn.toString();

            TypeEnvironment.addElement(new ElementVar(i.getAssignStat().getId().getLessema(), type, false));
            //TODO: si può evitare l'accept di id nell'assign? Se si rimuoverlo dall'assegnazione tra una id e una cost e
            //prendere solo il lessema con la get

            varNotDeclAndType.remove(i.getAssignStat().getId().getLessema());

            switch (type){
                case "integer"-> i.getAssignStat().getId().setIdType(new TypeOp(TypeOp.Type.integer));
                case "real"-> i.getAssignStat().getId().setIdType(new TypeOp(TypeOp.Type.real));
                case "string"-> i.getAssignStat().getId().setIdType(new TypeOp(TypeOp.Type.string));
                case "character"-> i.getAssignStat().getId().setIdType(new TypeOp(TypeOp.Type.character));
                case "bool"-> i.getAssignStat().getId().setIdType(new TypeOp(TypeOp.Type.bool));
                default -> i.getAssignStat().getId().setIdType(new TypeOp(TypeOp.Type.voidType));
            }
        }

        return TypeOp.Type.voidType.toString();
    }

    /**
     *
     * @param i
     * @return
     * @throws Exception
     */
    @Override
    public Object visit(IdInitOp i) throws Exception {
        String name = i.getId().getLessema();

        if (TypeEnvironment.getScope().containElement(name))
            throw new Error("VisitorSemantic - Scoping ERROR. Esiste gia' un id con lo stesso nome in questo scope - " + name);

        String type = "";
        Object typeReturn = null;

        if (!Utility.isNull(i.getAssignment().getExpr())){
            for(ExprOp e : i.getAssignment().getExpr()){
                typeReturn = e.accept(this);
                type = typeReturn.toString();
            }
        }

        if(i.getAssignment().getE()!=null){
            typeReturn = i.getAssignment().getE().accept(this);
            type = typeReturn.toString();
        }

        TypeEnvironment.addElement(new ElementVar(name, type, false));

        switch (type){
            case "integer"-> i.getId().setIdType(new TypeOp(TypeOp.Type.integer));
            case "real"-> i.getId().setIdType(new TypeOp(TypeOp.Type.real));
            case "string"-> i.getId().setIdType(new TypeOp(TypeOp.Type.string));
            case "character"-> i.getId().setIdType(new TypeOp(TypeOp.Type.character));
            case "bool"-> i.getId().setIdType(new TypeOp(TypeOp.Type.bool));
            default -> i.getId().setIdType(new TypeOp(TypeOp.Type.voidType));
        }
        return TypeOp.Type.voidType.toString();
    }

    @Override
    public Object visit(TypeOp typeOp) throws IOException {
        return typeOp.accept(this);
    }

    /**
     * (1) VarDecl -> Type IdInitList SEMI
     * (2) VarDecl -> VAR IdInitObblList SEMI
     * @param v
     * @return
     */
    @Override
    public Object visit(VarDeclOp v) throws Exception {

        //VarDecl -> Type IdInitList SEMI
        if(!Utility.isNull(v.getIdInitOpList())){
            //prelevo la lista di Identificativi
            ArrayList<IdInitOp> idInitOps = v.getIdInitOpList();
            String typeVar = v.getType().toString();

            //elemento da aggiungere allo scope corrente se tutti i controlli vanno a buon fine
            ElementVar elementVar;
            //valori da passare a ElementVar per istanziarlo:
            String identifierName;

            for(IdInitOp i : idInitOps){
                if(i.getId() != null)
                    i.getId().setIdType(v.getType());

                //se è una istanza di assegnazione
                if(i.getAssignment() != null){
                    i.getAssignment().getId().setIdType(v.getType());

                    //prelevo i valori
                    identifierName = i.getAssignment().getId().getLessema();

                    //valore di ritorno di AssignStat
                    String returnTypeA = (String) i.getAssignment().accept(this);

                    //se i tipi sono uguali
                    if(returnTypeA.equals(ConstOp.Type.falseConst.toString()) || returnTypeA.equals(ConstOp.Type.trueConst.toString()) )
                        returnTypeA = TypeOp.Type.bool.toString();

                    if(returnTypeA.equals(typeVar)){
                        elementVar = new ElementVar(identifierName, typeVar,false);
                        TypeEnvironment.getScope().addVar(elementVar);
                        varNotDeclAndType.remove(identifierName);
                    } else if(returnTypeA.equals(TypeOp.Type.integer.toString()) && typeVar.equals(TypeOp.Type.real.toString())){
                        elementVar = new ElementVar(identifierName, typeVar,false);
                        TypeEnvironment.getScope().addVar(elementVar);
                        varNotDeclAndType.remove(identifierName);
                    } else if(!returnTypeA.equals(TypeOp.Type.integer.toString()) && !returnTypeA.equals(TypeOp.Type.real.toString()) && !returnTypeA.equals(TypeOp.Type.string.toString())){
                        varNotDeclAndType.put(returnTypeA, typeVar);
                    }
                    else{
                        throw new Error("VisitorSemantic - ERROR.\nSi sta tentando di fare l'assegnazione fra due tipi differenti:\n" +
                                "- tipo1: " + typeVar + "\n" +
                                "- tipo2: " + returnTypeA);
                    }
                }
                //se è un identificativo
                else{
                    String nomeVar = i.getId().getLessema();

                    //controllo se NON è stato già dichiarato IN QUESTO SCOPE e lo aggiungo
                    if(!TypeEnvironment.getScope().containElement(nomeVar)) {

                        //potrebbe essere stata dichiarata nello scope globale
                        if(TypeEnvironment.getMainScope().containElement(nomeVar)){
                            throw new Error("VisitorSemantic - Error.\nLa variabile \""+nomeVar+"\" è stata già dichiarata nello scope globale");
                        }

                        ElementVar var = new ElementVar(nomeVar, typeVar, false);
                        TypeEnvironment.getScope().addVar(var);

                        if (varNotDeclAndType.containsKey(nomeVar)) {
                            if (varNotDeclAndType.get(nomeVar).equals(typeVar))
                                varNotDeclAndType.remove(nomeVar);
                            else
                                throw new Error("VisitorSemantic - ERROR.\nSi sta tentando di fare l'assegnazione fra due tipi differenti:\n" +
                                        "- tipo1: " + typeVar + "\n" +
                                        "- tipo2: " + varNotDeclAndType.get(nomeVar));
                        }

                    }
                    //se è gia stato dichiarato in questo SCOPE Errore
                    else {
                        throw new Exception("VisitorSemantic - ERROR. \n La variabile "+ nomeVar + " e' gia' dichiarata nello scope corrente.");
                    }
                }
            }

            return TypeOp.Type.values();
        }

        //VarDecl -> VAR IdInitObblList SEMI
        if(!Utility.isNull(v.getIdInitObblOpList())){
            ArrayList<IdInitObblOp> idInitObblOps = v.getIdInitObblOpList();

            //se entro in questa produzione, sono sicura che il tipo sia VAR
            String typeVar = VarDeclOp.Var.var.toString();

            //elemento da aggiungere allo scope corrente se tutti i controlli vanno a buon fine
            ElementVar elementVar;
            //valori da passare a ElementVar per istanziarlo:
            String identifierName;

            for(IdInitObblOp i : idInitObblOps){

                //se è una istanza di assegnazione
                if(i.getAssignStat() != null) {
                    //prelevo i valori
                    identifierName = i.getAssignStat().getId().getLessema();

                    Object returnTypeAss = i.getAssignStat().accept(this);
                    String returnTypeA = returnTypeAss.toString();

                    elementVar = new ElementVar(identifierName, returnTypeA, false);
                    TypeEnvironment.getScope().addVar(elementVar);
                    varNotDeclAndType.remove(identifierName);

                    if (varNotDeclAndType.containsKey(identifierName)) {
                        if(varNotDeclAndType.get(identifierName) != null)
                            if (varNotDeclAndType.get(identifierName).equals(returnTypeA))
                                varNotDeclAndType.remove(identifierName);
                            else
                                throw new Error("VisitorSemantic - ERROR.\nSi sta tentando di fare l'assegnazione fra due tipi differenti:\n" +
                                        "- tipo1: " + typeVar + "\n" +
                                        "- tipo2: " + varNotDeclAndType.get(identifierName));
                            }
                    }
                //se è un identificativo
                else{
                    String nomeVar = i.getAssignStat().getId().getLessema();
                    //controllo se NON è stato già dichiarato IN QUESTO SCOPE e lo aggiungo
                    if(!TypeEnvironment.getScope().containElement(nomeVar)) {
                        ElementVar var = new ElementVar(nomeVar, typeVar, false);
                        TypeEnvironment.getScope().addVar(var);

                        if (varNotDeclAndType.containsKey(nomeVar)) {
                            if (varNotDeclAndType.get(nomeVar).equals(typeVar))
                                varNotDeclAndType.remove(nomeVar);
                            else
                                throw new Error("VisitorSemantic - ERROR.\nSi sta tentando di fare l'assegnazione fra due tipi differenti:\n" +
                                        "- tipo1: " + typeVar + "\n" +
                                        "- tipo2: " + varNotDeclAndType.get(nomeVar));
                        }
                    }
                    //se è gia stato dichiarato in questo SCOPE Errore
                    else {
                        throw new Exception("VisitorSemantic - ERROR. \n La variabile "+ nomeVar + " e' gia' dichiarata nello scope corrente.");
                    }
                }
            }
            return VarDeclOp.Var.var.toString();
        }

        return 0;
    }

}

