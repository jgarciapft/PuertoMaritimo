import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Clase principal del proyecto
 *
 * @author Juan Pablo Garc�a Plaza P�rez
 * @author Jos� �ngel Concha Carrasco
 */

public class Main {

    private static final int NUM_BARCOS_ENTRADA_SIM = 2;    // N�mero de barcos de entrada creados para la simulaci�n
    private static final int NUM_BARCOS_SALIDA_SIM = 3;     // N�mero de barcos de salida creados para la simulaci�n
    private static final int NUM_BARCOS_MERCANTES_SIM = 2;  // N�mero de barcos mercantes creados para la simulaci�n
    public static final int NUM_BARCOS_PETROLEROS_SIM = 5; // N�mero de barcos petroleros creados para la simulaci�n

    private static final int NUM_CONT_AZUCAR_BR = 12;       // N�mero de contenedores de az�car que transporta un barco mercante
    private static final int NUM_CONT_HARINA_BR = 5;        // N�mero de contenedores de harina que transporta un barco mercante
    private static final int NUM_CONT_SAL_BR = 20;          // N�mero de contenedores de sal que transporta un barco mercante

    /**
     * Constructor por defecto. Inicia la simulaci�n
     */
    public Main() {
        simulacion();
    }

    /**
     * Realiza la simulaci�n del proyecto
     */
    private void simulacion() {
        List<Barco> barcos = new ArrayList<>();             // Colecci�n de barcos simulados
        List<Thread> hilos = new ArrayList<>();             // Colecci�n de hilos instanciados
        int id = 1;                                         // Identificador asignado a cada barco

        // Creaci�n de barcos

        for (int i = 0; i < NUM_BARCOS_ENTRADA_SIM; i++) {  // Crea los barcos que quieren entrar
            barcos.add(new Barco(id, ESTADO_BARCO.ENTRADA));
            id++;
        }
        for (int i = 0; i < NUM_BARCOS_SALIDA_SIM; i++) {   // Crea los barcos que quieren salir
            barcos.add(new Barco(id, ESTADO_BARCO.SALIDA));
            id++;
        }
        // Creaci�n e incorporaci�n de los barcos mercantes. Llevar�n identificadores negativos para distinguirlos.
        Barco mercante;
        for (int i = -1; i >= -NUM_BARCOS_MERCANTES_SIM; i--) {
            mercante = new BarcoMercante(i, NUM_CONT_AZUCAR_BR, NUM_CONT_HARINA_BR, NUM_CONT_SAL_BR);
            barcos.add(mercante);
        }
        // Creaci�n de gr�as. Sus indices comenzar�n a partir del 10 para distinguirlas
        new Grua(10, TIPO_CARGAMENTO.AZUCAR);
        new Grua(11, TIPO_CARGAMENTO.HARINA);
        new Grua(12, TIPO_CARGAMENTO.SAL);

        // Ejecuci�n de la simulaci�n

        Collections.shuffle(barcos);                        // Distribuye el orden de los barcos
        for (Barco barco : barcos) {
            Thread hiloBarco = new Thread(barco);
            hiloBarco.start();                              // Lanza cada hilo
            hilos.add(hiloBarco);                           // Guarda el hilo instanciado y lanzado
        }

        // Espera a que terminen todos los hilos de barcos

        for (Thread hilo : hilos) {
            try {
                hilo.join();
            } catch (InterruptedException e) {
                mostrarMensaje(e.getMessage());
            }
        }

        // Detiene las gr�as bloqueadas esperando por m�s cargamentos
        Plataforma.recuperarInstancia().setActiva(false);

        mostrarMensaje("FIN del HILO PRINCIPAL");
    }

    /**
     * Muestra un mensaje en una l�nea por consola
     *
     * @param mensaje Mensaje a mostrar
     */
    private void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    /**
     * Punto de entrada del proyecto
     *
     * @param args No definidos
     */
    public static void main(String[] args) {
        new Main();                                         // Inicia la simulaci�n
    }
}