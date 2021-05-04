/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.skardada.chess;

/**
 *
 * @author Adam Škarda
 */
public class ChessClock implements Runnable{
    private long  timer;
    private long  increment;
    private boolean timeFlows = false;
    private Thread clockThread = null;
    
    public ChessClock(String clock, String increment){
        this.timer = this.convertStringToLong(clock);
        this.increment = this.convertStringToLong(increment);
    }
    
    public void clockStart(){
        this.timeFlows = true;
        if(clockThread == null){
            clockThread = new Thread(this, "ChessClock");
            clockThread.start();
        }
    } 
    
    public void clockStop(){
        if (timeFlows) {
            timeFlows = false;
        }
        if(timer>0){
          timer += increment;  
        }

    }
    
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
    
    public long getRemainingTime(){
        return timer;
    }
    
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
    
    private long convertStringToLong(String input){
        long output = 0;
        String[] timeParts = input.split(":");
        for (int i = 0; i < timeParts.length; i++) {
            output += Long.parseLong(timeParts[i]) * 1000 * Math.pow(60, timeParts.length - 1 - i);
        }
        return output;
    }
}







