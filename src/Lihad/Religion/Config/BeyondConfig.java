package Lihad.Religion.Config;

import org.bukkit.entity.Player;

import Lihad.Religion.Religion;

public class BeyondConfig {
	public static Religion plugin;


	public BeyondConfig(Religion instance) {
		plugin = instance;
	}
	
	//get Functions
	public int getMemberBonus(){
		return BeyondConfigReader.getInt("MemberBonus");
	}
	public int getStorableItemid(){
		return BeyondConfigReader.getInt("StorableItem");
	}
	public int getMaximumAoE(){
		return BeyondConfigReader.getInt("MaximumAoe");
	}
	//has Functions
	//set Functions
	//delete Functions
}
