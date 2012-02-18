package Lihad.Religion.Listeners;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.block.Chest;
import org.bukkit.entity.CreatureType;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import Lihad.Religion.Religion;
import Lihad.Religion.Information.BeyondInfo;
import Lihad.Religion.Util.BeyondUtil;

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


		if(closestFrom.equals("null")&&!closestTo.equals("null")&&BeyondInfo.getReligion(event.getPlayer()) == null){
			if(BeyondUtil.timestampReference(closestTo)){
				event.setTo(event.getFrom());
				event.getPlayer().sendMessage("This is a religious area. Go away non-believer!");
				return;
			}else{
				event.getPlayer().sendMessage("Your heart begins to race!");
			}
		}
		if((!closestTo.equals("null"))&&(!closestFrom.equals("null"))&&BeyondInfo.getReligion(event.getPlayer()) == null){
			if(BeyondUtil.timestampReference(closestTo)){
				event.getPlayer().damage(6);
				event.getPlayer().sendMessage("Being in a religious area without your own religion is pulling your soul apart...");
				return;
			}
		}

		if(!closestFrom.equals(closestTo)){
			event.getPlayer().sendMessage("You are now entering the territory of "+BeyondUtil.getChatColor(event.getPlayer(), closestTo) + closestTo);
		}

	}
	public void onPlayerInteract(PlayerInteractEvent event){
		if(event.getClickedBlock() == null)return;
		else if(event.getClickedBlock().getType() == Material.CHEST && BeyondInfo.getReligion(event.getClickedBlock().getLocation()) != null){
			if(BeyondInfo.getReligion(event.getPlayer()) == null && BeyondUtil.timestampReference(BeyondInfo.getTower(event.getClickedBlock().getLocation()))){
				event.getPlayer().sendMessage("You need to be a member of this religion to interact with this chest.");
				event.setCancelled(true);
			}else if((!BeyondInfo.getTowers(BeyondInfo.getReligion(event.getClickedBlock().getLocation())).contains(BeyondInfo.getTowerName(event.getPlayer()))) 
					|| (BeyondInfo.getReligion(event.getPlayer()) == null && !BeyondUtil.timestampReference(BeyondInfo.getTower(event.getClickedBlock().getLocation())))){
				Chest chest = (Chest) event.getClickedBlock().getState();
				if(chest.getInventory().contains(Material.GOLD_INGOT)){
					event.getPlayer().sendMessage("You stole a gold bar from "+ChatColor.RED.toString()+BeyondInfo.getTower(event.getClickedBlock().getLocation()));
					//TODO: make damage configurable via Config.BeyondConfig
					int index =chest.getInventory().first(266);
					ItemStack items = chest.getInventory().getItem(index);
					if(items.getAmount() == 1)chest.getInventory().setItem(index, null);
					else{
						items.setAmount(items.getAmount()-1);
						chest.getInventory().setItem(index, items);
					}
					event.getPlayer().getInventory().addItem(new ItemStack(Material.GOLD_INGOT, 1));
					event.getPlayer().updateInventory();
					// TODO: Make this int a Config thing
					int damage = 10;
					if(event.getPlayer().getHealth()< damage)event.getPlayer().setHealth(0);
					else event.getPlayer().setHealth(event.getPlayer().getHealth()-damage);
				}else{
					String religion = BeyondInfo.getReligion(event.getClickedBlock().getLocation());
					String tower = BeyondInfo.getTower(event.getClickedBlock().getLocation());
					if(BeyondInfo.hasPlayer(event.getPlayer()))BeyondUtil.towerBroadcast(tower, "Oh no! Your tower has fallen to the Zealots of "+ChatColor.RED.toString()+BeyondInfo.getReligion(event.getPlayer())+ChatColor.WHITE.toString()+", tower of "+ChatColor.RED.toString()+BeyondInfo.getTowerName(event.getPlayer()));
					event.getPlayer().sendMessage("DOWN WITH IT!!!");
					BeyondInfo.removeTower(religion, tower);
					if(BeyondInfo.hasPlayer(event.getPlayer()))BeyondUtil.religionBroadcast(religion, "The tower "+ChatColor.GREEN.toString()+tower+ChatColor.WHITE.toString()+" has fallen to the Zealots of "+ChatColor.RED.toString()+BeyondInfo.getReligion(event.getPlayer())+ChatColor.WHITE.toString()+", tower of "+ChatColor.RED.toString()+BeyondInfo.getTowerName(event.getPlayer()));
					if(BeyondInfo.hasPlayer(event.getPlayer()))BeyondUtil.religionBroadcast(BeyondInfo.getReligion(event.getPlayer()), ChatColor.GREEN.toString()+"Rejoice! "+ChatColor.RED.toString()+tower+ChatColor.WHITE.toString()+" has fallen to the tower of "+ChatColor.GREEN.toString()+BeyondInfo.getTowerName(event.getPlayer()));
					//TODO: Make explosion yield value configurable via Config.BeyondConfig
					event.getClickedBlock().getLocation().getWorld().createExplosion(event.getClickedBlock().getLocation(), 60, true);
					event.getClickedBlock().setTypeId(0);
				}
				event.setCancelled(true);
			}else if(BeyondInfo.getTowers(BeyondInfo.getReligion(event.getClickedBlock().getLocation())).contains(BeyondInfo.getTowerName(event.getPlayer()))){
				Chest chest = (Chest) event.getClickedBlock().getState();
				if(BeyondUtil.getChestAmount(chest, Material.GOLD_INGOT) == 1728){
					event.getPlayer().sendMessage("Chest is full!");
				}
				else if(event.getPlayer().getInventory().contains(Material.GOLD_INGOT)){
					event.getPlayer().sendMessage("You gave "+ChatColor.YELLOW.toString()+"1 "+ChatColor.WHITE.toString()+"Gold Bar to "+ChatColor.GREEN.toString()+BeyondInfo.getTower(event.getClickedBlock().getLocation()));
					int index = event.getPlayer().getInventory().first(266);
					ItemStack items = event.getPlayer().getInventory().getItem(index);
					if(items.getAmount() == 1)event.getPlayer().getInventory().setItem(index, null);
					else{
						items.setAmount(items.getAmount()-1);
						event.getPlayer().getInventory().setItem(index, items);
					}
					chest.getInventory().addItem(new ItemStack(Material.GOLD_INGOT, 1));
					event.getPlayer().updateInventory();
					event.getPlayer().sendMessage("The chest of "+ChatColor.GREEN.toString()+BeyondInfo.getTower(event.getClickedBlock().getLocation())+ChatColor.WHITE.toString()+" now has "+ChatColor.GREEN.toString()+BeyondUtil.getChestAmount(chest, Material.GOLD_INGOT)+" Gold Bars!");

				}
				event.setCancelled(true);
			}
		}else if(event.getClickedBlock().getType() == Material.CHEST && BeyondInfo.getTrade(event.getClickedBlock().getLocation()) != null){
			Chest chest = (Chest) event.getClickedBlock().getState();
			//
			//
			//
			//
			if((!BeyondInfo.getTowerName(event.getPlayer()).equals(BeyondInfo.getClosestValidTower(event.getClickedBlock().getLocation()))) && BeyondInfo.getReligion(event.getPlayer()).equals(BeyondInfo.getReligion(BeyondInfo.getClosestValidTower(event.getClickedBlock().getLocation())))){
				event.getPlayer().sendMessage("You need to be a member of this tower to interact with this chest in that manner.");
				event.setCancelled(true);
			}else if(BeyondInfo.getTowerName(event.getPlayer()).equals(BeyondInfo.getClosestValidTower(event.getClickedBlock().getLocation()))){
				//OPENS
			}else if(!BeyondInfo.getReligion(event.getPlayer()).equals(BeyondInfo.getReligion(BeyondInfo.getClosestValidTower(event.getClickedBlock().getLocation())))){
				//STEALS
				int index = inventoryRandomizer(chest.getInventory());
				ItemStack stack = chest.getInventory().getItem(index);
				if(stack == null){
					event.getPlayer().sendMessage("Terrible Luck!  You couldn't grab anything from the chest.");
				}else{
					event.getPlayer().sendMessage("You were able to steal some "+stack.getType().name());
					chest.getInventory().setItem(index, null);
					event.getPlayer().getInventory().addItem(new ItemStack(Material.GOLD_INGOT, 1));
					event.getPlayer().updateInventory();
				}
				int damage = 6;
				if(event.getPlayer().getHealth()< damage)event.getPlayer().setHealth(0);
				else event.getPlayer().setHealth(event.getPlayer().getHealth()-damage);
				event.setCancelled(true);
			}
		}
	}
	public void onPlayerBedEnter(PlayerBedEnterEvent event){
		if(BeyondInfo.getReligion(BeyondInfo.getClosestValidTower((event.getBed().getLocation()))) == null){
			return;
		}else if(BeyondInfo.getReligion(event.getPlayer()) == null){
			event.getPlayer().sendMessage("Since you are not aligned with this religion, you are not allowed to sleep in their halls.");
			event.setCancelled(true);
		}else if(!BeyondInfo.getReligion(event.getPlayer()).equals(BeyondInfo.getReligion(BeyondInfo.getClosestValidTower(event.getBed().getLocation())))){
			event.getPlayer().sendMessage("You can not bare to sleep in the halls of these heretics!");
			event.setCancelled(true);
		}
	}
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event){

	}
	public void onPlayerRespawn(PlayerRespawnEvent event){
		if(BeyondInfo.getTowerName(event.getPlayer())== null && !(BeyondInfo.getClosestValidTower(event.getRespawnLocation()) == null) && event.isBedSpawn()){
			event.setRespawnLocation(event.getPlayer().getWorld().getSpawnLocation());
		}else if(BeyondInfo.getTowerName(event.getPlayer())== null)return;
		else if(BeyondInfo.getClosestValidTower(event.getRespawnLocation()) == null)return;
		else if(BeyondInfo.getClosestValidTower(event.getRespawnLocation()).equals(BeyondInfo.getReligion(event.getPlayer()))){
			event.setRespawnLocation(event.getPlayer().getWorld().getSpawnLocation());
		}

	}
	public void onPlayerTeleport(PlayerTeleportEvent event){
		if(Religion.handler.has(event.getPlayer(), "religion.tp") || event.getPlayer().isOp()) return;
		if(BeyondInfo.getReligion(event.getPlayer())==null && BeyondInfo.getClosestValidTower(event.getTo()) != null){
			event.setTo(event.getFrom());
			event.getPlayer().sendMessage("Your connection with the destination has been severed by "+BeyondInfo.getClosestValidTower(event.getTo()));
		}
		else if(BeyondInfo.getReligion(event.getPlayer())!=null && BeyondInfo.getClosestValidTower(event.getTo()) != null){
			if(!BeyondInfo.getReligion(event.getPlayer()).equals(BeyondInfo.getReligion(BeyondInfo.getClosestValidTower(event.getTo())))){
				event.setTo(event.getFrom());
				event.getPlayer().sendMessage("Your connection with the destination has been severed by "+BeyondInfo.getClosestValidTower(event.getTo()));
			}
		}
	}
	public void onPlayerChat(PlayerChatEvent event){
		
	}
	public int inventoryRandomizer(Inventory inventory){
		Random chance = new Random();
		int next = chance.nextInt(inventory.getSize());
		return next;
	}
}
