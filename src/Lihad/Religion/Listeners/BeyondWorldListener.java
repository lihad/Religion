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
    	
    }
    public void onChunkUnload(ChunkUnloadEvent event){
    	if(event.getChunk().equals(BeyondConfig.getAhkmedLocation().getChunk())){
    		if(Bosses.exist)Bosses.exist = false;
    	}
    }
    public void onChunkPopulate(ChunkPopulateEvent event){
    	System.out.println("FIRE");
    	if(event.getChunk().equals(BeyondConfig.getAhkmedLocation().getChunk())){
        	System.out.println("FIRE1");

			if(Bosses.boss == null || !Bosses.exist){
		    	System.out.println("FIRE2");

				Religion.bosses.spawnBoss(BeyondConfig.getAhkmedLocation());
			}
    	}
    }
}
