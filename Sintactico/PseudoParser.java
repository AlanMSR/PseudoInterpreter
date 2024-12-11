package Sintactico;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import Lexico.TipoToken;
import Lexico.Token;
import Lexico.PseudoLexer;

import TablaDeSimbolos.TipoIncorporado;
import TablaDeSimbolos.Metodo;
import TablaDeSimbolos.SemanticExpression;
import TablaDeSimbolos.PseudoGenerador;
import TablaDeSimbolos.Scope;
import TablaDeSimbolos.ScopeLocal;
import TablaDeSimbolos.Variable;

public class PseudoParser {
    private ArrayList<Token> tokens;
    private int indiceToken = 0;
    private SyntaxException ex;

    private Scope ts;
    private TipoIncorporado real;

    private Stack<PseudoGenerador> generadorStack;
    private PseudoGenerador generador;

    public PseudoParser(Scope ts, PseudoGenerador generador) {
        this.ts = ts;
        this.generador = generador;
        this.generadorStack = new Stack<>();
    }

    public void analizar(PseudoLexer lexer) throws SyntaxException, SemanticExpression {
        tokens = lexer.getTokens();

        real = new TipoIncorporado("real");
        ts.definir(real);

        if (Programa()) {
            if (indiceToken == tokens.size()) {
                System.out.println("\nLa sintaxis del programa es valida");
                return;
            }
        }

        throw ex;
    }

    // <Programa> -> inicio-programa <Enunciados> fin-programa
    private boolean Programa() throws SemanticExpression {
        while(FuncionDef())
            continue;
        if (match(TipoToken.INICIOPROGRAMA))
            if (Enunciados())
                if (match(TipoToken.FINPROGRAMA)) {
                    generador.crearTuplaFinPrograma();
                    return true;
                }

        return false;
    }

    // <Enunciados> -> <Enunciado> <Enunciados>
    private boolean Enunciados() throws SemanticExpression {
        int indiceAux = indiceToken;

        if (Enunciado()) {
            while (Enunciado());
            return true;
        }

        indiceToken = indiceAux;
        return false;
    }

    // <Enunciado> -> <Declaracion de variable> | <Asignacion> | <Leer> | <Escribir>
    // | <Si> | <Mientras> | <Repite>
    private boolean Enunciado() throws SemanticExpression {
        int indiceAux = indiceToken;

        if (currentToken(TipoToken.DEF))
            throw new SemanticExpression("No es posible declarar funciones anidadas");

        if (FuncionInv())
            return true;

        indiceToken = indiceAux;

        if (currentToken(TipoToken.VARIABLEDECL))
            if (VariableDecl())
                return true;

        indiceToken = indiceAux;

        if (currentToken(TipoToken.VARIABLE))
            if (Asignacion())
                return true;

        indiceToken = indiceAux;

        if (currentToken(TipoToken.LEER))
            if (Leer())
                return true;

        indiceToken = indiceAux;

        if (currentToken(TipoToken.ESCRIBIR))
            if (Escribir())
                return true;

        indiceToken = indiceAux;

        if (currentToken(TipoToken.SI))
            if (Si())
                return true;

        indiceToken = indiceAux;

        if (currentToken(TipoToken.MIENTRAS))
            if (Mientras())
                return true;

        indiceToken = indiceAux;

        if (currentToken(TipoToken.REPITE))
            if (Repite())
                return true;
        
        indiceToken = indiceAux;

        if (currentToken(TipoToken.REGRESA))
            if (Regresa())
                return true;

        indiceToken = indiceAux;
        return false;
    }

    // <VariableDecl> -> Variables: VARIABLE, VARIABLE...
    private boolean VariableDecl() throws SemanticExpression {
        int indiceAux = indiceToken;

        if (match(TipoToken.VARIABLEDECL)) {
            if (!match(TipoToken.DOSPUNTOS)) {
                indiceToken = indiceAux;
                return false;
            }
            // minimo una variable
            if (!match(TipoToken.VARIABLE)) {
                indiceToken = indiceAux;
                return false;
            }

            ts.definir(new Variable(tokens.get(indiceToken - 1).getNombre(), real));

            // mas variables
            while (match(TipoToken.COMA)) {
                if (!match(TipoToken.VARIABLE)) {
                    indiceToken = indiceAux;
                    return false; // Si hay coma, pero no otra variable
                }

                ts.definir(new Variable(tokens.get(indiceToken - 1).getNombre(), real));
            }

            return true;
        }

        indiceToken = indiceAux;
        return false;
    }

    // <Asignacion> -> VARIABLE = <Expresion>
    private boolean Asignacion() throws SemanticExpression {
        int indiceAux = indiceToken;

        if (match(TipoToken.VARIABLE)) {
            if (ts.resolver(tokens.get(indiceToken - 1).getNombre()) == null) {
                return false;
            }
            if (match(TipoToken.IGUAL))
                if (Expresion()) {
                    if (indiceToken - indiceAux >= 6) {
                        // funciones
                        List<Token> subLista = tokens.subList(indiceAux, indiceToken);
                        generador.crearTuplaAsignacion(indiceAux, indiceToken, subLista, ts);
                        return true;
                    } else {
                        generador.crearTuplaAsignacion(indiceAux, indiceToken);
                        return true;
                    }
                }
        }

        indiceToken = indiceAux;
        return false;
    }

    // <Expresion> -> <Valor> <Operador aritmetico> <Valor> | <Valor>
    private boolean Expresion() throws SemanticExpression {
        int indiceAux = indiceToken;

        if (Valor())
            if (match(TipoToken.OPARITMETICO))
                if (Valor())
                    return true;

        indiceToken = indiceAux;

        if (Valor())
            return true;

        indiceToken = indiceAux;
        return false;
    }

    // <Valor> -> VARIABLE | NUMERO | <Invocacion>
    private boolean Valor() throws SemanticExpression {
        int indiceAux = indiceToken;

        if (FuncionInv()) {
            return true;
        }

        indiceToken = indiceAux;

        if (match(TipoToken.VARIABLE))
            if (ts.resolver(tokens.get(indiceToken - 1).getNombre()) != null)
                return true;

        indiceToken = indiceAux;

        if (match(TipoToken.NUMERO))
            return true;

        indiceToken = indiceAux;
        return false;
    }

    // <Leer> -> leer VARIABLE
    private boolean Leer() {
        int indiceAux = indiceToken;

        if (match(TipoToken.LEER))
            if (match(TipoToken.VARIABLE)) {
                generador.crearTuplaLeer(indiceAux);
                return true;
            }

        indiceToken = indiceAux;
        return false;
    }

    // <Escribir> -> escribir CADENA , VARIABLE | escribir CADENA | escribir
    // VARIABLE
    private boolean Escribir() {
        int indiceAux = indiceToken;

        if (match(TipoToken.ESCRIBIR))
            if (match(TipoToken.CADENA))
                if (match(TipoToken.COMA))
                    if (match(TipoToken.VARIABLE)) {
                        generador.crearTuplaEscribir(indiceAux+1, indiceToken);
                        return true;
                    }

        indiceToken = indiceAux;

        if (match(TipoToken.ESCRIBIR))
            if (match(TipoToken.CADENA)) {
                generador.crearTuplaEscribir(indiceAux+1, indiceToken);
                return true;
            }

        indiceToken = indiceAux;

        if (match(TipoToken.ESCRIBIR))
            if (match(TipoToken.VARIABLE)) {
                generador.crearTuplaEscribir(indiceAux+1, indiceToken);
                return true;
            }

        indiceToken = indiceAux;
        return false;
    }

    // <Si> -> si <Comparacion> entonces <Enunciados> fin-si
    private boolean Si() throws SemanticExpression {
        int indiceAux = indiceToken;
        int indiceTupla = generador.getTuplas().size();

        if (match(TipoToken.SI))
            if (Comparacion())
                if (match(TipoToken.ENTONCES))
                    if (Enunciados())
                        if (match(TipoToken.FINSI)) {
                            generador.conectarSi(indiceTupla);
                            return true;
                        }

        indiceToken = indiceAux;
        return false;
    }

    private boolean Mientras() throws SemanticExpression {
        int indiceAux = indiceToken;
        int indiceTupla = generador.getTuplas().size();

        if (match(TipoToken.MIENTRAS)) {
            ts = new ScopeLocal(ts);
            if (Comparacion())
                if (Enunciados())
                    if (match(TipoToken.FINMIENTRAS)) {
                        generador.conectarMientras(indiceTupla);
                        ts = ts.getEnclosingScope();
                        return true;
                    }
        }

        indiceToken = indiceAux;
        return false;
    }

    // <Repite> -> repite ( VARIABLE, <Valor>, VARIABLE ) <Enunciados> fin-repite
    private boolean Repite() throws SemanticExpression {
        int indiceAux = indiceToken;
        int indiceTupla = generador.getTuplas().size();
        
        if (match(TipoToken.REPITE)) {
            ts = new ScopeLocal(ts);
            if (match(TipoToken.PARENTESISIZQ))
                if (match(TipoToken.VARIABLE)) {
                    Token token2 = tokens.get(indiceToken - 1);
                    if (match(TipoToken.COMA))
                        if (Comparacion())
                            if (match(TipoToken.COMA))
                                if (match(TipoToken.VARIABLE) || match(TipoToken.NUMERO)) {
                                    Token token = tokens.get(indiceToken - 1);
                                    if (match(TipoToken.PARENTESISDER))
                                        if (Enunciados())
                                            if (match(TipoToken.FINREPITE)) {
                                                System.out.println(tokens.get(indiceToken).getNombre());
                                                generador.conectarRepite(indiceTupla, token2, token, ts);
                                                ts = ts.getEnclosingScope();
                                                return true;
                                            }
                                }
                }
        }
        
        indiceToken = indiceAux;
        return false;
    }

    // <Comparacion> -> ( <Valor <Operador relacional> <Valor> )
    private boolean Comparacion() throws SemanticExpression {
        int indiceAux = indiceToken;

        if (match(TipoToken.PARENTESISIZQ))
            if (Valor())
                if (match(TipoToken.OPRELACIONAL))
                    if (Valor())
                        if (match(TipoToken.PARENTESISDER)) {
                            generador.crearTuplaComparacion(indiceAux+1);
                            return true;
                        }

        indiceToken = indiceAux;
        return false;
    }

    // funcion -> def <Variable> ( <Variable>,... | Nada ) <Enunciados> | Nada end-def
    private boolean FuncionDef() throws SemanticExpression {
        int indiceAux = indiceToken;
        
        if (match(TipoToken.DEF)) {
            PseudoGenerador funcionTupla = new PseudoGenerador(tokens);
            generadorStack.push(generador);
            generador = funcionTupla;
            if (match(TipoToken.VARIABLE)) {
                String nombreFuncion = tokens.get(indiceToken - 1).getNombre();
                
                if (match(TipoToken.PARENTESISIZQ)) {
                    Variable[] parametros = parsearParametros();
    
                    if (match(TipoToken.PARENTESISDER)) {
                        Metodo funcion = new Metodo(nombreFuncion, parametros, ts);
                        ts.definir(funcion);
                        ts = funcion;
    
                        if (Enunciados()) {
                            if (match(TipoToken.ENDDEF)) {
                                generador.crearTuplaFinMetodo();
                                funcion.setTuplas(generador.getTuplas());
                                generador = generadorStack.pop();
                                ts = ts.getEnclosingScope();
                                return true;
                            }
                        } else if (match(TipoToken.ENDDEF)) {
                            generador.crearTuplaFinMetodo();
                            funcion.setTuplas(generador.getTuplas());
                            generador = generadorStack.pop();
                            ts = ts.getEnclosingScope();
                            return true;
                        }
                    }
                }
            }
        }
    
        indiceToken = indiceAux;
        return false;
    }

    private Variable[] parsearParametros() throws SemanticExpression {
        ArrayList<Variable> simbolos = new ArrayList<>();
    
        if (match(TipoToken.VARIABLE)) {
            simbolos.add(new Variable(tokens.get(indiceToken - 1).getNombre(), real));
    
            // Parsear más variables si hay comas
            while (match(TipoToken.COMA)) {
                if (match(TipoToken.VARIABLE)) {
                    simbolos.add(new Variable(tokens.get(indiceToken - 1).getNombre(), real));
                } else {
                    throw new SemanticExpression("Se esperaba una variable después de la coma");
                }
            }
        }
        
        return simbolos.toArray(new Variable[0]);
    }

    // <Invocacion> -> <Variable> ( <Variable>, ... )
    private boolean FuncionInv() throws SemanticExpression {
        int indiceAux = indiceToken;
    
        if (match(TipoToken.VARIABLE)) { // deberia haber una funcion separada

            if (match(TipoToken.PARENTESISIZQ)) {
                
                Metodo funcion = (Metodo) ts.resolver(tokens.get(indiceToken - 2).getNombre());
                
                if (funcion == null)
                    return false;

                if (match(TipoToken.NUMERO) || match(TipoToken.VARIABLE)) {
                    while (match(TipoToken.COMA)) {
                        if (!(match(TipoToken.VARIABLE) || match(TipoToken.NUMERO))) {
                            indiceToken = indiceAux;
                            return false; // Si hay coma pero no hay otra variable o numero
                        }
                    }
                }
                if (match(TipoToken.PARENTESISDER)) {
                    return true;
                } 
            }
        }
    
        indiceToken = indiceAux;
        return false;
    }

    private boolean Regresa() {
        int indiceAux = indiceToken;
    
        if (match(TipoToken.REGRESA)) {
            if (match(TipoToken.VARIABLE) || match(TipoToken.NUMERO)) {
                generador.crearTuplaRegresa(indiceAux, indiceToken);
                return true;
            }
        }
    
        indiceToken = indiceAux;
        return false;
    }
    
    private boolean match(String nombre) {
        if (currentToken(nombre)) {
            System.out.println(nombre + ": " + tokens.get(indiceToken).getNombre());
            
            indiceToken++;
            return true;
        }

        if (ex == null)
            ex = new SyntaxException(nombre, tokens.get(indiceToken).getTipo().getNombre());

        return false;
    }

    private boolean currentToken(String name) {
        return tokens.get(indiceToken).getTipo().getNombre().equals(name);
    }
}