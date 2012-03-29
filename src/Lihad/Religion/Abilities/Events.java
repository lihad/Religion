package Lihad.Religion.Abilities;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import Lihad.Religion.Religion;
import Lihad.Religion.Information.BeyondInfo;

public class Events implements Listener {
	public static Religion plugin;


	public Events(Religion instance) {
		plugin = instance;
	}
	
	@EventHandler
	public static void sheepSacrificeEvent(EntityDamageByEntityEvent event){
		if(event.getEntity() instanceof Sheep && event.getDamager() instanceof Player && BeyondInfo.hasPlayer((Player)event.getDamager())){
			Player player = (Player)event.getDamager();
			Random rnd = new Random();
			int next = rnd.nextInt(1000);
			if(next<5){
				Block[] block = new Block[3];
				block[0] = plugin.getServer().getWorld("survival").getBlockAt(new Location(plugin.getServer().getWorld("survival"),19,36,26));
				block[1] = plugin.getServer().getWorld("survival").getBlockAt(new Location(plugin.getServer().getWorld("survival"),21,36,26));
				block[2] = plugin.getServer().getWorld("survival").getBlockAt(new Location(plugin.getServer().getWorld("survival"),23,36,26));
				for(int i = 0; i<block.length; i++){
					if(!(block[i].getState() instanceof Chest)){
						Religion.warning("Player tried accessing chest room.... misaligned");
						return;
					}
				}
				player.teleport(new Location(plugin.getServer().getWorld("survival"),21,36,40));
				
				
				
			}
		}
	}
}
