package main.visitor;

import main.visitor.utility.Utility;
import nodi.*;
import nodi.operation.*;
import nodi.statement.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class CGenerator implements Visitor {
    private static HashMap<String, Stack<TypeOp>> typeContext;

    private static ArrayList<String> varDeclared = new ArrayList<>();

    private static int parDecl_Counter = -1;

    private static int nConcat = 1; //per evitare variabili con lo stesso nome.

    //per controllare quando scrivere nella stampa della funzione
    private static boolean writeConcat_;

    private static ArrayList<Integer> isOuts = new ArrayList<>();

    private static File FILE;
    private static FileWriter WRITER;

    public CGenerator(String path) throws IOException {

        // Controlla se e' specificata una cartella nel path
        int lastSlash = path.lastIndexOf('/');
        if (lastSlash != -1) {
            String folderPath = path.substring(0, lastSlash);
            File directory = new File(folderPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }
        }

        FILE = new File(path);
        if (!FILE.exists()) {
            FILE.createNewFile();
        }
        WRITER = new FileWriter(FILE);

        CGenerator.typeContext = new HashMap<>();
        CGenerator.writeConcat_=true;

        //inserimento import necessari per c
        write("#include <stdio.h>\n");
        write("#include <stdlib.h>\n");
        write("#include <math.h>\n");
        write("#include <stdbool.h>\n");
        write("#include <string.h>\n\n");
        write("#define MAXDIM 100\n\n");

    }

    @Override
    public Object visit(AndOp andOp) throws Exception {
        ExprOp e1 = andOp.getE1();
        ExprOp e2 = andOp.getE2();

        String op = "&&";

        e1.accept(this);
        write(op);
        e2.accept(this);

        return null;
    }

    @Override
    public Object visit(DivOp divOp) throws Exception {
        ExprOp e1 = divOp.getE1();
        ExprOp e2 = divOp.getE2();

        String op = "/";

        e1.accept(this);
        write(op);
        e2.accept(this);

        return null;
    }

    @Override
    public Object visit(EqOp eqOp) throws Exception {
        ExprOp e1 = eqOp.getE1();
        ExprOp e2 = eqOp.getE2();

        String op = "==";

        Object type = "";
        Object type2 = "";

        //while(a = b)  while(a = "a") while("a" = "a") ...
        if(e1.getId() != null) {
            if(CGenerator.typeContext.containsKey(e1.getId().getLessema()))
                type = CGenerator.typeContext.get(e1.getId().getLessema()).peek().getType();
        }

        if(e1.getConstType() != null) {
            if(e1.getConstType().getType().equals(ConstOp.Type.string))
                type = e1.getConstType().getType();
        }

        if(e2.getId() != null) {
            if(CGenerator.typeContext.containsKey(e2.getId().getLessema()))
                type2 = CGenerator.typeContext.get(e2.getId().getLessema()).peek().getType();
        }

        if(e2.getConstType() != null) {
            if(e2.getConstType().getType().equals(ConstOp.Type.string))
                type2 = e2.getConstType().getType();
        }

        if(type.equals(TypeOp.Type.string) &&  type2.equals(ConstOp.Type.string)){
            write("strcmp(");
            e1.accept(this);
            write(", ");

            if (e2.getConstType().getType().equals(ConstOp.Type.string))
                write("\"");

            e2.accept(this);

            if (e2.getConstType().getType().equals(ConstOp.Type.string))
                write("\"");

            write(") == 0");
        } else {
            e1.accept(this);
            write(op);

            if (e2.getConstType().getType().equals(ConstOp.Type.string))
                write("\"");

            e2.accept(this);

            if (e2.getConstType().getType().equals(ConstOp.Type.string))
                write("\"");
        }

        return null;
    }

    @Override
    public Object visit(GeOp geOp) throws Exception {
        ExprOp e1 = geOp.getE1();
        ExprOp e2 = geOp.getE2();

        String op = ">=";

        e1.accept(this);
        write(op);
        e2.accept(this);

        return null;
    }

    @Override
    public Object visit(GtOp gtOp) throws Exception {
        ExprOp e1 = gtOp.getE1();
        ExprOp e2 = gtOp.getE2();

        String op = ">";

        e1.accept(this);
        write(op);
        e2.accept(this);

        return null;
    }

    @Override
    public Object visit(LeOp leOp) throws Exception {
        ExprOp e1 = leOp.getE1();
        ExprOp e2 = leOp.getE2();

        String op = "<=";

        e1.accept(this);
        write(op);
        e2.accept(this);

        return null;
    }

    @Override
    public Object visit(LtOp ltOp) throws Exception {
        ExprOp e1 = ltOp.getE1();
        ExprOp e2 = ltOp.getE2();

        String op = "<";

        e1.accept(this);
        write(op);
        e2.accept(this);

        return null;
    }

    @Override
    public Object visit(MinusOp minusOp) throws Exception {
        ExprOp e1 = minusOp.getE1();
        String op = "-";
        if(minusOp.getE2() == null) {
            write(op);
            e1.accept(this);
        } else {
            ExprOp e2 = minusOp.getE2();
            e1.accept(this);
            write(op);
            e2.accept(this);
        }

        return null;
    }

    @Override
    public Object visit(NeOp neOp) throws Exception {
        ExprOp e1 = neOp.getE1();
        ExprOp e2 = neOp.getE2();

        String op = "!=";

        e1.accept(this);
        write(op);
        e2.accept(this);

        return null;
    }

    @Override
    public Object visit(NotOp notOp) throws Exception {
        ExprOp e1 = notOp.getE1();
        String op = "!";

        write(op);
        e1.accept(this);

        return null;
    }

    //MIO
    @Override
    public Object visit(Operation operation) throws Exception {
        operation.accept(this);
        return null;
    }

    @Override
    public Object visit(OrOp orOp) throws Exception {
        ExprOp e1 = orOp.getE1();
        ExprOp e2 = orOp.getE2();

        String op = "||";

        e1.accept(this);
        write(op);
        e2.accept(this);

        return null;
    }

    @Override
    public Object visit(PlusOp plusOp) throws Exception {
        ExprOp e1 = plusOp.getE1();
        ExprOp e2 = plusOp.getE2();

        String op = "+";

        e1.accept(this);
        write(op);
        e2.accept(this);

        return null;
    }

    @Override
    public Object visit(PowOp powOp) throws Exception {
        ExprOp e1 = powOp.getE1();
        ExprOp e2 = powOp.getE2();

        write("pow(");
        e1.accept(this);
        write(",");
        e2.accept(this);
        write(")");

        return null;
    }

    /**
     *
     * @param stringConcatOp
     * @return
     * @throws Exception
     */
    @Override
    public Object visit(StringConcatOp stringConcatOp) throws Exception {
        //prelevo e1 ed e2
        ExprOp e1 = stringConcatOp.getE1();
        ExprOp e2 = stringConcatOp.getE2();

        ConstOp.Type constE1 = null, constE2 = null;
        Identifier lessemaE1 = null, lessemaE2 = null;
        FunCallOp funE1 = null, funE2 = null;
        Operation opE1 = null, opE2 = null;

        if(e1.getConstType() != null)
            constE1 = e1.getConstType().getType();
        if(e2.getConstType() != null)
            constE2 = e2.getConstType().getType();

        if(e1.getId() != null)
            lessemaE1 = e1.getId();
        if(e2.getId() != null)
            lessemaE2 = e2.getId();

        if(e1.getFunCallOp() != null)
            funE1 = e1.getFunCallOp();
        if(e2.getFunCallOp() != null)
            funE2 = e2.getFunCallOp();

        if(e1.getOperation() != null)
                opE1 = e1.getOperation();
        if(e2.getOperation() != null)
                opE2 = e2.getOperation();

        if(constE1 != null) {

            //se il primo è stringa
            if (constE1.equals(ConstOp.Type.string)) {
                if(constE2 != null) {
                    CGenerator.writeConcat_=true;
                    switch (constE2) {
                        // STRINGA - NUMERO
                        case trueConst, falseConst, integer -> {
                            write("\nchar concat_" + nConcat + "[MAXDIM];\n");
                            write("char buffer_bis_" + nConcat + "[MAXDIM] = \"");
                            e1.accept(this);

                            //so già che è %d perchè sono nel caso true, false, integer.
                            write("%d");

                            write("\\n\";\nsprintf(concat_" + nConcat + ",buffer_bis_" + nConcat);

                            write(",");
                            e2.accept(this);
                            write(");\n\n");
                        }
                        case real -> {
                            write("\nchar concat_" + nConcat + "[MAXDIM];\n");
                            write("char buffer_bis_" + nConcat + "[MAXDIM] = \"");
                            e1.accept(this);

                            //so già che è %f perchè sono nel caso real
                            write("%f");

                            write("\\n\";\nsprintf(concat_" + nConcat + ",buffer_bis_" + nConcat);

                            write(",");
                            e2.accept(this);
                            write(");\n\n");
                        }
                        //STRINGA - STRINGA
                        case string -> {
                            write("\nchar concat_"+nConcat+ "[MAXDIM] = \"");
                            e1.accept(this);
                            write("\";\n");

                            write("strcat(concat_"+nConcat+", \"");
                            e2.accept(this);
                            write("\"");
                            write(");\n\n");
                        }
                        //STRINGA - CARATTERE
                        case character -> {
                            write("\nchar concat_" + nConcat + "[MAXDIM] = \"");
                            e1.accept(this);
                            write(" ");
                            e2.accept(this);
                            write("\";\n");
                        }
                    }
                }

                //STRINGA - VARIABILE
                if (lessemaE2 != null) {
                    CGenerator.writeConcat_=true;
                    write("char concat_" + nConcat + "[MAXDIM];\n");
                    write("char buffer_bis_" + nConcat + "[MAXDIM] = \"");
                    e1.accept(this);
                    switch (lessemaE2.getIdType().getType()) {
                        case integer, bool -> write(" %d");
                        case real -> write(" %f");
                        case character -> write(" %c");
                    }

                    write("\\n\";\nsprintf(concat_" + nConcat + ",buffer_bis_" + nConcat);

                    write(",");
                    e2.accept(this);
                    write(");\n");
                }

                //TODO: <----------- STRINGA - CHIAMATA A FUNZIONE
                if (funE2 != null) {
                    CGenerator.writeConcat_=true;
                    //salvo il valore di ritorno della chiamata a funzione in una variabile
                    //concateno il valore della stringa a quello della chiamata a funzione
                    throw new Exception("C'e' stato un errore durante la generazione del codice C!\nNon è ancora possibile fare una concatenazione utilizzando il tipo di ritorno di una funzione\n" +
                            "Salva il valore di ritorno della funzione "+ funE2.getId().getLessema() + "() in una variabile prima di fare la concatenazione!\n\n");
                }

                //STRINGA - OPERAZIONE
                if (opE2 != null) {
                    CGenerator.writeConcat_=true;

                    if(e2.getOperation().getE1().getConstType() != null) {
                        write("char concat_" + nConcat + "[MAXDIM];\n");
                        write("char buffer_bis_" + nConcat + "[MAXDIM] = \"");
                        e1.accept(this);

                        write(" %f\\n\";\nfloat ");

                        write("v_tmp_" + nConcat + "= " + e2.getOperation().getE1().getConstType().getConstValue());

                        switch (e2.getOperation().getClass().toString()) {
                            case "class nodi.operation.PlusOp" -> write(" + ");
                            case "class nodi.operation.MinusOp" -> write(" - ");
                            case "class nodi.operation.TimesOp" -> write(" * ");
                            case "class nodi.operation.DivOp" -> write(" / ");
                            case "class nodi.operation.PowOp" -> write(" ^ ");
                        }

                        write(e2.getOperation().getE2().getConstType().getConstValue() + ";\n");

                        write("sprintf(concat_" + nConcat + ",buffer_bis_" + nConcat + ", v_tmp_" + nConcat);
                        write(");\n");
                    }
                    //caso in cui ho ad e2 un'altra concatenazione
                    else if(e2.getOperation().getClass().toString().equals("class nodi.operation.StringConcatOp")){
                        nConcat++;
                        CGenerator.writeConcat_ = false;
                        visit(e2.getOperation());
                    }
                }
            }
        }

        if(constE2 != null){
            //se il secondo è stringa
            if(constE2.equals(ConstOp.Type.string)){
                CGenerator.writeConcat_=true;

                //NUMERO - STRINGA ... CARATTERE - STRINGA
                if(constE1 != null && !constE1.equals(ConstOp.Type.string)) {
                    write("\nchar concat_" + nConcat + "[MAXDIM] = \"");
                    e1.accept(this);
                    write(" ");
                    e2.accept(this);
                    write("\";\n");

                }

                //VARIABILE - STRINGA
                if(lessemaE1 != null){
                    write("char concat_" + nConcat + "[MAXDIM];\n");
                    write("strcpy(concat_"+nConcat+",");
                    e1.accept(this);
                    write(");\n");

                    write("strcat(concat_"+nConcat+",\"");
                    e2.accept(this);
                    write("\");\n");
                }

                //TODO: <----------- CHIAMATA A FUNZIONE - STRINGA
                if(funE1 != null){
                    throw new Exception("C'e' stato un errore durante la generazione del codice C!\nNon è ancora possibile fare una concatenazione utilizzando il tipo di ritorno di una funzione\n" +
                            "Salva il valore di ritorno della funzione "+ funE2.getId().getLessema() + "() in una variabile prima di fare la concatenazione!\n\n");
                }

                //OPERAZIONE - STRINGA
                if(opE1 != null){

                    //se e1 è una operazione aritmetica
                    if(e1.getOperation().getE1().getConstType() != null) {
                        write("char concat_" + nConcat + "[MAXDIM];\n");

                        write("float v_tmp_" + nConcat + "= " + e1.getOperation().getE1().getConstType().getConstValue());

                        switch (e1.getOperation().getClass().toString()){
                            case "class nodi.operation.PlusOp" -> write(" + ");
                            case "class nodi.operation.MinusOp" -> write(" - ");
                            case "class nodi.operation.TimesOp" -> write(" * ");
                            case "class nodi.operation.DivOp" -> write(" / ");
                            case "class nodi.operation.PowOp" -> write(" ^ ");
                        }

                        if(e1.getOperation().getE2().getConstType() != null)
                            write(e1.getOperation().getE2().getConstType().getConstValue()+";\n");

                        write("sprintf(concat_" + nConcat + ",\"%f %s\", v_tmp_"+nConcat+",\"");
                        e2.accept(this);
                        write("\");\n");
                    }
                    //caso in cui ho ad e1 un'altra concatenazione
                    else if(e1.getOperation().getClass().toString().equals("class nodi.operation.StringConcatOp")){
                        nConcat++;
                        CGenerator.writeConcat_ = false;
                        visit(e1.getOperation());
                    }


                }
            }
        }

        if(lessemaE1 != null ){
            CGenerator.writeConcat_=true;
            if(lessemaE1.getIdType().getType().equals(TypeOp.Type.string)){

                if(constE2 != null) {
                    switch (constE2) {
                        //VARIABILE_STRINGA - NUMERO
                        case trueConst, falseConst, integer -> {

                            write("char concat_" + nConcat + "[MAXDIM];\n");

                            write("sprintf(concat_" + nConcat + ",\"%s %d\",");
                            e1.accept(this);
                            write(",");
                            e2.accept(this);
                            write(");\n");
                        }
                        case real -> {
                            write("char concat_" + nConcat + "[MAXDIM];\n");
                            write("char buffer_bis_" + nConcat + "[MAXDIM] = \"");
                            e1.accept(this);

                            //già so che è %f
                            write(" %f\\n\";\nsprintf(concat_" + nConcat + ",buffer_bis_" + nConcat);

                            write(",");
                            e2.accept(this);
                            write(");\n");
                        }
                        //VARIABILE_STRINGA - CARATTERE
                        case character -> {
                            write("char concat_" + nConcat + "[MAXDIM];\n");
                            write("char buffer_bis_" + nConcat + "[MAXDIM] = \"");
                            e1.accept(this);

                            //già so che è un carattere, quindi %c
                            write(" %c\\n\";\nsprintf(concat_" + nConcat + ",buffer_bis_" + nConcat);

                            write(", '");
                            e2.accept(this);
                            write("');\n");
                        }
                    }
                }

                //VARIABILE_STRINGA - VARIABILE
                if (lessemaE2 != null) {

                    TypeOp.Type typee2 = lessemaE2.getIdType().getType();

                    write("char concat_" + nConcat + "[MAXDIM];\n");

                    //già so che è una stringa quindi metto %s
                    write("\nsprintf(concat_" + nConcat);
                    write(",\"%s ");

                    switch (typee2){
                        case string -> write("%s\",");
                        case integer,bool -> write("%d\",");
                        case character -> write("%c\",");
                        case real -> write("%f\",");
                    }
                    e1.accept(this);
                    write(", ");
                    e2.accept(this);
                    write(");\n");
                }

                //TODO: <----------- VARIABILE_STRINGA - CHIAMATA A FUNZIONE
                if (funE2 != null) {
                    throw new Exception("C'e' stato un errore durante la generazione del codice C!\nNon è ancora possibile fare una concatenazione utilizzando il tipo di ritorno di una funzione\n" +
                            "Salva il valore di ritorno della funzione "+ funE2.getId().getLessema() + "() in una variabile prima di fare la concatenazione!\n\n");
                }

                //VARIABILE_STRINGA - OPERAZIONE
                if (opE2 != null) {
                    if( e2.getOperation().getE1().getConstType() != null){
                        write("char concat_" + nConcat + "[MAXDIM];\n");

                        write("float v_tmp_" + nConcat + "= " + e2.getOperation().getE1().getConstType().getConstValue());
                        switch (e2.getOperation().getClass().toString()) {
                            case "class nodi.operation.PlusOp" -> write(" + ");
                            case "class nodi.operation.MinusOp" -> write(" - ");
                            case "class nodi.operation.TimesOp" -> write(" * ");
                            case "class nodi.operation.DivOp" -> write(" / ");
                            case "class nodi.operation.PowOp" -> write(" ^ ");
                        }
                        write(e2.getOperation().getE2().getConstType().getConstValue() + ";\n");

                        write("sprintf(concat_" + nConcat + ",\"%s %f\",");
                        e1.accept(this);
                        write(",v_tmp_" + nConcat + ");\n");
                    }
                    //caso in cui ho ad e1 un'altra concatenazione
                    else if(e2.getOperation().getClass().toString().equals("class nodi.operation.StringConcatOp")){
                        nConcat++;
                        CGenerator.writeConcat_ = false;
                        visit(e2.getOperation());
                    }
                }
            }
        }

        if(lessemaE2 != null ){
            if(lessemaE2.getIdType().getType().equals(TypeOp.Type.string)){

                if(constE1 != null) {
                    CGenerator.writeConcat_=true;
                    switch (constE1) {

                        //NUMERO - VARIABILE_STRINGA
                        case trueConst, falseConst, integer -> {

                            write("char concat_" + nConcat + "[MAXDIM];\n");

                            //già so che è una stringa quindi metto %s
                            write("\nsprintf(concat_" + nConcat);
                            write(",\"%d %s\",");

                            e1.accept(this);
                            write(", ");
                            e2.accept(this);
                            write(");\n");
                        }

                        //NUMERO REALE - VARIABILE_STRINGA
                        case real -> {

                            write("char concat_" + nConcat + "[MAXDIM];\n");

                            //già so che è una stringa quindi metto %s
                            write("\nsprintf(concat_" + nConcat);
                            write(",\"%f %s\",");

                            e1.accept(this);
                            write(", ");
                            e2.accept(this);
                            write(");\n");
                        }

                        //STRINGA - VARIABILE_STRINGA
                        case string -> {
                            write("char concat_"+nConcat+ " [MAXDIM] = \"");
                            e1.accept(this);
                            write("\";\n");

                            write("strcat(concat_"+nConcat+", ");
                            e2.accept(this);
                            write(");\n");
                        }

                        //CARATTERE - VARIABILE_STRINGA
                        case character -> {
                            write("char concat_" + nConcat + "[MAXDIM];\n");

                            //già so che è una stringa quindi metto %s
                            write("\nsprintf(concat_" + nConcat);
                            write(",\"%c %s\",");

                            write("'");
                            e1.accept(this);
                            write("'");
                            write(", ");
                            e2.accept(this);
                            write(");\n");
                        }
                    }
                }

                //VARIABILE - VARIABILE_STRINGA
                if(lessemaE1 != null){
                    CGenerator.writeConcat_=true;
                    write("char concat_" + nConcat + "[MAXDIM];\n");

                    TypeOp.Type type1 = lessemaE1.getIdType().getType();

                    write("sprintf(concat_" + nConcat+", \"");
                    switch (type1){
                        case string -> write("%s");
                        case integer,bool -> write("%d");
                        case character -> write("%c");
                        case real -> write("%f");
                    }
                    write(" %s\",");
                    e1.accept(this);
                    write(",");
                    e2.accept(this);
                    write(");\n");

                }

                //TODO: <----------- CHIAMATA A FUNZIONE - VARIABILE_STRINGA
                if(funE1 != null){
                    CGenerator.writeConcat_=true;
                    throw new Exception("C'e' stato un errore durante la generazione del codice C!\nNon è ancora possibile fare una concatenazione utilizzando il tipo di ritorno di una funzione\n" +
                            "Salva il valore di ritorno della funzione "+ funE1.getId().getLessema() + "() in una variabile prima di fare la concatenazione!\n\n");
                }

                //OPERAZIONE - VARIABILE_STRINGA
                if(opE1 != null){
                    CGenerator.writeConcat_=true;
                    write("char concat_" + nConcat + "[MAXDIM];\n");

                    //caso in cui ho una operazione aritmetica
                    if(e1.getOperation().getE1().getConstType() != null) {
                        write("float v_tmp_" + nConcat + "= " + e1.getOperation().getE1().getConstType().getConstValue());
                        switch (e1.getOperation().getClass().toString()) {
                            case "class nodi.operation.PlusOp" -> write(" + ");
                            case "class nodi.operation.MinusOp" -> write(" - ");
                            case "class nodi.operation.TimesOp" -> write(" * ");
                            case "class nodi.operation.DivOp" -> write(" / ");
                            case "class nodi.operation.PowOp" -> write(" ^ ");
                        }

                        write(e1.getOperation().getE2().getConstType().getConstValue() + ";\n");

                        write("sprintf(concat_" + nConcat + ",\"%f %s\", v_tmp_" + nConcat + ",");
                        e2.accept(this);
                        write(");\n");
                    }
                    //caso in cui ho ancora una concatenazione
                    else if (e1.getOperation().getClass().toString().equals("class nodi.operation.StringConcatOp")){
                        nConcat++;
                        CGenerator.writeConcat_ = false;
                        visit(e1.getOperation());
                    }


                }

            }
        }
        //la printf viene fatta direttamente in WriteStatement
        return null;
    }

    @Override
    public Object visit(TimesOp timesOp) throws Exception {
        ExprOp e1 = timesOp.getE1();
        ExprOp e2 = timesOp.getE2();

        String op = "*";

        e1.accept(this);
        write(op);
        e2.accept(this);

        return null;
    }

    /**
     * AssignStat -> IdList ASSIGN ExprList
     * @param as
     * @return
     * @throws Exception
     */
    @Override
    public Object visit(AssignStatement as) throws Exception {

        //AssignStat -> IdList ASSIGN ExprList
        if(!Utility.isNull(as.getArrayListId()) && !Utility.isNull(as.getExpr())){
            ArrayList<Identifier> idList = as.getArrayListId();
            ArrayList<ExprOp> exprList = as.getExpr();

            TypeOp type = null;

            //scorro l'array di identifier (è uguale a quello di exprList)
            for (int i = 0; i < idList.size(); i++) {

                String lessemaId = idList.get(i).getLessema();

                if(CGenerator.typeContext.get(lessemaId) != null)
                    type = CGenerator.typeContext.get(lessemaId).peek();

                if (type != null && type.toString().equals("string")) {
                    /* * * * * * * * * * * * * * * * * * * * *
                     * [CASO D'USO]: (stringa<-) id = id     *
                     * es. result=a;                         *
                     * es. *size = valore;                   *
                     * * * * * * * * * * * * * * * * * * * * */
                    write("*");
                    if(exprList.get(i).getId() != null)
                        idList.get(i).accept(this);

                    write(" = ");
                    if(exprList.get(i).getId() != null)
                        write(exprList.get(i).getId().getLessema()+";");
                    write("\n");

                } else {
                    /* * * * * * * * * * * * * * * * * * * * *
                     * [CASO D'USO]: id = exprList/expr      *
                     * es. result=a+b+c+d;                   *
                     * es. risultato=sommac(a,x,b,&taglia);  *
                     * * * * * * * * * * * * * * * * * * * * */
                    idList.get(i).accept(this);
                    write("=");
                    exprList.get(i).accept(this);
                    write(";\n");
                }
            }
        }

        //(1) ForStat -> FOR ID ASSIGN INTEGER_CONST TO INTEGER_CONST LOOP Body
        //(2) IdInitObblList -> ID ASSIGN Const
        if(as.getId() != null && as.getConst() != null){
            //salvo il numero di caratteri del valore da inserire nella stringa
            int numC = as.getConst().getConstValue().toString().length()+1;

            if(as.getConst().getType().toString().equals("string")){
                /* * * * * * * * * * * * * * * * * * * * *
                 * [CASO D'USO]: char *id ="const"       *
                 * es. char valore="grande";            *
                 * es. char ans="no";                   *
                 * * * * * * * * * * * * * * * * * * * * */

                as.getId().accept(this);
                write("["+numC+"]=");
                //se la costante è una stringa allora inserisco le " (in apertura)
                if (as.getConst().getType().equals(ConstOp.Type.string))
                    write("\"");
                else
                    //se la costante è un char allora inserisco le ' (in apertura)
                    if (as.getConst().getType().equals(ConstOp.Type.character))
                        write("\'");

                as.getConst().accept(this);

                //inserisco le virgolette in chiusura
                //se la costante è una stringa allora inserisco le " (in chiusura)
                if (as.getConst().getType().equals(ConstOp.Type.string))
                    write("\"");

                //se la costante è un char allora inserisco le ' (in chiusura)
                if (as.getConst().getType().equals(ConstOp.Type.character))
                    write("\'");
            } else {
                /* * * * * * * * * * * * * * * * * * * * *
                 * [CASO D'USO]: type id = const         *
                 * es. int x=3;                          *
                 * es. float b=2.2                       *
                 * * * * * * * * * * * * * * * * * * * * */
                as.getId().accept(this);
                write("=");
                as.getConst().accept(this);
            }
        }

        // IdInitList -> ID ASSIGN Expr
        if(as.getId() != null && as.getE() != null){
            as.getId().accept(this);
            write("=");

            //stampo parentesi prima e dopo
            if(as.getId().getIdType().getType().equals(TypeOp.Type.string))
                write("\"");
            if(as.getId().getIdType().getType().equals(TypeOp.Type.character))
                write("\'");

            as.getE().accept(this);

            //stampo parentesi prima e dopo
            if(as.getId().getIdType().getType().equals(TypeOp.Type.string))
                write("\"");
            if(as.getId().getIdType().getType().equals(TypeOp.Type.character))
                write("\'");
        }
        return null;
    }

    @Override
    public Object visit(ForStatement f) throws Exception {

        //prelevo il valore di inizio e fine.
        int start = Integer.parseInt(f.getAssign().getConst().getConstValue().toString());
        int end = Integer.parseInt(f.getIntegerConst().getConstValue().toString());

        //prelevo il valore del lessema
        String lessema = f.getAssign().getId().getLessema();

        //setto il tipo del lessema del for a intero (sicuramente è intero)
        if(!CGenerator.typeContext.containsKey(lessema)){
            CGenerator.typeContext.put(lessema, new Stack<>());
        }
        CGenerator.typeContext.get(lessema).push(new TypeOp(TypeOp.Type.integer));

        write("for (int " + f.getAssign().getId().getLessema() + " = " + f.getAssign().getConst().getConstValue().toString());
        write(";" + f.getAssign().getId().getLessema() + " != " +  f.getIntegerConst().getConstValue().toString() + " ;");
        write(f.getAssign().getId().getLessema());
        write(
                (start < end) ? "++" : "--"
        );
            write("){\n");

            f.getBody().accept(this);

            write("}\n");
        return null;
    }

    @Override
    public Object visit(IfStatement i) throws Exception {
        write("if (");
        i.getExpr().accept(this);
        write("){\n");

        i.getBody1().accept(this);

        if(i.getBody2() != null) {
            write("} else {\n");
            i.getBody2().accept(this);
        }

        write("}\n");
        return null;
    }

    @Override
    public Object visit(ReadStatement r) throws Exception {
        if (r.getStringConst() != null) {
            write("printf(\"" + r.getStringConst().getConstValue() + "\");\n");
        }

        ArrayList<Identifier> list = r.getIdList();
        Collections.reverse(list);
        for (Identifier id : list) {
            write("scanf(\"");
            if(CGenerator.typeContext.containsKey(id.getLessema())){
                if(CGenerator.typeContext.get(id.getLessema()).peek().getType().equals(TypeOp.Type.integer)) {
                    write("%d\", &");
                }

                if(CGenerator.typeContext.get(id.getLessema()).peek().getType().equals(TypeOp.Type.real)){
                   write("%f\", &");
                }

                if(CGenerator.typeContext.get(id.getLessema()).peek().getType().equals(TypeOp.Type.string)){
                    write("%s\", ");
                }

                if(CGenerator.typeContext.get(id.getLessema()).peek().getType().equals(TypeOp.Type.character)){
                    write("%c\", &");
                }
            }
            id.accept(this);
            write(");\n");
        }
        return null;
    }

    @Override
    public Object visit(ReturnOp returnOp) throws Exception {
        if (returnOp.getExprOp() != null) {
            write("return ");
           returnOp.getExprOp().accept(this);
           write(";\n");
        }
        return null;
    }

    @Override
    public Object visit(Statement s) throws Exception {
        switch (s.getType()) {
            case ifStm -> ((IfStatement) s.getNode()).accept(this);
            case forStm -> ((ForStatement) s.getNode()).accept(this);
            case readStm -> ((ReadStatement) s.getNode()).accept(this);
            case writeStm -> ((WriteStatement) s.getNode()).accept(this);
            case assignStm -> ((AssignStatement) s.getNode()).accept(this);
            case whileStm -> ((WhileStatement) s.getNode()).accept(this);
            case returnStm -> ((ReturnOp) s.getNode()).accept(this);
            case funCall -> {
                ((FunCallOp) s.getNode()).accept(this);
                write(";\n");
            }
            default -> throw new Exception("VisitorSemantic - TypeChecking_ERROR. Errore di tipo in uno statement. ");
        };

        return null;
    }

    @Override
    public Object visit(WhileStatement w) throws Exception {
        write("while(");
        w.getExpr().accept(this);
        write("){\n");
        w.getBody().accept(this);

        write("}\n");

        return null;
    }

    /**
     * Il formato in C per una write (printf) è il seguente:
     * printf("format string", argument1, argument2, ...);
     * Per "format string" possiamo avere i valori:
     * - %d  se intero
     * - %f  se float
     * - %s  se string
     * - %c  se char
     * ----------------
     * WriteStat -> LPAR ExprList RPAR WRITE    (-->)
     * WriteStat -> LPAR ExprList RPAR WRITELN  (-->!)
     *
     * @param w
     * @return
     * @throws Exception
     */
    @Override
    public Object visit(WriteStatement w) throws Exception {

        //variabile necessaria per capire quando finisce un something_CONST
        boolean flag = false;

        /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  *
         * Variabile necessaria per vedere gli id che ci sono dopo la virgola                     *
         * [CASO D'USO in NewLang]. ("Il risultato dell'operazione scelta è :%-", risultato) -->! *
         * Devo prendere nota di tutti gli id che ci sono dopo il STRING_CONST (la virgola)       *
         * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * **/
        ArrayList<Identifier> ids = new ArrayList<>();

        for (ExprOp e : w.getExprList()) {
            if (e.getId() != null) {
                ids.add(e.getId());
            }
        }

        for (ExprOp e : w.getExprList()) {

            if (e.getOperation() != null && e.getOperation().getClass().toString().equals("class nodi.operation.StringConcatOp")) {
                flag = true;
                e.getOperation().accept(this);
                write("\n");
                write("printf(\"%s\", concat_"+nConcat+");\n");
            }

        }

        if(!flag){
            // In questo caso NON ho operazione di concatenazione.
            write("printf(\"");

            for (ExprOp e : w.getExprList()) {

                //se ho un id lo devo stampare con il % in base al tipo
                if (e.getId() != null && e.getConstType() == null) {
                    if (e.getId().getIdType() != null) {
                        switch (e.getId().getIdType().getType()) {
                            case integer, bool -> write("%d");
                            case real -> write("%f");
                            case string -> write("%s");
                            case character -> write("%c");
                        }

                    }
                } else if (e.getConstType() != null) {
                    e.getConstType().accept(this);

                } else if (e.getOperation() != null) {
                    switch (e.getOperation().getTypeOperation()) {
                        case integer, bool -> write("%d");
                        case real -> write("%f");
                        case string -> write("%s");
                        //l'operazione non ritorna un char.
                    }
                } else if (e.getFunCallOp() != null) {
                    if (Utility.getReturnFunctionType().containsKey(e.getFunCallOp().getId().getLessema())) {
                        String funName = e.getFunCallOp().getId().getLessema();
                        switch (Utility.getReturnFunctionType().get(funName)) {
                            case "integer" -> write("%d");
                            case "real" -> write("%f");
                            case "string" -> write("%s");
                            case "character" -> write("%c");
                        }
                    }
                }

            }

            if (w.getMode().equals(WriteStatement.Mode.writeln))
                write(" \\n");

            if (w.getExprList() != null)
                write("\"");


            //ripercorro i passi precedenti per capire se inserire i valori
            for (ExprOp e : w.getExprList()) {
                if (e.getOperation() != null && e.getOperation().getClass().toString().equals("class nodi.operation.StringConcatOp")) {
                    return null;
                }
                //se ho un id devo inserire i valori di id dopo la write.
                if (e.getId() != null) {
                    if (e.getId().getIdType() != null) {
                        write(", " + e.getId().getLessema());
                    }
                }
                //se ho una chiamata a funzione inserisco la firma dopo la write
                if (e.getFunCallOp() != null) {
                    write(", ");
                    write(e.getFunCallOp().getId().getLessema());
                    write("(");
                    for (int i = 0; i < e.getFunCallOp().getExpr().size(); i++) {

                        write(e.getFunCallOp().getExpr().get(i).getId().getLessema());
                        //se non è l'identificativo in ultima posizione metto la virgola
                        if (i != e.getFunCallOp().getExpr().size() - 1)
                            write(", ");
                    }
                    write(")");
                }

                if (e.getOperation() != null) {
                    write(", ");
                    e.getOperation().accept(this);
                }
            }

            write(");\n");
        }

        return null;
    }

    @Override
    public Object visit(BodyOp bodyOp) throws Exception {

        //stampa tutte le dichiarazioni di variabili
        if(!Utility.isNull(bodyOp.getVarDeclOps())){
            for (VarDeclOp v : bodyOp.getVarDeclOps()){
                v.accept(this);
            }
        }

        //stampa tutti gli statement (while, for, assign ...)
         if(!Utility.isNull(bodyOp.getStatList())){
             for (Statement s : bodyOp.getStatList()){
                 s.accept(this);
             }
         }

        if(!Utility.isNull(bodyOp.getVarDeclOps())){
            for (VarDeclOp v : bodyOp.getVarDeclOps()){
                String lessema = "";
                if(v.getIdInitOpList() != null) {
                    for (IdInitOp idInitOp : v.getIdInitOpList()) {
                        if (idInitOp.getId() != null) {
                            lessema = idInitOp.getId().getLessema();
                        } else if (idInitOp.getAssignment() != null) {
                            lessema = idInitOp.getAssignment().getId().getLessema();
                        }
                        CGenerator.typeContext.get(lessema).pop();
                    }
                } else if(v.getIdInitObblOpList() != null){
                    for (IdInitObblOp idInitObblOp : v.getIdInitObblOpList()) {
                        if(idInitObblOp.getAssignStat() != null){
                            lessema = idInitObblOp.getAssignStat().getId().getLessema();
                        }
                    }
                    CGenerator.typeContext.get(lessema).pop();
                }
            }
        }

        return null;
    }

    @Override
    public Object visit(ConstOp constOp) throws IOException {
        if(constOp.getType().toString().equals("trueConst")) {
            write("true");
        } else if(constOp.getType().toString().equals("falseConst")) {
            write("false");
        }else {
            write(constOp.getConstValue().toString());
        }
        //ritorno il tipo di ConsOp
        return constOp.getType();
    }

    @Override
    public Object visit(DeclOp declOp) throws Exception {
        if(declOp.getFunDeclOp() != null){
            declOp.getFunDeclOp().accept(this);
        }

        if(declOp.getVarDeclOp() != null){
            declOp.getVarDeclOp().accept(this);
        }

       return null;
    }

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
        return null;
    }

    @Override
    public Object visit(FunDeclOp funDeclOp) throws Exception {

        //stampo in C il tipo di ritorno della funzione
        funDeclOp.getType().accept(this);

        //stampo in C l'identificativo della funzione
        funDeclOp.getId().accept(this);

        //inizio a scrivere i parametri della funzione
        write("(");

        if(!Utility.isNull(funDeclOp.getParDeclList())) {

            ArrayList<ParDeclOp> paramReverse = funDeclOp.getParDeclList();
            Collections.reverse(paramReverse);

            int counterParams = 0;
            for (ParDeclOp p : paramReverse) {
                counterParams++;
                //i parametri della funzione vengono scritti, separati dalla virgola
                p.accept(this);
                if (paramReverse.indexOf(p) < paramReverse.size() - 1)
                    write(",");
            }
        }

        write(") {\n");
        //viene stampato il corpo delle funzioni N.B. quello del main è gestito a parte.
        funDeclOp.getBody().accept(this);
        write("}\n");
        parDecl_Counter = -1;
        return null;
    }

    /**
     * La dichiarazione della funzione in C è qualcosa del tipo
     * nomeFunzione(p1,p2,...,pn);
     * --------
     * per i parametri, questi possono essere Expr:
     * - booleani (true o false);
     * - stringhe "..."
     * - char '...'
     *
     * @param funCallOp
     * @return
     * @throws Exception
     */
    @Override
    public Object visit(FunCallOp funCallOp) throws Exception {

        int counter = -1;
        boolean flag = true;

        //controllo se il parametro di una funzione è una concatenazione di stringhe
        if(!Utility.isNull(funCallOp.getExpr())){
            for(ExprOp e : funCallOp.getExpr()){
                //se il parametro di una funzione è una concatenazione di stringhe
                if(e.getOperation() != null)
                    if(e.getOperation().getClass().toString().equals("class nodi.operation.StringConcatOp"))
                        e.getOperation().accept(this);
            }
        }

        //stampa in C il nome della funzione chiamata
        if(writeConcat_) {
            funCallOp.getId().accept(this);
            write("(");

            if (!Utility.isNull(funCallOp.getExpr())) {
                for (int i = 0; i < funCallOp.getExpr().size(); i++) {
                    counter++;

                    //se il parametro è un char devo mettere le virgolette
                    Object o = funCallOp.getExpr().get(i).getConstType();
                    if (o != null) {
                        if (funCallOp.getExpr().get(i).getConstType().getType().equals(ConstOp.Type.character))
                            write("\'");

                        //se il parametro è un string devo mettere le virgolette
                        if (funCallOp.getExpr().get(i).getConstType().getType().equals(ConstOp.Type.string))
                            write("\"");
                    }

                    for (Integer value : isOuts)
                        if (value == counter)
                            write("&");


                    //controllo se sono concatenazioni di stringhe - in quel caso non posso passare direttamente l'accept di e.
                    if (funCallOp.getExpr().get(i).getOperation() != null) {

                        if (funCallOp.getExpr().get(i).getOperation().getClass().toString().equals("class nodi.operation.StringConcatOp")) {
                            flag = false;
                            write("concat_" + nConcat);
                            nConcat++;
                        }
                    }

                    if (flag) {
                        funCallOp.getExpr().get(i).accept(this);
                        if (i != funCallOp.getExpr().size() - 1)
                            write(",");
                    }

                    //se il parametro è un char devo mettere le virgolette
                    if (o != null) {
                        if (funCallOp.getExpr().get(i).getConstType().getType().equals(ConstOp.Type.character))
                            write("\'");

                        //se il parametro è un string devo mettere le virgolette
                        if (funCallOp.getExpr().get(i).getConstType().getType().equals(ConstOp.Type.string))
                            write("\"");
                    }

                }
            }

            //non stampo ;\n perchè se ho un return con funzioni non stampa correttamente il C
            write(")");

        }


        return null;
    }

    @Override
    public Object visit(Identifier identifier) throws Exception {
        write(identifier.getLessema());
        return identifier;
    }

    /**
     * (1) IdInitObblList -> ID ASSIGN Const
     * (2) IdInitObblList -> IdInitObblList COMMA ID ASSIGN Const
     * @param i
     * @return
     * @throws Exception
     */
    @SuppressWarnings("ThrowableNotThrown")
    @Override
    public Object visit(IdInitObblOp i) throws Exception {

        //prelevo il tipo di IdInitObblOp dall'assegnazione
        ConstOp.Type type = i.getAssignStat().getConst().getType();

        //salvo il lessema per il metodo setLessemaAndType
        String lessema = i.getAssignStat().getId().getLessema();

        //la variabile non è stata già dichiarata in precedenza, scrivo il tipo
        if(!varDeclared.contains(lessema)) {
            if(!CGenerator.typeContext.containsKey(lessema)){
                CGenerator.typeContext.put(lessema, new Stack<>());
            }

            //stampo in C il tipo effettivo e setto con il metodo setLessemaAndType il tipo per produzioni IdInitObblList -> ID ASSIGN Const
            switch (type) {
                case character -> {
                    write("char ");
                    CGenerator.typeContext.get(lessema).push(new TypeOp(TypeOp.Type.character));
                }
                case integer -> {
                    write("int ");
                    CGenerator.typeContext.get(lessema).push(new TypeOp(TypeOp.Type.integer));
                }
                case real -> {
                    write("float ");
                    CGenerator.typeContext.get(lessema).push(new TypeOp(TypeOp.Type.real));
                }
                case string -> {
                    write("char ");
                    CGenerator.typeContext.get(lessema).push(new TypeOp(TypeOp.Type.string));
                }
                case trueConst, falseConst -> {
                    write("int ");
                    CGenerator.typeContext.get(lessema).push(new TypeOp(TypeOp.Type.bool));
                }
                default -> new Exception("[Visitor IdInitObblOp] ERRORE NEI TIPI");
            }
        }

        //stampo lessema << valore (=> lessema = valore;) assegnato alla costante var
        i.getAssignStat().accept(this);

        write(";\n");

        return null;
    }

    /**
     * IdInitList -> ID
     * IdInitList -> IdInitList COMMA ID
     * IdInitList -> ID ASSIGN Expr
     * IdInitList -> IdInitList COMMA ID ASSIGN Expr
     * @param i
     * @return
     * @throws Exception
     */
    @Override
    public Object visit(IdInitOp i) throws Exception {
        String lessema = i.getId().getLessema();
       if(i.getAssignment() == null){ //caso in cui NON ho l'assegnazione [integer i;]
           i.getId().accept(this);
           if(i.getId().getIdType().getType().equals(TypeOp.Type.string))
               write("="+lessema+"_tmp");
       } else { //caso in cui ho assignment [integer i << 1;]
           i.getAssignment().accept(this);
       }
        return null;
    }

    /**
     * Nel nostro linguaggio NewLang la funzione main può essere richiamata, in C no.
     * Si deve creare una funzione a parte per il main in modo tale da permettere
     * che questa possa essere richiamata.
     * @param mainOp
     * @return
     * @throws Exception
     */
    @Override
    public Object visit(MainOp mainOp) throws Exception {

        String mainId = mainOp.getFunDecl().getId().getLessema();
        ArrayList<ParDeclOp> params = mainOp.getFunDecl().getParDeclList();
        String returnType = Utility.getTypeString(mainOp.getFunDecl().getType());

        //se il nome del main è divero da main creo una nuova funzione con un nome diverso dal main per
        //permettere le chiamate da altre funzioni.
        if(!mainId.equals("main")){
            write("\n\n/* ------------------------------------------ */\n");
            write("/* Copia della funzione main che può essere   */\n");
            write("/* richiamata anche da altre funzioni.        */\n");
            write("/* ------------------------------------------ */\n");

            write(returnType + " " + mainId +"(");

            //se l'array list di parametri non è vuoto allora stampo anche i parametri della funzione
            if(!Utility.isNull(params)){
                for(ParDeclOp p : params)
                    p.accept(this);
            } else write("void");
            write("){\n");
            mainOp.getFunDecl().getBody().accept(this);
            write("}");

            write("\n\n/* ------------------------------------------ */\n");
            write("/*               MAIN FUNCTION                */\n");
            write("/* ------------------------------------------ */\n");
            write("int main(void){\n");
            if(!Utility.isNull(params)){
                for(ParDeclOp p : params){
                    if(p.getType().getType().equals(TypeOp.Type.string)){
                        write("char ");
                        for(Identifier i : p.getIdList()){
                            write(i.getLessema()+"_tmp[MAXDIM]=\"\";\n");
                            p.accept(this);
                            write(" = "+i.getLessema()+"_tmp");
                        }
                    }
                    write(";\n");
                }
            }
            mainOp.getFunDecl().getBody().accept(this);

        } else if(Utility.isNull(params)){
            write("\n\n/* ------------------------------------------ */\n");
            write("/* Copia della funzione main che può essere   */\n");
            write("/* richiamata anche da altre funzioni.        */\n");
            write("/* ------------------------------------------ */\n");
            write("//main\n");
            write("int " +mainId+ "(void){\n");
            mainOp.getFunDecl().getBody().accept(this);

            write("\n\n/* ------------------------------------------ */\n");
            write("/*               MAIN FUNCTION                */\n");
            write("/* ------------------------------------------ */\n");
            write("int main(void){\n");
            mainOp.getFunDecl().getBody().accept(this);
        }
        return null;
    }

    /**
     * (1) ParDecl -> Type IdList
     * (2) ParDecl -> OUT Type IdList
     * @param p
     * @return
     * @throws Exception
     */
    @Override
    public Object visit(ParDeclOp p) throws Exception {
        //salvo un array list di parametri per facilitare le operazioni
        ArrayList<Identifier> parametri = p.getIdList();

        //lessema da passare a CGenerator.typeContext
        String lessema;

        for(Identifier id : parametri){
            if(p.getParamType()){
                parDecl_Counter++;
                isOuts.add(parDecl_Counter);
            } else {
                parDecl_Counter++;
            }
        }

        for (int i = 0; i < parametri.size(); i++) {
            lessema = p.getIdList().get(i).getLessema();

            //stampo il tipo dei parametri passati alla funzione
            p.getType().accept(this);

            if(!CGenerator.typeContext.containsKey(lessema)){
                CGenerator.typeContext.put(lessema, new Stack<>());
            }
            CGenerator.typeContext.get(lessema).push(new TypeOp(p.getType().getType()));

            //se il parametro è di output inserisco l' * per passare il puntatore.
            if(p.getParamType())
                write("*");

            if(p.getType().getType().toString().equals("string"))
                write("*");

            //stampo l'identificativo dei parametri
            parametri.get(i).accept(this);

            //separo il tutto dalla virgola
            if (i < parametri.size() - 1)
                write(",");
        }
        return null;
    }

    @Override
    public Object visit(ProgramOp programOp) throws Exception {

        write("\n\n/* ------------------------------------------ */\n");
        write("/* --------- FIRMA DELLE FUNZIONI ----------- */\n");
        write("/* ------------------------------------------ */\n");
        signatureMethods(programOp);


        write("\n\n/* ------------------------------------------ */\n");
        write("/* ---- DICHIARAZIONI VARIABILI GLOBALI ----- */\n");
        write("/* ------------------------------------------ */\n");
        if(!Utility.isNull(programOp.getDeclList1())) {
            for (DeclOp v : programOp.getDeclList1()) {
                //se ho un VarDeclOp
                if(v.getVarDeclOp() != null)
                    v.accept(this);

            }
        }

        write("\n\n/* ------------------------------------------ */\n");
        write("/* ------ DICHIARAZIONI DELLE FUNZIONI ------ */\n");
        write("/* ------------------------------------------ */\n");
        if(!Utility.isNull(programOp.getDeclList2())) {
            for (DeclOp v : programOp.getDeclList2()) {
                //se ho un VarDeclOp
                if(v.getVarDeclOp() == null) {
                    v.accept(this);
                    write("\n");
                }
            }
        }

        if(!Utility.isNull(programOp.getDeclList1())) {
            for (DeclOp v : programOp.getDeclList1()) {
                if(v.getFunDeclOp() != null)
                    v.accept(this);
            }
        }

        if(!Utility.isNull(programOp.getDeclList2())) {
            for (DeclOp v : programOp.getDeclList2()) {
                if(v.getFunDeclOp() == null)
                    v.accept(this);
            }
        }

        programOp.getMain().accept(this);

        write("}");
        WRITER.close();

        return null;
    }

    /**
     * Per la gestione dei tipi.
     * @param typeOp
     * @return
     * @throws IOException
     */
    @Override
    public Object visit(TypeOp typeOp) throws IOException {

        String type = switch (typeOp.getType().toString()) {
            case  "integer", "bool" -> "int ";
            case "real" -> "float ";
            case "string", "character" -> "char ";
            default -> "void ";
        };

        write(type);

        //ritorno il tipo corrente
        return typeOp.getType();
    }

    @Override
    public Object visit(VarDeclOp v) throws Exception {
        if(v.getType() != null && !Utility.isNull(v.getIdInitOpList())) {

            //Prima di dichiarare la variabile vado a controllare, nel caso in cui ho assegnazioni con id,
            //se gli id sono stati dichiarati altrimenti, li vado a dichiarare in anticipo.
            for (IdInitOp i : v.getIdInitOpList()) {
                if (i.getAssignment() != null) {
                    if (i.getAssignment().getE().getId() != null) {
                        if (!CGenerator.typeContext.containsKey(i.getAssignment().getE().getId().getLessema())) {
                            CGenerator.typeContext.put(i.getAssignment().getE().getId().getLessema(), new Stack<>());

                            CGenerator.typeContext.get(i.getAssignment().getE().getId().getLessema()).push(v.getType());

                            varDeclared.add(i.getAssignment().getE().getId().getLessema());

                            //Se sono arrivata a questo punto, ho superato l'analisi semantica
                            //quindi il tipo della variabile da assegnare corrisponderà sicuro con quello della dichiarazione.
                            if (v.getType().getType().equals(TypeOp.Type.string)) {
                                write("char " + i.getId().getLessema() + "_tmp[MAXDIM]=\"\";\n");
                            }
                            v.getType().accept(this);
                            write(i.getAssignment().getE().getId().getLessema() + "; \n");
                        }
                    }
                }
            }

            String lessema;

            for (int i = 0; i < v.getIdInitOpList().size(); i++) {

                //caso in cui NON ho l'assegnazione
                if(v.getIdInitOpList().get(i).getAssignment() == null){
                    lessema = v.getIdInitOpList().get(i).getId().getLessema();

                    if (!CGenerator.typeContext.containsKey(lessema)) {
                        CGenerator.typeContext.put(lessema, new Stack<>());
                    }

                    CGenerator.typeContext.get(lessema).push(v.getType());

                    if(!varDeclared.contains(lessema)) {
                        if(v.getType().getType().equals(TypeOp.Type.string)){
                            if(v.getIdInitOpList().get(i).getId() != null)
                                write("char " +  lessema + "_tmp[MAXDIM]=\"\";\n");
                        }

                        //scrivo il tipo delle dichiarazioni di variabili
                        v.getType().accept(this);

                        //se nella lista si id incontro una stringa, allora inserisco la *
                        if (v.getIdInitOpList().get(i).getId().getIdType().getType().equals(TypeOp.Type.string))
                            write("*");

                        v.getIdInitOpList().get(i).accept(this);
                        //Se si vogliono stampare in riga più variabili
                        //stampo le virgole (,) fra gli id
                        //if (i != v.getIdInitOpList().size() - 1)
                        //  write(", ");
                        write(";\n");
                    }
                }
                //caso in cui ho assignment
                else {
                    lessema = v.getIdInitOpList().get(i).getAssignment().getId().getLessema();

                    if (!CGenerator.typeContext.containsKey(lessema)) {
                        CGenerator.typeContext.put(lessema, new Stack<>());
                    }
                    CGenerator.typeContext.get(lessema).push(v.getType());

                    if(!varDeclared.contains(lessema)) {


                        //scrivo il tipo delle dichiarazioni di variabili
                        v.getType().accept(this);

                        //se nella lista di id incontro una stringa, allora inserisco la *
                        if (v.getIdInitOpList().get(i).getAssignment().getId().getIdType().getType().equals(TypeOp.Type.string))
                            write("*");

                        v.getIdInitOpList().get(i).getAssignment().accept(this);

                        //Se si vogliono stampare in riga più variabili
                        // if (i != v.getIdInitOpList().size() - 1)
                        //   write(", ");
                        write(";\n");
                    }
                }
            }
        }
        //se ho un var DEVE necessariamente essere una assegnazione.
        else if (v.getVar() !=  null && !Utility.isNull(v.getIdInitObblOpList())){
            for (int i = 0; i < v.getIdInitObblOpList().size(); i++) {
                //[caso var] scrivo in C l'assegnazione con il tipo corretto
                v.getIdInitObblOpList().get(i).accept(this);
            }
        }

        return null;
    }

    private void write(String s) throws IOException {
        WRITER.write(s);
    }

    /**
     * METODI PER RIDURRE LA COMPLESSITA' LOGICA DEL CODICE
     */

    private void signatureMethods(ProgramOp programOp) throws IOException {

        //array list contenente i tipi di parametri passati alla funzione
        ArrayList<TypeOp> types;
        //arrayList contenente i parametri da passare alla funzione
        ArrayList<ParDeclOp> params;
        //arrayList per i parametri di out
        ArrayList<Integer> isOutParam = new ArrayList<>();
        TypeOp returnType;
        String funIdentifier;

        int counter = -1;

        if(!Utility.isNull(programOp.getDeclList1())) {
            for (DeclOp v : programOp.getDeclList1()) {

                //controllo solo le funzioni per inserire la firma
                if(v.getFunDeclOp() != null){
                    write("\n");
                    returnType = v.getFunDeclOp().getType();
                    funIdentifier = v.getFunDeclOp().getId().getLessema();
                    params = v.getFunDeclOp().getParDeclList();

                    if(!Utility.isNull(params)) {
                        isOutParam = Utility.getOutParams(params);
                    }

                    //stampo il tipo di ritorno della funzione
                    if(returnType.getType().equals(TypeOp.Type.voidType))
                        write("void ");
                    else
                        write(Utility.getTypeString(returnType));

                    //stampo l'identificativo della funzione
                    write(funIdentifier);

                    //se la lista di parametri è vuota o contiene voidType stampo direttamente (void)
                    if(Utility.isNull(params))
                        write("(void);\n");
                        //altrimenti stampo il tipo dei parametri
                    else{
                        types = Utility.getTypeFromParamList(params);
                        write("(");
                        for(int i = 0; i<types.size(); i++){
                            counter++;
                            //stampa il tipo del parametro
                            write(Utility.getTypeString(types.get(i)));

                            //Collections.reverse(isOutParam);
                            for(Integer value : isOutParam) {
                                if (value == counter)
                                    write("*");
                            }

                            //stampa la virgola
                            if(i!=types.size()-1){
                                write(",");
                            }
                        }
                        write(");");
                        counter = -1;
                    }
                }
            }
        }

        if(!Utility.isNull(programOp.getDeclList2())) {
            for (DeclOp v : programOp.getDeclList2()) {

                if(v.getFunDeclOp() != null){
                    write("\n");

                    returnType = v.getFunDeclOp().getType();
                    funIdentifier = v.getFunDeclOp().getId().getLessema();
                    params = v.getFunDeclOp().getParDeclList();

                    if(!Utility.isNull(params)) {
                        isOutParam = Utility.getOutParams(params);
                    }

                    //stampo il tipo di ritorno della funzione
                    if(returnType.getType().equals(TypeOp.Type.voidType))
                        write("void ");
                    else
                        write(Utility.getTypeString(returnType));

                    //stampo l'identificativo della funzione
                    write(funIdentifier);

                    //se la lista di parametri è vuota o contiene voidType stampo direttamente (void)
                    if(Utility.isNull(params))
                        write("(void);\n");
                        //altrimenti stampo il tipo dei parametri
                    else{
                        types = Utility.getTypeFromParamList(params);
                        write("(");
                        for(int i = 0; i<types.size(); i++){
                            counter++;
                            //stampa il tipo del parametro
                            write(Utility.getTypeString(types.get(i)));

                            //Collections.reverse(isOutParam);
                            for(Integer value : isOutParam) {
                                if (value == counter)
                                    write("*");
                            }

                            //stampa la virgola
                            if(i!=types.size()-1){
                                write(",");
                            }
                        }
                        write(");");
                        counter = -1;
                    }
                }
            }
        }

        //se si tratta del main
        if(programOp.getMain() != null) {
            returnType = programOp.getMain().getFunDecl().getType();
            funIdentifier = programOp.getMain().getFunDecl().getId().getLessema();
            params = programOp.getMain().getFunDecl().getParDeclList();

            if (!funIdentifier.equals("main")) {
                if (!Utility.isNull(params)) {
                    isOutParam = Utility.getOutParams(params);
                }

                //stampo il tipo di ritorno della funzione
                if (returnType.getType().equals(TypeOp.Type.voidType))
                    write("\nvoid ");
                else
                    write(Utility.getTypeString(returnType));

                //stampo l'identificativo della funzione
                write(funIdentifier);

                //se la lista di parametri è vuota o contiene voidType stampo direttamente (void)
                if (Utility.isNull(params))
                    write("(void);\n");
                    //altrimenti stampo il tipo dei parametri
                else {
                    types = Utility.getTypeFromParamList(params);
                    write("(");
                    for (int i = 0; i < types.size(); i++) {
                        counter++;
                        //stampa il tipo del parametro
                        write(Utility.getTypeString(types.get(i)));

                        //Collections.reverse(isOutParam);
                        for (Integer value : isOutParam) {
                            if (value == counter)
                                write("*");
                        }

                        //stampa la virgola
                        if (i != types.size() - 1) {
                            write(",");
                        }
                    }
                    write(");");
                    counter = -1;
                }
            }
        }

    }

}
