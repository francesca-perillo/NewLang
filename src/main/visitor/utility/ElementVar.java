package main.visitor.utility;

public class ElementVar extends Element{

    private String type;
    private boolean isOut;

    /**
     * Costruttore per una generica variabile
     * @param name nome della variabile
     * @param type tipo della variabile
     * @param isOut true: out
     */
    public ElementVar (String name, String type, boolean isOut){
        super(name, KindElement.var);
        this.type = type;
        this.isOut = isOut;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isOut() {
        return isOut;
    }

    public void setOut(boolean out) {
        isOut = out;
    }
}
