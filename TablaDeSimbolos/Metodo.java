package TablaDeSimbolos;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Metodo extends Simbolo implements Scope {
    private TipoIncorporado real;

    Scope enclosingScope;
    Map<String, Simbolo> miembros = new LinkedHashMap<String, Simbolo>();
    int n_params = 0;
    ArrayList<Tupla> tuplas = new ArrayList<Tupla>();

    public Metodo(String name, Variable[] orderedArgs, Scope enclosingScope) throws SemanticExpression {
        super(name);
        this.enclosingScope = enclosingScope;

        if (orderedArgs != null) {
            for (Variable v : orderedArgs) {
                definir(v);
                n_params++;
            }
        }

        // variable para retornos
        definir(new Variable("__valorRetorno", real));
    }

    // parche para pasar simbolos
    public Metodo(String name, Simbolo[] orderedArgs, Scope enclosingScope) throws SemanticExpression {
        super(name);
        this.enclosingScope = enclosingScope;

        if (orderedArgs != null)
            for (Simbolo v : orderedArgs)
                definir(v);

        definir(new Variable("__valorRetorno", real));
    }

    public int getNParams() {
        return n_params;
    }

    public String getScopeNombre() {
        return getNombre();
    }

    public Scope getEnclosingScope() {
        return this.enclosingScope;
    }

    public void definir(Simbolo s) {
        miembros.put(s.getNombre(), s);
    }

    public Simbolo resolver(String nombre) throws SemanticExpression {
        Simbolo s = miembros.get(nombre);
        if (s != null) {
            return s;
        }
        if (enclosingScope != null) {
            return enclosingScope.resolver(nombre);
        }
        return null;
    }

    public void setTuplas(ArrayList<Tupla> tuplas) {
        this.tuplas = tuplas;
    }

    public ArrayList<Tupla> getTuplas() {
        return tuplas;
    }

    public Map<String, Simbolo> getMiembros() {
        return miembros;
    }

    public List<Metodo> getMetodos() {
        return null;
    }
}