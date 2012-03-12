package Lihad.Religion.Abilities;

import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
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
		eventplayer.updateInventory();
	}
	public static void usesLeather(Player player, Player playerrec){
		removeItemInHandBy1(player);
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
			player.sendMessage("Awesome! You repaired "+playerrec.getName()+"'s "+playerrec.getItemInHand().getType().name()+" by "+var+" points!");
			playerrec.sendMessage("Alright! "+player.getName()+" repaired your "+playerrec.getItemInHand().getType().name()+" by "+var+" points!");
		}
		player.updateInventory();
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
		player.updateInventory();
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
		else if(chance==10)player.getWorld().spawn(player.getLocation(),Boat.class);
		else if(chance==7)player.getWorld().spawnCreature(player.getLocation(), EntityType.CHICKEN);
		else if(chance==8)player.getWorld().spawnCreature(player.getLocation(), EntityType.COW);
		else if(chance==9)player.getWorld().spawnCreature(player.getLocation(), EntityType.PIG);
		else if(chance==10)player.getWorld().spawn(player.getLocation(),Minecart.class);
		else if(chance==11)player.getWorld().spawnCreature(player.getLocation(), EntityType.SQUID);
		else if(chance==12)player.getWorld().spawnCreature(player.getLocation(), EntityType.ZOMBIE);
		else if(chance==13)player.getWorld().spawnCreature(player.getLocation(), EntityType.PIG_ZOMBIE);
		else if(chance==14)player.getWorld().spawnCreature(player.getLocation(), EntityType.SILVERFISH);
		else if(chance==15)player.getWorld().spawnCreature(player.getLocation(), EntityType.SHEEP);
		else player.sendMessage("You crumple up a piece of paper and throw it. Nothing happens");
	}
	public static void usesGlassBottle(Player player, Creature creature){
		removeItemInHandBy1(player);
		int chance = generalRandomizer(100);
		if(chance < 10){
			player.sendMessage(ChatColor.LIGHT_PURPLE.toString()+"You captured a "+creature.toString()+"!");
			if(creature instanceof Creeper)player.getInventory().addItem(new ItemStack(383,1,(short)0,(byte)50));
			else if(creature instanceof Skeleton)player.getInventory().addItem(new ItemStack(383,1,(short)0,(byte)51));
			else if(creature instanceof Spider)player.getInventory().addItem(new ItemStack(383,1,(short)0,(byte)52));
			else if(creature instanceof PigZombie)player.getInventory().addItem(new ItemStack(383,1,(short)0,(byte)57));
			else if(creature instanceof Zombie)player.getInventory().addItem(new ItemStack(383,1,(short)0,(byte)54));
			else if(creature instanceof CaveSpider)player.getInventory().addItem(new ItemStack(383,1,(short)0,(byte)59));
			else if(creature instanceof Silverfish)player.getInventory().addItem(new ItemStack(383,1,(short)0,(byte)60));
			else if(creature instanceof Pig)player.getInventory().addItem(new ItemStack(383,1,(short)0,(byte)90));
			else if(creature instanceof Sheep)player.getInventory().addItem(new ItemStack(383,1,(short)0,(byte)91));
			else if(creature instanceof Cow)player.getInventory().addItem(new ItemStack(383,1,(short)0,(byte)92));
			else if(creature instanceof Chicken)player.getInventory().addItem(new ItemStack(383,1,(short)0,(byte)93));
			else if(creature instanceof Squid)player.getInventory().addItem(new ItemStack(383,1,(short)0,(byte)94));
			else if(creature instanceof Wolf)player.getInventory().addItem(new ItemStack(383,1,(short)0,(byte)95));
			else if(creature instanceof MushroomCow)player.getInventory().addItem(new ItemStack(383,1,(short)0,(byte)96));
			else if(creature instanceof Snowman)player.getInventory().addItem(new ItemStack(383,1,(short)0,(byte)97));
			else if(creature instanceof Ocelot)player.getInventory().addItem(new ItemStack(383,1,(short)0,(byte)98));
			else if(creature instanceof IronGolem)player.getInventory().addItem(new ItemStack(383,1,(short)0,(byte)99));
			else if(creature instanceof Villager)player.getInventory().addItem(new ItemStack(383,1,(short)0,(byte)120));
			else player.sendMessage(ChatColor.RED.toString()+"You felt the power inside you to capture the creature, but failed!");
			creature.remove();
		}else{
			player.sendMessage(ChatColor.LIGHT_PURPLE.toString()+"You failed to capture the "+creature.toString()+"!");
			creature.setTarget(player);
		}
		player.updateInventory();

	}
	public static void usesDiamondPickAxeForIce(Player player, Block block){
		if(generalRandomizer(4) == 0){
			block.setTypeId(0);
			block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.ICE, 1));
		}
	}
	public static void usesDiamondItem(Player player, Villager villager, int useableindex){
		// 0 for weapon
		// 1 for armor
		// 2 for tool
		int chance = generalRandomizer(100);
		player.sendMessage(ChatColor.LIGHT_PURPLE.toString()+"I think I can enchant that "+player.getItemInHand().getType().name()+" for you....");
		if(chance<70){
			player.sendMessage(ChatColor.LIGHT_PURPLE.toString()+"HAHA!  We did it!");
			if(useableindex == 0)player.getItemInHand().addUnsafeEnchantment(BeyondUtil.weaponEnchantRandomizer(), BeyondUtil.weaponLevelRandomizer());
			if(useableindex == 1)player.getItemInHand().addUnsafeEnchantment(BeyondUtil.armorEnchantRandomizer(), BeyondUtil.armorLevelRandomizer());
			if(useableindex == 2)player.getItemInHand().addUnsafeEnchantment(BeyondUtil.toolEnchantRandomizer(), BeyondUtil.toolLevelRandomizer());
			player.getWorld().strikeLightningEffect(player.getLocation());
		}else if(chance<80){
			removeItemInHandBy1(player);
			player.sendMessage(ChatColor.LIGHT_PURPLE.toString()+"Oh no.... umm, I am so sorry...");
		}else{
			player.getWorld().strikeLightning(player.getLocation());
			villager.getWorld().strikeLightning(villager.getLocation());
			player.sendMessage(ChatColor.LIGHT_PURPLE.toString()+"OH! BLAAAAAARRRRRRGH");
		}
		player.updateInventory();
	}
	public static void usesPainting(Player player,Sheep sheep){
		Random chance = new Random();
		int next = chance.nextInt(10);
		removeItemInHandBy1(player);
		if(next<9)sheep.setColor(sheepRandomizer());
		else{
			player.sendMessage(ChatColor.RED.toString()+"BLAAAAAARRRRRAAWWWWRRRR");
			((Wolf)sheep.getWorld().spawnCreature(sheep.getLocation(), EntityType.WOLF)).setAngry(true);
			sheep.remove();
		}
		player.updateInventory();

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
		else if(chance < 45){
			player.sendMessage(ChatColor.LIGHT_PURPLE.toString()+"Thank you!  Now my wife will lay me for sure!");
			villager.getWorld().spawnCreature(villager.getLocation(), EntityType.VILLAGER);
		}
		else if(chance < 70){
			player.sendMessage(ChatColor.LIGHT_PURPLE.toString()+"Oh!!! So shiny!! I can'... .... ugh... oh, MY HEART!!!");
			villager.setHealth(0);
		}
		else player.sendMessage(ChatColor.LIGHT_PURPLE.toString()+"Thank you very much!");
		player.updateInventory();
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
	private static DyeColor sheepRandomizer(){
		Random chance = new Random();
		int next = chance.nextInt(16);
		switch(next){
		case 0:return DyeColor.WHITE;
		case 1:return DyeColor.ORANGE;
		case 2:return DyeColor.MAGENTA;
		case 3:return DyeColor.LIGHT_BLUE;
		case 4:return DyeColor.YELLOW;
		case 5:return DyeColor.LIME;
		case 6:return DyeColor.PINK;
		case 7:return DyeColor.GRAY;
		case 8:return DyeColor.SILVER;
		case 9:return DyeColor.CYAN;
		case 10:return DyeColor.PURPLE;
		case 11:return DyeColor.BLUE;
		case 12:return DyeColor.BROWN;
		case 13:return DyeColor.GREEN;
		case 14:return DyeColor.RED;
		case 15:return DyeColor.BLACK ;
		}
		return DyeColor.WHITE;
	}
}
