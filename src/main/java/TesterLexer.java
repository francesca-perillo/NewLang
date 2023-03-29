package main.java;

import java_cup.runtime.Symbol;
import main.generated.Lexer;
import main.generated.ParserSym;

import java.io.*;

public class TesterLexer {
    public static void main ( String[] args ) throws IOException {
        String filePath = args[0];
        InputStream in = new FileInputStream(filePath);
        Reader reader = new InputStreamReader(in);

        Symbol s;
        String nameToken;

        Lexer lexicalAnalyzer = new Lexer(reader);

        while ((s = lexicalAnalyzer.next_token()).sym != 0) {
            nameToken = ParserSym.terminalNames[s.sym];
            if (s.value != null) {
                System.out.println("<" + nameToken + "," + s.value + ">");
            } else System.out.println(nameToken);
        }
    }
}
