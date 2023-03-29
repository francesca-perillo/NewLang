package main.java;

import java_cup.runtime.Symbol;
import main.generated.Lexer;
import main.generated.Parser;
import main.visitor.CGenerator;
import main.visitor.VisitorGenerateTreeXML;
import main.visitor.VisitorSemantic;
import nodi.ProgramOp;
import java.io.FileReader;

public class NewLang2C {

    public static void main(String[] args) {

        try {

            Lexer l = new Lexer(new FileReader(args.length > 0 && args[0] != null ? args[0] : "test/Programma.txt"));
            Symbol s;
            Parser p = new Parser(l);

            VisitorGenerateTreeXML xmlTreeGenerator = new VisitorGenerateTreeXML();
            VisitorSemantic semanticVisitor = new VisitorSemantic();

            //usare debug_parser() per vedere tutti i passaggi - eliminato pre rendere più leggibile la pipeline
            ProgramOp program = (ProgramOp) p.parse().value;
            program.accept(xmlTreeGenerator);
            xmlTreeGenerator.saveTo("src/main/generated/xmlTreeGenerated.xml");
            program.accept(semanticVisitor);

            //prelevo il file_name da args
            String[] file_path = args[0].split("/");
            String file_name = file_path[file_path.length-1];
            String[] fn_noE = file_name.split("\\.");
            String file_name_noE = fn_noE[0];

            CGenerator code = new CGenerator("test_files/c_out/"+file_name_noE+".c");

            program.accept(code);

        } catch (Error | Exception e) {
            e.printStackTrace();
        }

    }

}
