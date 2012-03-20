package Lihad.Religion.Bosses;


import java.util.List;
import java.util.Random;

import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Wolf;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import Lihad.Religion.Religion;


public abstract class Ahkmed implements PigZombie {
	public static Religion plugin;
	
	public static EntityType getEntityType = EntityType.PIG_ZOMBIE;
	public static int getBaseHealth = 10000;
	

	
	
    public Ahkmed(Religion instance) {
        plugin = instance;
    }
    
    public static void setSpawnBossPotionEffects(LivingEntity entity){
		entity.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 72000, 2));
		entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 72000, 2));
		entity.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 72000, 1));
    }
    public static void randomEventRunner(PigZombie ahkmed){
    	Random rnd = new Random();
    	int next = rnd.nextInt(100);
    	if(next<10)triggerSpeedFaster(ahkmed);
    	else if(next <30)triggerDamResist(ahkmed);
    	else if(next <60)triggerSpeed(ahkmed);
    	else if(next <100)triggerWolf(ahkmed);
    }
    private static void triggerWolf(PigZombie ahkmed){
    	for(int i =0; i<20;i++){
    		((Wolf)ahkmed.getWorld().spawnCreature(ahkmed.getLocation(), EntityType.WOLF)).setAngry(true);
    	}
    }
    private static void triggerSpeed(PigZombie ahkmed){
    	ahkmed.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 5));
    }
    private static void triggerDamResist(PigZombie ahkmed){
    	ahkmed.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 5));
    }
    private static void triggerSpeedFaster(PigZombie ahkmed){
    	ahkmed.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 200, 8));
    }
    
    

	private void stageFire(EntityDamageEvent event){
		LivingEntity boss = ((LivingEntity)event.getEntity());
		if(Bosses.bossHealthMap.get(boss) < 2000){
			boss.setFireTicks(60);
			List<Entity> entities = boss.getNearbyEntities(5, 2, 5);
			for(int i = 0;i<entities.size();i++){
				entities.get(i).setFireTicks(60);
			}
		}
	}

}
