package Lexico;

public class TipoToken {
    private String nombre;
    private String patron;

    public static String NUMERO = "NUMERO";
    public static String CADENA = "CADENA";
    public static String OPARITMETICO = "OPARITMETICO";
    public static String OPRELACIONAL = "OPRELACIONAL";
    public static String IGUAL = "IGUAL";
    public static String COMA = "COMA";
    public static String PARENTESISIZQ = "PARENTESISIZQ";
    public static String PARENTESISDER = "PARENTESISDER";
    public static String INICIOPROGRAMA = "INICIOPROGRAMA";
    public static String FINPROGRAMA = "FINPROGRAMA";
    public static String LEER = "LEER";
    public static String ESCRIBIR = "ESCRIBIR";
    public static String DEF = "DEF";
    public static String ENDDEF = "ENDDEF";
    public static String REGRESA = "REGRESA";
    public static String SI = "SI";
    public static String ENTONCES = "ENTONCES";
    public static String FINSI = "FINSI";
    public static String REPITE = "REPITE";
    public static String FINREPITE = "FINREPITE";
    public static String MIENTRAS = "MIENTRAS";
    public static String FINMIENTRAS = "FINMIENTRAS";
    public static String VARIABLEDECL = "VARIABLEDECL";
    public static String DOSPUNTOS = "DOSPUNTOS";
    public static String VARIABLE = "VARIABLE";
    public static String ESPACIO = "ESPACIO";
    public static String ERROR = "ERROR";

    public TipoToken(String nombre, String patron) {
        this.nombre = nombre;
        this.patron = patron;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public String getPatron() {
        return patron;
    }
}
