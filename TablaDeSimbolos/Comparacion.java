package TablaDeSimbolos;


import Lexico.Token;

public class Comparacion extends Tupla {
    private Token operador;
    private Token valor1;
    private Token valor2;

    public Comparacion(Token valor1, Token operador, Token valor2, int sv, int sf) {
        super(sv, sf);
        this.operador = operador;
        this.valor1 = valor1;
        this.valor2 = valor2;
    }

    public String toString() {
        return "( " + super.toString() + ", [ " + valor1 + ", " + operador + ", " + valor2 + " ] )";
    }

    public int ejecutar(Scope ts) throws SemanticExpression {
        float operando1 = 0, operando2 = 0;

        if (valor1.getTipo().getNombre().equals("NUMERO")) {
            operando1 = Float.parseFloat(valor1.getNombre());
        } else {
            Variable v1 = (Variable)ts.resolver(valor1.getNombre());
            operando1 = v1.getValor();
        }

        if (valor2.getTipo().getNombre().equals("NUMERO")) {
            operando2 = Float.parseFloat(valor2.getNombre());
        } else {
            Variable v2 = (Variable)ts.resolver(valor2.getNombre());
            operando2 = v2.getValor();
        }

        switch (operador.getNombre()) {
            case "<": return operando1 < operando2 ? saltoVerdadero : saltoFalso;
            case "<=": return operando1 <= operando2 ? saltoVerdadero : saltoFalso;
            case ">": return operando1 > operando2 ? saltoVerdadero : saltoFalso;
            case ">=": return operando1 >= operando2 ? saltoVerdadero : saltoFalso;
            case "==": return operando1 == operando2 ? saltoVerdadero : saltoFalso;
            case "!=": return operando1 != operando2 ? saltoVerdadero : saltoFalso;
        }

        return saltoVerdadero;
    }
}