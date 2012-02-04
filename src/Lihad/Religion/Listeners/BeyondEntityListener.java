package Lihad.Religion.Listeners;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityListener;

import Lihad.Religion.Religion;
import Lihad.Religion.Information.BeyondInfo;

public class BeyondEntityListener extends EntityListener {
	public static Religion plugin;
	public BeyondEntityListener(Religion instance) {
		plugin = instance;
	}
	public void onEntityDamage(EntityDamageEvent event){
		if(event.getEntity() instanceof Block){
			Block block = (Block)event.getEntity();
			if(block.getType() == Material.CHEST){
				for(int i=0;i<BeyondInfo.getTowersAll().size();i++){
					if(block.getLocation().equals(BeyondInfo.getTowerLocation(BeyondInfo.getReligion(BeyondInfo.getTowersAll().get(i)), BeyondInfo.getTowersAll().get(i)))){
						event.setCancelled(true);
					}
				}
			}
		}
	}
	public void onEntityExplode(EntityExplodeEvent event){
		List<Block> blocklist = event.blockList();
		for(int i = 0;i<blocklist.size();i++){
			if(blocklist.get(i).getType()== Material.CHEST){
				for(int j=0;j<BeyondInfo.getTowersAll().size();j++){
					if(blocklist.get(i).getLocation().equals(BeyondInfo.getTowerLocation(BeyondInfo.getReligion(BeyondInfo.getTowersAll().get(j)), BeyondInfo.getTowersAll().get(j)))){
						event.setCancelled(true);
					}
				}
			}
		}
	}

}
