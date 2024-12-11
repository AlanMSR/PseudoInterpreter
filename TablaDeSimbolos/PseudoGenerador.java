package TablaDeSimbolos;

import java.util.ArrayList;
import java.util.List;

import Lexico.TipoToken;
import Lexico.Token;

public class PseudoGenerador {
    private ArrayList<Tupla> tuplas = new ArrayList<Tupla>();
    ArrayList<Token> tokens;
    
    public PseudoGenerador(ArrayList<Token> tokens) {
        this.tokens = tokens;
    }

    public void setTuplas(ArrayList<Tupla> tuplas) {
        this.tuplas = tuplas;
    }

    public void crearTuplaAsignacion(int indiceInicial, int indiceFinal) {
        if (indiceFinal - indiceInicial == 3)
            tuplas.add(new Asignacion(tokens.get(indiceInicial), 
                                      tokens.get(indiceInicial+2), 
                                      tuplas.size()+1, tuplas.size()+1));
        else if (indiceFinal - indiceInicial == 5)
            tuplas.add(new Asignacion(tokens.get(indiceInicial),
                                      tokens.get(indiceInicial+2),
                                      tokens.get(indiceInicial+3),
                                      tokens.get(indiceInicial+4),
                                      tuplas.size()+1, tuplas.size()+1));
    }

    public void crearTuplaAsignacion(Token v, Token n) {
        Token token = new Token(new TipoToken(TipoToken.OPARITMETICO, "[*/+-]"), "+");
        tuplas.add(new Asignacion(v,
                                  v,
                                  token,
                                  n,
                                  tuplas.size()+1, tuplas.size()+1));
    }

    public void crearTuplaAsignacion(int indiceInicial, int indiceFinal, List<Token> subLista, Scope scope) throws SemanticExpression {
        tuplas.add(new AsignacionCF(indiceInicial, 
                                    indiceFinal, 
                                    subLista, 
                                    scope,
                                    tuplas.size()+1, tuplas.size()+1));
    }

    public void crearTuplaLeer(int indiceInicial) {
        tuplas.add(new Leer(tokens.get(indiceInicial+1),
                            tuplas.size()+1, tuplas.size()+1));
    }

    public void crearTuplaEscribir(int indiceInicial, int indiceFinal) {
        if (indiceFinal - indiceInicial == 1)
            tuplas.add(new Escribir(tokens.get(indiceInicial),
                                    tuplas.size()+1, tuplas.size()+1));
        else if (indiceFinal - indiceInicial == 3)
            tuplas.add(new Escribir(tokens.get(indiceInicial),
                                    tokens.get(indiceInicial+2),
                                    tuplas.size()+1, tuplas.size()+1));
    }

    public void crearTuplaComparacion(int indiceInicial) {
        tuplas.add(new Comparacion(tokens.get(indiceInicial),
                                   tokens.get(indiceInicial+1),
                                   tokens.get(indiceInicial+2),
                                   tuplas.size()+1, tuplas.size()+1));
    }

    public void crearTuplaRegresa(int indiceInicial, int indiceFinal) {
        tuplas.add(new Regresa(tokens.get(indiceInicial+1),
                              tuplas.size()+1, tuplas.size()+1));
    }

    public void crearTuplaFinMetodo() {
        tuplas.add(new FinMetodo());
    }

    public void crearTuplaFinPrograma() {
        tuplas.add(new FinPrograma());
    }

    public void conectarSi(int tuplaInicial) {
        int tuplaFinal = tuplas.size()-1;

        if (tuplaInicial >= tuplas.size() || tuplaInicial >= tuplaFinal)
            return;
        
        tuplas.get(tuplaInicial).setSaltoFalso(tuplaFinal+1);
    }

    public void conectarMientras(int tuplaInicial) {
        int tuplaFinal = tuplas.size() - 1;
    
        if (tuplaInicial >= tuplas.size() || tuplaInicial >= tuplaFinal)
            return;
    
        tuplas.get(tuplaInicial).setSaltoFalso(tuplaFinal + 1);
        tuplas.get(tuplaFinal).setSaltoVerdadero(tuplaInicial);
        tuplas.get(tuplaFinal).setSaltoFalso(tuplaInicial);
    
        for (int i = tuplaFinal; i > tuplaInicial; i--) {
            Tupla t = tuplas.get(i);
    
            if (t instanceof Comparacion && t.getSaltoFalso() == tuplaFinal + 1)
                t.setSaltoFalso(tuplaInicial);
        }
    }

    public void conectarRepite(int tuplaInicial, Token token, Token token2, Scope ts) throws SemanticExpression {
        // int tuplaFinal = tuplas.size() - 1;
    
        // if (tuplaInicial >= tuplas.size() || tuplaInicial >= tuplaFinal)
        //     return;

        crearTuplaAsignacion(token, token2);
        int tuplaFinal = tuplas.size() - 1;
        System.out.println("tupla inicial: " + tuplaInicial);
        tuplas.get(tuplaInicial).setSaltoFalso(tuplaFinal + 1);
        tuplas.get(tuplaFinal).setSaltoVerdadero(tuplaInicial);
        tuplas.get(tuplaFinal).setSaltoFalso(tuplaInicial);
    
        // Ajustar saltos intermedios.
        for (int i = tuplaFinal; i > tuplaInicial; i--) {
            Tupla t = tuplas.get(i);
            if (t instanceof Comparacion && t.getSaltoFalso() == tuplaFinal + 1)
                t.setSaltoFalso(tuplaInicial + 1); // Salir del ciclo si la condición falla.
        }
    }
    
    public ArrayList<Tupla> getTuplas() {
        return tuplas;
    }
}