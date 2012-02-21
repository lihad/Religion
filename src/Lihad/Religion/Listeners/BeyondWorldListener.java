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
    	//System.out.println("Comparing X "+event.getChunk().getX()+" to "+BeyondConfig.getAhkmedLocation().getChunk().getX());
    	//if(event.getChunk()..getX() == (BeyondConfig.getAhkmedLocation().getChunk().getX()))System.out.println("Comparing Z"+event.getChunk().getZ()+" to "+BeyondConfig.getAhkmedLocation().getChunk().getZ());
    	//System.out.println("----------------------");

    	if(BeyondConfig.getAhkmedLocation().getChunk().isLoaded()){
        	//   System.out.println(Bosses.exist);
			//(Bosses.boss == null || !Bosses.exist){
		    //	System.out.println("FIRE2");
			//	Religion.bosses.spawnBoss(BeyondConfig.getAhkmedLocation());
			//}
    	}

    }
    public void onChunkUnload(ChunkUnloadEvent event){
    	if(!BeyondConfig.getAhkmedLocation().getChunk().isLoaded()){
    		//if(Bosses.exist)Bosses.exist = false;
    	}
    }
    public void onChunkPopulate(ChunkPopulateEvent event){

    }
}
