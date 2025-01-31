package Algorithms;

import java.util.ArrayList;
import java.util.List;


public class CVPNaive {


    // Función para encontrar el vector más cercano al punto objetivo t
    public static List<Integer> findClosestVector(List<List<Integer>> latticePoints, List<Integer> targetPoint) {
        List<Integer> closestVector = null;
        double closestDistance = Double.MAX_VALUE;

        // Compara todos los puntos de la red con el punto objetivo t
        for (List<Integer> point : latticePoints) {
            // Calcula el vector que va de t a cada punto de la red
            List<Integer> vector = Lattices.subtractVectors(point, targetPoint);
            double distance = Lattices.calculateNorm(vector); // Calcular la distancia entre t y el punto de la red

            // Si la distancia es la menor encontrada, actualizamos el vector más cercano
            if (distance < closestDistance) {
                closestDistance = distance;
                closestVector = point; // Guardamos el punto más cercano
            }
        }

        return closestVector;
    }

    // Función principal para probar
    public static void main(String[] args) {
        List<List<Integer>> base = Lattices.generateOrthogonalBase(2); // Base aleatoria en 2 dimensiones
        System.out.println("Base: " + base);
        int k = 5; // Explorar combinaciones hasta coeficientes en [-5, 5]
        List<List<Integer>> latticePoints = Lattices.generateLatticePoints(base, k);
        System.out.println("Lattice Points: " + latticePoints);

        // Definir el punto objetivo t
        List<Integer> targetPoint = new ArrayList<>();
        targetPoint.add(2); // x = 1
        targetPoint.add(2); // y = 2
        System.out.println("Punto objetivo t: " + targetPoint);

        // Encontrar el vector más cercano al punto t
        List<Integer> nearestVector = findClosestVector(latticePoints, targetPoint);
        System.out.println("Vector más cercano: " + nearestVector);
    }
}
