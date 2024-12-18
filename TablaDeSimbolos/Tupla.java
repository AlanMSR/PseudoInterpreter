package TablaDeSimbolos;

public abstract class Tupla {
    protected int saltoVerdadero, saltoFalso;

    public Tupla(int sv, int sf) {
        saltoVerdadero = sv;
        saltoFalso = sf;
    }

    public void setSaltoVerdadero(int sv) {
        saltoVerdadero = sv;
    }

    public int getSaltoVerdadero() {
        return saltoVerdadero;
    }

    public void setSaltoFalso(int sf) {
        saltoFalso = sf;
    }

    public int getSaltoFalso() {
        return saltoFalso;
    }

    public String toString() {
        return this.getClass().getSimpleName() + ", " +
            saltoVerdadero + ", " + saltoFalso;
    }

    public abstract int ejecutar(Scope ts) throws SemanticExpression;
}
