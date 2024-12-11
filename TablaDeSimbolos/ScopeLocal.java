package TablaDeSimbolos;

public class ScopeLocal extends ScopeBase {

    public ScopeLocal(Scope currentScope) {
        super(currentScope);
    }

    public String getScopeNombre() {
        return "local";
    }
}
