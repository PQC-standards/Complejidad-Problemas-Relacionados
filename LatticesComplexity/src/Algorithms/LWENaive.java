package Algorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LWENaive {
	
    private static final Random random = new Random();

    // Función para generar muestras LWE en el rango [0, q-1]
    public static List<LWESample> generateLWESamples(List<Integer> secret, int numSamples, int dim, int q, int noiseStddev) {
        List<LWESample> samples = new ArrayList<>();

        for (int i = 0; i < numSamples; i++) {
            // Generar vector 'a' aleatorio del retículo
            List<Integer> a = new ArrayList<>();
            for (int j = 0; j < dim; j++) {
                a.add(random.nextInt(q));  
            }

            // Calcular b = a * s mod q
            int b = 0;
            for (int j = 0; j < dim; j++) {
                b = ((b + a.get(j) * secret.get(j))+q) % q;
            }

            // Añadir un error pequeño en el rango [-noiseStddev, noiseStddev]       
            int error = random.nextInt(2 * noiseStddev + 1) - noiseStddev;
            b = (b + error + q) % q;  

            samples.add(new LWESample(a, b));
        }
        return samples;
    }

    public static List<Integer> solveLWE(List<LWESample> samples, List<List<Integer>> latticePoints, int q, int noiseStddev) {
        int dim = latticePoints.get(0).size();

        while (true) { // Bucle infinito que solo termina cuando se encuentra una solución
            try {
                for (List<Integer> candidate : latticePoints) {
                    boolean valid = true;

                    for (LWESample sample : samples) {
                        List<Integer> a = sample.getA();
                        int b = sample.getB();
                        int computed = 0;

                        for (int j = 0; j < dim; j++) {
                            computed = ((computed + a.get(j) * candidate.get(j))+q) % q;
                        }
                        // Si tolera el error damos por bueno el resultado
                        if (Math.abs((computed - b) % q) > noiseStddev) {
                            valid = false;
                            break;
                        }
                    }

                    if (valid) {
                        return candidate; // Solución encontrada
                    }
                }
            } catch (StackOverflowError e) {
                System.err.println("Error: StackOverflowError. Retomando ejecución.");
            }
        }
    }

    public static List<Integer> solveLWEFinito(List<LWESample> samples, List<List<Integer>> latticePoints, int q, int noiseStddev) {
        int dim = latticePoints.get(0).size();
        try {
            for (List<Integer> candidate : latticePoints) {
                boolean valid = true;
                for (LWESample sample : samples) {
                    List<Integer> a = sample.getA();
                    int b = sample.getB();
                    int computed = 0;
                    for (int j = 0; j < dim; j++) {
                        computed = ((computed + a.get(j) * candidate.get(j))+q) % q;
                    }
                    if (Math.abs((computed - b) % q) > noiseStddev) {
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

    public static List<Integer> solveLWEDebug(List<LWESample> samples, List<List<Integer>> latticePoints, int q, int noiseStddev) {
        int dim = latticePoints.get(0).size(); // Asumimos que todos los puntos tienen la misma dimensión

        try {
            for (List<Integer> candidate : latticePoints) {
                System.out.println("Probando el candidato: " + candidate);
                boolean valid = true;
                List<String> successReasons = new ArrayList<>(); // Para guardar detalles de éxito

                for (LWESample sample : samples) {
                    List<Integer> a = sample.getA();
                    int b = sample.getB();

                    int computed = 0;
                    for (int j = 0; j < dim; j++) {
                        computed = ((computed + a.get(j) * candidate.get(j))+q) % q;
                    }

                    if (Math.abs((computed - b) % q) > noiseStddev) {
                        valid = false;
                        System.out.println("  Falló con a: " + a + ", b: " + b + ", calculado: " + computed);
                        break;
                    } else {
                        successReasons.add("Cumple para a: " + a + ", b: " + b + ", calculado: " + computed);
                    }
                }

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
        return null; // No se encontró solución
    }

    public static void main(String[] args) {

        int numSamples = 10;  // Número de muestras a generar
        int dim = 4;           // Dimensión del secreto
        int q = 31;             // Rango de valores posibles para los elementos de 'a'
        int noiseStddev = 1;   // Desviación estándar del ruido
        List<Integer> secret = Lattices.generateRandomSecret(dim, q);
        System.out.println("El secreto es: " + secret);

        List<LWESample> samples = generateLWESamples(secret, numSamples, dim, q, noiseStddev);
        List<List<Integer>> base = Lattices.generateOrthogonalBase(dim);
        List<List<Integer>> latticePoints = Lattices.generateLatticePointsLWE(base, q);

        System.out.println("Base del retículo: " + base);

        List<Integer> recoveredSecret = solveLWEDebug(samples, latticePoints, q, noiseStddev);

        // Mostrar el resultado final
        if (recoveredSecret != null) {
            System.out.println("\nSecreto recuperado correctamente: " + recoveredSecret);
        } else {
            System.out.println("\nNo se encontró solución válida.");
        }
    }
}
