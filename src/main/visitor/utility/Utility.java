package main.visitor.utility;

import nodi.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Utility {

    /**
     * String: id della funzione
     * String: tipo ritorno della funzione
     */
    public static HashMap<String, String> returnFunctionType = new HashMap<>();

    public Utility() {}

    public static HashMap<String, String> getReturnFunctionType() {
        return returnFunctionType;
    }

    public static void setReturnFunctionType(HashMap<String, String> returnFunctionTypeNew) {
        returnFunctionType.putAll(returnFunctionTypeNew);
    }

    /**
     * Metodo che controlla se un intero ArrayList è null.
     * @param arrList ArrayList da controllare
     * @return true se l'ArrayList è vuoto, false altrimenti
     */
    public static Boolean isNull(ArrayList arrList) {
        if(arrList != null)
            if(arrList != null)
                for(Object a : arrList)
                    if(a != null) return false;
        return true;
    }

    public static void recDecl(FunDeclOp f) throws Exception {

        //istanzio i parametri da passare a ElementMethod -> da aggiungere alla tabella dei simboli
        String funName = f.getId().getLessema();
        ArrayList<String> paramsName = new ArrayList<>();
        ArrayList<String> paramsMode = new ArrayList<>();
        ArrayList<TypeOp> paramsType = new ArrayList<>();
        String returnType = f.getType().toString();

        //aggiorno i valori per l'Element da aggiungere, prima di aggiungerlo devo fare dei controlli
        if(!Utility.isNull(f.getParDeclList())){
            //(1) ParDecl -> Type IdList - (2) ParDecl -> OUT Type IdList
            for(ParDeclOp p : f.getParDeclList()){
                paramsType.add(p.getType());
                paramsMode.add(String.valueOf(p.getParamType()));
                //mi vado a prendere i lessemi
                for(Identifier i : p.getIdList()){
                    paramsName.add(i.getLessema());
                }
            }
        }

        //se NON esiste una dichiarazione per la funzione f -controllo solo il nome- la AGGIUNGO
        if(!TypeEnvironment.lookupBool(f.getId().getLessema())){
            ElementMethod elementMethod = new ElementMethod(funName,paramsName,paramsType,paramsMode,returnType);
            TypeEnvironment.addElement(elementMethod);
        }
        else {
            throw new Error("Redeclaration.");
        }
    }

    /**
     * Dato un array di ParDeclOp, restituisce un array di TypeOp corrispondente
     * ai tipi nell'array di ParDeclOp.
     * @param params ArrayList di ParDeclOp.
     * @return ArrayList di TypeOp.
     */
    public static ArrayList<TypeOp> getTypeFromParamList(ArrayList<ParDeclOp> params){

        ArrayList<TypeOp> types = new ArrayList<>();
        int counter = -1;

        for (ParDeclOp p : params){
            for(Identifier i : p.getIdList())
                counter ++;
            do {
                types.add(p.getType());
                counter --;
            } while (counter == 0);

        }

        Collections.reverse(types);
        return types;
    }


    public static ArrayList<Integer> getOutParams (ArrayList<ParDeclOp> params){
        ArrayList<Integer> isOuts = new ArrayList<>();
        int parDecl_counter = -1;

        //reverse della lista di parametri - altrimenti sono al contrario
        Collections.reverse(params);

        //per ogni ParDeclOp nella lista di parametri
        for(ParDeclOp p : params){

            //per ogni Identifier
            for(Identifier id : p.getIdList()){

                if(p.getParamType()) {
                    parDecl_counter++;
                    isOuts.add(parDecl_counter);
                } else {
                    parDecl_counter++;
                    isOuts.add(-1);
                }

            }
        }

        //reverse della lista di parametri per stabilizzarla
        Collections.reverse(params);

        return isOuts;
    }

    public static String getTypeString(TypeOp type){
        return switch (type.getType()){
            case integer, bool -> "int ";
            case real -> "float ";
            case string ->  "char* ";
            case character -> "char ";
            case voidType -> "void ";
            case notype -> null;
        };
    }

}
