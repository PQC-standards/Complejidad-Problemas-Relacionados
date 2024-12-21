package Algorithms;

import java.util.ArrayList;
import java.util.List;

public class CVPNaive {

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

    // Función para encontrar el vector más cercano al punto objetivo t
    public static List<Integer> findNearestVector(List<List<Integer>> latticePoints, List<Integer> targetPoint) {
        List<Integer> nearestVector = null;
        double nearestDistance = Double.MAX_VALUE;

        // Compara todos los puntos de la red con el punto objetivo t
        for (List<Integer> point : latticePoints) {
            // Calcula el vector que va de t a cada punto de la red
            List<Integer> vector = subtractVectors(point, targetPoint);
            double distance = calculateNorm(vector); // Calcular la distancia entre t y el punto de la red

            // Si la distancia es la menor encontrada, actualizamos el vector más cercano
            if (distance < nearestDistance) {
                nearestDistance = distance;
                nearestVector = point; // Guardamos el punto más cercano
            }
        }

        return nearestVector;
    }

    // Función para restar dos vectores
    public static List<Integer> subtractVectors(List<Integer> v1, List<Integer> v2) {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < v1.size(); i++) {
            result.add(v1.get(i) - v2.get(i)); // Restar componente a componente
        }
        return result;
    }

    // Función para calcular la norma (distancia euclidiana) de un vector
    public static double calculateNorm(List<Integer> vector) {
        return Math.sqrt(vector.stream().mapToDouble(x -> x * x).sum());
    }

    // Función para generar puntos del lattice (todos los puntos con coeficientes en [-k, k])
    public static List<List<Integer>> generateLatticePoints(List<List<Integer>> base, int k) {
        List<List<Integer>> latticePoints = new ArrayList<>();
        generateRecursive(base, k, new ArrayList<>(), latticePoints, 0);
        return latticePoints;
    }
    
 // Función para generar un punto aleatorio en Z^dim (espacio de dimensión 'dim') con valores en el rango [-maxValue, maxValue]
    public static List<Integer> generateRandomPoint(int dim, int maxValue) {
        List<Integer> point = new ArrayList<>();
        for (int i = 0; i < dim; i++) {
            point.add((int) (Math.random() * (2 * maxValue + 1)) - maxValue); // Valores aleatorios entre -maxValue y maxValue
        }
        return point;
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

    // Función principal para probar
    public static void main(String[] args) {
        List<List<Integer>> base = generateRandomBase(2); // Base aleatoria en 2 dimensiones
        System.out.println("Base: " + base);
        int k = 5; // Explorar combinaciones hasta coeficientes en [-5, 5]
        List<List<Integer>> latticePoints = generateLatticePoints(base, k);
        System.out.println("Lattice Points: " + latticePoints);

        // Definir el punto objetivo t
        List<Integer> targetPoint = new ArrayList<>();
        targetPoint.add(2); // x = 1
        targetPoint.add(2); // y = 2
        System.out.println("Punto objetivo t: " + targetPoint);

        // Encontrar el vector más cercano al punto t
        List<Integer> nearestVector = findNearestVector(latticePoints, targetPoint);
        System.out.println("Vector más cercano: " + nearestVector);
    }
}
