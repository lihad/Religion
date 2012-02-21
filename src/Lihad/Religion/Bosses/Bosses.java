package Lihad.Religion.Bosses;

import java.util.List;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Silverfish;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Spider;
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
		wolftrigger = false;
		powertrigger = false;
		powertrigger2 = false;
		boss = location.getWorld().spawnCreature(location, CreatureType.PIG_ZOMBIE);
		boss.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 72000, 2));
		boss.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 72000, 2));
		bossHealth = 20000;
		exist = true;
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
			if(event.getDamager() instanceof Player){
				((Player)event.getDamager()).sendMessage("Ahkmed has dropped below "+bossHealth+" health!!!");
			}

		}else{
			boss.damage(0, event.getDamager());
			bossHealth = bossHealth-(event.getDamage());
			if(event.getDamager() instanceof Player){

				bossHealth = bossHealth-enchantDamageCalculator((Player)event.getDamager(), event.getEntity());
			}
		}
		if(Bosses.boss.getHealth() == 0){
			ItemStack items = new ItemStack(Material.DIAMOND_SWORD, 1);
			items.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 8);
			items.addUnsafeEnchantment(Enchantment.LOOT_BONUS_MOBS, 4);

			Bosses.boss.getWorld().dropItemNaturally(Bosses.boss.getLocation(),items);
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
			boss.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 72000, 5));
		}
		if(bossHealth < 5000) powertrigger2 = true;
		else if(powertrigger2){
			boss.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 72000, 4));
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
	
	
	public int enchantDamageCalculator(Player player, Entity entity){

		int var = 0;
		if(player.getItemInHand().getEnchantments() != null){

			for(int i = 0; i<player.getItemInHand().getEnchantments().keySet().size();i++){
				Enchantment enchantment = (Enchantment) player.getItemInHand().getEnchantments().keySet().toArray()[i];
				int level = player.getItemInHand().getEnchantmentLevel(enchantment);
				if(enchantment.equals(Enchantment.DAMAGE_ALL)){

					for(int j=0;j<level;j++){
						var = var+randomizer(3);
					}
				}
				if(enchantment == Enchantment.DAMAGE_UNDEAD){
					if(entity instanceof Zombie || entity instanceof PigZombie || entity instanceof Skeleton){
						for(int j=0;j<level;j++){
							var = var+randomizer(4);
						}					}
				}
				if(enchantment == Enchantment.DAMAGE_ARTHROPODS){
					if(entity instanceof Spider || entity instanceof CaveSpider || entity instanceof Silverfish){
						for(int j=0;j<level;j++){
							var = var+randomizer(4);
						}
					}
				}
				if(enchantment == Enchantment.ARROW_DAMAGE){
					for(int j=0;j<level;j++){
						var = var+randomizer(3);
					}
				}
			}
		}
		return var;
	}
	public int randomizer(int max){
		Random random = new Random();
		int next = random.nextInt(max)+1;
		return next;
	}

}



