package TablaDeSimbolos;

import java.util.ArrayList;
import java.util.List;

public class TablaSimbolos implements Scope {
    private ArrayList<Simbolo> simbolos = new ArrayList<>();

    public TablaSimbolos() throws SemanticExpression {
        initTipos();
    }

    protected void initTipos() throws SemanticExpression {
        definir(new TipoIncorporado("int"));
        definir(new TipoIncorporado("float"));
    }

    public void definir(Simbolo simbolo) {
        for (Simbolo s: simbolos) {
            if (s.getNombre().equals(simbolo.getNombre())) {
                // throw new SemanticExpression("El simbolo " + simbolo.getNombre() + " ya fue declarado");
                throw new RuntimeException("El simbolo " + simbolo.getNombre() + " ya fue declarado");
            }
        }

        simbolos.add(simbolo);
    }

    public Simbolo resolver(String nombre) {
        for (Simbolo s: simbolos) {
            if (s.getNombre().equals(nombre)) {
                return s;
            }
        } 
        return null;
        // throw new SemanticExpression("El simbolo \"" + nombre + "\" no ha sido declarado");
    }

    public String getScopeNombre() {
        return "global";
    }

    public Scope getEnclosingScope() {
        return null;
    }

    public ArrayList<Simbolo> getSimbolos() {
        return simbolos;
    }

    public List<Metodo> getMetodos() {
        return null;
    }
}
