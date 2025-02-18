package Algorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Lattices {

	
	public static void main(String args[]) {
		List<List<Integer>> base = generateOrthogonalBase(6);
		System.out.println("tiempo en calcular los puntos del reticulo:");
		long startTime = System.nanoTime();
		generateLatticePoints(base,10);
        long endTime = System.nanoTime();
        System.out.println((endTime-startTime)/ 1_000_000_000.0 +" segundos");

	}
	
	 private static final Random random = new Random();

	 //Funcion para generar una base ortogonal:
	 public static List<List<Integer>> generateOrthogonalBase(int dim) {
		    List<List<Integer>> base = new ArrayList<>();

		    for (int i = 0; i < dim; i++) {
		        List<Integer> vector = new ArrayList<>();
		        
		        for (int j = 0; j < dim; j++) {
		            if (i == j) {
		                
		                vector.add(1);  // 1 en la diagonal principal
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
        
    // Funcion para calcular los puntos del retículo en rango [-k,k] dada una base
    public static List<List<Integer>> generateLatticePoints(List<List<Integer>> base, int k) {
        List<List<Integer>> latticePoints = new ArrayList<>();
        int dimensions = base.get(0).size();
        int[] point = new int[dimensions]; // Array reutilizable para mejorar eficiencia
        generateRecursive(base, k, new int[base.size()], 0, point, latticePoints);
        return latticePoints;
    }

    private static void generateRecursive(List<List<Integer>> base, int k, int[] coefficients, 
                                          int depth, int[] point, List<List<Integer>> latticePoints) {
        if (depth == base.size()) {
            // Se almacena una copia del punto calculado
            List<Integer> finalPoint = new ArrayList<>();
            for (int value : point) {
                finalPoint.add(value);
            }
            latticePoints.add(finalPoint);
            return;
        }

        for (int i = -k; i <= k; i++) {
            coefficients[depth] = i;

            // Actualizar el punto en la iteración actual sin recalcular desde cero
            for (int j = 0; j < point.length; j++) {
                point[j] += i * base.get(depth).get(j);
            }

            generateRecursive(base, k, coefficients, depth + 1, point, latticePoints);

            // Deshacer la actualización antes de la siguiente iteración
            for (int j = 0; j < point.length; j++) {
                point[j] -= i * base.get(depth).get(j);
            }
        }
    }
    
    // Funcion para calcular los puntos del retículo en rango [0,q) dada una base (pensado para LWE)
    public static List<List<Integer>> generateLatticePointsLWE(List<List<Integer>> base, int q) {
        List<List<Integer>> latticePoints = new ArrayList<>();
        int dimensions = base.get(0).size();
        int[] point = new int[dimensions]; // Array reutilizable para almacenar las coordenadas
        generateRecursiveLWE(base, q, new int[base.size()], 0, point, latticePoints);
        return latticePoints;
    }

    private static void generateRecursiveLWE(List<List<Integer>> base, int q, int[] coefficients,
                                             int depth, int[] point, List<List<Integer>> latticePoints) {
        if (depth == base.size()) {
            // Convertir `point` a una lista y agregarla al resultado
            List<Integer> finalPoint = new ArrayList<>();
            for (int value : point) {
                finalPoint.add((value + q) % q);
            }
            latticePoints.add(finalPoint);
            return;
        }

        for (int i = 0; i < q; i++) {
            coefficients[depth] = i;

            // Actualizar el punto sin recalcular desde cero
            for (int j = 0; j < point.length; j++) {
                point[j] += i * base.get(depth).get(j);
            }

            generateRecursiveLWE(base, q, coefficients, depth + 1, point, latticePoints);

            // Deshacer la actualización antes de la siguiente iteración
            for (int j = 0; j < point.length; j++) {
                point[j] -= i * base.get(depth).get(j);
            }
        }
    }

    
    // Función para generar un punto aleatorio en Z^dim con valores en el rango [-k, k]
    public static List<Integer> generateRandomPoint(int dim, int k) {
        List<Integer> point = new ArrayList<>();
        for (int i = 0; i < dim; i++) {
            point.add((int) (Math.random() * (2 * k + 1)) - k);
        }
        return point;
    }
    
 // Generar un vector aleatorio en Z^dim con valores en el rango [-k, k]
    public static List<Integer> generateRandomVector(int dim, int k) {
        List<Integer> vector = new ArrayList<>();
        for (int i = 0; i < dim; i++) {
            vector.add(random.nextInt(2 * k + 1) - k);
        }
        return vector;
    }
    
 // Función para generar un secreto aleatorio LWE en el rango [0, q-1]
    public static List<Integer> generateRandomSecret(int dim, int q) {
        List<Integer> secret = new ArrayList<>();
        for (int i = 0; i < dim; i++) {
            secret.add(random.nextInt(q)); 
        }
        return secret;
    }
    
}
