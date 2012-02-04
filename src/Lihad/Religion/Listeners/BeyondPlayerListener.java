package Lihad.Religion.Listeners;

import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import Lihad.Religion.Religion;
import Lihad.Religion.Information.BeyondInfo;

public class BeyondPlayerListener extends PlayerListener {
	public static Religion plugin;
	public BeyondPlayerListener(Religion instance) {
		plugin = instance;
	}
	public void onPlayerMove(PlayerMoveEvent event){
		String closestFrom = BeyondInfo.getClosestValidTower(event.getFrom());
		String closestTo = BeyondInfo.getClosestValidTower(event.getTo());
		if(closestFrom==null)closestFrom = "null";
		if(closestTo==null)closestTo = "null";

		if(!closestFrom.equals(closestTo)){
			event.getPlayer().sendMessage("You are now entering the territory of "+ closestTo);
		}


	}
	public void onPlayerInteract(PlayerInteractEvent event){
		if(event.getClickedBlock() == null) return;
		if(BeyondInfo.getReligion(event.getClickedBlock().getLocation()) == null) return;
		else if(event.getClickedBlock().getType() == Material.CHEST){
			if(BeyondInfo.getReligion(event.getPlayer()) == null){
				event.getPlayer().sendMessage("You need to be a member of this religion to interact with this chest.");
				event.setCancelled(true);
			}else if(!BeyondInfo.getTowers(BeyondInfo.getReligion(event.getClickedBlock().getLocation())).contains(BeyondInfo.getTowerName(event.getPlayer()))){
				Chest chest = (Chest) event.getClickedBlock().getState();
				if(chest.getInventory().contains(Material.GOLD_INGOT)){
					event.getPlayer().sendMessage("You stole a gold bar!");
					//TODO: make damage configurable via Config.BeyondConfig
					event.getPlayer().damage(10);
					int index =chest.getInventory().first(266);
					ItemStack items = chest.getInventory().getItem(index);
					items.setAmount(items.getAmount()-1);
					chest.getInventory().setItem(index, items);
					items.setAmount(1);
					event.getPlayer().getInventory().addItem(items);
					event.getPlayer().updateInventory();
				}else{
					event.getPlayer().sendMessage("DOWN WITH IT!!!");
					BeyondInfo.removeTower(BeyondInfo.getReligion(event.getClickedBlock().getLocation()), BeyondInfo.getTower(event.getClickedBlock().getLocation()));
					//TODO: Make explosion yield value configurable via Config.BeyondConfig
					event.getClickedBlock().getLocation().getWorld().createExplosion(event.getClickedBlock().getLocation(), 60, true);
					event.getClickedBlock().setTypeId(0);
				}
				event.setCancelled(true);
			}

		}
	}

}
