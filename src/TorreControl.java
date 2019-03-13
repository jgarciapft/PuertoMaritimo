import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Implementa el partrón de diseño Singleton
 * // TODO Documentar clase TorreControl
 *
 * @author Juan Pablo García Plaza Pérez
 * @author José Ángel Concha Carrasco
 */
public class TorreControl {

    private static TorreControl instancia = null;   // Instancia Singleton de la TorreControl
    private int barcosEntrando;                     // Contador de barcos entrando
    private int barcosSaliendo;                     // Contador de barcos que están saliendo
    private int barcosEsperandoSalir;               // Contador de barcos esperando por salir

    private Lock monitor;
    private Condition esperaEntrantes;
    private Condition esperaSalientes;

    /**
     * Constructor por defecto
     */
    private TorreControl() {
        barcosEntrando = 0;
        barcosSaliendo = 0;
        barcosEsperandoSalir = 0;

        monitor = new ReentrantLock(true);
        esperaEntrantes = monitor.newCondition();
        esperaSalientes = monitor.newCondition();
    }

    /**
     * Protocolo de entrada de los BARCOS de ENTRADA
     */
    public void permisoEntrada(Barco barco) {
        monitor.lock();
        try {
            // Protocolo de entrada
            imprimirConTimestamp("El barco " + barco.getIdentificador() + " pide permiso para entrar");
            while (barcosSaliendo > 0 || barcosEsperandoSalir > 0) {
                imprimirConTimestamp("El barco " + barco.getIdentificador() + " bloqueado para entrar");
                esperaEntrantes.await();
            }
            // Acción
            imprimirConTimestamp("El barco " + barco.getIdentificador() + " obtiene el permiso para entrar");
            barcosEntrando++;

            esperaEntrantes.signal();   // Si comienzan a entrar barcos es posible que haya alguno bloqueado que quiera entrar también
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            monitor.unlock();
        }
    }

    /**
     * Protocolo de entrada de los BARCOS de SALIDA
     */
    public void permisoSalida(Barco barco) {
        monitor.lock();
        try {
            // Protocolo de entrada
            imprimirConTimestamp("El barco " + barco.getIdentificador() + " pide permiso para salir");
            while (barcosEntrando > 0) {
                imprimirConTimestamp("El barco " + barco.getIdentificador() + " bloqueado para salir");
                barcosEsperandoSalir++;
                esperaSalientes.await();
            }
            // Acción
            imprimirConTimestamp("El barco " + barco.getIdentificador() + " obtiene permiso para salir");
            barcosSaliendo++;

            esperaSalientes.signal();   // Si comienzan a salir barcos es posible que haya alguno bloqueado que quiera salir también
            barcosEsperandoSalir--;     // Por tanto, el contador se pondrá a 0 cuando se hayan desbloqueado todos
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            monitor.unlock();
        }
    }

    /**
     * Protocolo de salida de los BARCOS de ENTRADA
     */
    public synchronized void finEntrada(Barco barco) {
        monitor.lock();
        try {
            // Acción
            imprimirConTimestamp("El barco " + barco.getIdentificador() + " finalmente ha entrado");
            barcosEntrando--;
            // Protocolo de salida
            if (barcosEntrando == 0) {
                imprimirConTimestamp("Entran todos los barcos de entrada");
                esperaSalientes.signal();
            }
        } finally {
            monitor.unlock();
        }
    }

    /**
     * Protocolo de salida de los BARCOS de SALIDA
     */
    public synchronized void finSalida(Barco barco) {
        monitor.lock();
        try {
            // Acción
            imprimirConTimestamp("El barco " + barco.getIdentificador() + " finalmente ha salido");
            barcosSaliendo--;
            // Protocolo de salida
            if (barcosSaliendo == 0) {
                imprimirConTimestamp("Salen todos los barcos de salida");
                esperaEntrantes.signal();
            }
        } finally {
            monitor.unlock();
        }
    }

    /**
     * Método accesor del atributo {@link TorreControl#barcosEntrando}
     *
     * @return Número de barcos entrando
     */
    public int getBarcosEntrando() {
        return barcosEntrando;
    }

    /**
     * Método accesor del atributo {@link TorreControl#barcosSaliendo}
     *
     * @return Número de barcos saliendo
     */
    public int getBarcosSaliendo() {
        return barcosSaliendo;
    }

    /**
     * Método modificador del atributo {@link TorreControl#barcosEntrando}
     *
     * @param barcosEntrando Nuevo número de barcos entrando
     */
    private synchronized void setBarcosEntrando(int barcosEntrando) {
        this.barcosEntrando = barcosEntrando;
    }

    /**
     * Método modificador del atributo {@link TorreControl#barcosSaliendo}
     *
     * @param barcosSaliendo Nuevo número de barcos saliendo
     */
    private synchronized void setBarcosSaliendo(int barcosSaliendo) {
        this.barcosSaliendo = barcosSaliendo;
    }

    /**
     * @return Instancia Singleton de la TorreControl
     */
    public synchronized static TorreControl recuperarInstancia() {
        if (instancia == null)
            instancia = new TorreControl();

        return instancia;
    }

    /**
     * Imprime un mensaje con marca de tiempo por consola en una línea
     *
     * @param mensaje Mensaje a imprimir
     */
    private void imprimirConTimestamp(String mensaje) {
        System.out.println("\t[" + System.currentTimeMillis() + "] " + mensaje);
    }

}