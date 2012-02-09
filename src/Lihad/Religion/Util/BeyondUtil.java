package Lihad.Religion.Util;

import java.util.List;

import org.bukkit.Location;
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
}
