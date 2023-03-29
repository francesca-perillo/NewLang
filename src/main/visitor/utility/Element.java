package main.visitor.utility;

public class Element {

   private String name;
   public enum KindElement{
       method, var
   }
   private KindElement kindElement;

   public Element(String name, KindElement kindElement){
       this.name = name;
       this.kindElement = kindElement;
   }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public KindElement getKindElement() {
        return kindElement;
    }

    public void setKindElement(KindElement kindElement) {
        this.kindElement = kindElement;
    }
}
