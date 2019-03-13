import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

/**
 * TODO Documentaci�n clase ZonaRepostaje
 *
 * @author Juan Pablo Garc�a Plaza P�rez
 * @author Jos� �ngel Concha Carrasco
 */
public class ZonaRepostaje {

    /**
     * Cantidad inicial de los contenedores de petr�leo registrados
     */
    private static final int CANT_INICIAL_CONT_PETR = 3000;
    /**
     * Capacidad m�xima de los contenedores de petr�leo registrados
     */
    private static final int CAP_MAX_CONT_PETR = 3000;

    /**
     * Instancia Singleton de la zona de repostaje
     */
    private static ZonaRepostaje instancia = null;
    /**
     * Colecci�n de contenedores de petr�leo reservados para los barcos petroleros. Cada barco registra mediante
     * identificador un contenedor de petr�leo privado
     */
    private Map<Integer, ContenedorPetroleo> contenPetroleo;
    /**
     * Sem�foro de exclusi�n mutua sobre la colecci�n de contenedores de petr�leo
     */
    private Semaphore mutexContenPetroleo;
    /**
     * Sem�foro de exclusi�n m�tua sobre el dep�sito de agua
     */
    private Semaphore mutexContenAgua;
    /**
     * Sem�foros para resolver la condici�n de sincronizaci�n inicial de que todos los barcos se esperen unos a otros
     */
    private List<Semaphore> esperaBarcos;
    /**
     * Sem�foros para bloquear a cada barco cuando vac�en su contenedor
     */
    private List<Semaphore> contenVacio;
    /**
     * Sem�foro en el que bloquear al repostador
     */
    private Semaphore esperaRepostador;

    private ZonaRepostaje() {
        contenPetroleo = new HashMap<>();

        /** INICIALIZACION DE LOS SEM�FOROS */

        mutexContenPetroleo = new Semaphore(1);
        mutexContenAgua = new Semaphore(1);

        esperaBarcos = new ArrayList<>(Main.NUM_BARCOS_PETROLEROS_SIM);
        for (int i = 0; i < Main.NUM_BARCOS_PETROLEROS_SIM; i++)
            esperaBarcos.add(new Semaphore(0));

        contenVacio = new ArrayList<>(Main.NUM_BARCOS_PETROLEROS_SIM);
        for (int i = 0; i < Main.NUM_BARCOS_PETROLEROS_SIM; i++)
            contenVacio.add(new Semaphore(0));

        esperaRepostador = new Semaphore(0);

    }

    /**
     * Registra un nuevo contenedor de petr�leo reservado para un barco por identificador
     *
     * @param barco Barco que registra el contenedor
     * @return Si se pudo registrar el contenedor. Devuelve Falso si ya hay un contenedor registrado con ese identificador
     * o el barco es nulo, Verdadero en otro caso
     */
    public boolean registrarContenedor(Barco barco) {
        if (barco != null) {
            if (!contenPetroleo.containsKey(barco.getIdentificador())) {
                contenPetroleo.put(barco.getIdentificador(), new ContenedorPetroleo(CANT_INICIAL_CONT_PETR, CAP_MAX_CONT_PETR));
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Reposta en el barco dado la cantidad dada de petr�leo
     *
     * @param barco    Barco que repostar
     * @param cantidad Cantidad de petr�leo a repostar en el barco
     */
    public void repostarPetroleo(BarcoPetrolero barco, int cantidad) {
        // TODO - implement ZonaRepostaje.repostarPetroleo
        barco.repostarPetroleo(contenPetroleo.get(barco.getIdentificador()).vaciar(cantidad));
    }

    /**
     * Reposta en el barco dado la cantidad dada de agua
     *
     * @param barco    Barco a repostar
     * @param cantidad Cantidad de agua a repostar en el barco
     */
    public void repostarAgua(BarcoPetrolero barco, int cantidad) {
        // TODO - implement ZonaRepostaje.repostarAgua
    }

    /**
     * Rellena a la capacidad m�xima los contenedores de petr�leo. Utilizado por el repostador
     */
    public void reponerContenedores() {
        // TODO - implement ZonaRepostaje.reponerContenedores
    }

    /**
     * @return Instancia Singleton de la zona de repostaje
     */
    public synchronized static ZonaRepostaje recuperarInstancia() {
        if (instancia == null)
            instancia = new ZonaRepostaje();

        return instancia;
    }

}