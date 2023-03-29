package main.java;

import java_cup.runtime.Symbol;
import main.generated.Lexer;
import main.generated.Parser;
import main.visitor.VisitorGenerateTreeXML;
import main.visitor.VisitorSemantic;
import nodi.ProgramOp;

import java.io.FileReader;

public class TesterParser {
    public static void main(String[] args) {

        try {

            Lexer l = new Lexer(new FileReader(args[0] != null ? args[0] : "test/Programma.txt"));
            Symbol s;
            Parser p = new Parser(l);

            VisitorGenerateTreeXML xmlTreeGenerator = new VisitorGenerateTreeXML();
            VisitorSemantic semanticVisitor = new VisitorSemantic();

            ProgramOp program = (ProgramOp) p.debug_parse().value;
            program.accept(xmlTreeGenerator);
            xmlTreeGenerator.saveTo("src/main/generated/xmlTreeGenerated.xml");
            program.accept(semanticVisitor);

        } catch (Error | Exception e) {
            e.printStackTrace();
        }
    }
}
