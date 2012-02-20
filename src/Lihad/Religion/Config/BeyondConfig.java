package Lihad.Religion.Config;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import Lihad.Religion.Religion;

public class BeyondConfig {
	public static Religion plugin;


	public BeyondConfig(Religion instance) {
		plugin = instance;
	}
	
	//get Functions
	public static int getMemberBonus(){
		return BeyondConfigReader.getInt("MemberBonus");
	}
	public static int getStorableItemid(){
		return BeyondConfigReader.getInt("StorableItem");
	}
	public static int getMaximumAoE(){
		return BeyondConfigReader.getInt("MaximumAoe");
	}
	public static Location getAhkmedLocation(){
		String[] array;
		String string = BeyondConfigReader.getString("Ahkmed");
		array = string.split(",");
		Location location = new Location(plugin.getServer().getWorld(array[3]), Integer.parseInt(array[0]), Integer.parseInt(array[1]), Integer.parseInt(array[2]));
		return location;	
		}
	//has Functions
	//set Functions
	//delete Functions
}
