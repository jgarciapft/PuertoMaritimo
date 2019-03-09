import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Clase principal del proyecto
 *
 * @author Juan Pablo Garc�a Plaza P�rez
 * @author Jos� �ngel Concha Carrasco
 */

public class Main {

    private static final int NUM_BARCOS_ENTRADA_SIM = 3;    // N�mero de barcos de entrada creados para la simulaci�n
    private static final int NUM_BARCOS_SALIDA_SIM = 3;     // N�mero de barcos de salida creados para la simulaci�n

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
        List<Grua> gruas = new ArrayList<>();               // Colecci�n de gr�as simuladas
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

        // Creaci�n e incorporaci�n de un barco mercante. Llevar� el identificador 0 para distinguirlo.
        Barco mercante = new BarcoMercante(0, ESTADO_BARCO.ENTRADA, 1, 1, 1);
        barcos.add(mercante);

        // Creaci�n de gr�as. Sus indices comenzar�n a partir del 10 para distinguirlas.
        gruas.add(new Grua(10, TIPO_CARGAMENTO.AZUCAR));
        gruas.add(new Grua(11, TIPO_CARGAMENTO.HARINA));
        gruas.add(new Grua(12, TIPO_CARGAMENTO.SAL));

        // Ejecuci�n de la simulaci�n

        Collections.shuffle(barcos);                        // Distribuye el orden de los barcos
        Iterator<Barco> barcoIterator = barcos.iterator();
        List<Thread> hilos = new ArrayList<>();             // Colecci�n de hilos instanciados
        while (barcoIterator.hasNext()) {
            Thread hiloBarco = new Thread(barcoIterator.next());
            hiloBarco.start();                              // Lanza cada hilo
            hilos.add(hiloBarco);                           // Guarda el hilo instanciado y lanzado
        }

        Collections.shuffle(gruas);                             // Lo mismo pero para gr�as
        Iterator<Grua> gruaIterator = gruas.iterator();
        while (gruaIterator.hasNext()) {
            Thread hiloGrua = new Thread(gruaIterator.next());
            hiloGrua.start();
            hilos.add(hiloGrua);
        }

        for (Thread hilo : hilos) {                         // Espera a que terminen todos los hilos
            try {
                hilo.join();
            } catch (InterruptedException e) {
                mostrarMensaje(e.getMessage());
            }
        }
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