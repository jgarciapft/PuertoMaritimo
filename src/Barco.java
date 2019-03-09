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
    private int identificador;

    /**
     * @see ESTADO_BARCO Estado en el que se encuentra el barco
     */
    private ESTADO_BARCO estado;

    /**
     * Constructor parametrizado. Instancia un nuevo barco a partir de un identificador y un estado
     *
     * @param identificador Identificador del barco
     * @param estado        Estado inicial del barco
     */
    public Barco(int identificador, ESTADO_BARCO estado) {
        this.identificador = identificador;
        this.estado = estado;
    }

    /**
     * // TODO Documentar m�todo run()
     */
    @Override
    public void run() {
        Puerta puerta = Puerta.recuperarInstancia();        // Instancia Singleton de la puerta
        TorreControl torreControl = TorreControl.recuperarInstancia();  // Instancia Singleton de la torre de control
        Plataforma plataforma = Plataforma.recuperarInstancia();    // Instancia Singleton de la plataforma

        switch (getEstado()) {                              // Determina la acci�n del barco adecuada a su estado
            case ENTRADA:
                // Protocolo de entrada
                torreControl.permisoEntrada(this);
                // Acci�n
                puerta.entrar(this);
                // Protocolo de salida
                torreControl.finEntrada(this);
                // Si el barco que ha entrado es mercante, se dirigir� a la plataforma
                if (this instanceof BarcoMercante) {
                    // Mientras tenga cargamentos intentar� soltarlos
                    while (((BarcoMercante) this).getCargamentosRestantes() > 0) {
                        plataforma.poner((BarcoMercante) this, ((BarcoMercante) this).obtenerCargamentoAleatorio());
                    }
                    // Ya no hay m�s cargamentos y abandona la zona de descarga
                    plataforma.setActivo(false);
                }
                break;
            case SALIDA:
                // Protocolo de entrada
                torreControl.permisoSalida(this);
                // Acci�n
                puerta.salir(this);
                // Protocolo de salida
                torreControl.finSalida(this);
                break;
        }
    }

    /**
     * M�todo accesor del atributo {@link Barco#identificador}
     *
     * @return Identificador del barco
     */
    public int getIdentificador() {
        return identificador;
    }

    /**
     * M�todo accesor del atributo {@link Barco#estado}
     *
     * @return Estado actual del barco
     */
    public ESTADO_BARCO getEstado() {
        return estado;
    }

    /**
     * M�todo modificador del atributo {@link Barco#estado}
     *
     * @param estado Nuevo estado
     */
    public void setEstado(ESTADO_BARCO estado) {
        this.estado = estado;
    }

    /**
     * Imprime un mensaje con marca de tiempo por consola en una l�nea
     *
     * @param mensaje Mensaje a imprimir
     */
    private void imprimirConTimestamp(String mensaje) {
        System.out.println("\t[" + System.currentTimeMillis() + "] " + mensaje);
    }

}