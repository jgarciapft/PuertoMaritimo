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
     * Instancia Singleton de la zona de repostaje
     */
    private static ZonaRepostaje instancia = null;
    /**
     * Colecci�n de contenedores de petr�leo reservados para los barcos petroleros. Cada barco registra mediante
     * identificador un contenedor de petr�leo privado
     */
    private Map<Integer, ContenedorPetroleo> contenPetroleo;
    /**
     * Bandera para indicar si la zona de repostaje est� operativa
     */
    private boolean activa;
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
        activa = true;

        /* INICIALIZACION DE LOS SEM�FOROS */

        mutexContenPetroleo = new Semaphore(1);
        mutexContenAgua = new Semaphore(1);

        esperaBarcos = new ArrayList<>(Main.NUM_BARCOS_PETROLEROS_SIM);
        contenVacio = new ArrayList<>(Main.NUM_BARCOS_PETROLEROS_SIM);
        for (int i = 0; i < Main.NUM_BARCOS_PETROLEROS_SIM; i++) {
            esperaBarcos.add(new Semaphore(0));
            contenVacio.add(new Semaphore(0));
        }
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
                contenPetroleo.put(barco.getIdentificador(), new ContenedorPetroleo(ContenedorPetroleo.CANT_INICIAL_CONT_PETROLEO,
                        ContenedorPetroleo.CANTIDAD_MAX_PETROLEO));
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
        barco.repostarAgua(cantidad);
    }

    /**
     * Rellena a la capacidad m�xima los contenedores de petr�leo. Utilizado por el repostador
     */
    public void reponerContenedores() {
        for (ContenedorPetroleo contenedorPetroleo : contenPetroleo.values())
            contenedorPetroleo.reponer();
    }

    /**
     * M�todo accesor del atributo {@link ZonaRepostaje:activa}
     */
    public boolean getActiva() {
        return activa;
    }

    /**
     * M�todo modificador del atributo {@link ZonaRepostaje:activa}
     *
     * @param activa Nuevo valor de la bandera
     */
    public void setActiva(boolean activa) {
        this.activa = activa;
        if (!getActiva())                   // Si la zona no est� activa desbloqueamos al repostaador para que finalice
            esperaRepostador.release();
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