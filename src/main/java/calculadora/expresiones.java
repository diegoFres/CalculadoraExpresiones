package calculadora;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;
import java.util.StringTokenizer;

public class expresiones {

    public static void main(String[] args) {

        Scanner nota = new Scanner(System.in);
        String inputString;

        while (true) {
            System.out.println("escribe una expresión en notación infija o 'quit' para salir:");
            inputString = nota.nextLine();

            if (inputString.equalsIgnoreCase("quit")) {
                break;
            }

            ArrayList<String> tokens = tokens(inputString);
            ArrayList<String> postfix = convertToPostfix(tokens);

            System.out.println("notación postfija: " + toString(postfix));
        }

        nota.close();
    }

    // Convierte infija a tokens
    public static ArrayList<String> tokens(String input) {
        ArrayList<String> tokenL = new ArrayList<>();
        StringTokenizer st = new StringTokenizer(input, "()+-*/^ ", true);

        while (st.hasMoreTokens()) {
            String token = st.nextToken().trim();  // Eliminamos espacios en blanco

            if (!token.isEmpty()) {
                tokenL.add(token);
            }
        }

        return tokenL;
    }

    // Convierte una lista de tokens en notación infija a postfija
    public static ArrayList<String> convertToPostfix(ArrayList<String> infixTokens) {
        Stack<String> sk = new Stack<>();
        ArrayList<String> output = new ArrayList<>();

        for (String token : infixTokens) {
            // Si es operando poner en la salida
            if (isOperand(token)) {
                output.add(token);
            } else if (token.equals("(")) {
                // Si es parentesis meter a la pila
                sk.push(token);
            } else if (token.equals(")")) {
                // Sacar operadores hasta encontrar '('
                while (!sk.isEmpty() && !sk.peek().equals("(")) {
                    output.add(sk.pop());
                }
                // Quitar parentesis
                sk.pop();
            } else if (operador(token)) {

                while (!sk.isEmpty() && precedencia(sk.peek()) > precedencia(token)) {
                    output.add(sk.pop());
                }

                // Si el operador es exponenciación es a la derecha
                if (token.equals("^") && !sk.isEmpty() && sk.peek().equals("^")) {
                    // Mantener exponenciación en el sk
                    sk.push(token);
                } else {
                    sk.push(token);
                }
            }
        }

        // Sacar los operadores que quedan pila
        while (!sk.isEmpty()) {
            output.add(sk.pop());
        }

        return output;
    }

    // Verifica si el token es un operando (número)
    public static boolean isOperand(String token) {
        try {
            Double.parseDouble(token);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Verifica si el token es un operador
    public static boolean operador(String token) {
        return token.equals("+") || token.equals("-") ||
                token.equals("*") || token.equals("/") || token.equals("^");
    }

    // precedencia del operador
    public static int precedencia(String token) {
        switch (token) {
            case "^":
                return 3;
            case "*":
            case "/":
                return 2;
            case "+":
            case "-":
                return 1;
            default:
                return 0;
        }
    }

    // Convierte una lista de tokens a una cadena de texto
    public static String toString(ArrayList<String> list) {
        StringBuilder output = new StringBuilder();
        for (String token : list) {
            output.append(token).append(" ");
        }
        return output.toString().trim();
    }
}
