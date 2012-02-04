package Lihad.Religion.Listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import Lihad.Religion.Religion;

public class BeyondBlockListener extends BlockListener {
	public static Religion plugin;
	public BeyondBlockListener(Religion instance) {
		plugin = instance;
	}
	/**
	 * TODO: Make it so a tower can not be named after a religion.
	 * TODO: Make it so only those without a religion/tower can start a new tower
	 */
	public void onSignChange(SignChangeEvent event){
		if(event.getBlock().getLocation().getBlockY() > 119){
			if(event.getLine(0).equals("[Religion]") && !(event.getLine(1).equals(null))&& !(event.getLine(2).equals(null))){
				if(Religion.info.getReligions().contains(event.getLine(1))){
					if(Religion.info.hasTower(event.getLine(2))){
						event.getPlayer().sendMessage("This tower already exist. Try a different name.");
						return;
					}
					Religion.info.addTower(event.getPlayer(), event.getLine(1), event.getLine(2), event.getBlock().getLocation());
					event.getBlock().setType(Material.CHEST);
				}else event.getPlayer().sendMessage("Your chosen religion does not exist.");
			}
		}
	}
	public void onBlockBreak(BlockBreakEvent event){
		if(event.getBlock().getType() == Material.CHEST){
			for(int i=0;i<Religion.info.getTowersAll().size();i++){
				if(event.getBlock().getLocation().equals(Religion.info.getTowerLocation(Religion.info.getReligion(Religion.info.getTowersAll().get(i)), Religion.info.getTowersAll().get(i)))){
					event.setCancelled(true);
				}
			}
		}
	}
	public void onBlockDamage(BlockDamageEvent event){
		if(event.getBlock().getType() == Material.CHEST){
			for(int i=0;i<Religion.info.getTowersAll().size();i++){
				if(event.getBlock().getLocation().equals(Religion.info.getTowerLocation(Religion.info.getReligion(Religion.info.getTowersAll().get(i)), Religion.info.getTowersAll().get(i)))){
					event.setCancelled(true);
				}
			}
		}
	}

}
