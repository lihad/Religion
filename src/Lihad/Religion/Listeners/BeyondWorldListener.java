package Lihad.Religion.Listeners;

import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkPopulateEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldListener;

import Lihad.Religion.Religion;
import Lihad.Religion.Bosses.Bosses;
import Lihad.Religion.Config.BeyondConfig;

public class BeyondWorldListener extends WorldListener{
	public static Religion plugin;
	public BeyondWorldListener(Religion instance) {
		plugin = instance;
	}
	public void onChunkLoad(ChunkLoadEvent event){
		if(Bosses.chunk.isLoaded()){
			System.out.println(Bosses.exist);
			if(Bosses.boss == null || !Bosses.exist){
				System.out.println("SPAWN AHKMED");
				Religion.bosses.spawnBoss(BeyondConfig.getAhkmedLocation());
			}
		}

	}
	public void onChunkUnload(ChunkUnloadEvent event){
		if(!Bosses.chunk.isLoaded()){
			if(Bosses.exist){
				Bosses.exist = false;
				Bosses.boss = null;
				System.out.println("AHKMED GONE");
			}
		}
	}
	public void onChunkPopulate(ChunkPopulateEvent event){

	}
}
