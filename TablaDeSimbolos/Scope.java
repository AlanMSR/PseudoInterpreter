package TablaDeSimbolos;

import java.util.List;

public interface Scope {
    public String getScopeNombre();
    public Scope getEnclosingScope();
    public void definir(Simbolo simbolo) throws SemanticExpression;
    public Simbolo resolver(String nombre) throws SemanticExpression;
    List<Metodo> getMetodos();
}
