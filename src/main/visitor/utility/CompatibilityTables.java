package main.visitor.utility;

import nodi.TypeOp;

public class CompatibilityTables {

    /**
     * enum di tutti i possibili tipi che uno (o due) operatori
     * possono avere.
     * N.B: true_const e false_const sono entrambi tipo bool
     *      character ricade in string
     */
    public enum TypeOperation {
        integer, real, bool, string, erroType
    }

    /**
     * enum che identifica il tipo di operazione (considerando optype1 e optype2) che sto andando a fare
     */
    public enum OpType {
        arithmeticOperation, //PLUS, MINUS, TIMES, POW
        div,
        stringConcat,
        logicalOperator, //AND, OR
        comparisonOperation, //LT, EQ, ...

        unaryMinus,
        unaryNot

    }
    /**
     * Metodo che implementa la tabella di compatibilità per opType2:
     * operatori binari.
     *  T |- e1:t1     T |- e2:t2      optype2(op2,t1,t2) = t
     *  ------------------------------------------------------
     *               T |- e1 op2 e2 : t
     * @param operazione tipo di operazione che si deve svolgere
     * @param tipoOperando1 tipo del primo operando
     * @param tipoOperando2 tipo del secondo operando
     * @return tipo risultante
     */
    public TypeOperation optype2(OpType operazione, String tipoOperando1, String tipoOperando2){

        if(operazione.equals(OpType.arithmeticOperation)){

            if(tipoOperando1.equals(TypeOperation.integer.toString())){
                //INTEGER   op  integer = integer
                if(tipoOperando2.equals(TypeOperation.integer.toString()))
                    return TypeOperation.integer;
                //INTEGER   op  float   = float
                if(tipoOperando2.equals(TypeOperation.real.toString()))
                    return TypeOperation.real;
            }

            if(tipoOperando1.equals(TypeOperation.real.toString())){
                //FLOAT   op  integer = real
                if(tipoOperando2.equals(TypeOperation.integer.toString()))
                    return TypeOperation.real;
                //FLOAT   op  float   = float
                if(tipoOperando2.equals(TypeOperation.real.toString()))
                    return TypeOperation.real;
            }
        }

        //regole di inferenza diverse per div, a prescindere il ritorno è real.
        if(operazione.equals(OpType.div)){

            if(tipoOperando1.equals(TypeOperation.integer.toString())){
                //INTEGER   op  integer = float
                if(tipoOperando2.equals(TypeOperation.integer.toString()))
                    return TypeOperation.real;
                //INTEGER   op  float   = float
                if(tipoOperando2.equals(TypeOperation.real.toString()))
                    return TypeOperation.real;
            }

            if(tipoOperando1.equals(TypeOperation.real.toString())){
                //FLOAT   op  integer = float
                if(tipoOperando2.equals(TypeOperation.integer.toString()))
                    return TypeOperation.real;
                //FLOAT   op  float   = float
                if(tipoOperando2.equals(TypeOperation.real.toString()))
                    return TypeOperation.real;
            }
        }

        //boolean   op  boolean = boolean
        if(operazione.equals(OpType.logicalOperator)){
            if(tipoOperando1.equals(TypeOperation.bool.toString()) && tipoOperando2.equals(TypeOperation.bool.toString()))
                return TypeOperation.bool;
        }

        //string    op  string  = string
        if(operazione.equals(OpType.stringConcat)){
            if(tipoOperando1.equals(TypeOperation.string.toString()) && tipoOperando2.equals(TypeOperation.string.toString()))
                return TypeOperation.string;
            if(tipoOperando1.equals(TypeOperation.string.toString()) && tipoOperando2.equals(TypeOperation.integer.toString()))
                return TypeOperation.string;
            if(tipoOperando1.equals(TypeOperation.string.toString()) && tipoOperando2.equals(TypeOperation.real.toString()))
                return TypeOperation.string;
            if(tipoOperando1.equals(TypeOperation.integer.toString()) && tipoOperando2.equals(TypeOperation.string.toString()))
                return TypeOperation.string;
            if(tipoOperando1.equals(TypeOperation.real.toString()) && tipoOperando2.equals(TypeOperation.string.toString()))
                return TypeOperation.string;
        }

        if(operazione.equals(OpType.comparisonOperation)){

            if(tipoOperando1.equals(TypeOperation.integer.toString())){
                //INTEGER   op  integer = boolean
                if(tipoOperando2.equals(TypeOperation.integer.toString()))
                    return TypeOperation.bool;
                //INTEGER   op  float   = boolean
                if(tipoOperando2.equals(TypeOperation.real.toString()))
                    return TypeOperation.bool;
            }

            if(tipoOperando1.equals(TypeOperation.real.toString())){
                //FLOAT   op  integer = boolean
                if(tipoOperando2.equals(TypeOperation.integer.toString()))
                    return TypeOperation.bool;
                //FLOAT   op  float   = boolean
                if(tipoOperando2.equals(TypeOperation.real.toString()))
                    return TypeOperation.bool;
            }

            if(tipoOperando1.equals(TypeOperation.string.toString()) && tipoOperando2.equals(TypeOperation.string.toString())){
                return TypeOperation.bool;

            }
        }

        return TypeOperation.erroType;
    }

    /**
     * Metodo che implementa la tabella di compatibilità per opType1:
     * operatori unari.
     * T |- e:t1       optype1(op1,t1) = t
     * -----------------------------------
     *      T |- op1 e : t
     * @param operazione
     * @param tipoOperando
     * @return
     */
    public TypeOperation optype1(OpType operazione, String tipoOperando){

        if(operazione.equals(OpType.unaryMinus)){
            if(tipoOperando.equals(TypeOperation.integer.toString()))
                return TypeOperation.integer;
            if(tipoOperando.equals(TypeOperation.real.toString()))
                return TypeOperation.real;
        }

        if(operazione.equals(OpType.unaryNot)){
            if(tipoOperando.equals(TypeOperation.bool.toString()))
                return TypeOperation.bool;
        }

        return TypeOperation.erroType;
    }
}
