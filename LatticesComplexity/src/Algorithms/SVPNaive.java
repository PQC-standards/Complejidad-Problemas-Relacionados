package Algorithms;

import java.util.ArrayList;
import java.util.List;

public class SVPNaive {

    // Función para generar una base aleatoria
    public static List<List<Integer>> generateRandomBase(int dim) {
        List<List<Integer>> base = new ArrayList<>();
        for (int i = 0; i < dim; i++) {
            List<Integer> vector = new ArrayList<>();
            for (int j = 0; j < dim; j++) {
                vector.add((int) (Math.random() * 10) - 5); // Valores aleatorios entre -5 y 5
            }
            base.add(vector);
        }
        return base;
    }

    // Función para encontrar el vector más corto, considerando todos los pares de puntos
    public static List<Integer> findShortestVector(List<List<Integer>> latticePoints) {
        List<Integer> shortestVector = null;
        double shortestNorm = Double.MAX_VALUE;

        // Compara todos los pares de puntos en latticePoints
        for (int i = 0; i < latticePoints.size(); i++) {
            for (int j = i + 1; j < latticePoints.size(); j++) {
                List<Integer> vector = subtractVectors(latticePoints.get(j), latticePoints.get(i));
                double norm = calculateNorm(vector);

                if (norm > 0 && norm < shortestNorm) { // Excluir el vector nulo
                    shortestNorm = norm;
                    shortestVector = vector;
                }
            }
        }

        return shortestVector;
    }

    // Función para restar dos vectores
    public static List<Integer> subtractVectors(List<Integer> v1, List<Integer> v2) {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < v1.size(); i++) {
            result.add(v1.get(i) - v2.get(i)); // Restar componente a componente
        }
        return result;
    }

    // Función para calcular la norma de un vector
    public static double calculateNorm(List<Integer> vector) {
        return Math.sqrt(vector.stream().mapToDouble(x -> x * x).sum());
    }

    // Función para generar puntos del lattice (todos los puntos con coeficientes en [-k, k])
    public static List<List<Integer>> generateLatticePoints(List<List<Integer>> base, int k) {
        List<List<Integer>> latticePoints = new ArrayList<>();
        generateRecursive(base, k, new ArrayList<>(), latticePoints, 0);
        return latticePoints;
    }

    // Método recursivo para generar los puntos del lattice
    private static void generateRecursive(List<List<Integer>> base, int k, List<Integer> coefficients,
                                          List<List<Integer>> latticePoints, int depth) {
        if (depth == base.size()) {
            List<Integer> point = new ArrayList<>();
            for (int i = 0; i < base.get(0).size(); i++) {
                int coordinate = 0;
                for (int j = 0; j < base.size(); j++) {
                    coordinate += coefficients.get(j) * base.get(j).get(i);
                }
                point.add(coordinate);
            }
            latticePoints.add(point);
            return;
        }
        for (int i = -k; i <= k; i++) {
            List<Integer> newCoefficients = new ArrayList<>(coefficients);
            newCoefficients.add(i);
            generateRecursive(base, k, newCoefficients, latticePoints, depth + 1);
        }
    }
    public static void main(String[] args) {
        List<List<Integer>> base = generateRandomBase(2); // Base aleatoria en 2 dimensiones
        System.out.println("Base: " + base);
        int k = 5; // Explorar combinaciones hasta coeficientes en [-5, 5]
        List<List<Integer>> latticePoints = generateLatticePoints(base, k);
        System.out.println("Lattice Points: " + latticePoints);

        // Encontrar el vector más corto
        List<Integer> shortestVector = findShortestVector(latticePoints);
        System.out.println("Vector más corto: " + shortestVector);
    }

}
