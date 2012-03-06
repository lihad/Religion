package Lihad.Religion.Abilities;

import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.material.MaterialData;
import org.bukkit.material.MonsterEggs;
import org.bukkit.material.Mushroom;

import Lihad.Religion.Religion;
import Lihad.Religion.Information.BeyondInfo;
import Lihad.Religion.Util.BeyondUtil;

public class Personal {
	public static Religion plugin;


	public Personal(Religion instance) {
		plugin = instance;
	}
	public static void usesBook(Player eventplayer){
		List<Entity> entities = eventplayer.getNearbyEntities(5, 2, 5);
		for(int i = 0; i<entities.size();i++){
			if(entities.get(i) instanceof Player){
				Player player = (Player)entities.get(i);
				if(player.getHealth() < 19){
					int var = healChance();
					if(var==0){
						eventplayer.sendMessage("You failed to heal "+BeyondUtil.getChatColor(player, BeyondInfo.getTowerName(eventplayer))+player.getName());
					}else{
						eventplayer.sendMessage("You healed "+BeyondUtil.getChatColor(player, BeyondInfo.getTowerName(eventplayer))+player.getName()+ChatColor.WHITE.toString()+" by "+var);
						player.sendMessage("You were healed by "+BeyondUtil.getChatColor(eventplayer, BeyondInfo.getTowerName(player))+eventplayer.getName()+ChatColor.WHITE.toString());
						player.setHealth(player.getHealth()+var);
						
						if(eventplayer.getItemInHand().getAmount() <= 1)eventplayer.setItemInHand(null);
						else eventplayer.getItemInHand().setAmount(eventplayer.getItemInHand().getAmount()-1);
					}
				}
			}
		}
	}
	public static void usesLeather(Player player, Player playerrec){
		int var = repairChance();
		if(var == 0){
			player.sendMessage("Oh No! The Leather broke without repairing! Poor "+playerrec.getName());
			playerrec.sendMessage("Oh No! "+player.getName()+" has failed to repair your item!");
		}else if(playerrec.getItemInHand().getDurability() <= 0){
			player.sendMessage("The item you are trying to repair is already at its maximum potential!");
		}else if(playerrec.getItemInHand().getEnchantments().keySet().size() > 0){
			player.sendMessage("You lack the potential to repair this item...");
		}else{
			playerrec.getItemInHand().setDurability((short)(player.getItemInHand().getDurability()-var));
			if(player.getItemInHand().getAmount() == 1)player.setItemInHand(null);
			else player.getItemInHand().setAmount(player.getItemInHand().getAmount()-1);
			player.sendMessage("Awesome! You repaired "+playerrec.getName()+"'s "+playerrec.getItemInHand().getType().name()+" by "+var+" points!");
			playerrec.sendMessage("Alright! "+player.getName()+" repaired your "+playerrec.getItemInHand().getType().name()+" by "+var+" points!");
		}
	}
	public static void usesCampfireCooking(Player player){
		if(player.getItemInHand().getType() == Material.RAW_BEEF){
			removeItemInHandBy1(player);
			if(generalRandomizer(3) < 2)player.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 1));
			else player.sendMessage(ChatColor.RED.toString()+"You terrible cook!  You burnt the meat to a crisp!");
		}
		if(player.getItemInHand().getType() == Material.RAW_CHICKEN){
			removeItemInHandBy1(player);
			if(generalRandomizer(3) < 2)player.getInventory().addItem(new ItemStack(Material.COOKED_CHICKEN, 1));
			else player.sendMessage(ChatColor.RED.toString()+"You terrible cook!  You burnt the meat to a crisp!");
		}
		if(player.getItemInHand().getType() == Material.RAW_FISH){
			removeItemInHandBy1(player);
			if(generalRandomizer(3) < 2)player.getInventory().addItem(new ItemStack(Material.COOKED_FISH, 1));
			else player.sendMessage(ChatColor.RED.toString()+"You terrible cook!  You burnt the meat to a crisp!");
		}
		if(player.getItemInHand().getTypeId() == 319){
			removeItemInHandBy1(player);
			if(generalRandomizer(3) < 2)player.getInventory().addItem(new ItemStack(320, 1));
			else player.sendMessage(ChatColor.RED.toString()+"You terrible cook!  You burnt the meat to a crisp!");
		}
		if(player.getItemInHand().getType() == Material.ROTTEN_FLESH){
			removeItemInHandBy1(player);
			if(generalRandomizer(5) < 2)player.getInventory().addItem(new ItemStack(319, 1));
			else player.sendMessage(ChatColor.RED.toString()+"You terrible cook!  You burnt the meat to a crisp!");
		}
	}
	public static void usesPaper(Player player){
		removeItemInHandBy1(player);
		int chance = generalRandomizer(100);
		if(chance==0)player.getWorld().spawnCreature(player.getLocation(), EntityType.IRON_GOLEM);
		else if(chance==1)player.getWorld().spawnCreature(player.getLocation(), EntityType.VILLAGER);
		else if(chance==2)player.getWorld().spawnCreature(player.getLocation(), EntityType.MUSHROOM_COW);
		else if(chance==3)player.getWorld().spawnCreature(player.getLocation(), EntityType.OCELOT);
		else if(chance==4)player.getWorld().spawnCreature(player.getLocation(), EntityType.WOLF);
		else if(chance==5)player.getWorld().spawnCreature(player.getLocation(), EntityType.SNOWMAN);
		else if(chance==6)player.getWorld().spawnCreature(player.getLocation(), EntityType.BOAT);
		else if(chance==7)player.getWorld().spawnCreature(player.getLocation(), EntityType.CHICKEN);
		else if(chance==8)player.getWorld().spawnCreature(player.getLocation(), EntityType.COW);
		else if(chance==9)player.getWorld().spawnCreature(player.getLocation(), EntityType.PIG);
		else if(chance==10)player.getWorld().spawnCreature(player.getLocation(), EntityType.MINECART);
		else if(chance==11)player.getWorld().spawnCreature(player.getLocation(), EntityType.SQUID);
		else if(chance==12)player.getWorld().spawnCreature(player.getLocation(), EntityType.ZOMBIE);
		else if(chance==13)player.getWorld().spawnCreature(player.getLocation(), EntityType.PIG_ZOMBIE);
		else if(chance==14)player.getWorld().spawnCreature(player.getLocation(), EntityType.SILVERFISH);
		else if(chance==15)player.getWorld().spawnCreature(player.getLocation(), EntityType.SHEEP);
		else player.sendMessage("You crumple up a piece of paper and throw it ... Nothing happens");
	}
	public static void usesGlassBottle(Player player, Creature creature){
		removeItemInHandBy1(player);
		int chance = generalRandomizer(100);
		if(chance < 10){
			player.sendMessage(ChatColor.LIGHT_PURPLE.toString()+"You captured a "+creature.toString()+"!");
			if(creature instanceof Creeper)creature.getWorld().dropItemNaturally(creature.getLocation(), (new MonsterEggs(383,(byte)50).toItemStack()));
			else if(creature instanceof Skeleton)creature.getWorld().dropItemNaturally(creature.getLocation(), (new MonsterEggs(383,(byte)51).toItemStack()));
			else if(creature instanceof Spider)creature.getWorld().dropItemNaturally(creature.getLocation(), (new MonsterEggs(383,(byte)52).toItemStack()));
			else if(creature instanceof Zombie)creature.getWorld().dropItemNaturally(creature.getLocation(), (new MonsterEggs(383,(byte)53).toItemStack()));
			else if(creature instanceof Slime)creature.getWorld().dropItemNaturally(creature.getLocation(), (new MonsterEggs(383,(byte)55).toItemStack()));
			else if(creature instanceof Ghast)creature.getWorld().dropItemNaturally(creature.getLocation(), (new MonsterEggs(383,(byte)56).toItemStack()));
			else if(creature instanceof Skeleton)creature.getWorld().dropItemNaturally(creature.getLocation(), (new MonsterEggs(383,(byte)57).toItemStack()));
			else if(creature instanceof PigZombie)creature.getWorld().dropItemNaturally(creature.getLocation(), (new MonsterEggs(383,(byte)58).toItemStack()));
			else if(creature instanceof CaveSpider)creature.getWorld().dropItemNaturally(creature.getLocation(), (new MonsterEggs(383,(byte)59).toItemStack()));
			else if(creature instanceof Silverfish)creature.getWorld().dropItemNaturally(creature.getLocation(), (new MonsterEggs(383,(byte)60).toItemStack()));
			else if(creature instanceof Blaze)creature.getWorld().dropItemNaturally(creature.getLocation(), (new MonsterEggs(383,(byte)61).toItemStack()));
			else if(creature instanceof MagmaCube)creature.getWorld().dropItemNaturally(creature.getLocation(), (new MonsterEggs(383,(byte)62).toItemStack()));
			else if(creature instanceof Pig)creature.getWorld().dropItemNaturally(creature.getLocation(), (new MonsterEggs(383,(byte)90).toItemStack()));
			else if(creature instanceof Sheep)creature.getWorld().dropItemNaturally(creature.getLocation(), (new MonsterEggs(383,(byte)91).toItemStack()));
			else if(creature instanceof Cow)creature.getWorld().dropItemNaturally(creature.getLocation(), (new MonsterEggs(383,(byte)92).toItemStack()));
			else if(creature instanceof Chicken)creature.getWorld().dropItemNaturally(creature.getLocation(), (new MonsterEggs(383,(byte)93).toItemStack()));
			else if(creature instanceof Squid)creature.getWorld().dropItemNaturally(creature.getLocation(), (new MonsterEggs(383,(byte)94).toItemStack()));
			else if(creature instanceof Wolf)creature.getWorld().dropItemNaturally(creature.getLocation(), (new MonsterEggs(383,(byte)95).toItemStack()));
			else if(creature instanceof MushroomCow)creature.getWorld().dropItemNaturally(creature.getLocation(), (new MonsterEggs(383,(byte)96).toItemStack()));
			else if(creature instanceof Villager)creature.getWorld().dropItemNaturally(creature.getLocation(), (new MonsterEggs(383,(byte)120).toItemStack()));
			else player.sendMessage(ChatColor.RED.toString()+"You felt the power inside you to capture the creature, but failed!");
			creature.remove();
		}else{
			player.sendMessage(ChatColor.LIGHT_PURPLE.toString()+"You failed to capture the "+creature.toString()+"!");
			creature.setTarget(player);
		}

	}
	public static void usesDiamondPickAxeForIce(Player player, Block block){
		if(generalRandomizer(4) == 0){
			block.setTypeId(0);
			block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.ICE, 1));
		}
	}
	public static void usesDiamond(Player player, Villager villager){
		removeItemInHandBy1(player);
		int chance = generalRandomizer(100);
		if(chance < 10){
			player.sendMessage(ChatColor.LIGHT_PURPLE.toString()+"Thank you!  Here is some Equipment I had in storage, hope it helps!");
			List<ItemStack> stacks = BeyondUtil.randomLootPump();
			for(int i=0;i<stacks.size();i++){
				villager.getWorld().dropItemNaturally(villager.getLocation(), stacks.get(i));
			}
		}
		else if(chance < 50){
			player.sendMessage(ChatColor.LIGHT_PURPLE.toString()+"Thank you!  Now my wife will lay me for sure!");
			villager.getWorld().spawnCreature(villager.getLocation(), EntityType.VILLAGER);
		}
		else if(chance < 70){
			player.sendMessage(ChatColor.LIGHT_PURPLE.toString()+"Oh!!! So shiny!! I can'... .... ugh... oh, MY HEART!!!");
			villager.setHealth(0);
		}
		else player.sendMessage(ChatColor.LIGHT_PURPLE.toString()+"Thank you very much!");
	}


	private static int healChance(){
		Random chance = new Random();
		int next = chance.nextInt(10);
		if(next<1)return 2;
		else if(next<6)return 1;
		else return 0;
	}
	private static int repairChance(){
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
	private static int generalRandomizer(int num){
		Random chance = new Random();
		return chance.nextInt(num);
	}
	private static void removeItemInHandBy1(Player player){
		if(player.getItemInHand().getAmount() == 1)player.setItemInHand(null);
		else player.getItemInHand().setAmount(player.getItemInHand().getAmount()-1);
	}
}
