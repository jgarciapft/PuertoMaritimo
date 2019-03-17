/**
 * // TODO Documentaci�n clase Grua
 *
 * @author Juan Pablo Garc�a Plaza P�rez
 * @author Jos� �ngel Concha Carrasco
 */

public class Grua implements Runnable {

    private Thread autoThread;              // Autothread
    /**
     * Identificador de la gr�a
     */
    private final int identificador;
    /**
     * Tipo de cargamento con el que opera la gr�a
     */
    private TIPO_CARGAMENTO tipo;

    /**
     * Constructor parametrizado. Instancia una nueva grua a partir de un identificador y un tipo.
     * Lanza el autothread
     *
     * @param identificador Identificador de la grua
     * @param tipo          Tipo de la grua
     */
    public Grua(int identificador, TIPO_CARGAMENTO tipo) {
        this.identificador = identificador;
        this.tipo = tipo;

        autoThread = new Thread(this);
        autoThread.start();
    }

    /**
     * // TODO Documentar m�todo run()
     */
    @Override
    public void run() {
        // Solo se ejecuta si es el autothread el que lo invoca
        if (autoThread.equals(Thread.currentThread())) {
            Plataforma plataforma = Plataforma.recuperarInstancia();    // Instancia Singleton de la plataforma
            // Las gruas deber�n estar operativas mientras la plataforma est� operativa
            while (plataforma.getActiva())
                plataforma.coger(this);
            System.out.println("\t\tLa grua " + getTipo() + " " + getIdentificador() + " ha terminado su trabajo");
        }
    }

    /**
     * M�todo accesor del atributo {@link Grua#identificador}
     *
     * @return Identificador actual de la gr�a
     */
    public int getIdentificador() {
        return identificador;
    }

    /**
     * M�todo accesor del atributo {@link Grua#tipo}
     *
     * @return Tipo actual de la gr�a
     */
    public TIPO_CARGAMENTO getTipo() {
        return tipo;
    }

    /**
     * M�todo modificador del atributo {@link Grua#tipo}
     *
     * @param tipo Nuevo tipo de la gr�a
     */
    public synchronized void setTipo(TIPO_CARGAMENTO tipo) {
        this.tipo = tipo;
    }
}