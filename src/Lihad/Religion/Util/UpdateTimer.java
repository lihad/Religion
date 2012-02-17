package Lihad.Religion.Util;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import Lihad.Religion.Religion;


public class UpdateTimer {
	public static Religion plugin;
	public static Timer timer;
	public static TimerTask task;
	public static Date date;
	
    public UpdateTimer(Religion instance) {
        plugin = instance;
        timer = new Timer();
    	//date = new Date(TownyTracker.config.getTimestamp());
    	
    	runnableTimer();
    }
    
    public void runnableTimer(){
    	System.out.println("[Religion] Timer has been set,.");
    	Calendar cal = Calendar.getInstance();    	
    	//TODO: Timer value needs to be configurable through Relgion.Config.BeyondConfig
    	timer.scheduleAtFixedRate(Religion.task, cal.getTime(),  1800000);
    } 
}
