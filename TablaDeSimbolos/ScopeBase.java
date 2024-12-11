package TablaDeSimbolos;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class ScopeBase implements Scope {
    Scope enclosingScope;
    Map<String, Simbolo> miembros = new LinkedHashMap<String, Simbolo>();

    public ScopeBase(Scope currentScope) {
        this.enclosingScope = currentScope;
    }

    public Scope getEnclosingScope() {
        return enclosingScope;
    }

    public void definir(Simbolo simbolo) throws SemanticExpression {
        if (miembros.containsKey(simbolo.getNombre())) {
            throw new SemanticExpression("El simbolo " + simbolo.getNombre() + " ya fue declarado");
        }

        miembros.put(simbolo.getNombre(), simbolo);
    }

    public Simbolo resolver(String nombre) throws SemanticExpression {
        Simbolo s = miembros.get(nombre);
    
        if (s != null) {
            return s;
        }

        if (enclosingScope != null)
            return enclosingScope.resolver(nombre);
    
        throw new SemanticExpression("El simbolo \"" + nombre + "\" no ha sido declarado");
    }

    public List<Metodo> getMetodos() {
        List<Metodo> metodos = new ArrayList<>();
        for (Simbolo simbolo : miembros.values()) {
            if (simbolo instanceof Metodo) {
                metodos.add((Metodo) simbolo);
            }
        }
        return metodos;
    }
}