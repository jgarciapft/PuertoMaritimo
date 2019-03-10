import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Implementa el patr�n de dise�o Singleton
 * // TODO Documentar clase Plataforma
 *
 * @author Juan Pablo Garc�a Plaza P�rez
 * @author Jos� �ngel Concha Carrasco
 */
public class Plataforma {
    private static Plataforma instancia = null;     // Instancia Singleton de la Plataforma
    private TIPO_CARGAMENTO almacenado;             // Cargamento almacenado en la Plataforma
    private boolean activa;                         // Bandera para indicar si la plataforma est� operativa

    private Lock monitor;
    private Condition esperaAzucar;                 // Variable de espera para las gr�as de Az�car
    private Condition esperaHarina;                 // Variable de espera para las gr�as de Harina
    private Condition esperaSal;                    // Variable de espera para las gr�as de Sal
    private Condition esperaMercante;               // Varible de espera para el barco mercante

    /**
     * Constructor por defecto
     */
    private Plataforma() {
        activa = true;
        almacenado = null;
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
            while (almacenado != null) {
                imprimirConTimestamp("El barco mercante " + barco.getIdentificador() + " est� bloqueado por la plataforma");
                esperaMercante.await();
            }
            // Acci�n
            almacenado = cargamento;
            imprimirConTimestamp("El barco mercante " + barco.getIdentificador() + " a�ade un cargamento de "
                    + cargamento + " a la plataforma");
            // Protocolo de salida: Desbloquea �nicamente a la gr�a bloqueada que corresponda con el cargamento depositado
            switch (cargamento) {
                case AZUCAR:
                    esperaAzucar.signal();
                    imprimirConTimestamp("Se ha desbloqueado G-az�car");
                    break;
                case HARINA:
                    esperaHarina.signal();
                    imprimirConTimestamp("Se ha desbloqueado G-harina");
                    break;
                case SAL:
                    esperaSal.signal();
                    imprimirConTimestamp("Se ha desbloqueado G-sal");
            }
            imprimirConTimestamp("El barco mercante " + barco.getIdentificador() +
                    " finalmente ha a�adido un cargamento a la plataforma. Cargamentos restantes: " + barco.getCargamentosRestantes());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            monitor.unlock();
        }
    }

    /**
     * Una grua intenta coger de la plataforma
     *
     * @param grua Grua que intenta coger de la plataforma
     */
    public void coger(Grua grua) {
        monitor.lock();
        try {
            // Protocolo de entrada: Se bloquea si no hay ningun cargamento en la plataforma o el cargamento no coincide con la grua
            while (getActiva() && (almacenado == null || almacenado != grua.getTipo())) {
                imprimirConTimestamp("La gr�a (" + grua.getTipo() + ") " + grua.getIdentificador() + " est� bloqueada");
                switch (grua.getTipo()) {
                    case AZUCAR:
                        esperaAzucar.await();
                        break;
                    case HARINA:
                        esperaHarina.await();
                        break;
                    case SAL:
                        esperaSal.await();
                }
            }
            // Acci�n: consiste en quitar el cargamento de la plataforma
            almacenado = null;
            imprimirConTimestamp("La gr�a (" + grua.getTipo().toString() + ") " + grua.getIdentificador() + " vac�a la plataforma");
            // Protocolo de salida: consiste en notific�rselo al barco mercante por si puediese estar bloqueado
            esperaMercante.signal();
            imprimirConTimestamp("La gr�a (" + grua.getTipo().toString() + ") " + grua.getIdentificador() + " finalmente ha vaciado la plataforma");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            monitor.unlock();
        }
    }

    /**
     * M�todo accesor del atributo almacenado.
     *
     * @return El cargamento almacenado
     */
    public TIPO_CARGAMENTO getAlmacenado() {
        return almacenado;
    }

    /**
     * M�todo accesor del atributo nBarcosMercantes
     *
     * @return El cargamento almacenado
     */
    public boolean getActiva() {
        return activa;
    }

    /**
     * M�todo modificador del atributo activa
     *
     * @param activa Nuevo estado de la plataforma
     */
    public void setActiva(Boolean activa) {
        monitor.lock();
        try {
            this.activa = activa;
            if (!getActiva()) {
                esperaAzucar.signal();
                esperaHarina.signal();
                esperaSal.signal();
            }
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
        System.out.println("\t\t[" + System.currentTimeMillis() + "] " + mensaje);
    }
}