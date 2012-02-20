package Lihad.Religion.Bosses;

import org.bukkit.Location;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import Lihad.Religion.Religion;

public class Bosses {
	public static Religion plugin;
	
	public static LivingEntity boss;
	public static int bossHealth;
	public static boolean exist = false;
	
	public Bosses(Religion instance) {
		plugin = instance;
	}
	
	public void spawnBoss(Location location){
		boss = location.getWorld().spawnCreature(location, CreatureType.PIG_ZOMBIE);
		boss.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 10000, 2));
		boss.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10000, 2));
		bossHealth = 20000;
		exist = true;
	}
}



