package Lihad.Religion.Bosses;

import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import Lihad.Religion.Religion;

public class Bosses {
	public static Religion plugin;
	
	public static LivingEntity boss;
	public static Chunk chunk;
	public static int bossHealth;
	public static boolean exist = false;
	
	//AHKED TRIGGERS
	public boolean wolftrigger = false;
	public boolean powertrigger = false;
	public boolean powertrigger2 = false;
	
	public Bosses(Religion instance) {
		plugin = instance;
	}
	
	public void spawnBoss(Location location){
		boss = location.getWorld().spawnCreature(location, CreatureType.PIG_ZOMBIE);
		boss.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 10000, 2));
		boss.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10000, 2));
		bossHealth = 20000;
		//exist = true;
	}
	public void healthDeplete(EntityDamageEvent event){
		if(boss.getHealth() >= (bossHealth/1000.0))boss.damage(1);
		//else boss.damage(0);
		bossHealth = bossHealth-event.getDamage();
	}
	public void healthDepleteByEntity(EntityDamageByEntityEvent event){
		if(boss.getHealth() >= (bossHealth/1000.0)){
			boss.damage(1, event.getDamager());
			bossHealth = bossHealth-event.getDamage();
		}else{
			boss.damage(0, event.getDamager());
			bossHealth = bossHealth-event.getDamage();
		}
		if(Bosses.boss.getHealth() == 0){
			//Bosses.exist = false;
			Bosses.boss.getWorld().dropItemNaturally(Bosses.boss.getLocation(), new ItemStack(Material.GOLD_INGOT, 1));
			Bosses.boss.remove();
		}
	}


	
	//
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// "Ahkmed"
	//
	public void triggersAhkmed(EntityDamageEvent event){
		stageWolf(event);
		stagePower(event);
		stageFire(event);
	}
	public void stageWolf(EntityDamageEvent event){
		if(bossHealth%100 == 0)wolftrigger = true;
		else if(wolftrigger){
			for(int i =0; i<10;i++){
				((Wolf)event.getEntity().getWorld().spawnCreature(event.getEntity().getLocation(), CreatureType.WOLF)).setAngry(true);
			}
			wolftrigger = false;
		}
	}
	public void stagePower(EntityDamageEvent event){
		if(bossHealth < 8000) powertrigger = true;
		else if(powertrigger){
			boss.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10000, 5));
		}
		if(bossHealth < 5000) powertrigger2 = true;
		else if(powertrigger2){
			boss.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 10000, 4));
		}
	}
	public void stageFire(EntityDamageEvent event){
		if(bossHealth < 2000){
			boss.setFireTicks(60);
			List<Entity> entities = boss.getNearbyEntities(5, 2, 5);
			for(int i = 0;i<entities.size();i++){
				entities.get(i).setFireTicks(60);
			}
		}
	}

}



