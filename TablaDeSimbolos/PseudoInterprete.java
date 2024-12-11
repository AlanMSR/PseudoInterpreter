package TablaDeSimbolos;

import java.util.ArrayList;

public class PseudoInterprete {
    Scope ts;

    public PseudoInterprete(Scope ts) {
        this.ts = ts;
    }

    public void interpretar(ArrayList<Tupla>tuplas) {
        int indiceTupla = 0;
        Tupla t = tuplas.get(0);

        do {
            try {
                // indiceTupla = t.ejecutar(ts);
                indiceTupla = t.ejecutar(ts);
                t = tuplas.get(indiceTupla);
            } catch (SemanticExpression e) {
                System.out.println(e.getMessage());
                break;
            }
        } while (!(t instanceof FinPrograma));
    }
}