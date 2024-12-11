package TablaDeSimbolos;

import Lexico.Token;

public class Regresa extends Tupla {
    Token valor;

    public Regresa(Token valor, int sv, int sf) {
        super(sv, sf);
        this.valor = valor;
    }

    public String toString() {
        return "( " + super.toString() + ", [ \"" + valor + "\" ] )";
    }

    public int ejecutar(Scope ts) throws SemanticExpression {
        float valorR = 0;
        if (valor.getTipo().getNombre().equals("NUMERO")) {
            valorR = Float.parseFloat(valor.getNombre());
        } else {
            valorR = ((Variable) ts.resolver(valor.getNombre())).getValor();
        }

        Variable v = (Variable) ts.resolver("__valorRetorno");
        v.setValor(valorR);

        return saltoVerdadero;
    }
}