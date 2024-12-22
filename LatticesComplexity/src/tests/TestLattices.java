package tests;

import java.util.List;
import java.util.function.Function;

import org.apache.commons.math3.fitting.WeightedObservedPoint;

import Algorithms.CVPNaive;
import Algorithms.LWENaive;
import Algorithms.LWESample;
import Algorithms.Lattices;
import Algorithms.SVPNaive;
import us.lsi.curvefitting.DataFile;
import us.lsi.curvefitting.Exponential;
import us.lsi.curvefitting.Fit;
import us.lsi.curvefitting.GenData;
import us.lsi.graphics.MatPlotLib;

public class TestLattices {

    private static Integer nMin = 1;  // Dimensión mínima
    private static Integer nMax = 6; // Dimensión máxima
    private static Integer nIncr = 1; // Incremento de dimensión
    private static Integer nIter = 20; // Número de iteraciones por medición
    private static Integer range = 5; // Rango de combinaciones [-k, k]
    private static Integer maxVectorValue = 5; // Mayor Valor que puede tener una coordenada en un vector
    private static Integer warmup = 1000; // Calentamiento para saturar la caché
    private static Integer noiseStddev = 3; // Desviación estándar para ruido LWE
    private static Integer numSamples = 100; // Número de muestras LWE
 
    
    public static void genDataSVP() {
        String file = "ficheros_generados/SVP.txt";

        // Función que mide el tiempo de ejecución
        Function<Integer, Long> f1 = dim -> {
            List<List<Integer>> base = Lattices.generateOrthogonalBase(dim, maxVectorValue); // Base aleatoria
            List<List<Integer>> latticePoints = Lattices.generateLatticePoints(base, range);
            long startTime = System.nanoTime();
            SVPNaive.findShortestVector(latticePoints);
            long endTime = System.nanoTime();

            return endTime - startTime;
        };

        // Generar datos de tiempo para varias dimensiones
        GenData.tiemposEjecucionAritmetica(f1, file, nMin, nMax, nIncr, nIter, warmup);
    }

    public static void genDataNVP() {
        String file = "ficheros_generados/NVP.txt";

        // Función que mide el tiempo de ejecución
        Function<Integer, Long> f1 = dim -> {
            List<List<Integer>> base = Lattices.generateOrthogonalBase(dim, maxVectorValue); // Base aleatoria
            List<List<Integer>> latticePoints = Lattices.generateLatticePoints(base, range);
            // El punto está fuera del retículo
            List<Integer> randomPoint = Lattices.generateRandomPoint(1000, 1000);
            long startTime = System.nanoTime();
            CVPNaive.findNearestVector(latticePoints, randomPoint);
            long endTime = System.nanoTime();

            return endTime - startTime;
        };
        // Generar datos de tiempo para varias dimensiones
        GenData.tiemposEjecucionAritmetica(f1, file, nMin, nMax, nIncr, nIter, warmup);
    }

    public static void genDataLWE() {
        String file = "ficheros_generados/LWE.txt";

        // Función que mide el tiempo de ejecución
        Function<Integer, Long> f1 = dim -> {
            List<Integer> secret = LWENaive.generateSecret(dim, maxVectorValue);
            List<LWESample> samples = LWENaive.generateLWESamples(secret, numSamples, dim, maxVectorValue, noiseStddev);
            List<List<Integer>> base = Lattices.generateOrthogonalBase(dim, maxVectorValue);
            List<List<Integer>> latticePoints = Lattices.generateLatticePoints(base, range);

            long startTime = System.nanoTime();
            LWENaive.solveLWE(samples, latticePoints,noiseStddev);
            long endTime = System.nanoTime();

            return endTime - startTime;
        };

        // Generar datos de tiempo para varias dimensiones
        GenData.tiemposEjecucionAritmetica(f1, file, nMin, nMax, nIncr, nIter, warmup);
    }

    public static void showSVP() {
        String file = "ficheros_generados/SVP.txt";
        List<WeightedObservedPoint> data = DataFile.points(file);
        Fit pl = Exponential.of();
        pl.fit(data);
        System.out.println(pl.getExpression());
        System.out.println(pl.getEvaluation().getRMS());
        MatPlotLib.show(file, pl.getFunction(), pl.getExpression());
    }

    public static void showNVP() {
        String file = "ficheros_generados/NVP.txt";
        List<WeightedObservedPoint> data = DataFile.points(file);
        Fit pl = Exponential.of();
        pl.fit(data);
        System.out.println(pl.getExpression());
        System.out.println(pl.getEvaluation().getRMS());
        MatPlotLib.show(file, pl.getFunction(), pl.getExpression());
    }

    public static void showLWE() {
        String file = "ficheros_generados/LWE.txt";
        List<WeightedObservedPoint> data = DataFile.points(file);
        Fit pl = Exponential.of();
        pl.fit(data);
        System.out.println(pl.getExpression());
        System.out.println(pl.getEvaluation().getRMS());
        MatPlotLib.show(file, pl.getFunction(), pl.getExpression());
    }

    public static void showCombined() {
        MatPlotLib.showCombined("Tiempos",
                List.of("ficheros_generados/SVP.txt", "ficheros_generados/NVP.txt", "ficheros_generados/LWE.txt"),
                List.of("SVP", "NVP", "LWE"));

    }

    public static void main(String[] args) {
    	//genDataSVP();
        genDataNVP();
        genDataLWE();
        showSVP();
        showNVP();
        showLWE();
        showCombined();
    }
}
