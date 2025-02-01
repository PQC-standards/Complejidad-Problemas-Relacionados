package Algorithms;

import java.util.ArrayList;
import java.util.List;


public class CVPNaive {


    // Función para encontrar el vector más cercano al punto objetivo t
    public static List<Integer> findClosestVector(List<List<Integer>> latticePoints, List<Integer> targetPoint) {
        List<Integer> closestVector = null;
        double closestDistance = Double.MAX_VALUE;

        for (List<Integer> point : latticePoints) {
        	//Calcula el vector que une ambos puntos
            List<Integer> vector = Lattices.subtractVectors(point, targetPoint);
            double distance = Lattices.calculateNorm(vector); 

            if (distance < closestDistance) {
                closestDistance = distance;
                closestVector = vector; 
            }
        }
        return closestVector;
    }

    public static void main(String[] args) {
    	int dim = 5;
        int k = 5;
        // base y puntos del reticulo
        List<List<Integer>> base = Lattices.generateOrthogonalBase(dim); 
        System.out.println("Base: " + base);
        List<List<Integer>> latticePoints = Lattices.generateLatticePoints(base, k);
        System.out.println("Lattice Points: " + latticePoints);

        // Punto objetivo en el rango [-1000,1000]
        List<Integer> targetPoint = Lattices.generateRandomPoint(dim,1000 );
        System.out.println("Punto objetivo t: " + targetPoint);

        // vector mas cercano al punto objetivo
        List<Integer> closestVector = findClosestVector(latticePoints, targetPoint);
        System.out.println("Vector más cercano: " + closestVector);
        System.out.println("Norma del vector más cercano: " + Lattices.calculateNorm(closestVector));
    }
}
