package back;
import java.util.Timer;
import java.util.TimerTask;
public class Time {

    private Timer timer = new Timer(); 
    private int seconds=0; 

    //Clase interna que funciona como contador
    class TimeCount extends TimerTask {
        public void run() {
            seconds++;
   //  System.out.println("segundo: " + segundos);
        }
    }
    //Crea un timer, inicia segundos a 0 y comienza a contar
    public void Count(float vel)
    {
        this.seconds=0;      
        timer = new Timer();
        timer.schedule(new TimeCount(), 0, (long) (1000/vel));
    }
    //Detiene el contador
    public void Stop() {
        timer.cancel();
    }
    //Metodo que retorna los segundos transcurridos
    public int getSeconds()
    {
        return this.seconds;
    }
	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}

}



