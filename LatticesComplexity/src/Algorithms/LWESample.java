package Algorithms;

import java.util.List;

	// Clase auxiliar para almacenar muestras LWE
		public class LWESample {
		    private final List<Integer> a;
		    private final int b;

		    public LWESample(List<Integer> a, int b) {
		        this.a = a;
		        this.b = b;
		    }

		    public List<Integer> getA() {
		        return a;
		    }

		    public int getB() {
		        return b;
		    }

		    @Override
		    public String toString() {
		        return "LWESample{" + "a=" + a + ", b=" + b + '}';
		    }

		    
		}

