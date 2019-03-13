/**
 * TODO Documentaci�n clase ContenedorPetroleo
 *
 * @author Juan Pablo Garc�a Plaza P�rez
 * @author Jos� �ngel Concha Carrasco
 */
public class ContenedorPetroleo {

    /**
     * Cantidad de petr�leo en el contenedor
     */
    private int cantidad;
    /**
     * Capacidad m�xima de petr�leo en el contenedor
     */
    private int maxCapacidad;

    public ContenedorPetroleo() {
        // TODO - implement ContenedorPetroleo.ContenedorPetroleo
    }

    /**
     * @param cantidad
     * @param capacidadMaxima
     */
    public ContenedorPetroleo(int cantidad, int capacidadMaxima) {
        // TODO - implement ContenedorPetroleo.ContenedorPetroleo
    }

    /**
     * Vac�a del contenedor una cantidad dada
     *
     * @param cantidad Cantidad a vaciar
     * @return Cantidad vaciada
     */
    public int vaciar(int cantidad) {
        // TODO - implement ContenedorPetroleo.vaciar
        return 0;
    }

    /**
     * Rellena el contenedor con la capacidad m�xima de petr�leo
     */
    public void reponer() {
        // TODO - implement ContenedorPetroleo.reponer
    }

    /**
     * M�todo accesor del atributo cantidad
     */
    public int getCantidad() {
        return cantidad;
    }

    /**
     * M�todo accesor del atributo maxCapacidad
     */
    public int getMaxCapacidad() {
        return maxCapacidad;
    }

    /**
     * M�todo modificador del atributo capacidad
     *
     * @param cantidad Nueva cantidad de petroleo del contenedor
     */
    private void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * M�todo modificador del atributo maxCapacidad
     *
     * @param maxCapacidad Nueva capacidad m�xima de petr�leo del contenedor
     */
    public void setMaxCapacidad(int maxCapacidad) {
        this.maxCapacidad = maxCapacidad;
    }

}