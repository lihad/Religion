package Lihad.Religion.Bosses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.Creature;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Creeper;
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
import org.bukkit.util.Vector;

import Lihad.Religion.Religion;
import Lihad.Religion.Config.BeyondConfig;
import Lihad.Religion.Util.BeyondUtil;

public class Bosses {
	public static Religion plugin;
	
    public static Map<Location, String> configBossMap = BeyondConfig.getBossLocation();
    public static Map<LivingEntity, Integer> bossHealthMap = new HashMap<LivingEntity, Integer>();
    public static Map<LivingEntity, Integer> bossMaxHealthMap = new HashMap<LivingEntity, Integer>();
    public static Map<LivingEntity, Boolean> bossExistMap = new HashMap<LivingEntity, Boolean>();
    public static Map<String, LivingEntity> bossNameMap = new HashMap<String, LivingEntity>();
	
	//AHKED TRIGGERS
	public boolean wolftrigger = false;
	public boolean powertrigger = false;
	public boolean powertrigger2 = false;
	
	//XTAL TRIGGERS
	public boolean xtalMobTrigger = false;
	
	public Bosses(Religion instance) {
		plugin = instance;
	}
	public void bossInit(){
		for(int i = 0;i<configBossMap.size();i++){
			spawnBoss((Location)configBossMap.keySet().toArray()[i], configBossMap.get((Location)configBossMap.keySet().toArray()[i]));
		}
	}
	public void spawnBoss(Location location, String bossname){
		
		location.getBlock().setTypeId(0);
		LivingEntity boss = location.getWorld().spawnCreature(location, getCreatureType(bossname));
		loadTriggers(bossname);
		setSpawnBossPotionEffects(boss, bossname);
		bossHealthMap.put(boss, getBossHealth(bossname));
		bossMaxHealthMap.put(boss, getBossHealth(bossname));
		bossExistMap.put(boss, true);
		bossNameMap.put(bossname, boss);
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////  BOSS CREATION AND TRIGGER LOADER
	private void loadTriggers(String bossname){
		if(bossname.equals("Ahkmed")){
			wolftrigger = false;
			powertrigger = false;
			powertrigger2 = false;
		}
		if(bossname.equals("Xtal")){
			xtalMobTrigger = false;
		}
	}
	private CreatureType getCreatureType(String bossname){
		if(bossname.equals("Ahkmed"))return CreatureType.PIG_ZOMBIE;
		if(bossname.equals("Xtal"))return CreatureType.CREEPER;

		else return null;
	}
	private int getBossHealth(String bossname){
		if(bossname.equals("Ahkmed"))return 10000;
		if(bossname.equals("Xtal"))return 50000;
		else return 0;
	}
	private void setSpawnBossPotionEffects(LivingEntity entity, String bossname){
		if(bossname.equals("Ahkmed")){
			entity.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 72000, 2));
			entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 72000, 2));
			entity.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 72000, 1));
		}
		if(bossname.equals("Xtal")){
			entity.getWorld().strikeLightningEffect(entity.getLocation());
			entity.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 72000, 5));
			entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 72000, 5));
			entity.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 72000, 1));
		}
		if(bossname.equals("Jamal")){
			entity.getWorld().strikeLightningEffect(entity.getLocation());
			entity.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 72000, 5));
			entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 72000, 5));
			entity.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 72000, 1));
		}
	}
	
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////  BOSS HEALTH HANDLER


	public void healthDeplete(EntityDamageEvent event){
		LivingEntity boss = ((LivingEntity)event.getEntity());
		if(boss.getHealth() >= (bossHealthMap.get(boss)/(bossMaxHealthMap.get(boss)/20))){
			System.out.println("FIRE NORMAL "+boss.getHealth()+" to "+(bossHealthMap.get(boss)/(bossMaxHealthMap.get(boss)/20)));
			boss.setHealth(boss.getHealth()-1);
			//flinch add?
		}
		bossHealthMap.put(boss, bossHealthMap.get(boss)-event.getDamage());
	}
	public void healthDepleteByEntity(EntityDamageByEntityEvent event){
		LivingEntity boss = ((LivingEntity)event.getEntity());
		if(event.getDamager() instanceof LivingEntity){
			if((double)boss.getHealth() >= (bossHealthMap.get(boss)/(bossMaxHealthMap.get(boss)/20))){
				System.out.println("FIRE ATTACK "+boss.getHealth()+" to "+(bossHealthMap.get(boss)/(bossMaxHealthMap.get(boss)/20)));
				((Creature)boss).setTarget((LivingEntity) event.getDamager());
				boss.setHealth(boss.getHealth()-1);
				bossHealthMap.put(boss, bossHealthMap.get(boss)-event.getDamage());
				if(event.getDamager() instanceof Player){
					((Player)event.getDamager()).sendMessage("Boss has dropped below "+bossHealthMap.get(boss)+" health!!!");
				}

			}else{
				((Creature)boss).setTarget((LivingEntity) event.getDamager());
				bossHealthMap.put(boss, bossHealthMap.get(boss)-event.getDamage());
				if(event.getDamager() instanceof Player){
					bossHealthMap.put(boss, bossHealthMap.get(boss)-enchantDamageCalculator((Player)event.getDamager(), event.getEntity()));
				}
			}
		}
		if(boss.getHealth() == 0){
			Location location = null;
			if(bossNameMap.get("Ahkmed").equals((LivingEntity)event.getEntity())){
				location = BeyondConfig.getBossSimpleLocation("Ahkmed");
			}else if(bossNameMap.get("Xtal").equals((LivingEntity)event.getEntity())){
				location = BeyondConfig.getBossSimpleLocation("Xtal");
			}
			location.getBlock().setType(Material.CHEST);
			Chest chest = (Chest) location.getBlock().getState();
			
			List<ItemStack> stacky = bossLootTable((LivingEntity) event.getEntity());
			for(int i=0; i<stacky.size();i++){
				chest.getInventory().addItem(stacky.get(i));
			}
			boss.remove();
			
			//bossHealthMap.remove(boss);
		}
	} 
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////  TRIGGER REDIRECTS
	
	public void damageTriggers(EntityDamageEvent event, String bossname){
		if(bossname.equals("Ahkmed")){
			stageWolf(event);
			stagePower(event);
			stageFire(event);
		}
		if(bossname.equals("Xtal")){
			stageBoom(event);
			stageBurn(event);
			stageRaid(event);
			xtalPushBack(event);
		}
	}
	
	//
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// "Ahkmed" Triggers
	//
	public void stageWolf(EntityDamageEvent event){
		LivingEntity boss = ((LivingEntity)event.getEntity());
		if(bossHealthMap.get(boss)%40 == 0)wolftrigger = true;
		else if(wolftrigger){
			for(int i =0; i<20;i++){
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
			boss.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 72000, 8));


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
	//
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// "Xtal" Triggers
	//
	public void xtalCharge(EntityDamageEvent event){
		if(bossHealthMap.get((LivingEntity)event.getEntity())%500 == 0){
			Entity entity = event.getEntity();
			List<Entity> entities = entity.getNearbyEntities(7, 50, 7);
			for(int i=0;i<entities.size();i++){
				if(entities.get(i) instanceof Player){
					Player player = (Player)entities.get(i);
					player.getLocation().getWorld().strikeLightning(player.getLocation());
					player.setFireTicks(100);
					player.sendMessage("Xtal destroyed your "+player.getItemInHand().getType().name()+"!");
					player.setItemInHand(null);
				}
			}
		}
	}
	public void xtalPushBack(EntityDamageEvent event){
		if(bossHealthMap.get((LivingEntity)event.getEntity())%45 == 0){
			Entity entity = event.getEntity();
			List<Entity> entities = entity.getNearbyEntities(5, 2, 5);
			for(int i = 0; i<entities.size();i++){
				Entity raw = entities.get(i);
				if(raw instanceof LivingEntity){
					LivingEntity living = ((LivingEntity)raw);
					Location locationEntity = living.getLocation();
					Location locationOrigin = entity.getLocation();
					living.damage(10);
					living.setVelocity(new Vector((locationEntity.getX()-locationOrigin.getX())/2, 1, (locationEntity.getZ()-locationOrigin.getZ())/2));

				}
			}
		}
	}
	public void stageBoom(EntityDamageEvent event){
		LivingEntity boss = ((LivingEntity)event.getEntity());
		if(bossHealthMap.get(boss)%30 == 0){
			boss.getWorld().strikeLightningEffect(boss.getLocation());
			List<Entity> entities = boss.getNearbyEntities(5, 20, 5);
			for(int i=0;i<entities.size();i++){
				if(entities.get(i) instanceof Player){
					Player player = (Player)entities.get(i);
					if(player.getFoodLevel() > 2)player.setFoodLevel(player.getFoodLevel()-2);
					player.damage(1);
					player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,100, 2));
				}
			}
		}
	}
	public void stageBurn(EntityDamageEvent event){
		LivingEntity boss = ((LivingEntity)event.getEntity());
		if(bossHealthMap.get(boss)%16 == 0){
			List<Entity> entities = boss.getNearbyEntities(2, 5, 2);
			for(int i=0;i<entities.size();i++){
				if(entities.get(i) instanceof Player){
					Player player = (Player)entities.get(i);
					player.setFireTicks(100);
				}
			}
		}
	}
	public void stageRaid(EntityDamageEvent event){
		LivingEntity boss = ((LivingEntity)event.getEntity());
		if(bossHealthMap.get(boss)%50 == 0)xtalMobTrigger = true;
		else if(xtalMobTrigger){
			for(int i =0; i<20;i++){
				Silverfish fish = ((Silverfish)event.getEntity().getWorld().spawnCreature(event.getEntity().getLocation(), CreatureType.SILVERFISH));
				fish.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10000, 6));
				fish.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 10000, 9));
			}
			xtalMobTrigger = false;
		}
	}
	

	//
	//
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	

	private List<ItemStack> bossLootTable(LivingEntity entity){
		int chance = randomizer(100);
		List<ItemStack> stack = bossRandomLootPump();
		stack.addAll(bossRandomLootPump());
		stack.addAll(bossRandomLootPump());
		stack.addAll(bossRandomLootPump());
		stack.addAll(bossRandomLootPump());

		if(Bosses.bossNameMap.get("Ahkmed").equals((LivingEntity)entity)){
			stack.add(new ItemStack(Material.GOLD_INGOT, chancizerForHotDrops()+1));
			if(chance<10){
				ItemStack items = new ItemStack(Material.DIAMOND_SWORD, 1);
				items.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 8);
				items.addUnsafeEnchantment(Enchantment.LOOT_BONUS_MOBS, 4);
				stack.add(items);
			}else if(chance<30){
				ItemStack items = new ItemStack(Material.IRON_BOOTS, 1);
				items.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 6);
				items.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 2);
				stack.add(items);
			}else if(chance<70){
				ItemStack items = new ItemStack(Material.DIAMOND_BLOCK, 1);
				stack.add(items);
			}
		}else if(Bosses.bossNameMap.get("Xtal").equals((LivingEntity)entity)){
			stack.add(new ItemStack(Material.GOLD_INGOT, chancizerForHotDrops()+1));
			if(chance<10){
				ItemStack items = new ItemStack(Material.DIAMOND_SWORD, 1);
				items.addUnsafeEnchantment(Enchantment.DAMAGE_UNDEAD, 10);
				items.addUnsafeEnchantment(Enchantment.LOOT_BONUS_MOBS, 5);
				items.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 2);
				stack.add(items);
			}else if(chance<30){
				ItemStack items = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
				items.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
				items.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, 10);
				stack.add(items);
			}else if(chance<70){
				ItemStack items = new ItemStack(Material.DIAMOND_BLOCK, 5);
				stack.add(items);
			}
		}
			
		return stack;
	}
	
	private List<ItemStack> bossRandomLootPump(){
		List<ItemStack> stacks = new ArrayList<ItemStack>();
		if(chancizerForHotDrops() == 0){
			ItemStack stack = new ItemStack(BeyondUtil.toolTypeRandomizer(), 1);
			stack.addUnsafeEnchantment(BeyondUtil.toolEnchantRandomizer(), BeyondUtil.toolLevelRandomizer());
			stacks.add(stack);
		}else if(chancizerForHotDrops() == 1){
			ItemStack stack = new ItemStack(BeyondUtil.armorTypeRandomizer(), 1);
			stack.addUnsafeEnchantment(BeyondUtil.armorEnchantRandomizer(), BeyondUtil.armorLevelRandomizer());
			stacks.add(stack);
		}else if(chancizerForHotDrops() == 2){
			ItemStack stack = new ItemStack(BeyondUtil.weaponTypeRandomizer(), 1);
			stack.addUnsafeEnchantment(BeyondUtil.weaponEnchantRandomizer(), BeyondUtil.weaponLevelRandomizer());
			stacks.add(stack);
		}else if(chancizerForHotDrops() == 3){
			ItemStack stack = new ItemStack(BeyondUtil.armorTypeRandomizer(), 1);
			stack.addUnsafeEnchantment(BeyondUtil.armorEnchantRandomizer(), BeyondUtil.armorLevelRandomizer());
			ItemStack stack2 = new ItemStack(BeyondUtil.toolTypeRandomizer(), 1);
			stack2.addUnsafeEnchantment(BeyondUtil.toolEnchantRandomizer(), BeyondUtil.toolLevelRandomizer());
			stacks.add(stack);
			stacks.add(stack2);
		}else if(chancizerForHotDrops() == 4){
			ItemStack stack = new ItemStack(BeyondUtil.toolTypeRandomizer(), 1);
			stack.addUnsafeEnchantment(BeyondUtil.toolEnchantRandomizer(), BeyondUtil.toolLevelRandomizer());
			ItemStack stack2 = new ItemStack(BeyondUtil.weaponTypeRandomizer(), 1);
			stack2.addUnsafeEnchantment(BeyondUtil.weaponEnchantRandomizer(), BeyondUtil.weaponLevelRandomizer());
			stacks.add(stack);
			stacks.add(stack2);
		}else if(chancizerForHotDrops() == 5){
			ItemStack stack = new ItemStack(BeyondUtil.armorTypeRandomizer(), 1);
			stack.addUnsafeEnchantment(BeyondUtil.armorEnchantRandomizer(), BeyondUtil.armorLevelRandomizer());
			ItemStack stack2 = new ItemStack(BeyondUtil.weaponTypeRandomizer(), 1);
			stack2.addUnsafeEnchantment(BeyondUtil.weaponEnchantRandomizer(), BeyondUtil.weaponLevelRandomizer());
			stacks.add(stack);
			stacks.add(stack2);
		}else{
			ItemStack stack = new ItemStack(BeyondUtil.toolTypeRandomizer(), 1);
			stack.addUnsafeEnchantment(BeyondUtil.toolEnchantRandomizer(), BeyondUtil.toolLevelRandomizer());
			ItemStack stack2 = new ItemStack(BeyondUtil.armorTypeRandomizer(), 1);
			stack2.addUnsafeEnchantment(BeyondUtil.armorEnchantRandomizer(), BeyondUtil.armorLevelRandomizer());
			ItemStack stack3 = new ItemStack(BeyondUtil.weaponTypeRandomizer(), 1);
			stack3.addUnsafeEnchantment(BeyondUtil.weaponEnchantRandomizer(), BeyondUtil.weaponLevelRandomizer());
			stacks.add(stack);
			stacks.add(stack2);
			stacks.add(stack3);
		}
		return stacks;

	}
	
	
	
	
	
	
	
	
	
	private int enchantDamageCalculator(Player player, Entity entity){

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
	private int randomizer(int max){
		Random random = new Random();
		int next = random.nextInt(max)+1;
		return next;
	}
	private int chancizerForHotDrops(){
		Random random = new Random();
		int next = random.nextInt(7);
		return next;
	}

}



