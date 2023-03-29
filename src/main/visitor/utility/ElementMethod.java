package main.visitor.utility;

import nodi.TypeOp;

import java.util.ArrayList;

public class ElementMethod extends Element{

    private ArrayList<String> paramsName;
    private ArrayList<TypeOp> paramsType;
    private ArrayList<String> paramsMode;
    private String returnType;

    /**
     * Costruttore per un generico metodo.
     * @param name id del metodo
     * @param paramsName nome dei parametri nel metodo
     * @param paramsType tipi dei parametri nel metodo
     * @param paramsMode modalità dei parametri nel metodo (out)
     * @param returnType tipo di ritorno.
     */
    public ElementMethod (String name, ArrayList<String> paramsName, ArrayList<TypeOp> paramsType,
                          ArrayList<String> paramsMode, String returnType) {
        super(name, KindElement.method);
        this.paramsName = paramsName;
        this.paramsType = paramsType;
        this.paramsMode = paramsMode;
        this.returnType = returnType;
    }

    public ArrayList<String> getParamsName() {
        return paramsName;
    }

    public void setParamsName(ArrayList<String> paramsName) {
        this.paramsName = paramsName;
    }

    public ArrayList<TypeOp> getParamsType() {
        return paramsType;
    }

    public void setParamsType(ArrayList<TypeOp> paramsType) {
        this.paramsType = paramsType;
    }

    public ArrayList<String> getParamsMode() {
        return paramsMode;
    }

    public void setParamsMode(ArrayList<String> paramsMode) {
        this.paramsMode = paramsMode;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

}
