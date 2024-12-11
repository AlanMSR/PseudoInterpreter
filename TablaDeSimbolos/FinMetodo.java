package TablaDeSimbolos;

public class FinMetodo extends Tupla {
    public FinMetodo() {
        super(-1, -1);
    }

    public String toString() {
        return "( " + super.toString() + ", [], " + " )";
    }

    public int ejecutar(Scope ts) throws SemanticExpression {
        return -1;
    }
}
