/**
 * // TODO Documentaci�n clase Barco
 *
 * @author Juan Pablo Garc�a Plaza P�rez
 * @author Jos� �ngel Concha Carrasco
 */

public class Barco implements Runnable {

    /**
     * Entero �nico para cada barco que lo identifica. Es de solo lectura
     */
    private final int identificador;

    /**
     * @see ComporBarco Estado en el que se encuentra el barco
     */
    private ComporBarco comporBarco;

    /**
     * Constructor parametrizado. Instancia un nuevo barco a partir de un identificador y un estado
     *
     * @param identificador Identificador del barco
     * @param comporBarco   Comportamiento inicial del barco
     */
    public Barco(int identificador, ComporBarco comporBarco) {
        this.identificador = identificador;
        this.comporBarco = comporBarco;
    }

    /**
     * TODO Documentar m�todo run()
     */
    @Override
    public void run() {
        Puerta puerta = Puerta.recuperarInstancia();                    // Instancia Singleton de la puerta
        TorreControl torreControl = TorreControl.recuperarInstancia();  // Instancia Singleton de la torre de control

        comporBarco.comporBarco(this);
    }

    /**
     * M�todo accesor del atributo {@link Barco#identificador}
     *
     * @return Identificador del barco
     */
    protected int getIdentificador() {
        return identificador;
    }

    /**
     * M�todo accesor del atributo {@link Barco#comporBarco}
     *
     * @return Comportamiento actual del barco
     */
    protected ComporBarco getComporBarco() {
        return comporBarco;
    }

    /**
     * M�todo modificador del atributo {@link Barco#comporBarco}
     *
     * @param comporBarco Nuevo estado
     */
    protected void setComporBarco(ComporBarco comporBarco) {
        this.comporBarco = comporBarco;
    }
}