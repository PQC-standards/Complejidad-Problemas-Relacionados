package Algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class LWENaive {
    private static final Random random = new Random();

    // Genera un secreto aleatorio
    public static List<Integer> generateSecret(int dim, int maxValue) {
        List<Integer> secret = new ArrayList<>();
        for (int i = 0; i < dim; i++) {
            secret.add(random.nextInt(2 * maxValue + 1) - maxValue);
        }
        return secret;
    }


    // Función para generar muestras LWE
    public static List<LWESample> generateLWESamples(List<Integer> secret, int numSamples, int dim, int maxValue, int noiseStddev) {
        List<LWESample> samples = new ArrayList<>();
        
        Random rand = new Random();
        
        for (int i = 0; i < numSamples; i++) {
            // Generar vector 'a' aleatorio del retículo
            List<Integer> a = new ArrayList<>();
            for (int j = 0; j < dim; j++) {
                a.add(rand.nextInt(2 * maxValue + 1) - maxValue);  // Aleatorio en el rango [-maxValue, maxValue]
            }

            // Calcular b = a * s + e, donde 'e' es el error aleatorio
            int b = 0;
            for (int j = 0; j < dim; j++) {
                b += a.get(j) * secret.get(j);
            }

            // Añadir un error pequeño
            // Error en el rango [-noiseStddev, noiseStddev]
            int error = rand.nextInt(2 * noiseStddev + 1) - noiseStddev;
            b += error;  // Se suma el error

            // Crear una muestra LWE con 'a' y 'b'
            samples.add(new LWESample(a, b));
        }

        return samples;
    }
    public static List<Integer> solveLWE(List<LWESample> samples, List<List<Integer>> latticePoints, int noiseStddev) {
        int dim = latticePoints.get(0).size();
        try {
            for (List<Integer> candidate : latticePoints) {
                boolean valid = true;
                for (LWESample sample : samples) {
                    List<Integer> a = sample.getA();
                    int b = sample.getB();
                    int computed = 0;
                    for (int j = 0; j < dim; j++) {
                        computed += a.get(j) * candidate.get(j);
                    }
                    if (Math.abs(computed - b) > noiseStddev) {
                        valid = false;
                        break;
                    }
                }
                if (valid) {
                    return candidate;
                }
            }
        } catch (StackOverflowError e) {
            return null;
        }
        return null;
    }
    
 // Resolver LWE de manera ingenua (probando puntos del retículo)
    public static List<Integer> solveLWEDebug(List<LWESample> samples, List<List<Integer>> latticePoints, int noiseStddev) {
        int dim = latticePoints.get(0).size(); // Asumimos que todos los puntos tienen la misma dimensión

        try {
            // Probar cada punto de la red como solución potencial
            for (List<Integer> candidate : latticePoints) {
                System.out.println("Probando el candidato: " + candidate);
                boolean valid = true;
                List<String> successReasons = new ArrayList<>(); // Para guardar detalles de éxito

                for (LWESample sample : samples) {
                    List<Integer> a = sample.getA();
                    int b = sample.getB();

                    // Calcular a * candidate
                    int computed = 0;
                    for (int j = 0; j < dim; j++) {
                        computed += a.get(j) * candidate.get(j);
                    }

                    // Verificar si el valor calculado está dentro del margen de error
                    if (Math.abs(computed - b) > noiseStddev) { // Comparar con el margen
                        valid = false;
                        System.out.println("  Falló con a: " + a + ", b: " + b + ", calculado: " + computed);
                        break;
                    } else {
                        successReasons.add("Cumple para a: " + a + ", b: " + b + ", calculado: " + computed);
                    }
                }

                // Si el candidato es válido para todas las muestras, se devuelve
                if (valid) {
                    System.out.println("Éxito: El candidato " + candidate + " cumple con todas las muestras.");
                    for (String reason : successReasons) {
                        System.out.println("  " + reason);
                    }
                    return candidate;
                }
            }
        } catch (StackOverflowError e) {
            System.err.println("Error: StackOverflowError. Deteniendo ejecución.");
            return null; // Devolvemos null si ocurre un desbordamiento de pila.
        }

        // Si no se encuentra una solución válida después de probar todos los puntos, se devuelve null
        return null; // No se encontró solución
    }

    public static void main(String[] args) {
    
        int numSamples = 1000;  // Número de muestras a generar
        int dim = 5;  			// Dimensión del secreto
        int maxValue = 2;  	// Rango de valores posibles para los elementos de 'a'
        int noiseStddev = 1;  	// Desviación estándar del ruido
        List<Integer> secret = Lattices.generateRandomVector(dim, maxValue);
        System.out.println("El secreto es: "+secret);
        // Generar muestras LWE
        List<LWESample> samples = generateLWESamples(secret, numSamples, dim, maxValue, noiseStddev);
        
        // Imprimir las muestras generadas
        /*
        for (LWESample sample : samples) {
            System.out.println("a: " + sample.getA() + ", b: " + sample.getB());
        }
        */
        
        List<List<Integer>> base=Lattices.generateOrthogonalBase(dim, maxValue);
        List<List<Integer>> latticePoints=Lattices.generateLatticePoints(base, maxValue);

        System.out.println("Base del reticulo: "+ base);
        // Resolver el sistema LWE utilizando la búsqueda de puntos en el retículo
        List<Integer> recoveredSecret = solveLWE(samples, latticePoints, noiseStddev);

        // Mostrar el resultado final
        if (recoveredSecret != null) {
            System.out.println("\nSecreto recuperado correctamente: " + recoveredSecret);
        } else {
            System.out.println("\nNo se encontró solución válida.");
        }
    }


}
