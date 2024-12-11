package TablaDeSimbolos;

import Lexico.TipoToken;
import Lexico.Token;

public class Asignacion extends Tupla{
    Token variable, valor1, valor2, operador;

    public Asignacion(Token variable, Token valor, int sv, int sf) {
        super(sv, sf);
        this.variable = variable;
        this.valor1 = valor;
    }
    
    public Asignacion(Token variable, Token valor1, Token operador, Token valor2, int sv, int sf) {
        super(sv, sf);
        this.variable = variable;
        this.valor1 = valor1;
        this.valor2 = valor2;
        this.operador = operador;
    }

    public String toString() {
        if (operador == null)
            return "( " + super.toString() + ", [ \"" + variable + ", " + valor1 +  "\" ] )";
        else 
            return "( " + super.toString() + ", [ \"" + variable + ", " + valor1 + ", " + operador + ", " + valor2 + "\" ] )";
    }

    public int ejecutar(Scope ts) throws SemanticExpression {
        Variable v = (Variable) ts.resolver(variable.getNombre());
        float operando1 = 0, operando2 = 0;
    
        // Resolver el primer operando (puede ser un número, una variable o una función)
        if (valor1.getTipo().getNombre().equals(TipoToken.NUMERO)) {
            operando1 = Float.parseFloat(valor1.getNombre());
        } else if (ts.resolver(valor1.getNombre()) instanceof Metodo) {

        } else {
            operando1 = ((Variable) ts.resolver(valor1.getNombre())).getValor();
        }
    
        // Resolver el segundo operando si existe
        if (valor2 != null) {
            if (valor2.getTipo().getNombre().equals(TipoToken.NUMERO)) {
                operando2 = Float.parseFloat(valor2.getNombre());
            } else if (ts.resolver(valor2.getNombre()) instanceof Metodo) {
            
            } else {
                operando2 = ((Variable) ts.resolver(valor2.getNombre())).getValor();
            }
        }
    
        if (operador == null) {
            v.setValor(operando1);
        } else {
            switch (operador.getNombre()) {
                case "+":
                    v.setValor(operando1 + operando2);
                    break;
                case "-":
                    v.setValor(operando1 - operando2);
                    break;
                case "*":
                    v.setValor(operando1 * operando2);
                    break;
                case "/":
                    if (operando2 == 0) {
                        throw new SemanticExpression("Error: división por cero");
                    }
                    v.setValor(operando1 / operando2);
                    break;
                default:
                    throw new SemanticExpression("Operador no soportado: " + operador.getNombre());
            }
        }
    
        return saltoVerdadero;
    }
}