package Lihad.Religion.Config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import Lihad.Religion.Religion;
import Lihad.Religion.Information.BeyondInfoReader;

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
	public static Location getBossSimpleLocation(String bossname){
		String[] array;
		String string = BeyondConfigReader.getString("Bosses."+bossname);
		array = string.split(",");
		Location location = new Location(plugin.getServer().getWorld(array[3]), Integer.parseInt(array[0]), Integer.parseInt(array[1]), Integer.parseInt(array[2]));
		return location;	
	}
	public static Map<Location,String> getBossLocation(){
	    Map<Location, String> map = new HashMap<Location, String>();
		Set<String> names = BeyondConfigReader.getKeyList("Bosses");
		if (names != null)
			for(int i = 0; i<names.size(); i++){
				String[] array;
				String string = BeyondConfigReader.getString("Bosses."+(String)names.toArray()[i]);
				array = string.split(",");
				Location location = new Location(plugin.getServer().getWorld(array[3]), Integer.parseInt(array[0]), Integer.parseInt(array[1]), Integer.parseInt(array[2]));
				map.put(location, (String) names.toArray()[i]);
			}
		return map;
	}
	public static Set<String> getBosses(){
		return BeyondConfigReader.getKeyList("Bosses");
	}
	//has Functions
	//set Functions
	//delete Functions
}
