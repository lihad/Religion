package Lihad.Religion.Abilities;

import org.bukkit.entity.Player;

import Lihad.Religion.Religion;

public class SpellAoE {
	public static Religion plugin;


	public SpellAoE(Religion instance) {
		plugin = instance;
	}
	
	public boolean handler(String spell, Player player){
		return true;
	}
}
