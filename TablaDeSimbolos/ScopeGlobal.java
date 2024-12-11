package TablaDeSimbolos;

import java.util.List;

public class ScopeGlobal extends ScopeBase {
    public ScopeGlobal(Scope currentScope) {
        super(null);
    }

    public String getScopeNombre() {
        return "global";
    }

    @Override
    public List<Metodo> getMetodos() {
        return super.getMetodos();
    }
}
