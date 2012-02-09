package Lihad.Religion.Abilities;

import org.bukkit.entity.Player;

import Lihad.Religion.Religion;


public class SpellAoE {
	
	/**
	 * This class is dedicated to tower-AoE based instant-cast abilitites.
	 * 
	 * 
	 */
	
	
	public static Religion plugin;
	public SpellAoE(Religion instance) {
		plugin = instance;
	}
	public boolean handler(String spell, Player player){
		if(spell.equalsIgnoreCase("chickenrain")) return chickenRain(player);
		if(spell.equalsIgnoreCase("heal")) return heal(player);
		if(spell.equalsIgnoreCase("mobbegone")) return mobbegone(player);

		return false;
	}

	private boolean mobbegone(Player player) {
		// TODO Auto-generated method stub
		return true;
	}

	private boolean heal(Player player) {
		// TODO Auto-generated method stub
		return true;
	}

	private boolean chickenRain(Player player) {
		
		return true;
	}
}
