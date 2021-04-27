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
public class ChessClock {
    private long startTime;
    private long timer;
    private long end;
    private long increment;
    private boolean timeFlows = false;
    
    public ChessClock(long clock){
        startTime = System.currentTimeMillis();
        end = startTime + clock;
        this.timer = clock;
        System.out.println(Long.toString(this.startTime));
        System.out.println(Long.toString(this.timer));
        System.out.println(Long.toString(this.end));
    }
    
    public ChessClock(String clock){
        startTime = System.currentTimeMillis();
        timer = timeFromString(clock);
        end = startTime + timer;
        System.out.println("Clock start: "+Long.toString(this.startTime));
        System.out.println("Clock timer: "+Long.toString(this.timer));
        System.out.println("Clock end: "+Long.toString(this.end));
    }
    
    public ChessClock(String clock, String increment){
        
    }
    
    public long getRemainingTime(){
        this.timeFlows = true;
        timer = this.end - System.currentTimeMillis();
        return 234567;
    }
    
    private long timeFromString(String time){
        String[] pieces = time.split(":");
        long trueTime = 0;
        for (int i =  pieces.length; i >= 0; i--) {
            //hours, second minutes
            if(i == 0){
                trueTime += Integer.parseInt(pieces[i])*1000;
            }
            if(i == 1){
                trueTime += Integer.parseInt(pieces[i])*1000*60;
            }
            if(i == 2){
                trueTime += Integer.parseInt(pieces[i])*1000*60*60;
            }
            else{
                break;
            }
        }
        return trueTime;
    }
    
    @Override
    public String toString(){
        String repr = "";
        
        return repr;
    }
}
