import java.util.*;

public class Calculadora {

    public static void main(String[] args) {
        Scanner escaner = new Scanner(System.in);
        while (true) {
            System.out.println("Introduce una expresión en notación infija como por ejemplo: (2+1)*2^2 (o 'salir' para terminar):");
            System.out.print("> ");
            String entrada = escaner.nextLine();

            if (entrada.equalsIgnoreCase("salir")) {
                break;
            }

            try {
                List<String> tokens = obtenerTokens(entrada);
                List<String> postfijo = aPostfijo(tokens);
                double resultado = evaluarPostfijo(postfijo);

                System.out.println("Resultado: " + resultado);
            } catch (Exception e) {
                System.out.println("Error en la expresión: " + e.getMessage());
            }
        }
        escaner.close();
    }

    // infijo a postfijo
    public static List<String> aPostfijo(List<String> tokensInfijo) {
        Stack<String> pila = new Stack<>();
        List<String> salida = new ArrayList<>();

        for (String token : tokensInfijo) {
            if (esOperando(token)) {
                salida.add(token);
            } else if (token.equals("(")) {
                pila.push(token);
            } else if (token.equals(")")) {
                while (!pila.isEmpty() && !pila.peek().equals("(")) {
                    salida.add(pila.pop());
                }
                pila.pop();
            } else if (esOperador(token)) {
                while (!pila.isEmpty() && obtenerPrecedencia(token) <= obtenerPrecedencia(pila.peek())) {
                    salida.add(pila.pop());
                }
                pila.push(token);
            }
        }

        while (!pila.isEmpty()) {
            salida.add(pila.pop());
        }

        return salida;
    }

    public static double evaluarPostfijo(List<String> tokensPostfijo) {
        Stack<Double> pila = new Stack<>();

        for (String token : tokensPostfijo) {
            if (esOperando(token)) {
                pila.push(Double.parseDouble(token));
            } else if (esOperador(token)) {
                double b = pila.pop();
                double a = pila.pop();
                switch (token) {
                    case "+":
                        pila.push(a + b);
                        break;
                    case "-":
                        pila.push(a - b);
                        break;
                    case "*":
                        pila.push(a * b);
                        break;
                    case "/":
                        pila.push(a / b);
                        break;
                    case "^":
                        pila.push(Math.pow(a, b));
                        break;
                }
            }
        }

        return pila.pop();
    }

    public static boolean esOperador(String token) {
        return "+-*/^".contains(token);
    }

    public static boolean esOperando(String token) {
        try {
            Double.parseDouble(token);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static int obtenerPrecedencia(String operador) {
        switch (operador) {
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

    public static List<String> obtenerTokens(String expresion) {
        StringTokenizer tokenizador = new StringTokenizer(expresion, "()+-*/^ ", true);
        List<String> tokens = new ArrayList<>();

        while (tokenizador.hasMoreTokens()) {
            String token = tokenizador.nextToken().trim();
            if (!token.isEmpty()) {
                tokens.add(token);
            }
        }

        return tokens;
    }
}
