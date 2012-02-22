package Lihad.Religion.Bosses;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import Lihad.Religion.Config.BeyondConfig;

public class Bosses {
	public static Religion plugin;
	
    public static Map<Location, String> configBossMap = BeyondConfig.getBossLocation();
    public static Map<LivingEntity, Integer> bossHealthMap = new HashMap<LivingEntity, Integer>();
    public static Map<LivingEntity, Boolean> bossExistMap = new HashMap<LivingEntity, Boolean>();
    public static Map<String, LivingEntity> bossNameMap = new HashMap<String, LivingEntity>();
	
	//AHKED TRIGGERS
	public boolean wolftrigger = false;
	public boolean powertrigger = false;
	public boolean powertrigger2 = false;
	
	public Bosses(Religion instance) {
		plugin = instance;
	}
	public void bossInit(){
		for(int i = 0;i<configBossMap.size();i++){
			spawnBoss((Location)configBossMap.keySet().toArray()[i], configBossMap.get((Location)configBossMap.keySet().toArray()[i]));
		}
	}
	public void spawnBoss(Location location, String bossname){
		
		LivingEntity boss = location.getWorld().spawnCreature(location, getCreatureType(bossname));
		loadTriggers(bossname);
		setSpawnBossPotionEffects(boss, bossname);
		bossHealthMap.put(boss, getBossHealth(bossname));
		bossExistMap.put(boss, true);
		bossNameMap.put(bossname, boss);
	}
	private void loadTriggers(String bossname){
		if(bossname.equals("Ahkmed")){
			wolftrigger = false;
			powertrigger = false;
			powertrigger2 = false;
		}
	}
	private CreatureType getCreatureType(String bossname){
		if(bossname.equals("Ahkmed"))return CreatureType.PIG_ZOMBIE;
		else return null;
	}
	private int getBossHealth(String bossname){
		if(bossname.equals("Ahkmed"))return 20000;
		else return 0;
	}
	private void setSpawnBossPotionEffects(LivingEntity entity, String bossname){
		if(bossname.equals("Ahkmed")){
			entity.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 72000, 2));
			entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 72000, 2));
			entity.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 72000, 4));
		}
	}
	
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////


	public void healthDeplete(EntityDamageEvent event){
		LivingEntity boss = ((LivingEntity)event.getEntity());
		if(boss.getHealth() >= (bossHealthMap.get(boss)/1000.0)){
			System.out.println("FIRE NORMAL "+boss.getHealth()+" to "+(bossHealthMap.get(boss)/1000.0));
			boss.setHealth(boss.getHealth()-1);
			//flinch add?
		}
		bossHealthMap.put(boss, bossHealthMap.get(boss)-event.getDamage());
	}
	public void healthDepleteByEntity(EntityDamageByEntityEvent event){
		LivingEntity boss = ((LivingEntity)event.getEntity());
		if((double)boss.getHealth() >= (bossHealthMap.get(boss)/1000.0)){
			System.out.println("FIRE ATTACK "+boss.getHealth()+" to "+(bossHealthMap.get(boss)/1000.0));
			((PigZombie)boss).setTarget((LivingEntity) event.getDamager());
			boss.setHealth(boss.getHealth()-1);
			bossHealthMap.put(boss, bossHealthMap.get(boss)-event.getDamage());
			if(event.getDamager() instanceof Player){
				((Player)event.getDamager()).sendMessage("Ahkmed has dropped below "+bossHealthMap.get(boss)+" health!!!");
			}

		}else{
			((PigZombie)boss).setTarget((LivingEntity) event.getDamager());
			bossHealthMap.put(boss, bossHealthMap.get(boss)-event.getDamage());
			if(event.getDamager() instanceof Player){
				bossHealthMap.put(boss, bossHealthMap.get(boss)-enchantDamageCalculator((Player)event.getDamager(), event.getEntity()));
			}
		}
		if(boss.getHealth() == 0){
			ItemStack items = new ItemStack(Material.DIAMOND_SWORD, 1);
			items.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 8);
			items.addUnsafeEnchantment(Enchantment.LOOT_BONUS_MOBS, 4);

			boss.getWorld().dropItemNaturally(boss.getLocation(),items);
			boss.remove();
			//bossHealthMap.remove(boss);
		}
	}
	public void damageTriggers(EntityDamageEvent event, String bossname){
		if(bossname.equals("Ahkmed")){
			stageWolf(event);
			stagePower(event);
			stageFire(event);
		}
	}
	
	//
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// "Ahkmed" Triggers
	//
	public void stageWolf(EntityDamageEvent event){
		LivingEntity boss = ((LivingEntity)event.getEntity());
		if(bossHealthMap.get(boss)%50 == 0)wolftrigger = true;
		else if(wolftrigger){
			for(int i =0; i<10;i++){
				((Wolf)event.getEntity().getWorld().spawnCreature(event.getEntity().getLocation(), CreatureType.WOLF)).setAngry(true);
			}
			wolftrigger = false;
		}
	}
	public void stagePower(EntityDamageEvent event){
		LivingEntity boss = ((LivingEntity)event.getEntity());
		if(bossHealthMap.get(boss) < 8000) powertrigger = true;
		else if(powertrigger){
			boss.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 72000, 5));
		}
		if(bossHealthMap.get(boss) < 5000) powertrigger2 = true;
		else if(powertrigger2){
			boss.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 72000, 4));
		}
	}
	public void stageFire(EntityDamageEvent event){
		LivingEntity boss = ((LivingEntity)event.getEntity());
		if(bossHealthMap.get(boss) < 2000){
			boss.setFireTicks(60);
			List<Entity> entities = boss.getNearbyEntities(5, 2, 5);
			for(int i = 0;i<entities.size();i++){
				entities.get(i).setFireTicks(60);
			}
		}
	}
	//
	//
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
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



