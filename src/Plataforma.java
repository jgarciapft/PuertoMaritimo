import java.util.concurrent.SynchronousQueue;

/**
 * Implementa el patr�n de dise�o Singleton. Esta entidad encapsula toda la l�gica de sincronizaci�n relacionada con el
 * paso 6. Los barcos mercantes deben descargar sus contenedores en una plataforma, que solo puede albergar un contenedor,
 * solo uno a la vez. Los contenedores ser�n entonces recogidos por su gr�a pertinente. Las gr�as siempre intentan coger
 * m�s contenedores hasta que son apagadas y solo pueden coger un contenedor si est� en la plataforma
 *
 * @author Juan Pablo Garc�a Plaza P�rez
 * @author Jos� �ngel Concha Carrasco
 */
public class Plataforma {

    /**
     * Instancia Singleton de la plataforma
     */
    private static Plataforma instancia = null;
    /**
     * Bandera para indicar si la plataforma est� operativa
     */
    private boolean activa;

    private SynchronousQueue<TIPO_CARGAMENTO> esperaAzucar;         // Cola s�ncrona para bloquear a la gr�a de AZUCAR
    private SynchronousQueue<TIPO_CARGAMENTO> esperaHarina;         // Cola s�ncrona para bloquear a la gr�a de HARINA
    private SynchronousQueue<TIPO_CARGAMENTO> esperaSal;            // Cola s�ncrona para bloquear a la gr�a de SAL

    /**
     * Constructor por defecto
     */
    private Plataforma() {
        activa = true;

        // Todas las colas s�ncronas tienen pol�tica de justicia FIFO
        esperaAzucar = new SynchronousQueue<>(true);
        esperaHarina = new SynchronousQueue<>(true);
        esperaSal = new SynchronousQueue<>(true);
    }

    /**
     * Deposita un cargamento siempre que la capacidad de la plataforma lo permita y se lo notifica a la gr�a correspondiente.
     *
     * @param barco      Barco mercante
     * @param cargamento Cargamento que deposita en la plataforma
     */
    public void poner(BarcoMercante barco, TIPO_CARGAMENTO cargamento) {
        // Notifica un intento de poner un cargamento en la plataforma
        imprimirConTimestamp("El barco mercante " + barco.getIdentificador() + " intenta a�adir un cargamento de "
                + cargamento + " a la plataforma");
        try {
            switch (cargamento) {                                   // Determina a qu� cola s�ncrona debe asignarse cada cargamento
                case AZUCAR:
                    esperaAzucar.put(cargamento);
                    imprimirConTimestamp("Se ha desbloqueado G-az�car");
                    break;
                case HARINA:
                    esperaHarina.put(cargamento);
                    imprimirConTimestamp("Se ha desbloqueado G-harina");
                    break;
                case SAL:
                    esperaSal.put(cargamento);
                    imprimirConTimestamp("Se ha desbloqueado G-sal");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Notifica el �xito de poner un cargamento en la plataforma
        imprimirConTimestamp("El barco mercante " + barco.getIdentificador() +
                " finalmente ha a�adido un cargamento a la plataforma. Cargamentos restantes: " + barco.getCargamentosRestantes());
    }

    /**
     * Una grua intenta coger de la plataforma
     *
     * @param grua Grua que intenta coger de la plataforma
     */
    public void coger(Grua grua) {
        // Notifica la voluntad de una gr�a de coger un cargamento
        imprimirConTimestamp("La gr�a (" + grua.getTipo() + ") " + grua.getIdentificador() + " est� bloqueada");
        try {
            switch (grua.getTipo()) {                               // Determina de qu� cola s�ncrona coger el cargamento seg�n el tipo de gr�a
                case AZUCAR:
                    esperaAzucar.take();
                    break;
                case HARINA:
                    esperaHarina.take();
                    break;
                case SAL:
                    esperaSal.take();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Notifica el �xito de la gr�a de coger un cargamento de la plataforma
        if (getActiva())                                            // Comprueba que la plataforma est� operativa
            imprimirConTimestamp("La gr�a (" + grua.getTipo().toString() + ") " + grua.getIdentificador() + " vac�a la plataforma");
    }

    /**
     * Termina la ejecuci�n de las gr�as {@link Grua} asociadas a la plataforma de manera que el proograma no quede bloqueado por
     * esperar indefinidadmente otro cargamento
     */
    public void apagarGruas() {
        setActiva(false);                                           // Apaga la plataforma
        try {
            // Pone un elemento m�s para desbloquear a las gr�as esperando y que se apaguen tambi�n
            esperaAzucar.put(TIPO_CARGAMENTO.AZUCAR);
            esperaHarina.put(TIPO_CARGAMENTO.HARINA);
            esperaSal.put(TIPO_CARGAMENTO.SAL);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * M�todo accesor del atributo {@link Plataforma:nBarcosMercantes}
     *
     * @return El cargamento almacenado
     */
    public boolean getActiva() {
        return activa;
    }

    /**
     * M�todo modificador del atributo {@link Plataforma:activa}
     *
     * @param activa Nuevo estado de la plataforma
     */
    public void setActiva(Boolean activa) {
        this.activa = activa;
    }

    /**
     * @return Instancia Singleton de la Plataforma
     */
    public synchronized static Plataforma recuperarInstancia() {
        if (instancia == null)
            instancia = new Plataforma();

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