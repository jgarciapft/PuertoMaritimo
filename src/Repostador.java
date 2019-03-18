/**
 * TODO Documentaci�n clase Repostador
 *
 * @author Juan Pablo Garc�a Plaza P�rez
 * @author Jos� �ngel Concha Carrasco
 */
public class Repostador implements Runnable {

    public Repostador() {
    }

    /**
     * El repostador trata de repostar los contenedores de petr�leo de la zona de repostaje
     */
    public void run() {
        repostar();
    }

    /**
     * Reposta todos los contenedores de petr�leo de la zona de repostaje dada a su capacidad m�xima
     */
    private void repostar() {
        ZonaRepostaje.recuperarInstancia().reponerContenedores();
    }
}