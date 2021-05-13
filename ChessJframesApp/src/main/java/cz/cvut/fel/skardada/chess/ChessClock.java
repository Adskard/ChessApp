
package cz.cvut.fel.skardada.chess;

/**
 * ChessClock is a class that measures time. Its used for clock implementation, and as such runs on a different thread.
 * @author Adam Škarda
 */
public class ChessClock implements Runnable{
    private long  timer;
    private long  increment;
    private boolean timeFlows = false;
    private Thread clockThread = null;
    
    /**
     * Constructor for string
     * 
     * @param clock remaining time in format hh:mm:ss
     * @param increment increment in format hh:mm:ss
     */
    public ChessClock(String clock, String increment){
        this.timer = this.convertStringToLong(clock);
        this.increment = this.convertStringToLong(increment);
    }
    
    /**
     * Constructor for long
     * @param clock time remaining for turns
     * @param increment increment (gets added to remaining time when clock gets stopped)
     */
    public ChessClock(long clock, long increment){
        this.timer = clock;
        this.increment = increment;
    }
    
    /**
     * Starts the clock and runs it in sepparate clock thread
     */
    public void clockStart(){
        this.timeFlows = true;
        if(clockThread == null){
            clockThread = new Thread(this, "ChessClock");
            clockThread.start();
        }
    } 
    
    /**
     * stops the clock and add increment
     */
    public void clockStop(){
        if (timeFlows) {
            timeFlows = false;
        }
        if(timer>0){
          timer += increment;  
        }

    }
    
    /**
     * override run for working Runnable implementation
     * perpetual loop can only end if timer reaches 0 while timeFlows
     */
    @Override
    public void run(){
        while(timer > 0){
            long then = System.currentTimeMillis();
            try{     
                Thread.sleep(1000); 
            }
            catch(InterruptedException ex){
                System.err.println("Exception in Thread" + Thread.currentThread());
            }
            long now = System.currentTimeMillis();

            timer -= (now-then);  
            //do not terminate this thread
            while(!this.timeFlows){
                try{     
                    Thread.sleep(10); 
                }
                catch(InterruptedException ex){
                    System.err.println("Exception in Thread" + Thread.currentThread());
                }
            }
        }
    }
    
    /**
     *
     * @return returns remaining time
     */
    public long getRemainingTime(){
        return timer;
    }
    
    /**
     *
     * @return returns remaining time in seconds as a String
     */
    public String getRemainingSeconds(){
        long seconds = timer / 1000;
        long s = seconds % 60;
        long m = ((seconds - s) / 60) % 60;
        long h = (( m ) / 60) % 60;
        String sec = Long.toString(s);
        String min = Long.toString(m);
        String hour = Long.toString(h);
        return hour + ":" + min + ":" + sec;
    }
    
    /**
     * @param input converts string segmented by ":" character to long
     * @return return conversion of time from input
     */
    private long convertStringToLong(String input){
        long output = 0;
        String[] timeParts = input.split(":");
        for (int i = 0; i < timeParts.length; i++) {
            output += Long.parseLong(timeParts[i]) * 1000 * Math.pow(60, timeParts.length - 1 - i);
        }
        return output;
    }

    /**
     *
     * @return returns increment used
     */
    public long getIncrement() {
        return increment;
    }
}







