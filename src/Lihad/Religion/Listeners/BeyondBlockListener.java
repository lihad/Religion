package Lihad.Religion.Listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import Lihad.Religion.Religion;
import Lihad.Religion.Abilities.SpellAoE;
import Lihad.Religion.Information.BeyondInfo;
import Lihad.Religion.Util.BeyondUtil;

public class BeyondBlockListener implements Listener {
	public static Religion plugin;
	public BeyondBlockListener(Religion instance) {
		plugin = instance;
	}
	
	/**
	 * TODO: Make it so a tower can not be named after a religion.
	 * TODO: Make it so only those without a religion/tower can start a new tower
	 */
	@EventHandler
	public void onSignChange(SignChangeEvent event){
		if(event.getBlock().getLocation().getBlockY() > 119 && BeyondInfo.getClosestValidTower(event.getBlock().getLocation()) == null){
			//TODO: make statement "event.getLine(2).length() > 4" be > a configurable value.  That value is the minimum length a tower name can be.
			if(event.getLine(0).equals("[Religion]") && !(event.getLine(1).equals(null))&& (event.getLine(2).length() > 4)){
				if(BeyondInfo.getReligions().contains(event.getLine(1))){
					if(BeyondInfo.hasTower(event.getLine(2))||BeyondInfo.getReligions().contains(event.getLine(2))){
						event.getPlayer().sendMessage("This name is already being used - try a different one.");
						return;
					}
					if(BeyondInfo.isSpawnZone(event.getPlayer().getLocation())){
						event.getPlayer().sendMessage("You need to be farther away from spawn!!.");
						return;
					}
					if(event.getLine(2).contains(" ") || event.getLine(2).contains("?") || event.getLine(2).contains("!") || event.getLine(2).contains(".")|| event.getLine(2).contains(",")|| event.getLine(2).contains("'")){
						event.getPlayer().sendMessage("Your tower may have no spaces or symbols in the name.");
						return;
					}
					if(BeyondInfo.hasPlayer(event.getPlayer())){
						event.getPlayer().sendMessage("You are already a member of a religion!");
						return;
					}
					if(BeyondUtil.isActiveArea(event.getPlayer(), event.getLine(1))){
						event.getPlayer().sendMessage("This area is already being used.  Move away, or get the other players to join your religion!");
						return;
					}
					BeyondInfo.addTower(event.getPlayer(), event.getLine(1), event.getLine(2), event.getBlock().getLocation());
					if(event.getBlock().getRelative(1, 0, 0).getType() == Material.CHEST) event.getBlock().getRelative(1, 0, 0).setTypeId(0);
					if(event.getBlock().getRelative(-1, 0, 0).getType() == Material.CHEST) event.getBlock().getRelative(-1, 0, 0).setTypeId(0);
					if(event.getBlock().getRelative(0, 0, 1).getType() == Material.CHEST) event.getBlock().getRelative(0, 0, 1).setTypeId(0);
					if(event.getBlock().getRelative(0, 0, -1).getType() == Material.CHEST) event.getBlock().getRelative(0, 0, -1).setTypeId(0);

					event.getBlock().setType(Material.CHEST);
				}else event.getPlayer().sendMessage("Your chosen religion does not exist.");
			}else if(event.getLine(0).equals("[Religion]") && event.getLine(2).length() <= 4) event.getPlayer().sendMessage("Your chosen tower name is not long enough, try again.");
			
		/**	
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
		 */
		}else if(BeyondInfo.getClosestValidTower(event.getBlock().getLocation()) == null) return;
		else if(event.getBlock().getLocation().getBlockY() > 64 && event.getBlock().getLocation().getBlockY() < 120&& BeyondInfo.getClosestValidTower(event.getBlock().getLocation()).equals(BeyondInfo.getTowerName(event.getPlayer())) && BeyondInfo.isPlayerLeader(event.getPlayer())){
			if(event.getLine(0).equals("[Trade]")){
				if(event.getLine(1).equals("Blacksmith") || event.getLine(1).equals("Fishery") || event.getLine(1).equals("Fletcher")){
					if(!BeyondInfo.hasTrade(BeyondInfo.getTowerName(event.getPlayer()), event.getLine(1))){
						if(BeyondInfo.getTrades(BeyondInfo.getClosestValidTower(event.getBlock().getLocation())) != null){
							for(int i = 0; i<BeyondInfo.getTrades(BeyondInfo.getClosestValidTower(event.getBlock().getLocation())).size();i++){
								if(BeyondUtil.isNextTo(BeyondInfo.getTradeLocation(BeyondInfo.getClosestValidTower(event.getBlock().getLocation()), BeyondInfo.getTrades(BeyondInfo.getClosestValidTower(event.getBlock().getLocation())).get(i)), event.getBlock().getLocation())){
									event.getPlayer().sendMessage("You can not place this next to another chest.");
									event.setCancelled(true);
									return;
								}
							}
						}
						BeyondInfo.addTrade(BeyondInfo.getTowerName(event.getPlayer()), event.getLine(1), event.getBlock().getLocation());
						event.getBlock().setType(Material.CHEST);
						event.getPlayer().sendMessage("You have created a "+event.getLine(1));

					}else event.getPlayer().sendMessage("You already have a "+event.getLine(1));
				} else event.getPlayer().sendMessage("The following isn't a valid Trade: "+event.getLine(1));
			}
		} 
	}
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event){
		if(event.getBlock().getType() == Material.CHEST){
			for(int i=0;i<BeyondInfo.getTowersAll().size();i++){
				if(event.getBlock().getLocation().equals(BeyondInfo.getTowerLocation(BeyondInfo.getTowersAll().get(i)))){
					event.setCancelled(true);
				}else if(BeyondInfo.getTrades(BeyondInfo.getTowersAll().get(i)) != null){
					for(int j=0;j<BeyondInfo.getTrades(BeyondInfo.getTowersAll().get(i)).size();j++){
						if(event.getBlock().getLocation().equals(BeyondInfo.getTradeLocation(BeyondInfo.getTowersAll().get(i), BeyondInfo.getTrades(BeyondInfo.getTowersAll().get(i)).get(j)))){
							event.setCancelled(true);
						}
					}
				}
			}
		}
	}
	@EventHandler
	public void onBlockDamage(BlockDamageEvent event){
		if(event.getBlock().getType() == Material.CHEST){
			for(int i=0;i<BeyondInfo.getTowersAll().size();i++){
				if(event.getBlock().getLocation().equals(BeyondInfo.getTowerLocation(BeyondInfo.getTowersAll().get(i)))){
					event.setCancelled(true);
				}else if(BeyondInfo.getTrades(BeyondInfo.getTowersAll().get(i)) != null){
					for(int j=0;j<BeyondInfo.getTrades(BeyondInfo.getTowersAll().get(i)).size();j++){
						if(event.getBlock().getLocation().equals(BeyondInfo.getTradeLocation(BeyondInfo.getTowersAll().get(i), BeyondInfo.getTrades(BeyondInfo.getTowersAll().get(i)).get(j)))){
							event.setCancelled(true);
						}
					}
				}
			}
		}
	}
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event){
		// TODO: Make this less terrible
		/**
		 * 
		 * Trying to make it so a player cant place another chest next to a tower chest (therefore making a double-chest)
		 * Well, its 5am and this is the best I could come up with... It works, but in an ugly way. please make it better
		 */
		if(event.getBlock().getType() == Material.CHEST){
			Location place = event.getBlock().getLocation();
			if(BeyondInfo.getClosestValidTower(event.getBlock().getLocation())==null)return;
			Location modified = BeyondInfo.getTowerLocation(BeyondInfo.getClosestValidTower(event.getBlock().getLocation()));
			if(BeyondUtil.isNextTo(modified, place)) event.setCancelled(true);
			if(BeyondInfo.getTrades(BeyondInfo.getClosestValidTower(event.getBlock().getLocation()))==null)return;
			for(int i = 0; i<BeyondInfo.getTrades(BeyondInfo.getClosestValidTower(event.getBlock().getLocation())).size();i++){
				if(BeyondUtil.isNextTo(BeyondInfo.getTradeLocation(BeyondInfo.getClosestValidTower(event.getBlock().getLocation()), BeyondInfo.getTrades(BeyondInfo.getClosestValidTower(event.getBlock().getLocation())).get(i)), place)) event.setCancelled(true);
			}
		}
	}
}
