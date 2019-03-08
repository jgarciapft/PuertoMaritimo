import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Implementa el patr�n de dise�o Singleton
 * // TODO Documentar clase TorreControl
 *
 * @author Juan Pablo Garc�a Plaza P�rez
 * @author Jos� �ngel Concha Carrasco
 */
public class Plataforma {

    private static Plataforma instancia = null;   // Instancia Singleton de la Plataforma

    private Lock monitor;
    private Condition esperaAzucar;             // Variable de espera para las gr�as de az�car
    private Condition esperaHarina;             // Variable de espera para las gr�as de Harina
    private Condition esperaSal;                // Variable de espera para las gr�as de Sal
    private Condition esperaMercante;           // Varible de espera para el barco mercante

    /**
     * Constructor por defecto
     */
    private Plataforma() {

        monitor = new ReentrantLock(true);
        esperaAzucar = monitor.newCondition();
        esperaHarina = monitor.newCondition();
        esperaSal = monitor.newCondition();
        esperaMercante = monitor.newCondition();

    }

    public void poner(Barco barco, TIPO_CARGAMENTO tipoCargamento){

    }

    public void coger(Grua grua){

    }

    /**
     * @return Instancia Singleton de la Plataforma
     */
    public synchronized static Plataforma recuperarInstancia() {
        if (instancia != null)
            return instancia;
        else
            instancia = new Plataforma();

        return instancia;
    }


}