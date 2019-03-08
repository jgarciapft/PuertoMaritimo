/**
 * // TODO Documentaci�n clase Grua
 *
 * @author Juan Pablo Garc�a Plaza P�rez
 * @author Jos� �ngel Concha Carrasco
 */

public class Grua implements Runnable {

    private int identificador;          // Identificador de la Gr�a
    private TIPO_GRUA tipo;             // Tipo de la gr�a (dependiente de los cargamentos que vaya a coger)

    /**
     * Constructor parametrizado. Instancia una nueva grua a partir de un identificador y un tipo
     *
     * @param identificador Identificador de la grua
     * @param tipo          Tipo de la grua
     */
    public Grua(int identificador, TIPO_GRUA tipo) {
        this.identificador = identificador;
        this.tipo = tipo;
    }

    /**
     * // TODO Documentar m�todo run()
     */
    @Override
    public void run() {

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
    public TIPO_GRUA getTipo() {
        return tipo;
    }

    /**
     * M�todo modificador del atributo {@link Grua#identificador}
     *
     * @param identificador Nuevo identificador de la gr�a
     */
    public void setIdentificador(int identificador) {
        this.identificador = identificador;
    }

    /**
     * M�todo modificador del atributo {@link Grua#tipo}
     *
     * @param tipo Nuevo tipo de la gr�a
     */
    public void setTipo(TIPO_GRUA tipo) {
        this.tipo = tipo;
    }
}