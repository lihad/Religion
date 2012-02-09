package Lihad.Religion.Listeners;

import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.SignChangeEvent;
import Lihad.Religion.Religion;
import Lihad.Religion.Information.BeyondInfo;

public class BeyondBlockListener extends BlockListener {
	public static Religion plugin;
	public BeyondBlockListener(Religion instance) {
		plugin = instance;
	}
	/**
	 * TODO: Make it so a tower can not be named after a religion.
	 * TODO: Make it so only those without a religion/tower can start a new tower
	 * 
	 * How about just make it so that if you're in a tower, you get booted when you make a new one?
	 */
	public void onSignChange(SignChangeEvent event){
		if(event.getBlock().getLocation().getBlockY() > 119){
			if(event.getLine(0).equals("[Religion]") && !(event.getLine(1).equals(null))&& !(event.getLine(2).equals(null))){
				if(BeyondInfo.getReligions().contains(event.getLine(1))){
					if(BeyondInfo.hasTower(event.getLine(2))||BeyondInfo.getReligions().contains(event.getLine(2))){
						event.getPlayer().sendMessage("This name is already being used - try a different one.");
						return;
					}
					BeyondInfo.addTower(event.getPlayer(), event.getLine(1), event.getLine(2), event.getBlock().getLocation());
					event.getBlock().setType(Material.CHEST);
				}else event.getPlayer().sendMessage("Your chosen religion does not exist.");
			}
		}
	}
	public void onBlockBreak(BlockBreakEvent event){
		if(event.getBlock().getType() == Material.CHEST){
			for(int i=0;i<BeyondInfo.getTowersAll().size();i++){
				if(event.getBlock().getLocation().equals(BeyondInfo.getTowerLocation(BeyondInfo.getReligion(BeyondInfo.getTowersAll().get(i)), BeyondInfo.getTowersAll().get(i)))){
					event.setCancelled(true);
				}
			}
		}
	}
	public void onBlockDamage(BlockDamageEvent event){
		if(event.getBlock().getType() == Material.CHEST){
			for(int i=0;i<BeyondInfo.getTowersAll().size();i++){
				if(event.getBlock().getLocation().equals(BeyondInfo.getTowerLocation(BeyondInfo.getReligion(BeyondInfo.getTowersAll().get(i)), BeyondInfo.getTowersAll().get(i)))){
					event.setCancelled(true);
				}
			}
		}
	}

}
