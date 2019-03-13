import java.util.Map;

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
    private static ZonaRepostaje instancia;
    /**
     * Colecci�n de contenedores de petr�leo reservados para los barcos petroleros. Cada barco registra mediante identificador un contenedor de petr�leo privado
     */
    private Map<Integer, ContenedorPetroleo> contenPetroleo;

    /**
     * Registra un nuevo contenedor de petr�leo reservado para un barco por identificador
     *
     * @param barco Barco que registra el contenedor
     * @return Si se pudo registrar el contenedor. Devuelve Falso si ya hay un contenedor registrado con ese identificador o el barco es nulo, Verdadero en otro caso
     */
    public boolean registrarContenedor(Barco barco) {
        // TODO - implement ZonaRepostaje.registrarContenedor
        return false;
    }

    /**
     * Reposta en el barco dado la cantidad dada de petr�leo
     *
     * @param barco    Barco que repostar
     * @param cantidad Cantidad de petr�leo a repostar en el barco
     */
    public void repostarPetroleo(BarcoPetrolero barco, int cantidad) {
        // TODO - implement ZonaRepostaje.repostarPetroleo
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
     * M�todo accesor del atributo contenPetroleo
     */
    public Map<Integer, ContenedorPetroleo> getContenPetroleo() {
        return contenPetroleo;
    }

    /**
     * @return Instancia Singleton de la zona de repostaje
     */
    public static ZonaRepostaje recuperarInstancia() {
        // TODO - implement ZonaRepostaje.recuperarInstancia
        return null;
    }

    private ZonaRepostaje() {
        // TODO - implement ZonaRepostaje.ZonaRepostaje
    }

}