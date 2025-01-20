# Sobre el nuevo estándar de encapsulado de clave (KEM) post–cuántico

![image](https://github.com/user-attachments/assets/65c9aa92-a7f1-4685-9d1e-272ca69c054c)

**Realizado por:**  
[Gabriel Vacaro Goytia](https://github.com/Gabrielvcg) (gabvacgoy@alum.us.es)  
[Ignacio Warleta Murcia](https://github.com/ignaciowarleta) (ignwarmur@alum.us.es) <br>

En este repositorio se encuentra el proyecto en Java encargado de el análisis empírico de la complejidad de los algoritmos CVP, SVP y LWE, todos desde el punto de vista de un ataque a fuerza bruta.

Este repositorio hace uso de el repositorio de Miguel Toro ([https://github.com/migueltoro/adda_v3.git](https://github.com/migueltoro/adda_v3.git)), en concreto del proyecto ParteComun, este deberá ser importado y añadido al Build Path del proyecto LatticeComplexity.

## Contenido

- [Descripción del Proyecto](#descripción-del-proyecto)
- [Instalación](#instalación)
- [Estructura](#estructura)
- [Contribución](#contribución)

## Descripción del Proyecto

Este repositorio contiene el análisis empírico de la complejidad de los problemas que se tratan en el TFG «Sobre el nuevo estándar de encapsulado de clave (KEM) post–cuántico» del departamento de matemáticas de la ETSII, con el objetivo de respaldar y proveer evidencia de las afirmaciones sobre la intratabilidad de estos algoritmos. Realizado por Gabriel Vacaro e Ignacio Warleta, alumnos de ingeniería del software.

El proyecto tiene como objetivo complementar el trabajo de documentación e investigación que se ha realizado sobre el nuevo estándar del NIST KYBER-KEM de una forma didáctica. De forma que se pueda entender completamente la primitiva matemática en la que se basa la mayoría de algoritmos de criptografía post-cuántica, los retículos. Así como los problemas en los que se sustentan estos algoritmos.

Los elementos principales del trabajo incluyen:
- Análisis de la complejidad de los problemas del vector mas cercano (CVP), el vector mas corto (SVP) y aprendizaje con errores (LWE).

## Instalación

Para ejecutar este proyecto necesitaremos realizar una instalacion en local, debemos seguir estos pasos:

1. Instalar eclipse en el siguiente enlace: https://www.eclipse.org/downloads/packages/

2. Clonar este repositorio en tu máquina local, asi como el repositorio de miguel toro:  
   ```bash
   git clone https://github.com/migueltoro/adda_v3.git
   git clone https://github.com/PQC-standards/Complejidad-Problemas-Relacionados.git

3. En eclipse, ir a Windows > Preferences > Java > Installed JRES: y nos aseguraremos que nuestro JDK sea de la version 22 o superior, hacemos doble click sobre el JRE y en el campo Default VM arguments añadiremos "--enable-preview"

4. En eclipse, ir a Windows > Preferences > Java > Compiler: y asegurarnos que en el campo Compiler compilance level esta definido "22", y la opcion Enable preview features for java 22 esta activada, por último, en el campo Preview features with severity level asignaremos "ignore".
   
5. Al ejecutar algoritmos recursivos, si el tamaño del problema es elevado puede ser necesario aumentar la cantidad de memoria que la máquina virtual de Java utiliza para almacenar las llamadas a los métodos. Las llamadas a los métodos requieren direcciones de memoria en las que ser almacenadas al igual que las referencias a los objetos que forman parte de su código. Si al ejecutar un test obtenemos una excepción del tipo StackOverflowError, debemos ampliar el tamaño de dicha memoria (cuyo valor por defecto es 1MB) en Run Configurations > Arguments > VM Arguments. Mediante la opción -Xss podemos ampliar dicho tamaño a por ejemplo 2MB de la forma: -Xss2m (también -Xss2M). Servirá únicamente para dicho test.

6. Para aquellos algoritmos que requieran algún agregado de datos (por ejemplo listas o conjuntos) cuyo tamaño determine el tamaño del problema, puede ser necesario aumentar la cantidad de memoria que la máquina virtual de Java utiliza para almacenar sus elementos. Si al ejecutar un test obtenemos una excepción del tipo OutOfMemoryError: Java heap space, debemos ampliar el tamaño de dicha memoria en Run Configurations > Arguments > VM Arguments. Mediante las opciones -Xms y -Xmx podemos establecer respectivamente el tamaño inicial y máximo de dicha memoria (montículo) de forma análoga a -Xss (por ejemplo -Xmx1500m). Nuevamente, servirá únicamente para dicho test.

7. Los componentes del paquete us.lsi.curvefitting del proyecto ParteComun que utilizaremos necesitan librerías que enlazan con otras externas implementadas en Python, por tanto, debemos instalarlo, en concreto, usaremos la version 3.12 https://www.python.org/downloads/

8. Con python ya instalado, en la consola de comandos ejecutaremos:
   ```bash
   pip install matplotlib
     
## Estructura

Una vez completados los pasos de instalación, puedes utilizar las clases java de este repositorio para explorar y entender las primitivas matemáticas y los problemas relacionados con la criptografía post-cuántica, a continuación se explica la estructura del proyecto:

1. La carpeta src contiene dos paquetes, el primero de ellos es "Algorithms", donde se implementan los algoritmos per se, asi como la clase padre Lattices.java, que auna algunas funcionalidades comunes de los retículos. El segundo paquete "tests" contiene el análisis empírico de la complejidad de cada algoritmo, asi como una comparación entre ellos.

2. La carpeta ficheros_generados será la responsable de guardar los .txt creados por el programa para luego ser representados en forma de gráfica.
   
3. Para realizar diversas pruebas, basta con modíficar los parámetros iniciales de la clase, tenga en cuenta que cambios en el tamaño del problema conllevarán un gran impacto en el rendimiento, dada la intratabilidad de los problemas, el aumento de memoría y calculos requeridos será exponencial.

## Contribución

Si quieres contribuir a este proyecto, nos encantaría tu ayuda. Sigue estas pautas para colaborar:

1. Haz un fork de este repositorio para tener tu propia copia.
   
2. Crea una rama para tu contribución:
   ```bash
   git checkout -b feature/nuevamejora

3. Realiza los cambios en la rama. Asegúrate de que sean claros y estén bien documentados.

4. Haz commit de tus cambios:
   ```bash
   git commit -m "Añadir descripción breve del cambio"

5. Sube tus cambios a tu repositorio remoto:
   ```bash
   git push origin feature/nueva-mejora

6. Crea una pull request hacia este repositorio explicando detalladamente los cambios que has realizado.
   








