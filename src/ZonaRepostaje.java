import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

/**
 * Clase que modela el comportamiento de la Zona de Repostaje. Sigue un patr�n de dise�o Singleton (derivado del plantemiento
 * de los problemas). Su finalidad es la de albergar los distintos contenedores y gestionar las posibles operaciones que ciertos
 * barcos determinados pueden realizar con estos. Entre estas funciones destacamos Repostar petr�leo y Repostar agua.
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
     * Contador de barcos petroleros esperando repostar
     */
    private int barcosEsperando;
    /**
     * Contador de contenedores de petr�leo vac�os esperando a ser repostados
     */
    private int contenRepostar;
    /**
     * Bandera para indicar si la zona de repostaje est� operativa
     */
    private boolean activa;

    /* SEM�FOROS */

    /**
     * Sem�foro de exclusi�n mutua sobre el contador de barcos esperando para repostar
     */
    private Semaphore mutexBarcosEsperando;
    /**
     * Sem�foro de exclusi�n mutua sobre el contador de contenedores a repostar
     */
    private Semaphore mutexContenRepostar;
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
        barcosEsperando = 0;
        contenRepostar = 0;
        activa = true;

        /* INICIALIZACION DE LOS SEM�FOROS */

        mutexBarcosEsperando = new Semaphore(1);
        mutexContenAgua = new Semaphore(1);
        mutexContenRepostar = new Semaphore(1);

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
                    ContenedorPetroleo.CANTIDAD_MAX_CONT_PETROLEO));
    }

    /**
     * Pide permiso a la zona de repostaje para empezar a repostar. Ning�n barco comienza a repostar hasta que no
     * hayan llegado todos a la zona de repostaje
     *
     * @param barco Barco que llega a la zona de repostaje
     */
    public void permisoRepostaje(BarcoPetrolero barco) {
        try {
            // Protocolo de entrada - 'barcosEsperando' : Adquirir exclusi�n m�tua
            imprimirConTimestamp("El barco " + barco.getIdentificador() + " pide permiso para repostar");
            mutexBarcosEsperando.acquire();
            // Acci�n - 'barcosEsperando' : Incrementar en 1 el contador de barcos esperando
            setBarcosEsperando(getBarcosEsperando() + 1);
            // Protocolo de entrada - 'barcosEsperando' : Liberar exclusi�n m�tua
            mutexBarcosEsperando.release();

            if (getBarcosEsperando() == Main.NUM_BARCOS_PETROLEROS_SIM) {   // Comprueba si este barco es el �ltimo en llegar
                imprimirConTimestamp("El barco " + barco.getIdentificador() + " DESBLOQUEA a los barcos petroleros");
                for (Semaphore semaphore : esperaBarcos.values())           // Libera los sem�foros de espera al resto de barcos
                    semaphore.release();
            } else {
                imprimirConTimestamp("El barco " + barco.getIdentificador() + " bloqueado para repostar");
                esperaBarcos.get(barco.getIdentificador()).acquire();       // El barco se bloquea esperando al resto
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reposta en el barco dado la cantidad dada de petr�leo
     *
     * @param barco    Barco que repostar
     * @param cantidad Cantidad de petr�leo a repostar en el barco
     */
    public void repostarPetroleo(BarcoPetrolero barco, int cantidad) {
        // Mientras se pueda repostar sigue repostando. Si no se repost� ninguna cantidad el contenedor est� vacio
        imprimirConTimestamp("El barco petrolero " + barco.getIdentificador() + " INTENTA repostar " + cantidad + " L de PETROLEO");
        if (!barco.repostarPetroleo(contenPetroleo.get(barco.getIdentificador()).vaciar(cantidad))) {
            try {
                // Protocolo de entrada - 'contenRepostar' : Adquirir exclusi�n m�tua
                imprimirConTimestamp("El barco petrolero " + barco.getIdentificador() + " tiene VAC�O su CONTENEDOR de PETROLEO");
                mutexContenRepostar.acquire();
                // Acci�n - 'contenRepostar' : Incrementar en 1 el contador de contenedores a repostar
                setContenRepostar(getContenRepostar() + 1);
                // Protocolo de entrada - 'contenRepostar' : Liberar exclusi�n m�tua
                mutexContenRepostar.release();

                // Comprueba si este barco fue el �ltimo en vaciar su contenedor de petr�leo
                if (getContenRepostar() == Main.NUM_BARCOS_PETROLEROS_SIM) {
                    esperaRepostador.release();
                    setContenRepostar(0);                                   // Reinicia el contador de contenedor a repostar
                } else {
                    imprimirConTimestamp("El barco petrolero " + barco.getIdentificador() + " BLOQUEADO para REPOSTAR PETROLEO");
                    contenVacio.get(barco.getIdentificador()).acquire();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            imprimirConTimestamp("El barco petrolero " + barco.getIdentificador() + " REPOSTA " + cantidad + " L de PETROLEO. Repostado : " +
                    barco.getDepositoPetroleo() + "L");
        }
    }

    /**
     * Reposta en el barco dado la cantidad dada de agua
     *
     * @param barco    Barco a repostar
     * @param cantidad Cantidad de agua a repostar en el barco
     */
    public void repostarAgua(BarcoPetrolero barco, int cantidad) {
        // Protocolo de entrada: Adquirir exclusi�n mutua sobre el dep�sito de agua
        imprimirConTimestamp("El barco petrolero " + barco.getIdentificador() + " INTENTA repostar " + cantidad + " L de AGUA");
        try {
            mutexContenAgua.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Acci�n: Repostar una cantidad de agua
        barco.repostarAgua(cantidad);
        // Protocolo de salida: Liberar exclusi�n mutua sobre el dep�sito de agua
        imprimirConTimestamp("El barco petrolero " + barco.getIdentificador() + " HA REPOSTADO " + cantidad +
                " L de AGUA. Repostado : " + barco.getDepositoAgua() + "L");
        mutexContenAgua.release();
    }

    /**
     * Rellena a la capacidad m�xima los contenedores de petr�leo. Utilizado por el repostador
     */
    public void reponerContenedores() {
        // Protocolo de entrada: Bloqueo del repostador. Los contenedores est�n inicialmente llenos
        imprimirConTimestamp("El Repostador INTENTA REPONER los contenedores de petr�leo");
        try {
            esperaRepostador.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (getActiva()) {                                              // Comprueba que la plataforma est� operativa
            // Acci�n: Repostar todos los contenedores
            imprimirConTimestamp("El Repostador REPOSTA los contenedores de petr�leo");
            for (ContenedorPetroleo contenedorPetroleo : contenPetroleo.values())
                contenedorPetroleo.reponer();
            // Protocolo de salida: Desbloquear a los barcos que quieren seguir repostando
            imprimirConTimestamp("El Repostador HA REPOSTADO los contenedores de petr�leo");
            for (Semaphore semaphore : contenVacio.values())
                semaphore.release();
        }
    }

    /**
     * M�todo accesor del atributo {@link ZonaRepostaje:activa}
     */
    public boolean getActiva() {
        return activa;
    }

    /**
     * M�todo accesor del atributo {@link ZonaRepostaje:barcosEsperando}
     */
    private int getBarcosEsperando() {
        return barcosEsperando;
    }

    /**
     * M�todo accesor del atributo {@link ZonaRepostaje:contenRepostar}
     */
    private int getContenRepostar() {
        return contenRepostar;
    }

    /**
     * M�todo modificador del atributo {@link ZonaRepostaje:activa}
     *
     * @param activa Nuevo valor de la bandera
     */
    public void setActiva(boolean activa) {
        this.activa = activa;
        // Si la zona no est� activa desbloqueamos al repostaador para que finalice
        if (!getActiva())
            esperaRepostador.release();
    }

    /**
     * M�todo modificador del atributo {@link ZonaRepostaje:barcosEsperando}
     *
     * @param barcosEsperando Nuevo n�mero de barcos esperando
     */
    private void setBarcosEsperando(int barcosEsperando) {
        this.barcosEsperando = barcosEsperando;
    }

    /**
     * M�todo modificador del atributo {@link ZonaRepostaje:contenRepostar}
     *
     * @param contenRepostar Nuevo n�mero de contenedores a repostar
     */
    private void setContenRepostar(int contenRepostar) {
        this.contenRepostar = contenRepostar;
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