package Lihad.Religion.Listeners;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityListener;

import Lihad.Religion.Religion;

public class BeyondEntityListener extends EntityListener {
	public static Religion plugin;
	public BeyondEntityListener(Religion instance) {
		plugin = instance;
	}
	public void onEntityDamage(EntityDamageEvent event){
		if(event.getEntity() instanceof Block){
			Block block = (Block)event.getEntity();
			if(block.getType() == Material.CHEST){
				for(int i=0;i<Religion.info.getTowersAll().size();i++){
					if(block.getLocation().equals(Religion.info.getTowerLocation(Religion.info.getReligion(Religion.info.getTowersAll().get(i)), Religion.info.getTowersAll().get(i)))){
						event.setCancelled(true);
					}
				}
			}
		}
	}
	public void onEntityExplode(EntityExplodeEvent event){
		System.out.println("check 1");
		List<Block> blocklist = event.blockList();
		for(int i = 0;i<blocklist.size();i++){
			if(blocklist.get(i).getType()== Material.CHEST){
				System.out.println("check 2");
				for(int j=0;j<Religion.info.getTowersAll().size();j++){
					if(blocklist.get(i).getLocation().equals(Religion.info.getTowerLocation(Religion.info.getReligion(Religion.info.getTowersAll().get(j)), Religion.info.getTowersAll().get(j)))){
						System.out.println("check3");
						event.setCancelled(true);
					}
				}
			}
		}
	}

}
