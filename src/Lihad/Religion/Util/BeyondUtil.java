package Lihad.Religion.Util;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;

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
}
