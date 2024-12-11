package Sintactico;

import Lexico.LexicalException;
import Lexico.PseudoLexer;
import Lexico.Token;

import TablaDeSimbolos.SemanticExpression;
import TablaDeSimbolos.TipoIncorporado;
import TablaDeSimbolos.Tupla;
import TablaDeSimbolos.Metodo;
import TablaDeSimbolos.PseudoGenerador;
import TablaDeSimbolos.PseudoInterprete;
import TablaDeSimbolos.Scope;
import TablaDeSimbolos.ScopeGlobal;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class PseudoInterpreter {
    public static void main(String[] args) throws LexicalException, SyntaxException, SemanticExpression, FileNotFoundException {
        
        // Leer el contenido del archivo
        File file = new File(args[0]);
        Scanner scanner = new Scanner(file);
        StringBuilder entrada = new StringBuilder();

        while (scanner.hasNextLine()) {
            entrada.append(scanner.nextLine()).append("\n");
        }
        scanner.close();

        PseudoLexer lexer = new PseudoLexer();
        lexer.analizar(entrada.toString());

        System.out.println("*** Analisis lexico ***");

        for (Token t : lexer.getTokens()) {
            System.out.println(t);
        }

        System.out.println("\n*** Analisis sintactico ***");
        Scope currentScope = new ScopeGlobal(null);
        currentScope.definir(new TipoIncorporado("int"));
        currentScope.definir(new TipoIncorporado("float"));
        currentScope.definir(new TipoIncorporado("void"));

        PseudoGenerador generador = new PseudoGenerador(lexer.getTokens());
        PseudoParser parser = new PseudoParser(currentScope, generador);
        parser.analizar(lexer);

        System.out.println("\n*** Funciones generadas ***\n");
        for (Metodo m : currentScope.getMetodos()) {
            System.out.println(m);
            for (Tupla t : m.getTuplas()) {
                System.out.println(t);
            }
        }

        System.out.println("\n*** Tuplas generadas ***\n");
        for (Tupla t : generador.getTuplas()) {
            System.out.println(t);
        }

        System.out.println("\n*** Ejecución del programa ***\n");
        PseudoInterprete interprete = new PseudoInterprete(currentScope);
        interprete.interpretar(generador.getTuplas());
    }
}