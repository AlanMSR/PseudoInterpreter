package TablaDeSimbolos;


public class FinPrograma extends Tupla {
    public FinPrograma() {
        super(-1, -1);
    }

    public String toString() {
        return "( " + super.toString() + ", [], " + " )";
    }

    public int ejecutar(Scope ts) throws SemanticExpression {
        return -1;
    }
}