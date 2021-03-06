package Lihad.Religion.Listeners;

import java.util.LinkedList;
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
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import Lihad.Religion.Religion;
import Lihad.Religion.Abilities.Personal;
import Lihad.Religion.Bosses.Bosses;
import Lihad.Religion.Config.BeyondConfig;
import Lihad.Religion.Information.BeyondInfo;
import Lihad.Religion.Util.BeyondUtil;
import Lihad.Religion.Util.BukkitSchedulePlayerMove;
import Lihad.Religion.Util.Notification;

public class BeyondPlayerListener implements Listener {
	public static Religion plugin;
	public static List<PlayerMoveEvent> queue = new LinkedList<PlayerMoveEvent>();
	public static LinkedList<String> playersOnQueue = new LinkedList<String>();
	public static LinkedList<String> playersOnQueueJoin = new LinkedList<String>();

	public static int listenerDropRate = 0;
	public static int listenerMaxDropRate = 0;

	public BeyondPlayerListener(Religion instance) {
		plugin = instance;
	}
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event){
		playersOnQueueJoin.add(event.getPlayer().getName());
		plugin.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, new Runnable() {
			public void run() {
				if(plugin.getServer().getPlayer(playersOnQueueJoin.get(0)) != null){
					plugin.setPlayerSuffix(plugin.getServer().getPlayer(playersOnQueueJoin.get(0)));
				}
				playersOnQueueJoin.remove(0);
			}
		}, 1L);
	}
	public static void onPlayerMoveExecutor(Player player, Location from, Location to){


		//Player AoE Cooldown
		if(BeyondInfo.getCooldownPlayers() != null &&
				BeyondInfo.getCooldownPlayers().contains(player) &&
				BeyondInfo.getClosestValidTower(to) != null &&
				(BeyondInfo.getReligion(player) == null ||
						!BeyondInfo.getReligion(BeyondInfo.getClosestValidTower(to)).equals(BeyondInfo.getReligion(player))) &&
						System.currentTimeMillis()-BeyondInfo.getPlayerCooldown(player) < 300000){
			player.teleport(from);
			player.sendMessage(ChatColor.RED.toString()+"You're shell-shocked. Your will is too weak to continue in.");
		}
		if(BeyondInfo.getClosestValidTower(to) != null && (BeyondInfo.getClosestValidTower(from) == null)){
			player.sendMessage("You are now entering the territory of "+BeyondUtil.getChatColor(player, BeyondInfo.getClosestValidTower(to)) + BeyondInfo.getClosestValidTower(to));
		}
		if(BeyondInfo.getClosestValidTower(to) == null && (BeyondInfo.getClosestValidTower(from) != null)){
			player.sendMessage("You are now entering the "+ChatColor.AQUA.toString()+"wilds");
		}
		//BOSS SPAWNER
		/**
		for(int a = 0;a<BeyondInfo.getBosses().size();a++){
			Location location = (Location) BeyondInfo.getBossLocation(BeyondInfo.getBosses().get(a));
			if(to.getWorld().equals(location.getWorld())){
				if((location.distance(to) < 40 &&Bosses.bossExistMap != null && Bosses.bossExistMap.get(BeyondInfo.getBosses().get(a)) != null && !Bosses.bossExistMap.get(BeyondInfo.getBosses().get(a)))){
					System.out.println("BOSS SPAWN - "+BeyondInfo.getBosses().get(a));
					Religion.bosses.spawnBoss(location, BeyondInfo.getBosses().get(a));
				}
			}
		}
		*/
		////////////////////////////////////////////////////////////////////////////
		if(BeyondInfo.isDevastationZone(to) && !BeyondInfo.isDevastationZone(from))player.sendMessage(ChatColor.GRAY.toString()+"You are entering a Devastation Zone (DZ)");
		if(BeyondInfo.isDevastationZone(from) && !BeyondInfo.isDevastationZone(to))player.sendMessage(ChatColor.GRAY.toString()+"You are leaving a Devastation Zone (DZ)");
		if(BeyondInfo.is500Tower(to) && !BeyondInfo.is500Tower(from) && !BeyondInfo.hasPlayer(player))player.sendMessage(ChatColor.RED.toString()+"The dark eye of "+ChatColor.LIGHT_PURPLE.toString()+BeyondInfo.getClosestTower(to)+ChatColor.RED.toString()+" is watching your every move. Build at your own risk. Type "+ChatColor.WHITE.toString()+"/reach"+ChatColor.RED.toString()+" for more info");
		if(BeyondInfo.is500Tower(from) && !BeyondInfo.is500Tower(to) && !BeyondInfo.hasPlayer(player))player.sendMessage(ChatColor.GREEN.toString()+"You have passed into the wilderness.");
		if(BeyondInfo.is25Tower(to) && !BeyondInfo.is25Tower(from) && !BeyondInfo.hasPlayer(player))player.sendMessage(ChatColor.RED.toString()+"You are entering the core of "+ChatColor.LIGHT_PURPLE.toString()+BeyondInfo.getClosestTower(to));
		if(BeyondInfo.is25Tower(from) && !BeyondInfo.is25Tower(to) && !BeyondInfo.hasPlayer(player))player.sendMessage(ChatColor.GREEN.toString()+"You have passed into city of "+ChatColor.LIGHT_PURPLE.toString()+BeyondInfo.getClosestTower(to));
		if(BeyondInfo.isHolyZone(to) && !BeyondInfo.isHolyZone(from))player.sendMessage(ChatColor.GREEN.toString()+"You have entered a Holy Area. No Griefing allowed!");
		if(BeyondInfo.isHolyZone(from) && !BeyondInfo.isHolyZone(to))player.sendMessage(ChatColor.RED.toString()+"You have left a Holy Area.");
	}
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event){
		//Ability Interacts
		if(event.getPlayer().getItemInHand() != null && event.getPlayer().getItemInHand().getType() == Material.BOOK && Religion.handler.has(event.getPlayer(), "religion.ability.heal")
				&& (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK))Personal.usesBook(event.getPlayer());
		else if(event.getClickedBlock() != null && event.getClickedBlock().getType()== Material.ICE && event.getPlayer().getItemInHand() != null && event.getAction() == Action.LEFT_CLICK_BLOCK
				&& event.getPlayer().getItemInHand().getType() == Material.DIAMOND_PICKAXE && Religion.handler.has(event.getPlayer(), "religion.ability.icepick"))Personal.usesDiamondPickAxeForIce(event.getPlayer(), event.getClickedBlock());
		else if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.TORCH && event.getPlayer().getItemInHand() != null && event.getPlayer().getItemInHand().getAmount() > 1  && event.getAction() == Action.RIGHT_CLICK_BLOCK
				&& Religion.handler.has(event.getPlayer(), "religion.ability.cooking"))Personal.usesCampfireCooking(event.getPlayer());
		else if(event.getPlayer().getItemInHand() != null && event.getPlayer().getItemInHand().getType() == Material.PAPER
				&& Religion.handler.has(event.getPlayer(), "religion.ability.origami"))Personal.usesPaper(event.getPlayer());
		else if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.CHEST)chestInteract(event);
	}
	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event){
		if(event.getPlayer().getItemInHand() == null)return;
		if(event.getPlayer().getItemInHand().getType() == Material.LEATHER && event.getRightClicked() instanceof Player
				&& Religion.handler.has(event.getPlayer(), "religion.ability.repair"))Personal.usesLeather(event.getPlayer(), (Player)event.getRightClicked());
		if(event.getPlayer().getItemInHand().getType() == Material.DIAMOND && event.getRightClicked() instanceof Villager && ((Villager)event.getRightClicked()).getProfession() == Profession.BLACKSMITH
				&& Religion.handler.has(event.getPlayer(), "religion.ability.trade"))Personal.usesDiamond(event.getPlayer(), (Villager)event.getRightClicked());
		if(event.getPlayer().getItemInHand().getType() == Material.GLASS_BOTTLE && event.getRightClicked() instanceof Creature
				&& Religion.handler.has(event.getPlayer(), "religion.ability.egg"))Personal.usesGlassBottle(event.getPlayer(), (Creature)event.getRightClicked());
		if(event.getPlayer().getItemInHand().getType() == Material.DIAMOND_SWORD && event.getRightClicked() instanceof Villager && ((Villager)event.getRightClicked()).getProfession() == Profession.BLACKSMITH
				&& Religion.handler.has(event.getPlayer(), "religion.ability.enchantsword"))Personal.usesDiamondItem(event.getPlayer(), (Villager)event.getRightClicked(), 0);
		if((event.getPlayer().getItemInHand().getType() == Material.DIAMOND_HELMET || event.getPlayer().getItemInHand().getType() == Material.DIAMOND_LEGGINGS || event.getPlayer().getItemInHand().getType() == Material.DIAMOND_CHESTPLATE || event.getPlayer().getItemInHand().getType() == Material.DIAMOND_BOOTS)
				&& event.getRightClicked() instanceof Villager && ((Villager)event.getRightClicked()).getProfession() == Profession.BLACKSMITH
				&& Religion.handler.has(event.getPlayer(), "religion.ability.enchantarmor"))Personal.usesDiamondItem(event.getPlayer(), (Villager)event.getRightClicked(), 1);
		if((event.getPlayer().getItemInHand().getType() == Material.DIAMOND_HOE || event.getPlayer().getItemInHand().getType() == Material.DIAMOND_AXE || event.getPlayer().getItemInHand().getType() == Material.DIAMOND_PICKAXE || event.getPlayer().getItemInHand().getType() == Material.DIAMOND_SPADE)
				&& event.getRightClicked() instanceof Villager && ((Villager)event.getRightClicked()).getProfession() == Profession.BLACKSMITH
				&& Religion.handler.has(event.getPlayer(), "religion.ability.enchanttool"))Personal.usesDiamondItem(event.getPlayer(), (Villager)event.getRightClicked(), 2);
		if(event.getPlayer().getItemInHand().getType() == Material.PAINTING && event.getRightClicked() instanceof Sheep
				&& Religion.handler.has(event.getPlayer(), "religion.ability.sheep"))Personal.usesPainting(event.getPlayer(), (Sheep)event.getRightClicked());
		if(event.getPlayer().getItemInHand().getType() == Material.GOLD_INGOT && event.getRightClicked() instanceof Villager && ((Villager)event.getRightClicked()).getProfession() == Profession.PRIEST
				&& Religion.handler.has(event.getPlayer(), "religion.ability.bless"))Personal.usesGoldIngotPriest(event.getPlayer(), (Villager)event.getRightClicked());
		if(event.getPlayer().getItemInHand().getType() == Material.GOLD_INGOT && event.getRightClicked() instanceof Villager && ((Villager)event.getRightClicked()).getProfession() == Profession.BUTCHER
				&& Religion.handler.has(event.getPlayer(), "religion.ability.food"))Personal.usesGoldIngotButcher(event.getPlayer(), (Villager)event.getRightClicked());
		if(event.getPlayer().getItemInHand().getType() == Material.GOLD_INGOT && event.getRightClicked() instanceof Villager && ((Villager)event.getRightClicked()).getProfession() == Profession.FARMER
				&& Religion.handler.has(event.getPlayer(), "religion.ability.farmerbow"))Personal.usesGoldIngotFarmer(event.getPlayer(), (Villager)event.getRightClicked());
		if(event.getPlayer().getItemInHand().getType() == Material.GOLD_INGOT && event.getRightClicked() instanceof Villager && ((Villager)event.getRightClicked()).getProfession() == Profession.BLACKSMITH
				&& Religion.handler.has(event.getPlayer(), "religion.ability.butchergold"))Personal.usesGoldIngotBlacksmith(event.getPlayer(), (Villager)event.getRightClicked());
		if(event.getPlayer().getItemInHand().getType() == Material.GOLD_INGOT && event.getRightClicked() instanceof Villager && ((Villager)event.getRightClicked()).getProfession() == Profession.LIBRARIAN
				&& Religion.handler.has(event.getPlayer(), "religion.ability.levelup"))Personal.usesGoldIngotLibrarian(event.getPlayer(), (Villager)event.getRightClicked());
		if(event.getPlayer().getItemInHand().getType() == Material.DIAMOND && event.getRightClicked() instanceof Villager && ((Villager)event.getRightClicked()).getProfession() == Profession.FARMER
				&& Religion.handler.has(event.getPlayer(), "religion.ability.spy"))Personal.usesDiamondSpy(event.getPlayer(), (Villager)event.getRightClicked());


	
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
	public int inventoryRandomizer(Inventory inventory){
		Random chance = new Random();
		int next = chance.nextInt(inventory.getSize());
		return next;
	}
	private void chestInteract(PlayerInteractEvent event){
		if(BeyondInfo.getReligion(event.getClickedBlock().getLocation()) != null){
			if(BeyondInfo.getReligion(event.getPlayer()) == null && BeyondUtil.timestampReference(BeyondInfo.getTower(event.getClickedBlock().getLocation()))){
				event.getPlayer().sendMessage("You need to be a member of this religion to interact with this chest.");
				event.setCancelled(true);
			}else if((!BeyondInfo.getTowers(BeyondInfo.getReligion(event.getClickedBlock().getLocation())).contains(BeyondInfo.getTowerName(event.getPlayer()))) 
					|| (BeyondInfo.getReligion(event.getPlayer()) == null && !BeyondUtil.timestampReference(BeyondInfo.getTower(event.getClickedBlock().getLocation())))){
				Chest chest = (Chest) event.getClickedBlock().getState();
				if(chest.getInventory().contains(Material.GOLD_INGOT)){
					event.getPlayer().sendMessage("You stole a gold bar from "+ChatColor.RED.toString()+BeyondInfo.getTower(event.getClickedBlock().getLocation()));
					Notification.event.add(ChatColor.WHITE.toString()+" - "+ChatColor.LIGHT_PURPLE.toString()+BeyondInfo.getTowerName(event.getPlayer())+"("+BeyondInfo.getReligion(event.getPlayer())+")"+ChatColor.GRAY.toString()+" attacked "+ChatColor.RED.toString()+BeyondInfo.getTower(event.getClickedBlock().getLocation())+"("+BeyondInfo.getReligion(event.getClickedBlock().getLocation())+")");
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
		}else if(BeyondInfo.getTrade(event.getClickedBlock().getLocation()) != null){
			Chest chest = (Chest) event.getClickedBlock().getState();
			if(BeyondInfo.getTowerName(event.getPlayer()) == null){
				event.getPlayer().sendMessage("You must be a member of a Religion to interact with this chest.");
				event.setCancelled(true);
			}else if((!BeyondInfo.getTowerName(event.getPlayer()).equals(BeyondInfo.getClosestValidTower(event.getClickedBlock().getLocation()))) && BeyondInfo.getReligion(event.getPlayer()).equals(BeyondInfo.getReligion(BeyondInfo.getClosestValidTower(event.getClickedBlock().getLocation())))){
				event.getPlayer().sendMessage("You need to be a member of this tower to interact with this chest in that manner.");
				event.setCancelled(true);
			}else if(BeyondInfo.getTowerName(event.getPlayer()).equals(BeyondInfo.getClosestValidTower(event.getClickedBlock().getLocation())) && (BeyondInfo.isMemberTrusted(event.getPlayer()) || BeyondInfo.isPlayerLeader(event.getPlayer())))  {
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
}
