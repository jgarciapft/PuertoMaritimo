/**
 * TODO Documentaci�n clase BarcoPetrolero
 *
 * @author Juan Pablo Garc�a Plaza P�rez
 * @author Jos� �ngel Concha Carrasco
 */
public class BarcoPetrolero extends Barco {

    /**
     * Cantidad de petroleo en el dep�sito de petr�leo
     */
    private int depositoPetroleo;
    /**
     * Cantidad de agua en el dep�sito de agua
     */
    private int depositoAgua;

    /**
     * @param identificador
     * @param depositoPetroleo
     * @param depositoAgua
     */
    public BarcoPetrolero(int identificador, int depositoPetroleo, int depositoAgua) {
        super(identificador, ESTADO_BARCO.ENTRADA);
        // TODO - implement BarcoPetrolero.BarcoPetrolero
    }

    /**
     * Un barco petrolero trata de entrar en el puerto, rellenar sus dep�sitos y salir del puerto
     */
    public void run() {
        // TODO - implement BarcoPetrolero.run
    }

    /**
     * Reposta una cantidad dada de petr�leo
     *
     * @param cantidad Cantidad a repostar de petr�leo
     */
    public void repostarPetroleo(int cantidad) {
        // TODO - implement BarcoPetrolero.repostarPetroleo
    }

    /**
     * Reposta una cantidad dada de agua
     *
     * @param cantidad Cantidad a repostar de agua
     */
    public void repostarAgua(int cantidad) {
        // TODO - implement BarcoPetrolero.repostarAgua
    }

    /**
     * M�todo accesor del atributo depositoPetroleo
     */
    public int getDepositoPetroleo() {
        return depositoPetroleo;
    }

    /**
     * M�todo accesor del atributo depositoAgua
     */
    public int getDepositoAgua() {
        return depositoAgua;
    }

    /**
     * M�todo modificador del atributo depositoPetroleo
     *
     * @param depositoPetroleo Nueva cantidad de petr�leo en el dep�sito
     */
    private void setDepositoPetroleo(int depositoPetroleo) {
        this.depositoPetroleo = depositoPetroleo;
    }

    /**
     * M�todo modificador del atributo depositoAgua
     *
     * @param depositoAgua Nuevo cantidad de agua en el dep�sito
     */
    private void setDepositoAgua(int depositoAgua) {
        this.depositoAgua = depositoAgua;
    }

}