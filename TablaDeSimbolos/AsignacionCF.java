package TablaDeSimbolos;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Lexico.TipoToken;
import Lexico.Token;

public class AsignacionCF extends Tupla {
    List<Token> subLista;

    Token variable;
    ArrayList<Token> funciones;
    ArrayList<List<Token>> argumentos;
    Token operador;
    Token valor;
    Scope scope;

    public AsignacionCF(int indiceInicial, int indiceFinal, List<Token> subLista, Scope scope, int sv, int sf)
            throws SemanticExpression {
        super(sv, sf);
        this.scope = scope;
        this.subLista = subLista;
        this.funciones = new ArrayList<Token>();
        this.argumentos = new ArrayList<List<Token>>();
        crearTuplaAsignacion();
    }

    public void crearTuplaAsignacion() throws SemanticExpression {
        variable = subLista.get(0);
        int index = 2; // despues de "="

        while (index < subLista.size()) {
            if (subLista.get(index).getTipo().getNombre().equals(TipoToken.VARIABLE)) {
                if (scope.resolver(subLista.get(index).getNombre()) instanceof Metodo) {
                    funciones.add(subLista.get(index));

                    index++;
                    List<Token> argumentosFuncion = new ArrayList<Token>();
                    while (index < subLista.size()
                            && !subLista.get(index).getTipo().getNombre().equals(TipoToken.PARENTESISDER)) {
                        if (subLista.get(index).getTipo().getNombre().equals(TipoToken.COMA) ||
                                subLista.get(index).getTipo().getNombre().equals(TipoToken.PARENTESISIZQ)) {
                            index++;
                            continue;
                        }
                        argumentosFuncion.add(subLista.get(index));
                        index++;
                    }
                    argumentos.add(argumentosFuncion);
                } else {
                    valor = subLista.get(index);
                    index++;
                }
            } else if (subLista.get(index).getTipo().getNombre().equals(TipoToken.OPARITMETICO)) {
                operador = subLista.get(index);
                index++;
            } else {
                if (subLista.get(index).getTipo().getNombre().equals(TipoToken.NUMERO) ||
                        subLista.get(index).getTipo().getNombre().equals(TipoToken.VARIABLE)) {
                    valor = subLista.get(index);
                    index++;
                } else {
                    index++;
                }
            }
        }
    }

    public String toString() {
        if (valor == null) {
            if (funciones.size() == 1) {
                return "( " + super.toString() + ", [ \"" + variable + ", " + funciones.get(0) + ", "
                        + argumentos.get(0) + "\" ] )";
            } else {
                return "( " + super.toString() + ", [ \"" + variable + ", [" + funciones.get(0) + ", "
                        + argumentos.get(0) + "\"]" + "[" + funciones.get(1) + ", " + argumentos.get(1) + "\"]" + "] )";
            }
        } else {
            int valorIndex = subLista.indexOf(valor);
            int funcionIndex = subLista.indexOf(funciones.get(0));
            System.out.println("valorIndex: " + valorIndex + " valorFuncion: " + funcionIndex);
            if (valorIndex < funcionIndex) {
                return "( " + super.toString() + ", [ \"" + variable + ", " + valor + "\" ]" + "[" + funciones.get(0)
                        + ", " + argumentos.get(0) + "\"] )";
            } else {
                return "( " + super.toString() + ", [ \"" + variable + ", [" + funciones.get(0) + ", "
                        + argumentos.get(0) + "\"]" + valor + "\" ] )";
            }
        }
    }

        public int ejecutar(Scope ts) throws SemanticExpression {
        if (valor == null) {
            if (funciones.size() == 2) {
                ejecutarMetodo(ts, funciones.get(0));
                ejecutarMetodo(ts, funciones.get(1));
                
                Metodo v1 = (Metodo) ts.resolver(funciones.get(0).getNombre());
                Metodo v2 = (Metodo) ts.resolver(funciones.get(1).getNombre());
                
                float vrv1 = ((Variable) v1.resolver("__valorRetorno")).getValor();
                float vrv2 = ((Variable) v2.resolver("__valorRetorno")).getValor();
                
                Variable v = (Variable) ts.resolver(variable.getNombre());
                realizarOperacion(v, vrv1, vrv2);
            } else {
                Variable v = (Variable) ts.resolver(variable.getNombre());
    
                ejecutarMetodo(ts, funciones.get(0));
                Metodo v1 = (Metodo) ts.resolver(funciones.get(0).getNombre());
                float vrv1 = ((Variable) v1.resolver("__valorRetorno")).getValor();
    
                v.setValor(vrv1);
            }  
        } else {
            ejecutarMetodo(ts, funciones.get(0));
            Metodo v1 = (Metodo) ts.resolver(funciones.get(0).getNombre());
            float vrv1 = ((Variable) v1.resolver("__valorRetorno")).getValor();
    
            Variable v = (Variable) ts.resolver(variable.getNombre());
            float valorR;
    
            if (valor.getTipo().getNombre().equals(TipoToken.NUMERO)) {
                valorR = Float.parseFloat(valor.getNombre());
            } else {
                valorR = ((Variable) ts.resolver(valor.getNombre())).getValor();
            }
    
            realizarOperacion(v, vrv1, valorR);
        }
    
        return saltoVerdadero;
    }
    
    private void realizarOperacion(Variable v, float vrv1, float vrv2) throws SemanticExpression {
        switch (operador.getNombre()) {
            case "+":
                v.setValor(vrv1 + vrv2);
                break;
            case "-":
                v.setValor(vrv1 - vrv2);
                break;
            case "*":
                v.setValor(vrv1 * vrv2);
                break;
            case "/":
                if (vrv2 == 0) {
                    throw new SemanticExpression("Error: división por cero");
                }
                v.setValor(vrv1 / vrv2);
                break;
            default:
                throw new SemanticExpression("Operador no soportado: " + operador.getNombre());
        }
    }

    private void ejecutarMetodo(Scope ts, Token funcionToken) throws SemanticExpression {
        Metodo funcion = (Metodo) scope.resolver(funcionToken.getNombre());
        if (funcion == null) {
            throw new SemanticExpression("Método no encontrado: " + funcionToken.getNombre());
        }
        
        asignarValorParams(funcion, funciones.indexOf(funcionToken));

        int indiceTupla = 0;
        Tupla tupla = funcion.getTuplas().get(0);
        do {
            try {
                // indiceTupla = tupla.ejecutar(ts);
                indiceTupla = tupla.ejecutar(funcion);
                tupla = funcion.getTuplas().get(indiceTupla);
            } catch (SemanticExpression e) {
                System.out.println(e.getMessage());
                break;
            }
        } while (!(tupla instanceof FinMetodo) && !(tupla instanceof Regresa));

        if (tupla instanceof Regresa) {
            tupla.ejecutar(funcion);
        }
    }

    public void asignarValorParams(Metodo funcion, int indexFuncion) throws SemanticExpression {
        Map<String, Simbolo> miembros = funcion.getMiembros();
        int n_params = funcion.getNParams();
        List<Token> argumentosFuncion = argumentos.get(indexFuncion);

        if (n_params != argumentosFuncion.size()) {
            throw new SemanticExpression("No existe la funcion/Número de argumentos incorrecto");
        }

        List<Simbolo> miembrosList = new ArrayList<>(miembros.values());
        int index = 0;
        for (int i = 0; i < n_params; i++) {
            Variable v = (Variable) miembrosList.get(i);
            Token argumento = argumentosFuncion.get(index);

            if (argumento.getTipo().getNombre().equals(TipoToken.NUMERO)) {
                v.setValor(Float.parseFloat(argumento.getNombre()));
            } else {
                Variable v2 = (Variable) scope.resolver(argumento.getNombre());

                v.setValor(v2.getValor());
            }
            index++;
        }
    }
}