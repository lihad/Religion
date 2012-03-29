package Lihad.Religion.Util;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.ChatColor;

import Lihad.Religion.Religion;

public class Notification implements Runnable {
	public static Religion plugin;
	public static List<String> event = new LinkedList<String>();
	
	
	public Notification(Religion instance) {
		plugin = instance;
	}
	
	
	
	
	public static void notifyGeneral(){
		plugin.getServer().broadcastMessage(ChatColor.GRAY.toString()+"----- "+ChatColor.AQUA.toString()+"Religion Report"+ChatColor.GRAY.toString()+" -----");
		if(event.isEmpty()){
			plugin.getServer().broadcastMessage(ChatColor.GRAY.toString()+"-- "+ChatColor.GOLD.toString()+"Nothing to Report...");
		}
		else{
			for(int i = 0;i<event.size();i++){
				if(i == 5)break;
				plugin.getServer().broadcastMessage(event.get(i));
			}
			event.clear();
		}
		plugin.getServer().broadcastMessage(ChatColor.GRAY.toString()+"----- ----- ----- -----");

	}




	@Override
	public void run() {
		notifyGeneral();		
	}
	
	
}
