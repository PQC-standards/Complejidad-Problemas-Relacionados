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
    private static Integer nMax = 4; // Dimensión máxima
    private static Integer nIncr = 1; // Incremento de dimensión
    private static Integer nIter = 5; // Número de iteraciones por medición
    private static Integer range = 5; // Rango del reticulo [-k, k]
    private static Integer q= 11;	  // primo que se usa de módulo en LWE && rango en el reticulo para LWE
    private static Integer warmup = 1000; // Calentamiento para saturar la caché
    private static Integer noiseStddev = 0; // Desviación estándar para ruido LWE
    private static Integer numSamples = 100; // Número de muestras LWE
 
    
    public static void genDataSVP() {
        String file = "ficheros_generados/SVP.txt";

        // Función que mide el tiempo de ejecución
        Function<Integer, Long> f1 = dim -> {
            List<List<Integer>> base = Lattices.generateOrthogonalBase(dim); // Base aleatoria
            List<List<Integer>> latticePoints = Lattices.generateLatticePoints(base, range);
            long startTime = System.nanoTime();
            SVPNaive.findShortestVector(latticePoints);
            long endTime = System.nanoTime();

            return endTime - startTime;
        };

        // Generar datos de tiempo para varias dimensiones
        GenData.tiemposEjecucionAritmetica(f1, file, nMin, nMax, nIncr, nIter, warmup);
    }

    public static void genDataCVP() {
        String file = "ficheros_generados/CVP.txt";

        // Función que mide el tiempo de ejecución
        Function<Integer, Long> f1 = dim -> {
            List<List<Integer>> base = Lattices.generateOrthogonalBase(dim); // Base aleatoria
            List<List<Integer>> latticePoints = Lattices.generateLatticePoints(base, range);
            // El punto está fuera del retículo
            List<Integer> randomPoint = Lattices.generateRandomPoint(1000, 1000);
            long startTime = System.nanoTime();
            CVPNaive.findClosestVector(latticePoints, randomPoint);
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
            List<Integer> secret = Lattices.generateRandomSecret(dim, q);
            List<LWESample> samples = LWENaive.generateLWESamples(secret, numSamples, dim,q, noiseStddev);
            List<List<Integer>> base = Lattices.generateOrthogonalBase(dim);
            List<List<Integer>> latticePoints = Lattices.generateLatticePointsLWE(base, q);

            long startTime = System.nanoTime();
            LWENaive.solveLWE(samples, latticePoints,q,noiseStddev);
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

    public static void showCVP() {
        String file = "ficheros_generados/CVP.txt";
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
                List.of("ficheros_generados/SVP.txt", "ficheros_generados/CVP.txt", "ficheros_generados/LWE.txt"),
                List.of("SVP", "CVP", "LWE"));

    }

    public static void main(String[] args) {
    	//genDataSVP();
    	//genDataCVP();
    	//genDataLWE();
    	showSVP();
      	showCVP();
        showLWE();
        showCombined();
    }
}
