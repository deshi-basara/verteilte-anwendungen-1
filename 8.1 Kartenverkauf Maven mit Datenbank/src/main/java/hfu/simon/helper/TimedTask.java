package hfu.simon.helper;

import hfu.simon.model.Sale;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by simon on 01.05.15.
 */
public class TimedTask {

    private static int taskIndex = 0;
    private boolean running = true;

    /**
     * Attributes
     */
    private Timer timer = new Timer();
    private TimerTask timerTask = null;
    private Date executionTime;
    private Sale saleModel;

    /**
     * Sets object-parameters and schedules timer.
     * @param stopDate
     * @param saleModel
     */
    public TimedTask(Date stopDate, Sale saleModel) {
        this.executionTime = stopDate;
        this.saleModel = saleModel;
        this.taskIndex++;

        // schedule timer
        this.timerTask = new TaskAction();
        this.timer.schedule(timerTask, this.executionTime);

        System.out.println("New timed task #"+ this.taskIndex +" added, execution at: " + this.executionTime);
    }

    /**
     * Returns the execution time this task.
     * @return
     */
    public String getExecutionTime() {
        SimpleDateFormat html5Date = new SimpleDateFormat("d MMMM, yyyy 'um' hh:mm 'Uhr'", Locale.GERMAN);

        return html5Date.format(this.executionTime);
    }

    /**
     * Stops the execution of this task.
     */
    public void stopExecution() {
        // disable run-execution and cancel timer & timerTask
        this.timerTask.cancel();
        this.saleModel.setTimedTask(null);

        System.out.println("Timed task #" + this.taskIndex + " cancled");
    }

    /**
     * Blueprint of a task, that will be executed by the timer
     */
    private class TaskAction extends TimerTask {

        public void run() {
            saleModel.disableSaleEnabled();
            saleModel.resetBookings();
            saleModel.setTimedTask(null);

            // disable timer, after execution
            timer.cancel();
        }
    }
}
