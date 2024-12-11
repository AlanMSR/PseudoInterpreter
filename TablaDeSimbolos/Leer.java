package TablaDeSimbolos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import Lexico.Token;

public class Leer extends Tupla {
    Token variable;

    public Leer(Token variable, int sv, int sf) {
        super(sv, sf);
        this.variable = variable;
    }

    public String toString() {
        return "( " + super.toString() + ", [ " + variable + " ] )";
    }

    public int ejecutar(Scope ts) throws SemanticExpression {
        String valor = "0.0";
        // System.out.print("Ingrese el valor de " + variable.getNombre() + ": ");
        BufferedReader entrada  = new BufferedReader(new InputStreamReader(System.in));

        try {
            valor = entrada.readLine();
        } catch (IOException e) {}

        Variable v = (Variable) ts.resolver(variable.getNombre());

        try {
            v.setValor(Float.parseFloat(valor));
        } catch (NumberFormatException e) {
            System.out.println("Error: valor no es un número");
        }

        return saltoVerdadero;
    }
}
