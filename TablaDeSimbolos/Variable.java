package TablaDeSimbolos;

public class Variable extends Simbolo {
    private float valor;

    public Variable(String nombre, Tipo tipo) {
        super(nombre, tipo);
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public float getValor() {
        return valor;
    }
}
