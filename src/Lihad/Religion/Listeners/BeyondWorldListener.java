package Lihad.Religion.Listeners;

import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkPopulateEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import Lihad.Religion.Religion;
import Lihad.Religion.Bosses.Bosses;
import Lihad.Religion.Config.BeyondConfig;

public class BeyondWorldListener implements Listener{
	public static Religion plugin;
	public BeyondWorldListener(Religion instance) {
		plugin = instance;
	}
	public void onChunkLoad(ChunkLoadEvent event){

	}
	public void onChunkUnload(ChunkUnloadEvent event){
		
	}
	public void onChunkPopulate(ChunkPopulateEvent event){

	}
}
