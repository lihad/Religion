package Lihad.Religion.Listeners;

import java.util.List;
import java.util.Random;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.entity.SheepRegrowWoolEvent;

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
					if(block.getLocation().equals(BeyondInfo.getTowerLocation(BeyondInfo.getTowersAll().get(i)))){
						event.setCancelled(true);
					}
				}
			}
		}else if(event instanceof EntityDamageByEntityEvent)onEntityDamageByEntity((EntityDamageByEntityEvent)event);

		/**
		 * The following 'if' makes players that take damage in their own religions AoE take half.  Those who take damage in opposing religion AoE
		 * take +1 
		 */
		if(event.getEntity() instanceof Player){
			Player player = (Player)event.getEntity();
			if(BeyondInfo.getClosestValidTower(player.getLocation()) ==  null || BeyondInfo.getReligion(player) == null) return;
			else{
				if(BeyondInfo.getReligion(BeyondInfo.getClosestValidTower(player.getLocation())).equals(BeyondInfo.getReligion(player))){
					player.damage(event.getDamage()/2);
					event.setCancelled(true);
				}else{
					player.damage(event.getDamage()+1);
					event.setCancelled(true);
				}
			}
		}
	}
	public void onEntityExplode(EntityExplodeEvent event){
		List<Block> blocklist = event.blockList();
		for(int i = 0;i<blocklist.size();i++){
			if(blocklist.get(i).getType()== Material.CHEST){
				for(int j=0;j<BeyondInfo.getTowersAll().size();j++){
					if(blocklist.get(i).getLocation().equals(BeyondInfo.getTowerLocation(BeyondInfo.getTowersAll().get(j)))){
						event.setCancelled(true);
					}
				}
			}
		}
	}
	// Left off here
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event){
		if(event.getDamager() instanceof Player && event.getEntity() instanceof Sheep){
			if(event.getDamage() > ((Sheep)event.getEntity()).getHealth())new EntityDeathEvent(event.getEntity(), null, 0);
		}
	}
}
