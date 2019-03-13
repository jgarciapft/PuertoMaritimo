import java.util.Collection;
import java.util.HashMap;
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

    /* SEM�FOROS */

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
    private Map<Integer, Semaphore> esperaBarcos;
    /**
     * Sem�foros para bloquear a cada barco cuando vac�en su contenedor
     */
    private Map<Integer, Semaphore> contenVacio;
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

        esperaBarcos = new HashMap<>(Main.NUM_BARCOS_PETROLEROS_SIM);
        contenVacio = new HashMap<>(Main.NUM_BARCOS_PETROLEROS_SIM);
        esperaRepostador = new Semaphore(0);
    }

    /**
     * Registra los sem�foros requeridos para cada barco petrolero esperado
     *
     * @param barcosPetroleros Colecci�n de barcos petroleros esperados
     */
    public void registrarSemaforos(Collection<? extends BarcoPetrolero> barcosPetroleros) {
        for (BarcoPetrolero barcosPetrolero : barcosPetroleros) {
            esperaBarcos.put(barcosPetrolero.getIdentificador(), new Semaphore(0));
            contenVacio.put(barcosPetrolero.getIdentificador(), new Semaphore(0));
        }
    }

    /**
     * Registra un contenedor de petr�leo para cada barco petrolero esperado
     *
     * @param barcosPetroleros Colecci�n de barcos petroleros esperados
     */
    public void registrarContenedores(Collection<? extends BarcoPetrolero> barcosPetroleros) {
        for (BarcoPetrolero barcosPetrolero : barcosPetroleros)
            contenPetroleo.put(barcosPetrolero.getIdentificador(), new ContenedorPetroleo(ContenedorPetroleo.CANT_INICIAL_CONT_PETROLEO,
                    ContenedorPetroleo.CANTIDAD_MAX_PETROLEO));
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
        // Protocolo de entrada: Exclusi�n mutua sobre el dep�sito de agua
        imprimirConTimestamp("El barco petrolero " + barco.getIdentificador() + " INTENTA repostar " + cantidad + " L de AGUA");
        try {
            mutexContenAgua.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Acci�n: Repostar una cantidad de agua
        imprimirConTimestamp("El barco petrolero " + barco.getIdentificador() + " REPOSTA " + cantidad + " L de AGUA");
        barco.repostarAgua(cantidad);
        // Protocolo de salida: Exclusi�n mutua sobre el dep�sito de agua
        imprimirConTimestamp("El barco petrolero " + barco.getIdentificador() + " HA REPOSTADO" + cantidad + " L de AGUA");
        mutexContenAgua.release();
    }

    /**
     * Rellena a la capacidad m�xima los contenedores de petr�leo. Utilizado por el repostador
     */
    public void reponerContenedores() {
        // Protocolo de entrada
        imprimirConTimestamp("El Repostador INTENTA REPONER los contenedores de petr�leo");
        try {
            esperaRepostador.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Acci�n: repostar todos los contenedores
        imprimirConTimestamp("El Repostador REPOSTA los contenedores de petr�leo");
        for (ContenedorPetroleo contenedorPetroleo : contenPetroleo.values())
            contenedorPetroleo.reponer();
        imprimirConTimestamp("El Repostador HA REPOSTADO los contenedores de petr�leo");
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

    /**
     * Imprime un mensaje con marca de tiempo por consola en una l�nea
     *
     * @param mensaje Mensaje a imprimir
     */
    private void imprimirConTimestamp(String mensaje) {
        System.out.println("\t\t[" + System.currentTimeMillis() + "] " + mensaje);
    }
}