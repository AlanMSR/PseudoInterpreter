# PseudoInterpreter
Pseudo interpreter in Java

## Overview

PseudoInterpreter is a Java-based interpreter for a pseudo-language. It reads a source file, performs lexical, syntactic, and semantic analysis, and then executes the parsed instructions.


## How to Build and Run

### Prerequisites

- Java Development Kit (JDK) installed
- A terminal or command prompt

### Build

1. Open a terminal in the root directory of your project.
2. Compile the Java files:

   ```sh
   javac ./Sintactico/PseudoInterpreter.java
   ```

### Run

1. Execute the `PseudoInterpreter` class with the source file as an argument:

   ```sh
   java Sintactico.PseudoInterpreter textfile-route
   ```

## Example

The `pse.txt` file contains an example pseudo-language program:

```txt
def mayor(x, y)
    si (x > y) entonces
        regresa x
    fin-si
    regresa y
end-def

inicio-programa
    variables: a, b, r

    escribir "Ingresa a: "
    leer a

    repite(a, (a < 3), 1)
        escribir "holaa ", a
    fin-repite

fin-programa
```