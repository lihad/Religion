package Lihad.Religion.Util;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionType;
import org.bukkit.potion.Potion.Tier;

import de.diddiz.LogBlock.BlockChange;
import de.diddiz.LogBlock.QueryParams;
import de.diddiz.LogBlock.QueryParams.BlockChangeType;

import Lihad.Religion.Religion;
import Lihad.Religion.Information.BeyondInfo;

/**
 * 
 * Utility class, holds static convenience methods
 * 
 * @author Lihad
 * @author Joren
 *
 */
public class BeyondUtil {

	/**
	 * Broadcasts a message to all players who are part of specified Tower
	 * @param towerName - name of the target Tower
	 * @param message - the message to be broadcast
	 */
	public static void towerBroadcast(String towerName, String message)
	{
		List<Player> players = BeyondInfo.getTowerPlayers(towerName);
		for (Player player: players)
		{
			player.sendMessage(message);
		}
	}
	
	/**
	 * Broadcasts a message to all players who are part of specified Religion
	 * @param religionName - name of the target religion
	 * @param message - the message to be broadcast
	 */
	public static void religionBroadcast(String religionName, String message)
	{
		List<Player> players = BeyondInfo.getReligionPlayers(religionName);
		for (Player player: players)
		{
			player.sendMessage(message);
		}
	}
	
	/**
	 * Compares two locations to see if they share the same block.
	 * 
	 * @param a - Location to be compared with b
	 * @param b - Location to be compared with a
	 * @return true if they share the same block, false if not, false if either Location is null
	 */
	
	public static boolean isSameBlock(Location a, Location b)
	{
		return (a!=null && b!=null && a.getBlock().equals(b.getBlock()));
	}
	
	/**
	 *  Returns specified ChatColor for user display based on their religion
	 * 
	 * @param player
	 * @param towrname
	 * @return Returns a string for the ChatColor in form "ChatColor.<arg>.toString()"
	 */
	public static String getChatColor(Player player, String towername){
		String color = ChatColor.WHITE.toString();
		if(BeyondInfo.getReligion(player) == null) color = ChatColor.WHITE.toString();
		else if(towername == null) color = ChatColor.WHITE.toString();
		else if(towername.equals("null")) color = ChatColor.WHITE.toString();
		else if(BeyondInfo.getReligion(towername).equals(BeyondInfo.getReligion(player))) color = ChatColor.GREEN.toString();
		else color = ChatColor.RED.toString();
		return color;
	}
	
	/**
	 * 
	 * @param chest
	 * @param material
	 * @return Returns amount of specified material in specified chest
	 */
	public static int getChestAmount(Chest chest, Material material){
		int amount = 0;
		for(int i=0; i<chest.getInventory().getSize(); i++){
			if(chest.getInventory().getItem(i).getType() == material){
				amount = amount+chest.getInventory().getItem(i).getAmount();
			}
		}
		return amount;
	}
	
	/**
	 * 
	 * Checks to see if a location is next to another block. z,x ONLY
	 */
	public static boolean isNextTo(Location reference, Location variable){
		variable.setX(variable.getBlockX()+1);
		if(reference.equals(variable)){
			return true;
		}
		variable.setX(variable.getBlockX()-2);
		if(reference.equals(variable)){
			return true;
		}
		variable.setX(variable.getBlockX()+1);
		variable.setZ(variable.getBlockZ()+1);
		if(reference.equals(variable)){
			return true;
		}
		variable.setZ(variable.getBlockZ()-2);
		if(reference.equals(variable)){
			return true;
		}
		return false;
		
	}
	public static Location randomTowerLocation(String towername){
		int influence = (int) BeyondInfo.getTowerInfluence(towername);
		int towerX = BeyondInfo.getTowerLocation(towername).getBlockX();
		int towerZ = BeyondInfo.getTowerLocation(towername).getBlockZ();
		int randomNum = -influence + (int)(Math.random() * ((influence - -influence) + 1));
		int randomNum2 = -influence + (int)(Math.random() * ((influence - -influence) + 1));

		
		
		return new Location(BeyondInfo.getTowerLocation(towername).getWorld(),towerX+randomNum,127,towerZ+randomNum2);
	}
	public static boolean timestampReference(String towername){
		if((System.currentTimeMillis()-BeyondInfo.getTimestamp(towername)) > 86400000L) return true;
		return false;
	}
	
	public static int rarity(ItemStack stack){
		int levelvalue = 0;
		int itemvalue = 0;
		for(int i=0;i<stack.getEnchantments().keySet().size();i++){
			levelvalue = levelvalue + stack.getEnchantmentLevel((Enchantment)stack.getEnchantments().keySet().toArray()[i]);
		}
		if(stack.getType() == Material.LEATHER_BOOTS
				|| stack.getType() == Material.LEATHER_CHESTPLATE
				|| stack.getType() == Material.LEATHER_HELMET
				|| stack.getType() == Material.LEATHER_LEGGINGS
				|| stack.getType() == Material.WOOD_AXE
				|| stack.getType() == Material.WOOD_SPADE
				|| stack.getType() == Material.WOOD_PICKAXE
				|| stack.getType() == Material.WOOD_SWORD)
			itemvalue = 60;
		else if(stack.getType() == Material.STONE_PICKAXE
				|| stack.getType() == Material.STONE_SPADE
				|| stack.getType() == Material.STONE_AXE
				|| stack.getType() == Material.STONE_SWORD)
			itemvalue = 80;
		else if(stack.getType() == Material.GOLD_BOOTS
				|| stack.getType() == Material.GOLD_CHESTPLATE
				|| stack.getType() == Material.GOLD_HELMET
				|| stack.getType() == Material.GOLD_LEGGINGS
				|| stack.getType() == Material.GOLD_AXE
				|| stack.getType() == Material.GOLD_SPADE
				|| stack.getType() == Material.GOLD_PICKAXE
				|| stack.getType() == Material.GOLD_SWORD)
			itemvalue = 100;
		else if(stack.getType() == Material.IRON_BOOTS
				|| stack.getType() == Material.IRON_CHESTPLATE
				|| stack.getType() == Material.IRON_HELMET
				|| stack.getType() == Material.IRON_LEGGINGS
				|| stack.getType() == Material.IRON_AXE
				|| stack.getType() == Material.IRON_SPADE
				|| stack.getType() == Material.IRON_PICKAXE
				|| stack.getType() == Material.IRON_SWORD)
			itemvalue = 120;
		else if(stack.getType() == Material.DIAMOND_BOOTS
				|| stack.getType() == Material.DIAMOND_CHESTPLATE
				|| stack.getType() == Material.DIAMOND_HELMET
				|| stack.getType() == Material.DIAMOND_LEGGINGS
				|| stack.getType() == Material.DIAMOND_AXE
				|| stack.getType() == Material.DIAMOND_SPADE
				|| stack.getType() == Material.DIAMOND_PICKAXE
				|| stack.getType() == Material.DIAMOND_SWORD)
			itemvalue = 140;



		return (levelvalue+itemvalue);
	}
	public static String getColorOfRarity(double index){
		if(index >= 180)return ChatColor.DARK_RED.toString();
		else if(index >= 160)return ChatColor.RED.toString();
		else if(index >= 140)return ChatColor.LIGHT_PURPLE.toString();
		else if(index >= 120)return ChatColor.BLUE.toString();
		else if(index >= 100)return ChatColor.GREEN.toString();
		else if(index >= 80)return ChatColor.YELLOW.toString();
		else return ChatColor.GRAY.toString();
	}
	public static String getColorOfTotalRarity(int index){
		if(index >= 1000)return ChatColor.DARK_RED.toString();
		else if(index >= 800)return ChatColor.RED.toString();
		else if(index >= 700)return ChatColor.LIGHT_PURPLE.toString();
		else if(index >= 600)return ChatColor.BLUE.toString();
		else if(index >= 500)return ChatColor.GREEN.toString();
		else if(index >= 400)return ChatColor.YELLOW.toString();
		else return ChatColor.GRAY.toString();
	}
	public static String getColorOfLevel(int index){
		if(index >= 12)return ChatColor.DARK_RED.toString();
		else if(index >= 10)return ChatColor.RED.toString();
		else if(index >= 8)return ChatColor.LIGHT_PURPLE.toString();
		else if(index >= 6)return ChatColor.BLUE.toString();
		else if(index >= 4)return ChatColor.GREEN.toString();
		else if(index >= 2)return ChatColor.YELLOW.toString();
		else return ChatColor.GRAY.toString();
	}
	//
	//
	// Helper Functions
	//
	//
	
	
	public static int calculator(Player player){
		Random chance = new Random();
		int next = chance.nextInt(1000);
		int playerlevel = player.getLevel()/10;
		if(playerlevel > 10)playerlevel = 10;
		return (next - (playerlevel*3));
	}
	public static PotionType potionTypeRandomizer(){
		Random chance = new Random();
		int next = chance.nextInt(9);
		switch(next){
		case 0: return PotionType.SPEED;
		case 1: return PotionType.SLOWNESS;
		case 2: return PotionType.FIRE_RESISTANCE;
		case 3: return PotionType.REGEN;
		case 4: return PotionType.INSTANT_DAMAGE;
		case 5: return PotionType.INSTANT_HEAL;
		case 6: return PotionType.POISON;
		case 7: return PotionType.WEAKNESS;
		case 8: return PotionType.STRENGTH;
		}
		return PotionType.SPEED;
	}
	public static Tier potionTierRandomizer(){
		Random chance = new Random();
		int next = chance.nextInt(3);
		if(next < 2){
			return Tier.ONE;
		}else return Tier.TWO;
	}
	public static boolean potionSplashRandomizer(){
		Random chance = new Random();
		int next = chance.nextInt(2);
		if(next == 0)return true;
		else return false;
	}
	public static Material weaponTypeRandomizer(){
		Random chance = new Random();
		int next = chance.nextInt(100);
		if(next<10)return Material.DIAMOND_SWORD;
		else if(next<25)return Material.IRON_SWORD;
		else if(next<50)return Material.GOLD_SWORD;
		else return Material.WOOD_SWORD;
	}
	public static Enchantment weaponEnchantRandomizer(){
		Random chance = new Random();
		int next = chance.nextInt(6);
		switch(next){
		case 0: return Enchantment.LOOT_BONUS_MOBS;
		case 1: return Enchantment.KNOCKBACK;
		case 2: return Enchantment.FIRE_ASPECT;
		case 3: return Enchantment.DAMAGE_UNDEAD;
		case 4: return Enchantment.DAMAGE_ARTHROPODS;
		case 5: return Enchantment.DAMAGE_ALL;
		}
		return Enchantment.DAMAGE_ALL;
	}
	public static int weaponLevelRandomizer(){
		Random chance = new Random();
		int next = chance.nextInt(100);
		if(next<1)return 10;
		else if(next<3)return 9;
		else if(next<6)return 8;
		else if(next<10)return 7;
		else if(next<15)return 6;
		else if(next<20)return 5;
		else if(next<30)return 4;
		else if(next<40)return 3;
		else if(next<50)return 2;
		else return 1;
	}
	public static Material armorTypeRandomizer(){
		Random chance = new Random();
		int next = chance.nextInt(100);
		if(next<2)return Material.DIAMOND_CHESTPLATE;
		else if(next<5)return Material.DIAMOND_BOOTS;
		else if(next<7)return Material.DIAMOND_HELMET;
		else if(next<10)return Material.DIAMOND_LEGGINGS;
		else if(next<13)return Material.IRON_CHESTPLATE;
		else if(next<17)return Material.IRON_BOOTS;
		else if(next<21)return Material.IRON_HELMET;
		else if(next<25)return Material.IRON_LEGGINGS;
		else if(next<29)return Material.GOLD_CHESTPLATE;
		else if(next<36)return Material.GOLD_BOOTS;
		else if(next<43)return Material.GOLD_HELMET;
		else if(next<50)return Material.GOLD_LEGGINGS;
		else if(next<60)return Material.LEATHER_CHESTPLATE;
		else if(next<73)return Material.LEATHER_BOOTS;
		else if(next<86)return Material.LEATHER_HELMET;
		else return Material.LEATHER_LEGGINGS;
	}
	public static Enchantment armorEnchantRandomizer(){
		Random chance = new Random();
		int next = chance.nextInt(4);
		switch(next){
		case 0: return Enchantment.PROTECTION_FIRE;
		case 1: return Enchantment.PROTECTION_PROJECTILE;
		case 2: return Enchantment.PROTECTION_ENVIRONMENTAL;
		case 3: return Enchantment.PROTECTION_EXPLOSIONS;
		}
		return Enchantment.PROTECTION_EXPLOSIONS;
	}
	public static int armorLevelRandomizer(){
		Random chance = new Random();
		int next = chance.nextInt(100);
		if(next<1)return 10;
		else if(next<3)return 9;
		else if(next<6)return 8;
		else if(next<10)return 7;
		else if(next<15)return 6;
		else if(next<20)return 5;
		else if(next<30)return 4;
		else if(next<40)return 3;
		else if(next<50)return 2;
		else return 1;
	}
	public static Material toolTypeRandomizer(){
		Random chance = new Random();
		int next = chance.nextInt(100);
		if(next<4)return Material.DIAMOND_AXE;
		else if(next<7)return Material.DIAMOND_PICKAXE;
		else if(next<10)return Material.DIAMOND_SPADE;
		else if(next<15)return Material.IRON_AXE;
		else if(next<20)return Material.IRON_PICKAXE;
		else if(next<25)return Material.IRON_SPADE;
		else if(next<29)return Material.GOLD_AXE;
		else if(next<33)return Material.GOLD_PICKAXE;
		else if(next<37)return Material.GOLD_SPADE;
		else if(next<41)return Material.STONE_AXE;
		else if(next<45)return Material.STONE_PICKAXE;
		else if(next<50)return Material.STONE_SPADE;
		else if(next<60)return Material.WOOD_PICKAXE;
		else if(next<85)return Material.WOOD_AXE;
		else return Material.WOOD_SPADE;
	}
	public static Enchantment toolEnchantRandomizer(){
		Random chance = new Random();
		int next = chance.nextInt(4);
		switch(next){
		case 0: return Enchantment.DURABILITY;
		case 1: return Enchantment.LOOT_BONUS_BLOCKS;
		case 2: return Enchantment.DIG_SPEED;
		case 3: return Enchantment.SILK_TOUCH;
		}
		return Enchantment.PROTECTION_EXPLOSIONS;
	}
	public static int toolLevelRandomizer(){
		Random chance = new Random();
		int next = chance.nextInt(100);
		if(next<1)return 10;
		else if(next<3)return 9;
		else if(next<6)return 8;
		else if(next<10)return 7;
		else if(next<15)return 6;
		else if(next<20)return 5;
		else if(next<30)return 4;
		else if(next<40)return 3;
		else if(next<50)return 2;
		else return 1;
	}
	
	/**
	 * @param block - Location where player wants to place a tower
	 */
	public static boolean isActiveArea(Player iCanHasTowerPlz, String religion, Block block)
	{
		//Player parameter will be used when we add some code for them to invite others to join the new tower...?
		
		QueryParams params = new QueryParams(Religion.logBlock);
		List<Player> coReligionists = BeyondInfo.getReligionPlayers(religion);
		Iterator<Player> iter = coReligionists.iterator();
		params.players = new ArrayList<String>();
		while(iter.hasNext())
			params.players.add(iter.next().getName());
		params.players.add("\"Fire\"");
		params.players.add("\"Enderman\"");
		params.players.add("\"Water\"");
		params.players.add("\"WaterFlow\"");
		params.players.add("\"Lava\"");
		params.players.add("\"LavaFlow\"");
		params.players.add("\"Creeper\"");
		params.players.add("\"Environment\"");

		params.bct = BlockChangeType.CREATED;
		params.limit = 500;
		params.since = 20160;
		params.world = block.getWorld();
		params.needDate = true;
		params.needType = true;
		params.needData = true;
		params.needPlayer = true;
		params.excludePlayersMode = true;
		try
		{
			if (Religion.logBlock.getBlockChanges(params).size() > 0)
				return false;
		} catch (SQLException e)
		{
			Religion.severe("LogBlock exception: " + e.getLocalizedMessage());
			e.printStackTrace();
		} catch (NullPointerException e)
		{
			Religion.warning("LogBlock returned a null, probably means no changes nearby: " + e.getLocalizedMessage());
			e.printStackTrace();
		}
		return true;
	}
}
