/**
 * @authors Esposito Mariarosaria, Perillo Francesca
*/
package main.java;
import java_cup.runtime.Symbol; //This is how we pass tokens to the parser
import main.generated.ParserSym;
















%%

%public
%class Lexer
%unicode //Definisce il set di caratteri su cui funzionera lo scanner
%cupsym Token
%cup  //Passa alla modalita di compatibilita CUP per interfacciarsi con un parser generato da CUP
%line //Attiva il conteggio delle righe (e possibile accedere al numero di riga corrente tramite la variabile yyline)
%column //Attiva il conteggio delle colonne (si accede alla colonna corrente tramite yycolumn)

/*
   Qui dichiariamo le variabili membro e le funzioni che vengono utilizzate all interno delle azioni dello scanner.
   Dichiariamo una stringa StringBuffer in cui memorizzeremo parti di letterali stringa e due funzioni di supporto
   symbol che creano oggetti java cup.runtime.Symbol con informazioni sulla posizione del token corrente.
*/

%{
  StringBuffer string = new StringBuffer();

   private Symbol symbol(int type) {
        return new Symbol(type, yyline, yycolumn);
   }

   private Symbol symbol(int type, Object lessema) {
        return new Symbol(type, yyline, yycolumn, lessema.toString());
   }
%}

/*
  Dichiarazioni macro.
  Le macro sono abbreviazioni per espressioni regolari, utilizzate per rendere le specifiche lessicali
  più facili da leggere e comprendere.
  Una dichiarazione di macro consiste in:
  - un identificatore di macro seguito da =, quindi seguito dall'espressione regolare che rappresenta.
*/

whitespace = [ \r\n\t\f]
whitespaceNoLN = [ \r\t\f]
ln = [\n]

identifier = [A-Za-z][\_A-Za-z0-9]*
stringConstOp = "\""[^\n\r\"\\]+"\""
charConst = "\'"[^\n\r\"\\]"\'"

digit = [0-9]
integer = {digit}+
float = {integer}("."{integer})?
main = "start:"
accented = à|è|é|ì|ò|ù //per i commenti
special = _|-|\!|\"|£|%|&|\/|\(|\)|=|\?|\^|,|\.|;|:|°|#|@|ç|\{|\}|\[|\]|\*|\+|§|<|>|\/|\\ //per i commenti

/* comments */
CommentMoreLine = "|*"({identifier}|{integer}|{whitespace}|"'"|{accented}|{special})*"|"
CommentLine = "||"({identifier}|{integer}|{whitespaceNoLN}|"'"|{accented}|{special})*{ln}

//commento per gestire l'errore di un commento non chiuso
Comment_not_close = "|*"({identifier}|{integer}|{whitespace}|"'")*

%state STRING
%state CHAR

%%

<YYINITIAL>{

    //delimitatori
    {whitespace}         { /* ignore */ }

    //commenti
    {CommentMoreLine}    {/* ignore */}
    {CommentLine}        {/* ignore */}
    {Comment_not_close}  {System.out.println("ATTENZIONE: probabile commento non chiuso");}

    //main
    {main}               { return symbol(ParserSym.MAIN, yytext()); }

    //inoltro a classe STRING
    \"                   { string.setLength(0); yybegin(STRING);}

    //inoltro a classe CHAR
    \'                   { string.setLength(0); yybegin(CHAR);}

    //tipo int
    {integer}            { return symbol(ParserSym.INTEGER_CONST, yytext()); }

    //tipo float
    {float}              { return symbol(ParserSym.REAL_CONST, yytext()); }

    //string_const
    {stringConstOp}        { return symbol(ParserSym.STRING_CONST, yytext().substring(1, yytext().length()-1)); }

    //char_const
    {charConst}          { return symbol(ParserSym.CHAR_CONST, yytext().substring(1, yytext().length()-1)); }

    //Separatori
    "("                  { return symbol(ParserSym.LPAR); }
    ")"                  { return symbol(ParserSym.RPAR); }
    "{"                  { return symbol(ParserSym.LBRAC); }
    "}"                  { return symbol(ParserSym.RBRAC); }
    ","                  { return symbol(ParserSym.COMMA); }
    ";"                  { return symbol(ParserSym.SEMI); }
    "|"                  { return symbol(ParserSym.PIPE); }
    ":"                  { return symbol(ParserSym.COLON); }

    //Operatori
    "<"                  { return symbol(ParserSym.LT); }
    "<="                 { return symbol(ParserSym.LE); }
    ">"                  { return symbol(ParserSym.GT); }
    ">="                 { return symbol(ParserSym.GE); }
    "+"                  { return symbol(ParserSym.PLUS); }
    "-"                  { return symbol(ParserSym.MINUS); }
    "*"                  { return symbol(ParserSym.TIMES); }
    "/"                  { return symbol(ParserSym.DIV); }
    "^"                  { return symbol(ParserSym.POW); }
    "&"                  { return symbol(ParserSym.STR_CONCAT); }
    "<>"                 { return symbol(ParserSym.NE); }
    "!="                 { return symbol(ParserSym.NE); }
    "="                  { return symbol(ParserSym.EQ); }
    "<<"                 { return symbol(ParserSym.ASSIGN); }

    "<--"                { return symbol(ParserSym.READ); }
    "-->"                { return symbol(ParserSym.WRITE); }
    "-->!"               { return symbol(ParserSym.WRITELN); }

    "def"                { return symbol(ParserSym.DEF);}
    "out"                { return symbol(ParserSym.OUT);}
    "if"                 { return symbol(ParserSym.IF);}
    "then"               { return symbol(ParserSym.THEN);}
    "else"               { return symbol(ParserSym.ELSE);}
    "while"              { return symbol(ParserSym.WHILE);}
    "loop"               { return symbol(ParserSym.LOOP);}
    "for"                { return symbol(ParserSym.FOR);}
    "to"                 { return symbol(ParserSym.TO);}

    "integer"            { return symbol(ParserSym.INTEGER);}
    "float"              { return symbol(ParserSym.REAL);}
    "var"                { return symbol(ParserSym.VAR);}
    "string"             { return symbol(ParserSym.STRING);}
    "boolean"            { return symbol(ParserSym.BOOL);}
    "char"               { return symbol(ParserSym.CHAR);}
    "void"               { return symbol(ParserSym.VOID);}

    "true"               { return symbol(ParserSym.TRUE);}
    "false"              { return symbol(ParserSym.FALSE);}
    "and"                { return symbol(ParserSym.AND);}
    "or"                 { return symbol(ParserSym.OR);}
    "not"                { return symbol(ParserSym.NOT);}
    "else"               { return symbol(ParserSym.ELSE);}

    "return"             { return symbol(ParserSym.RETURN);}


    "v_tmp_"{integer}   { try {
                            throw new Exception("Attenzione, stai tentando di usare una lessema riservato!");
                               } catch (Exception e) {
                                   e.printStackTrace();
                               }
                        }
    "MAXDIM"{integer}   { try {
                                throw new Exception("Attenzione, stai tentando di usare una lessema riservato!");
                                   } catch (Exception e) {
                                     e.printStackTrace();
                                   }
                            }
    "concat_"{integer}   { try {
                             throw new Exception("Attenzione, stai tentando di usare una lessema riservato!");
                                } catch (Exception e) {
                                  e.printStackTrace();
                                }
                         }
    "buffer_bis_"{integer}   { try {
                                 throw new Exception("Attenzione, stai tentando di usare una lessema riservato!");
                                    } catch (Exception e) {
                                      e.printStackTrace();
                                    }
                             }

     //identificatori
    {identifier}         { return symbol(ParserSym.ID, yytext()); }
}

   <STRING> {
      [\"]                      { yybegin(YYINITIAL);
                                  return symbol(ParserSym.STRING_CONST, string.toString());}
      [^\n\r\"\'\\]+            { string.append( yytext() ); }
      \\t                       { string.append('\t'); }
      \\n                       { string.append('\n'); }

      \\r                       { string.append('\r'); }
      \\\"                      { string.append('\"'); }
      \\\'                      { string.append('\''); }
      \\                        { string.append('\\'); }
   }

   <CHAR> {
      [\']                      { yybegin(YYINITIAL);
                                  return symbol(ParserSym.CHAR_CONST, string.toString());}
      [^\n\r\"\'\\]             { string.append( yytext() ); }
      \\\"                      { string.append('\"'); }
      \\\'                      { string.append('\''); }
   }

[^]                      { return symbol(ParserSym.error, "Illegal character <"+yytext()+">"); }

<<EOF>>                  { return symbol(ParserSym.EOF); }