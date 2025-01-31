package Algorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Lattices {

	 private static final Random random = new Random();

	 
	 public static List<List<Integer>> generateOrthogonalBase(int dim) {
		    List<List<Integer>> base = new ArrayList<>();

		    // Generar la base ortogonal con valores aleatorios en la diagonal
		    for (int i = 0; i < dim; i++) {
		        List<Integer> vector = new ArrayList<>();
		        
		        // Crear un vector (0, ..., 0, val, 0, ..., 0) con 'val' en la posición i-ésima
		        for (int j = 0; j < dim; j++) {
		            if (i == j) {
		                
		                vector.add(1);
		            } else {
		                vector.add(0);  // Los demás elementos son 0
		            }
		        }
		        
		        base.add(vector);
		    }

		    return base;
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
    
    
 // Función para generar puntos del lattice LWE (todos los puntos con coeficientes en [0, q-1])
    public static List<List<Integer>> generateLatticePointsLWE(List<List<Integer>> base, int q) {
        List<List<Integer>> latticePoints = new ArrayList<>();
        generateRecursiveLWE(base, q, new ArrayList<>(), latticePoints, 0);
        return latticePoints;
    }

    // Método recursivo para generar los puntos del lattice LWE
    private static void generateRecursiveLWE(List<List<Integer>> base, int q, List<Integer> coefficients,
                                             List<List<Integer>> latticePoints, int depth) {
        if (depth == base.size()) {
            List<Integer> point = new ArrayList<>();
            // Calcular la coordenada de cada punto en el lattice
            for (int i = 0; i < base.get(0).size(); i++) {
                int coordinate = 0;
                for (int j = 0; j < base.size(); j++) {
                    coordinate += coefficients.get(j) * base.get(j).get(i);
                }
                // Tomar módulo q para que la coordenada esté en el rango [0, q-1]
                point.add((coordinate % q + q) % q);  // Para manejar negativos correctamente
            }
            latticePoints.add(point);
            return;
        }

        // Modificar el rango para que los coeficientes estén en [0, q-1]
        for (int i = 0; i < q; i++) {  // Aquí cambiamos [-k, k] por [0, q-1]
            List<Integer> newCoefficients = new ArrayList<>(coefficients);
            newCoefficients.add(i);
            generateRecursiveLWE(base, q, newCoefficients, latticePoints, depth + 1);
        }
    }

    
    
    // Función para generar un punto aleatorio en Z^dim (espacio de dimensión 'dim') con valores en el rango [-maxValue, maxValue]
    public static List<Integer> generateRandomPoint(int dim, int maxValue) {
        List<Integer> point = new ArrayList<>();
        for (int i = 0; i < dim; i++) {
            point.add((int) (Math.random() * (2 * maxValue + 1)) - maxValue); // Valores aleatorios entre -maxValue y maxValue
        }
        return point;
    }
    
 // Generar un vector aleatorio
    public static List<Integer> generateRandomVector(int dim, int maxValue) {
        List<Integer> vector = new ArrayList<>();
        for (int i = 0; i < dim; i++) {
            vector.add(random.nextInt(2 * maxValue + 1) - maxValue);
        }
        return vector;
    }
    
 // Función para generar un secreto aleatorio LWE en el rango [0, q-1]
    public static List<Integer> generateRandomSecret(int dim, int q) {
        List<Integer> secret = new ArrayList<>();
        for (int i = 0; i < dim; i++) {
            secret.add(random.nextInt(q));  // Genera un valor aleatorio entre 0 y q-1
        }
        return secret;
    }
    
    
    
}
