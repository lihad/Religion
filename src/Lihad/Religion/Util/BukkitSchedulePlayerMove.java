package Lihad.Religion.Util;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import Lihad.Religion.Religion;
import Lihad.Religion.Listeners.BeyondPlayerListener;

public class BukkitSchedulePlayerMove implements Runnable {
	public static Religion plugin;
    public static Map<String, Location> prevLocation = new HashMap<String, Location>();

	public BukkitSchedulePlayerMove(Religion instance) {
		plugin = instance;
	}
	@Override
	public void run() {
		Player[] players = plugin.getServer().getOnlinePlayers();
		for(int i = 0;i<players.length;i++){
			if(prevLocation.containsKey(players[i].getName())){
				BeyondPlayerListener.onPlayerMoveExecutor(players[i],prevLocation.get(players[i].getName()),players[i].getLocation());
			}
			prevLocation.put(players[i].getName(), players[i].getLocation());
		}
	}
}
