/**
 * TODO Documentaci�n clase Repostador
 *
 * @author Juan Pablo Garc�a Plaza P�rez
 * @author Jos� �ngel Concha Carrasco
 */
public class Repostador implements Runnable {

    private Thread autoThread;          // Autothread

    public Repostador() {
        autoThread = new Thread(this);
        autoThread.start();
    }

    /**
     * El repostador trata de repostar los contenedores de petr�leo de la zona de repostaje
     */
    public void run() {
        if (autoThread.equals(Thread.currentThread())) {
            while (ZonaRepostaje.recuperarInstancia().getActiva())
                repostar();
            System.out.println("\t\tEl Repostador ha terminado su trabajo");
        }
    }

    /**
     * Reposta todos los contenedores de petr�leo de la zona de repostaje dada a su capacidad m�xima
     */
    private void repostar() {
        ZonaRepostaje.recuperarInstancia().reponerContenedores();
    }
}