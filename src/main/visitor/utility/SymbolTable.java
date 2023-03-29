package main.visitor.utility;

import java.util.HashMap;

public class SymbolTable {

    private HashMap<String, Element> symbolTable;

    public SymbolTable() {
        this.symbolTable = new HashMap<>();
    }

    /**
     * Metodo per aggiungere un metodo (funzione) nella tabella dei simboli
     * non vengono effettuati controlli, in quanto questi vengono gia fatti nel visitor semantico
     * @param method metodo da aggiungere alla tabella dei simboli
     * @throws Exception
     */
    public void addMethod(ElementMethod method) {
        symbolTable.put(method.getName(), method);
    }

    /**
     * Metodo per aggiungere una variabile nella tabella dei simboli
     * non vengono effettuati controlli, in quanto questi vengono gia fatti nel visitor semantico
     * @param var metodo da aggiungere alla tabella dei simboli
     * @throws Exception
     */
    public void addVar(ElementVar var){
        symbolTable.put(var.getName(), var);
    }

    public boolean containElement(String key){
        return this.symbolTable.containsKey(key);
    }

    /**
     * Metodo per recuperare un elemento dalla tabella dei simboli
     * @param key chiave dell'elemento da recuperare
     * @return null se l'elemento non esiste
     */
    public Element getElement(String key) {
        return containElement(key) ? symbolTable.get(key) : null;
    }
}


