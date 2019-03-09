import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Implementa el patr�n de dise�o Singleton
 * // TODO Documentar clase TorreControl
 *
 * @author Juan Pablo Garc�a Plaza P�rez
 * @author Jos� �ngel Concha Carrasco
 */
public class Plataforma {

    private static Plataforma instancia = null;   // Instancia Singleton de la Plataforma

    private static final int CAPACIDAD = 1;     // N�mero de cargamentos simult�neos en la Plataforma

    private ArrayList<TIPO_CARGAMENTO> almacenados; // Colecci�n de cargamentos almacenados en la Plataforma

    private Lock monitor;
    private Condition esperaAzucar;             // Variable de espera para las gr�as de az�car
    private Condition esperaHarina;             // Variable de espera para las gr�as de Harina
    private Condition esperaSal;                // Variable de espera para las gr�as de Sal
    private Condition esperaMercante;           // Varible de espera para el barco mercante

    /**
     * Constructor por defecto
     */
    private Plataforma() {

        monitor = new ReentrantLock(true);
        esperaAzucar = monitor.newCondition();
        esperaHarina = monitor.newCondition();
        esperaSal = monitor.newCondition();
        esperaMercante = monitor.newCondition();

    }

    /**
     * Deposita un cargamento siempre que la capacidad de la plataforma lo permita y se lo notifica a la gr�a correspondiente.
     *
     * @param barco      Barco mercante
     * @param cargamento Cargamento que deposita en la plataforma
     */
    public void poner(BarcoMercante barco, TIPO_CARGAMENTO cargamento) {
        monitor.lock();
        try {
            // Protocolo de entrada
            while (almacenados.size() > (CAPACIDAD - 1)) {
                imprimirConTimestamp("\t El barco con id: " + barco.getIdentificador() + " est� bloqueado por la plataforma.");
                esperaMercante.await();
            }
            // Acci�n
            TIPO_CARGAMENTO nuevoCargamento = barco.obtenerCargamentoAleatorio();
            almacenados.add(nuevoCargamento);
            imprimirConTimestamp("\t El barco con id: " + barco.getIdentificador() + " a�ade un cargamento" + nuevoCargamento.toString() + "a la plataforma.");
            // Protocolo de salida: Desbloquea �nicamente a la gr�a bloqueada que corresponda con el cargamento depositado
            switch (nuevoCargamento) {
                case AZUCAR:
                    esperaAzucar.signal();
                    imprimirConTimestamp("\t Se ha desbloqueado G-az�car.");
                    break;
                case HARINA:
                    esperaHarina.signal();
                    imprimirConTimestamp("\t Se ha desbloqueado G-harina.");
                    break;
                case SAL:
                    esperaSal.signal();
                    imprimirConTimestamp("\t Se ha desbloqueado G-sal.");
                    break;
            }
            imprimirConTimestamp("\t El barco con id: " + barco.getIdentificador() + " finalmente ha a�adido un cargamento a la plataforma.");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            monitor.unlock();
        }
    }

    public void coger(Grua grua) {
        monitor.lock();
        try {
            // Protocolo de entrada: Se bloquea si no hay ningun cargamento en la plataforma o el cargamento no coincide con la grua
            while (almacenados.size() == 0 || (almacenados.get(0) != grua.getTipo())) {
                imprimirConTimestamp("\t La gr�a con id: " + grua.getIdentificador() + " est� bloqueada.");
                switch (grua.getTipo()) {
                    case AZUCAR:
                        esperaAzucar.await();
                        break;
                    case HARINA:
                        esperaHarina.await();
                        break;
                    case SAL:
                        esperaSal.await();
                        break;
                }
            }
            // Acci�n: consiste en quitar el cargamento de la plataforma
            almacenados.remove(0);
            imprimirConTimestamp("\t La gr�a con id: " + grua.getIdentificador() + " vac�a la plataforma");
            // Protocolo de salida: consiste en notific�rselo al barco mercante por si puediese estar bloqueado
            esperaMercante.signal();
            imprimirConTimestamp("\t La gr�a con id: " + grua.getIdentificador() + " finalmente ha vaciado la plataforma");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            monitor.unlock();
        }
    }

    /**
     * @return Instancia Singleton de la Plataforma
     */
    public synchronized static Plataforma recuperarInstancia() {
        if (instancia != null)
            return instancia;
        else
            instancia = new Plataforma();

        return instancia;
    }

    /**
     * Imprime un mensaje con marca de tiempo por consola en una l�nea
     *
     * @param mensaje Mensaje a imprimir
     */
    private void imprimirConTimestamp(String mensaje) {
        System.out.println("\t[" + System.currentTimeMillis() + "] " + mensaje);
    }
}