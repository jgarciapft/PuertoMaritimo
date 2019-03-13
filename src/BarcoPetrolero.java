/**
 * TODO Documentaci�n clase BarcoPetrolero
 *
 * @author Juan Pablo Garc�a Plaza P�rez
 * @author Jos� �ngel Concha Carrasco
 */
public class BarcoPetrolero extends Barco {

    private static final int LIMITE_PETROLEO = 3000;     // Cantidad de petr�leo a recoger
    private static final int LIMITE_AGUA = 5000;         // Cantidad de agua a recoger
    private static final int CANTIDAD_REPOSTAJE = 1000;     // Cantidad que se repone de forma gen�rica

    private int depositoPetroleo;           // Cantidad de petroleo en el dep�sito de petr�leo
    private int depositoAgua;               // Cantidad de agua en el dep�sito de agua

    /**
     * @param identificador
     * @param depositoPetroleo
     * @param depositoAgua
     */
    public BarcoPetrolero(int identificador, int depositoPetroleo, int depositoAgua) {
        super(identificador, ESTADO_BARCO.ENTRADA);
        this.depositoPetroleo = depositoPetroleo;
        this.depositoAgua = depositoAgua;
    }

    /**
     * Un barco petrolero trata de entrar en el puerto, rellenar sus dep�sitos y salir del puerto
     */
    public void run() {

        ZonaRepostaje zonaRepostaje = ZonaRepostaje.recuperarInstancia();

        // Protocolo com�n a los barcos de entrada
        super.run();

        // Protocolo espec�fico
        while (!estaLleno()) {
            // En caso de que le falte agua repostar� agua.
            if (!aguaCompleto()) zonaRepostaje.repostarAgua(this, CANTIDAD_REPOSTAJE);
            // En caso de que le falte petr�leo repostar� petr�leo.
            if (!petroleoCompleto()) zonaRepostaje.repostarPetroleo(this, CANTIDAD_REPOSTAJE);
        }

        // Ya no hay m�s cargamentos y abandona la zona de descarga
        imprimirConTimestamp("El barco " + getIdentificador() + " abandona la zona de repostaje");

        // Los barcos que abandonan la zona de descarga salen del puerto
        setEstado(ESTADO_BARCO.SALIDA);
        // Protocolo com�n a los barcos de salida
        super.run();

    }

    /**
     * Reposta una cantidad dada de petr�leo
     *
     * @param cantidad Cantidad a repostar de petr�leo
     */
    public void repostarPetroleo(int cantidad) {
        setDepositoPetroleo(getDepositoPetroleo() + cantidad);
    }

    /**
     * Reposta una cantidad dada de agua
     *
     * @param cantidad Cantidad a repostar de agua
     */
    public void repostarAgua(int cantidad) {
        setDepositoAgua(getDepositoAgua() + cantidad);
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

    /**
     * Devuelve true en caso de que el barco haya llenado sus depositos. False en caso contrario.
     *
     * @return True si los dep�sitos del barco est�n llenos.
     */
    private boolean estaLleno() {
        boolean resultado = false;
        if (aguaCompleto() && petroleoCompleto()) {
            resultado = true;
        }
        return resultado;
    }

    /**
     * Devuelve true en caso de que el dep�sito de agua est� completo. False en caso contrario.
     *
     * @return True si el dep�sito de agua est� lleno.
     */
    private boolean aguaCompleto() {
        boolean resultado = false;
        if (getDepositoAgua() == LIMITE_AGUA) resultado = true;
        return resultado;
    }

    /**
     * Devuelve true en caso de que el dep�sito de petr�leo est� completo. False en caso contrario.
     *
     * @return True si el dep�sito de petr�leo est� lleno.
     */
    private boolean petroleoCompleto() {
        boolean resultado = false;
        if (getDepositoPetroleo() == LIMITE_PETROLEO) resultado = true;
        return resultado;
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