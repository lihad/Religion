package Lihad.Religion.Listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import Lihad.Religion.Religion;
import Lihad.Religion.Abilities.SpellAoE;
import Lihad.Religion.Information.BeyondInfo;
import Lihad.Religion.Util.BeyondUtil;

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
		System.out.println(event.getBlock().getLocation().toString());
		if(event.getBlock().getLocation().getBlockY() > 119 && BeyondInfo.getClosestValidTower(event.getBlock().getLocation()) == null){
			//TODO: make statement "event.getLine(2).length() > 4" be > a configurable value.  That value is the minimum length a tower name can be.
			if(event.getLine(0).equals("[Religion]") && !(event.getLine(1).equals(null))&& (event.getLine(2).length() > 4)){
				if(BeyondInfo.getReligions().contains(event.getLine(1))){
					if(BeyondInfo.hasTower(event.getLine(2))||BeyondInfo.getReligions().contains(event.getLine(2))){
						event.getPlayer().sendMessage("This name is already being used - try a different one.");
						return;
					}
					if(event.getLine(2).contains(" ") || event.getLine(2).contains("?") || event.getLine(2).contains("!") || event.getLine(2).contains(".")|| event.getLine(2).contains(",")|| event.getLine(2).contains("'")){
						event.getPlayer().sendMessage("Your tower may have no spaces or symbols in the name.");
						return;
					}
					BeyondInfo.addTower(event.getPlayer(), event.getLine(1), event.getLine(2), event.getBlock().getLocation());
					event.getBlock().setType(Material.CHEST);
				}else event.getPlayer().sendMessage("Your chosen religion does not exist.");
			}else if(event.getLine(0).equals("[Religion]") && event.getLine(2).length() <= 4) event.getPlayer().sendMessage("Your chosen tower name is not long enough, try again.");
		}else if(!(BeyondInfo.getClosestValidTower(event.getBlock().getLocation()) == null) && BeyondInfo.hasPlayer(event.getPlayer())){
			if(event.getLine(0).equals("[Religion]") && (event.getLine(1).equalsIgnoreCase("Spell"))&& !(event.getLine(2).equals(null)) && BeyondUtil.isNextTo(BeyondInfo.getTowerLocation(event.getPlayer()), event.getBlock().getLocation())){
				if(BeyondInfo.getClosestValidTower(event.getBlock().getLocation()).equals(BeyondInfo.getTowerName(event.getPlayer()))){
					if(Religion.spell.handler(event.getLine(2), event.getPlayer())) event.getPlayer().sendMessage("You cast "+event.getLine(2));
					else event.getPlayer().sendMessage("Invalid Spell");

				}else{
					event.getPlayer().sendMessage("You can not cast spells from this tower.");
				}
				event.getBlock().setTypeId(0);
			}
		}
	}
	public void onBlockBreak(BlockBreakEvent event){
		if(event.getBlock().getType() == Material.CHEST){
			for(int i=0;i<BeyondInfo.getTowersAll().size();i++){
				if(event.getBlock().getLocation().equals(BeyondInfo.getTowerLocation(BeyondInfo.getTowersAll().get(i)))){
					event.setCancelled(true);
				}
			}
		}
	}
	public void onBlockDamage(BlockDamageEvent event){
		if(event.getBlock().getType() == Material.CHEST){
			for(int i=0;i<BeyondInfo.getTowersAll().size();i++){
				if(event.getBlock().getLocation().equals(BeyondInfo.getTowerLocation(BeyondInfo.getTowersAll().get(i)))){
					event.setCancelled(true);
				}
			}
		}
	}
	
	public void onBlockPlace(BlockPlaceEvent event){
		// TODO: Make this less terrible
		/**
		 * 
		 * Trying to make it so a player cant place another chest next to a tower chest (therefore making a double-chest)
		 * Well, its 5am and this is the best I could come up with... It works, but in an ugly way. please make it better
		 */
		if(event.getBlock().getType() == Material.CHEST){
			Location place = event.getBlock().getLocation();
			Location modified = BeyondInfo.getTowerLocation(BeyondInfo.getClosestValidTower(event.getBlock().getLocation()));
			if(BeyondUtil.isNextTo(modified, place)) event.setCancelled(true);
		}
	}

}
