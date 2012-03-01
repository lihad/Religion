package Lihad.Religion.Listeners;

import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import Lihad.Religion.Religion;
import Lihad.Religion.Bosses.Bosses;
import Lihad.Religion.Config.BeyondConfig;
import Lihad.Religion.Information.BeyondInfo;
import Lihad.Religion.Util.BeyondUtil;

public class BeyondPlayerListener implements Listener {
	public static Religion plugin;
	public BeyondPlayerListener(Religion instance) {
		plugin = instance;
	}
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event){
		String closestFrom = BeyondInfo.getClosestValidTower(event.getFrom());
		String closestTo = BeyondInfo.getClosestValidTower(event.getTo());

		if(closestFrom==null)closestFrom = "null";
		if(closestTo==null)closestTo = "null";

		////////////////////////////////
		// TODO: This is crude. And ugly.
		if(closestFrom.equals("null")&&!closestTo.equals("null")){
			if(BeyondInfo.getReligion(event.getPlayer()) != null){
				if(BeyondInfo.getReligion(closestTo).equals(BeyondInfo.getReligion(event.getPlayer()))){

				}else if(BeyondInfo.getCooldownPlayers().contains(event.getPlayer())){
					if(System.currentTimeMillis()-BeyondInfo.getPlayerCooldown(event.getPlayer()) < 300000){
						event.setTo(event.getFrom());
						event.getPlayer().sendMessage(ChatColor.RED.toString()+"You're shell-shocked. Your will is too weak to continue in.");
					}
				}
			}else if(BeyondInfo.getCooldownPlayers().contains(event.getPlayer())){
				if(System.currentTimeMillis()-BeyondInfo.getPlayerCooldown(event.getPlayer()) < 300000){
					event.setTo(event.getFrom());
					event.getPlayer().sendMessage(ChatColor.RED.toString()+"You're shell-shocked. Your will is too weak to continue in.");
				}
			}
		}
			
		////////////////////////////////	
		
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
		//BOSS SPAWNER
		for(int a = 0;a<Bosses.configBossMap.size();a++){
			Location location = (Location) Bosses.configBossMap.keySet().toArray()[a];
			if(event.getPlayer().getWorld().equals(location.getWorld())){
				if((location.distance(event.getPlayer().getLocation()) < 40 &&Bosses.bossExistMap.isEmpty()) || (location.distance(event.getPlayer().getLocation()) < 40 && !Bosses.bossExistMap.get(Bosses.bossNameMap.get(Bosses.configBossMap.get(location))))){
					System.out.println("BOSS SPAWN");
					Religion.bosses.spawnBoss(location, Bosses.configBossMap.get(location));
				}

			}

		}
		//
		//
		//
		if(Bosses.bossExistMap != null && Bosses.bossExistMap.containsValue(true)){
			//System.out.println("-------------------------");

			//System.out.println(Bosses.bossExistMap);
			//System.out.println(Bosses.bossHealthMap);
			//System.out.println(Bosses.bossMaxHealthMap);
			//System.out.println(Bosses.bossNameMap);

			//System.out.println("-------------------------");

			for(int a = 0; a<Bosses.bossHealthMap.size();a++){
				Creature boss = (Creature) Bosses.bossHealthMap.keySet().toArray()[a];
				List<Entity> list = boss.getNearbyEntities(40, 40, 40);
				if(list.isEmpty()){
					System.out.println("Despawn1");
					Bosses.bossExistMap.remove(boss);
					Bosses.bossHealthMap.remove(boss);
					Bosses.bossMaxHealthMap.remove(boss);
					boss.remove();
				}else{
					for(int j=0;j<list.size();j++){
						if(list.get(j) instanceof Player){
							break;
						}else if(list.size()-1==j){
							System.out.println("Despawn2");
							Bosses.bossExistMap.remove(boss);
							Bosses.bossHealthMap.remove(boss);
							Bosses.bossMaxHealthMap.remove(boss);
							boss.remove();
							break;
						}
					}
				}
				if(boss.getTarget() != null){
					if(!list.contains(boss.getTarget())){
						for(int i = 0; i<list.size(); i++){
							if(list.get(i) instanceof Player){
								boss.setTarget((LivingEntity) list.get(i));
								break;
							}
						}
					}
				}else{
					for(int i = 0; i<list.size(); i++){
						if(list.get(i) instanceof Player){
							boss.setTarget((LivingEntity) list.get(i));
							break;
						}
					}
				}
			}
		}
		if(event.getPlayer().getNearbyEntities(20, 20, 20).contains(Bosses.bossNameMap.get("Xtal")) && Bosses.bossExistMap.get(Bosses.bossNameMap.get("Xtal"))==true &&((Creature)Bosses.bossNameMap.get("Xtal")).getTarget() != null){
			Random chance = new Random();
			for(int i=0;i<event.getPlayer().getNearbyEntities(20, 20, 20).size();i++){
				if(event.getPlayer().getNearbyEntities(20, 20, 20).get(i)instanceof Player){
					int random = chance.nextInt(1000);
					if(random<1 && !event.getPlayer().getNearbyEntities(20, 20, 20).get(i).equals((LivingEntity)Bosses.bossNameMap.get("Xtal"))){
						event.getPlayer().getNearbyEntities(20, 20, 20).get(i).getWorld().strikeLightning(event.getPlayer().getNearbyEntities(20, 20, 20).get(i).getLocation());
					}
				}
			}
		}
		if(BeyondInfo.isDevastationZone(event.getTo()) && !BeyondInfo.isDevastationZone(event.getFrom())){
			event.getPlayer().sendMessage(ChatColor.GRAY.toString()+"You are entering a Devastation Zone (DZ)");
		}
		if(BeyondInfo.isDevastationZone(event.getFrom()) && !BeyondInfo.isDevastationZone(event.getTo())){
			event.getPlayer().sendMessage(ChatColor.GRAY.toString()+"You are leaving a Devastation Zone (DZ)");
		}
		if(BeyondInfo.is500Tower(event.getTo()) && !BeyondInfo.is500Tower(event.getFrom()) && !BeyondInfo.hasPlayer(event.getPlayer())){
			event.getPlayer().sendMessage(ChatColor.RED.toString()+"The dark eye of "+ChatColor.LIGHT_PURPLE.toString()+BeyondInfo.getClosestTower(event.getTo())+ChatColor.RED.toString()+" is watching your every move. Build at your own risk. Type "+ChatColor.WHITE.toString()+"/reach"+ChatColor.RED.toString()+" for more info");
		}
		if(BeyondInfo.is500Tower(event.getFrom()) && !BeyondInfo.is500Tower(event.getTo()) && !BeyondInfo.hasPlayer(event.getPlayer())){
			event.getPlayer().sendMessage(ChatColor.GREEN.toString()+"You have passed into the wilderness.");
		}
		if(BeyondInfo.isHolyZone(event.getTo()) && !BeyondInfo.isHolyZone(event.getFrom())){
			event.getPlayer().sendMessage(ChatColor.GREEN.toString()+"You have entered a Holy Area. No Griefing allowed!");
		}
		if(BeyondInfo.isHolyZone(event.getFrom()) && !BeyondInfo.isHolyZone(event.getTo())){
			event.getPlayer().sendMessage(ChatColor.RED.toString()+"You have left a Holy Area.");
		}
	}
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event){
		if(event.getPlayer().getItemInHand().getType() == Material.BOOK && Religion.handler.has(event.getPlayer(), "religion.heal") && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)){
			List<Entity> entities = event.getPlayer().getNearbyEntities(5, 2, 5);
			for(int i = 0; i<entities.size();i++){
				if(entities.get(i) instanceof Player){
					Player player = (Player)entities.get(i);
					if(player.getHealth() < 19){
						int var = healChance();
						if(var==0){
							event.getPlayer().sendMessage("You failed to heal "+BeyondUtil.getChatColor(player, BeyondInfo.getTowerName(event.getPlayer()))+player.getName());
						}else{
							event.getPlayer().sendMessage("You healed "+BeyondUtil.getChatColor(player, BeyondInfo.getTowerName(event.getPlayer()))+player.getName()+ChatColor.WHITE.toString()+" by "+var);
							player.sendMessage("You were healed by "+BeyondUtil.getChatColor(event.getPlayer(), BeyondInfo.getTowerName(player))+event.getPlayer().getName()+ChatColor.WHITE.toString());
							player.setHealth(player.getHealth()+var);
							
							if(event.getPlayer().getItemInHand().getAmount() <= 1)event.getPlayer().setItemInHand(null);
							else event.getPlayer().getItemInHand().setAmount(event.getPlayer().getItemInHand().getAmount()-1);
						}
					}else{
						event.getPlayer().sendMessage("You failed to heal "+BeyondUtil.getChatColor(player, BeyondInfo.getTowerName(event.getPlayer()))+player.getName());
					}
				}
			}
		}
		
		
		
		
		
		///// ON"Y CHEST INTERACTS AFTER THIS POINT
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
			if(BeyondInfo.getTowerName(event.getPlayer()) == null){
				event.getPlayer().sendMessage("You must be a member of a Religion to interact with this chest.");
				event.setCancelled(true);
			}else if((!BeyondInfo.getTowerName(event.getPlayer()).equals(BeyondInfo.getClosestValidTower(event.getClickedBlock().getLocation()))) && BeyondInfo.getReligion(event.getPlayer()).equals(BeyondInfo.getReligion(BeyondInfo.getClosestValidTower(event.getClickedBlock().getLocation())))){
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
					event.getPlayer().getInventory().addItem(stack);
					event.getPlayer().updateInventory();
				}
				int damage = 6;
				if(event.getPlayer().getHealth()< damage)event.getPlayer().setHealth(0);
				else event.getPlayer().setHealth(event.getPlayer().getHealth()-damage);
				event.setCancelled(true);
			}
		}
	}
	@EventHandler
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
	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event){
		if(event.getPlayer().getItemInHand().getType() == Material.LEATHER && Religion.handler.has(event.getPlayer(), "religion.repair")){
			if(event.getRightClicked() instanceof Player){
				Player player = (Player) event.getRightClicked();
				int var = repairChance();
				if(var == 0){
					event.getPlayer().sendMessage("Oh No! The Leather broke without repairing! Poor "+player.getName());
					player.sendMessage("Oh No! "+event.getPlayer().getName()+" has failed to repair your item!");
				}else if(player.getItemInHand().getDurability() <= 0){
					event.getPlayer().sendMessage("The item you are trying to repair is already at its maximum potential!");
				}else if(player.getItemInHand().getEnchantments().keySet().size() > 0){
					event.getPlayer().sendMessage("You lack the potential to repair this item...");
				}else{
					player.getItemInHand().setDurability((short)(player.getItemInHand().getDurability()-var));
					if(event.getPlayer().getItemInHand().getAmount() == 1)event.getPlayer().setItemInHand(null);
					else event.getPlayer().getItemInHand().setAmount(event.getPlayer().getItemInHand().getAmount()-1);
					event.getPlayer().sendMessage("Awesome! You repaired "+player.getName()+"'s "+player.getItemInHand().getType().name()+" by "+var+" points!");
					player.sendMessage("Alright! "+event.getPlayer().getName()+" repaired your "+player.getItemInHand().getType().name()+" by "+var+" points!");
				}
			}
		}
	}
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event){
		if(BeyondInfo.getTowerName(event.getPlayer())== null && !(BeyondInfo.getClosestValidTower(event.getRespawnLocation()) == null) && event.isBedSpawn()){
			event.setRespawnLocation(event.getPlayer().getWorld().getSpawnLocation());
		}else if(BeyondInfo.getTowerName(event.getPlayer())== null)return;
		else if(BeyondInfo.getClosestValidTower(event.getRespawnLocation()) == null)return;
		else if(BeyondInfo.getClosestValidTower(event.getRespawnLocation()).equals(BeyondInfo.getReligion(event.getPlayer()))){
			event.setRespawnLocation(event.getPlayer().getWorld().getSpawnLocation());
		}

	}
	@EventHandler
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
	@EventHandler
	public void onPlayerChat(PlayerChatEvent event){
		
	}
	public int inventoryRandomizer(Inventory inventory){
		Random chance = new Random();
		int next = chance.nextInt(inventory.getSize());
		return next;
	}
	
	private int repairChance(){
		Random chance = new Random();
		int next = chance.nextInt(100);
		if(next<2)return 20;
		else if(next<10)return 15;
		else if(next<20)return 10;
		else if(next<30)return 5;
		else if(next<45)return 3;
		else if(next<60)return 2;
		else if(next<80)return 1;
		else return 0;
	}
	private int healChance(){
		Random chance = new Random();
		int next = chance.nextInt(10);
		if(next<1)return 2;
		else if(next<6)return 1;
		else return 0;
	}
}
