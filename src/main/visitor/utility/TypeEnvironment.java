package main.visitor.utility;

import nodi.FunCallOp;

import java.util.Stack;
public class TypeEnvironment {
    private static final Stack<SymbolTable> stack = new Stack<>();

    /**
     * Viene agginto una nuova tabella dei
     * simboli al Type Environment
     */
    public static void newScope() {
        stack.push(new SymbolTable());
    }

    /**
     * Viene ritornato l'ultima tabella dei simboli,
     * ovvero la più vicina.
     * @return
     */
    public static SymbolTable getScope() {
        return stack.lastElement();
    }

    public static SymbolTable getMainScope() {

        Stack<SymbolTable> stackCopy = (Stack<SymbolTable>) stack.clone();
        for(int i = 0; i<stackCopy.size()-1; i++){
            stackCopy.pop();
        }
        return stackCopy.pop();
    }

    /**
     * Viene rimosso lo scope
     */
    public static void removeScope() {
        stack.pop();
    }

    public static int getSize(){
        return stack.size();
    }

    /**
     * Viene aggiunto un elemento nello scope
     * corrente, a seconda se questo è una variabile
     * o un metodo.
     * @param e
     * @throws Exception
     */
    public static void addElement(Element e) throws Exception {
        if (e.getKindElement().equals(Element.KindElement.var))
            getScope().addVar((ElementVar) e);
        else
            getScope().addMethod((ElementMethod) e);
    }

    /**
     * Metodo che fa il lookup di un Element
     * @param name elemento sul quale fare il lookup (solo il nome)
     * @return Element o null se non esiste l'elemento.
     */
    public static Element lookup(String name) {
        int i;
        for (i= stack.size()-1; i >= 0; i--) {
            SymbolTable s = stack.get(i);
            if (s.containElement(name))
                return s.getElement(name);
        }
        return null;
    }

    /**
     * Metodo che fa il lookup di un Element
     * @param name elemento sul quale fare il lookup (solo il nome)
     * @return true se l'Element esiste, false altrimenti.
     */
    public static boolean lookupBool(String name) {
        int i;
        for (i= stack.size()-1; i >= 0; i--) {
            SymbolTable s = stack.get(i);
            if (s.containElement(name))
                return true;
        }
        return false;
    }
}
