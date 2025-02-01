package Algorithms;

import java.util.List;

public class SVPNaive {


    // Función para encontrar el vector más corto, considerando todos los pares de puntos
    public static List<Integer> findShortestVector(List<List<Integer>> latticePoints) {
        List<Integer> shortestVector = null;
        double shortestNorm = Double.MAX_VALUE;

        for (int i = 0; i < latticePoints.size(); i++) {
            for (int j = i + 1; j < latticePoints.size(); j++) {
                List<Integer> vector = Lattices.subtractVectors(latticePoints.get(j), latticePoints.get(i));
                double norm = Lattices.calculateNorm(vector);

                if (norm > 0 && norm < shortestNorm) { // Excluir el vector nulo
                    shortestNorm = norm;
                    shortestVector = vector;
                }
            }
        }

        return shortestVector;
    }
   
    public static void main(String[] args) {
    	int dim = 3;
    	int k = 5; // Explorar combinaciones hasta coeficientes en [-k, k]
    	
    	//Base y puntos de lreticulo
        List<List<Integer>> base = Lattices.generateOrthogonalBase(dim); // Base aleatoria en 2 dimensiones
        System.out.println("Base: " + base);
        List<List<Integer>> latticePoints = Lattices.generateLatticePoints(base, k);
        System.out.println("Lattice Points: " + latticePoints);

        // Encontrar el vector más corto
        List<Integer> shortestVector = findShortestVector(latticePoints);
        System.out.println("Vector más corto: " + shortestVector);
    }
}
