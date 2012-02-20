package Lihad.Religion.Listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.entity.SheepRegrowWoolEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.Potion.Tier;
import org.bukkit.potion.PotionType;

import Lihad.Religion.Religion;
import Lihad.Religion.Bosses.Bosses;
import Lihad.Religion.Information.BeyondInfo;
import Lihad.Religion.Util.BeyondUtil;

public class BeyondEntityListener extends EntityListener {
	public static Religion plugin;
	
	//AHKED TRIGGERS
	public boolean wolftrigger = false;
	public boolean powertrigger = false;
	public boolean powertrigger2 = false;

	
	
	public BeyondEntityListener(Religion instance) {
		plugin = instance;
	}
	public void onEntityDamage(EntityDamageEvent event){
		if(event.getEntity() instanceof Block){
			Block block = (Block)event.getEntity();
			if(block.getType() == Material.CHEST){
				for(int i=0;i<BeyondInfo.getTowersAll().size();i++){
					if(block.getLocation().equals(BeyondInfo.getTowerLocation(BeyondInfo.getTowersAll().get(i)))){
						event.setCancelled(true);
					}else if(BeyondInfo.getTrades(BeyondInfo.getTowersAll().get(i)) != null){
						for(int j=0;j<BeyondInfo.getTrades(BeyondInfo.getTowersAll().get(i)).size();j++){
							if(block.getLocation().equals(BeyondInfo.getTradeLocation(BeyondInfo.getTowersAll().get(i), BeyondInfo.getTrades(BeyondInfo.getTowersAll().get(i)).get(j)))){
								event.setCancelled(true);
							}
						}
					}
				}
			}
		}else if(event instanceof EntityDamageByEntityEvent)onEntityDamageByEntity((EntityDamageByEntityEvent)event);

		/**
		 * The following 'if' makes players that take damage in their own religions AoE take half.  Those who take damage in opposing religion AoE
		 * take +1 
		 */
		if(event.getEntity() instanceof Player){
			Player player = (Player)event.getEntity();
			if(BeyondInfo.getClosestValidTower(player.getLocation()) ==  null || BeyondInfo.getReligion(player) == null) return;
			else{
				if(BeyondInfo.getReligion(BeyondInfo.getClosestValidTower(player.getLocation())).equals(BeyondInfo.getReligion(player))){
					player.damage(event.getDamage()/2);
					event.setCancelled(true);
				}else{
					player.damage(event.getDamage()+1);
					event.setCancelled(true);
				}
			}
		}
		if(((LivingEntity)event.getEntity()).equals(Bosses.boss) && !(event instanceof EntityDamageByEntityEvent)){
			int health = Bosses.bossHealth;
			int rawHealth = Bosses.boss.getHealth();
			if(rawHealth >= (health/1000.0)){
				Bosses.boss.damage(1);
				Bosses.bossHealth = health-event.getDamage();
			}else{
				Bosses.boss.damage(0);
				Bosses.bossHealth = health-event.getDamage();
			}

			event.setCancelled(true);
		}
	}
	public void onEntityExplode(EntityExplodeEvent event){
		List<Block> blocklist = event.blockList();
		for(int b = 0;b<blocklist.size();b++){
			if(blocklist.get(b).getType() == Material.CHEST){
				for(int i=0;i<BeyondInfo.getTowersAll().size();i++){
					if(blocklist.get(b).getLocation().equals(BeyondInfo.getTowerLocation(BeyondInfo.getTowersAll().get(i)))){
						event.setCancelled(true);
					}else if(BeyondInfo.getTrades(BeyondInfo.getTowersAll().get(i)) != null){
						for(int j=0;j<BeyondInfo.getTrades(BeyondInfo.getTowersAll().get(i)).size();j++){
							if(blocklist.get(b).getLocation().equals(BeyondInfo.getTradeLocation(BeyondInfo.getTowersAll().get(i), BeyondInfo.getTrades(BeyondInfo.getTowersAll().get(i)).get(j)))){
								event.setCancelled(true);
							}
						}
					}
				}
			}
		}
	}
	// Left off here
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event){
		
		//AHKMED
		if(((LivingEntity)event.getEntity()).equals(Bosses.boss)){
			int health = Bosses.bossHealth;
			int rawHealth = Bosses.boss.getHealth();
			//WOLF STAGE
			if(health%20 == 0)wolftrigger = true;

			else if(wolftrigger){
				for(int i =0; i<10;i++){
					((Wolf)event.getEntity().getWorld().spawnCreature(event.getEntity().getLocation(), CreatureType.WOLF)).setAngry(true);
				}
				wolftrigger = false;
			}
			//POWER STAGE
			if(health < 8000) powertrigger = true;
			else if(powertrigger){
				Bosses.boss.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10000, 5));
			}
			if(health < 5000) powertrigger2 = true;
			else if(powertrigger2){
				Bosses.boss.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 10000, 4));
			}
			//FIRE STAGE
			if(health < 2000){
				Bosses.boss.setFireTicks(60);
				List<Entity> entities = Bosses.boss.getNearbyEntities(5, 2, 5);
				for(int i = 0;i<entities.size();i++){
					entities.get(i).setFireTicks(60);
				}
			}
			//
			if(rawHealth >= (health/1000.0)){
				Bosses.boss.damage(1, event.getDamager());
				Bosses.bossHealth = health-event.getDamage();
			}else{
				Bosses.boss.damage(0, event.getDamager());
				Bosses.bossHealth = health-event.getDamage();
			}
			if(Bosses.boss.getHealth() == 0){
				Bosses.exist = false;
				Bosses.boss.getWorld().dropItemNaturally(Bosses.boss.getLocation(), new ItemStack(Material.GOLD_INGOT, 1));
				Bosses.boss.remove();
			}
			event.setCancelled(true);
		}
		else if(event.getDamager() instanceof Player){
			Player player = (Player)event.getDamager();
			if(BeyondInfo.getReligion(player) != null){
				int dice = calculator(player);
				if(event.getDamager() instanceof Player && event.getEntity() instanceof Sheep){
					if(event.getDamage() >= ((Sheep)event.getEntity()).getHealth() && calculator(player) < 20){
						event.getEntity().remove();
						event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), new ItemStack(Material.GOLD_INGOT, 3));
					}
				}
				if(event.getDamager() instanceof Player && event.getEntity() instanceof Creeper){
					if(event.getDamage() >= ((Creeper)event.getEntity()).getHealth()){
						if((BeyondInfo.getReligion(player).equals("Lihazism") && dice < 5)
								|| (BeyondInfo.getReligion(player).equals("Fercism") && dice < 5)
								|| (BeyondInfo.getReligion(player).equals("Pandasidism") && dice < 5)
								|| (BeyondInfo.getReligion(player).equals("Notchitism") && dice < 20)
								|| (BeyondInfo.getReligion(player).equals("Jorism") && dice < 20)){
							event.getEntity().remove();
							((Wolf)event.getEntity().getWorld().spawnCreature(event.getEntity().getLocation(), CreatureType.WOLF)).setOwner(player);
							((Wolf)event.getEntity().getWorld().spawnCreature(event.getEntity().getLocation(), CreatureType.WOLF)).setOwner(player);
							((Wolf)event.getEntity().getWorld().spawnCreature(event.getEntity().getLocation(), CreatureType.WOLF)).setOwner(player);
							((Wolf)event.getEntity().getWorld().spawnCreature(event.getEntity().getLocation(), CreatureType.WOLF)).setOwner(player);
						}else if((BeyondInfo.getReligion(player).equals("Lihazism") && dice < 30)
								|| (BeyondInfo.getReligion(player).equals("Fercism") && dice < 30)
								|| (BeyondInfo.getReligion(player).equals("Pandasidism") && dice < 30)
								|| (BeyondInfo.getReligion(player).equals("Notchitism") && dice < 100)
								|| (BeyondInfo.getReligion(player).equals("Jorism") && dice < 100)){
							event.getEntity().remove();
							event.getEntity().getWorld().spawnCreature(event.getEntity().getLocation(), CreatureType.CHICKEN);
							event.getEntity().getWorld().spawnCreature(event.getEntity().getLocation(), CreatureType.CHICKEN);
							event.getEntity().getWorld().spawnCreature(event.getEntity().getLocation(), CreatureType.CHICKEN);
							event.getEntity().getWorld().spawnCreature(event.getEntity().getLocation(), CreatureType.CHICKEN);
							event.getEntity().getWorld().spawnCreature(event.getEntity().getLocation(), CreatureType.CHICKEN);
							event.getEntity().getWorld().spawnCreature(event.getEntity().getLocation(), CreatureType.CHICKEN);
							event.getEntity().getWorld().spawnCreature(event.getEntity().getLocation(), CreatureType.CHICKEN);
							event.getEntity().getWorld().spawnCreature(event.getEntity().getLocation(), CreatureType.CHICKEN);
						}
					}
				}
				if(event.getDamager() instanceof Player && event.getEntity() instanceof Zombie){
					if(event.getDamage() >= ((Zombie)event.getEntity()).getHealth()){
						if((BeyondInfo.getReligion(player).equals("Lihazism") && dice < 7)
								|| (BeyondInfo.getReligion(player).equals("Fercism") && dice < 7)
								|| (BeyondInfo.getReligion(player).equals("Pandasidism") && dice < 40)
								|| (BeyondInfo.getReligion(player).equals("Notchitism") && dice < 7)
								|| (BeyondInfo.getReligion(player).equals("Jorism") && dice < 7)){
							event.getEntity().remove();
							Potion potion = new Potion(potionTypeRandomizer(), potionTierRandomizer(), potionSplashRandomizer());
							ItemStack stack = new ItemStack(Material.POTION, 1);
							potion.apply(stack);
							event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), stack);	
						}
					}
				}
				if(event.getDamager() instanceof Player && event.getEntity() instanceof PigZombie){
					if(event.getDamage() >= ((PigZombie)event.getEntity()).getHealth()){
						if((BeyondInfo.getReligion(player).equals("Lihazism") && dice < 5)
								|| (BeyondInfo.getReligion(player).equals("Fercism") && dice < 20)
								|| (BeyondInfo.getReligion(player).equals("Pandasidism") && dice < 5)
								|| (BeyondInfo.getReligion(player).equals("Notchitism") && dice < 5)
								|| (BeyondInfo.getReligion(player).equals("Jorism") && dice < 5)){
							event.getEntity().remove();
							ItemStack stack = new ItemStack(weaponTypeRandomizer(), 1);
							while(stack.getEnchantments().isEmpty()){
								try{
									stack.addUnsafeEnchantment(weaponEnchantRandomizer(), weaponLevelRandomizer());
									Random chance = new Random();
									int next = chance.nextInt(100);
									if(next<10){
										stack.addUnsafeEnchantment(weaponEnchantRandomizer(), weaponLevelRandomizer());
									}
									if(next<5){
										stack.addUnsafeEnchantment(weaponEnchantRandomizer(), weaponLevelRandomizer());
									}
									if(next<1){
										stack.addUnsafeEnchantment(weaponEnchantRandomizer(), weaponLevelRandomizer());
									}
								}catch(IllegalArgumentException e){
									System.out.println("[Religion] DEBUG - weaponpower");
								}
							}
							event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), stack);
							((Player)event.getDamager()).sendMessage("Hooray! A "+ChatColor.BLUE.toString()+stack.getType().toString()+ChatColor.WHITE.toString()+" dropped! Rarity Index: "+BeyondUtil.getColorOfRarity(BeyondUtil.rarity(stack))+BeyondUtil.rarity(stack));
						}
					}
				}
				if(event.getDamager() instanceof Player && event.getEntity() instanceof PigZombie){
					if(event.getDamage() >= ((PigZombie)event.getEntity()).getHealth()){
						if((BeyondInfo.getReligion(player).equals("Lihazism") && dice < 20)
								|| (BeyondInfo.getReligion(player).equals("Fercism") && dice < 5)
								|| (BeyondInfo.getReligion(player).equals("Pandasidism") && dice < 5)
								|| (BeyondInfo.getReligion(player).equals("Notchitism") && dice < 5)
								|| (BeyondInfo.getReligion(player).equals("Jorism") && dice < 5)){
							event.getEntity().remove();
							ItemStack stack = new ItemStack(armorTypeRandomizer(), 1);
							stack.addUnsafeEnchantment(armorEnchantRandomizer(), armorLevelRandomizer());
							Random chance = new Random();
							int next = chance.nextInt(100);
							if(next<10){
								stack.addUnsafeEnchantment(armorEnchantRandomizer(), armorLevelRandomizer());
							}
							if(next<5){
								stack.addUnsafeEnchantment(armorEnchantRandomizer(), armorLevelRandomizer());
							}
							if(next<1){
								stack.addUnsafeEnchantment(armorEnchantRandomizer(), armorLevelRandomizer());
							}
							event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), stack);
							((Player)event.getDamager()).sendMessage("Hooray! A "+ChatColor.BLUE.toString()+stack.getType().toString()+ChatColor.WHITE.toString()+" dropped! Rarity Index: "+BeyondUtil.getColorOfRarity(BeyondUtil.rarity(stack))+BeyondUtil.rarity(stack));
						}
					}
				}
				if(event.getDamager() instanceof Player && event.getEntity() instanceof Enderman){
					if(event.getDamage() >= ((Enderman)event.getEntity()).getHealth()){
						if((BeyondInfo.getReligion(player).equals("Lihazism") && dice < 5)
								|| (BeyondInfo.getReligion(player).equals("Fercism") && dice < 5)
								|| (BeyondInfo.getReligion(player).equals("Pandasidism") && dice < 5)
								|| (BeyondInfo.getReligion(player).equals("Notchitism") && dice < 30)
								|| (BeyondInfo.getReligion(player).equals("Jorism") && dice < 5)){
							event.getEntity().remove();
							ItemStack stack = new ItemStack(toolTypeRandomizer(), 1);
							while(stack.getEnchantments().isEmpty()){
								try{
									stack.addUnsafeEnchantment(toolEnchantRandomizer(), toolLevelRandomizer());
									Random chance = new Random();
									int next = chance.nextInt(100);
									if(next<10){
										stack.addUnsafeEnchantment(toolEnchantRandomizer(), toolLevelRandomizer());
									}
									if(next<5){
										stack.addUnsafeEnchantment(toolEnchantRandomizer(), toolLevelRandomizer());
									}
									if(next<1){
										stack.addUnsafeEnchantment(toolEnchantRandomizer(), toolLevelRandomizer());
									}
								}catch(IllegalArgumentException e){
									System.out.println("[Religion] DEBUG - toolpower");
								}
							}
							event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), stack);
							((Player)event.getDamager()).sendMessage("Hooray! A "+ChatColor.BLUE.toString()+stack.getType().toString()+ChatColor.WHITE.toString()+" dropped! Rarity Index: "+BeyondUtil.getColorOfRarity(BeyondUtil.rarity(stack))+BeyondUtil.rarity(stack));
						}
					}
				}
			}
		}
	}
	public int calculator(Player player){
		Random chance = new Random();
		int next = chance.nextInt(1000);
		int playerlevel = player.getLevel()/10;
		if(playerlevel > 10)playerlevel = 10;
		return (next - (playerlevel*3));
	}
	public PotionType potionTypeRandomizer(){
		Random chance = new Random();
		int next = chance.nextInt(9);
		switch(next){
		case 0: return PotionType.SPEED;
		case 1: return PotionType.SLOWNESS;
		case 2: return PotionType.FIRE_RESISTANCE;
		case 3: return PotionType.REGEN;
		case 4: return PotionType.INSTANT_DAMAGE;
		case 5: return PotionType.INSTANT_HEAL;
		case 6: return PotionType.POISON;
		case 7: return PotionType.WEAKNESS;
		case 8: return PotionType.STRENGTH;
		}
		return PotionType.SPEED;
	}
	public Tier potionTierRandomizer(){
		Random chance = new Random();
		int next = chance.nextInt(3);
		if(next < 2){
			return Tier.ONE;
		}else return Tier.TWO;
	}
	public boolean potionSplashRandomizer(){
		Random chance = new Random();
		int next = chance.nextInt(2);
		if(next == 0)return true;
		else return false;
	}
	public Material weaponTypeRandomizer(){
		Random chance = new Random();
		int next = chance.nextInt(100);
		if(next<10)return Material.DIAMOND_SWORD;
		else if(next<25)return Material.IRON_SWORD;
		else if(next<50)return Material.GOLD_SWORD;
		else return Material.WOOD_SWORD;
	}
	public Enchantment weaponEnchantRandomizer(){
		Random chance = new Random();
		int next = chance.nextInt(6);
		switch(next){
		case 0: return Enchantment.LOOT_BONUS_MOBS;
		case 1: return Enchantment.KNOCKBACK;
		case 2: return Enchantment.FIRE_ASPECT;
		case 3: return Enchantment.DAMAGE_UNDEAD;
		case 4: return Enchantment.DAMAGE_ARTHROPODS;
		case 5: return Enchantment.DAMAGE_ALL;
		}
		return Enchantment.DAMAGE_ALL;
	}
	public int weaponLevelRandomizer(){
		Random chance = new Random();
		int next = chance.nextInt(100);
		if(next<1)return 10;
		else if(next<3)return 9;
		else if(next<6)return 8;
		else if(next<10)return 7;
		else if(next<15)return 6;
		else if(next<20)return 5;
		else if(next<30)return 4;
		else if(next<40)return 3;
		else if(next<50)return 2;
		else return 1;
	}
	public Material armorTypeRandomizer(){
		Random chance = new Random();
		int next = chance.nextInt(100);
		if(next<2)return Material.DIAMOND_CHESTPLATE;
		else if(next<5)return Material.DIAMOND_BOOTS;
		else if(next<7)return Material.DIAMOND_HELMET;
		else if(next<10)return Material.DIAMOND_LEGGINGS;
		else if(next<13)return Material.IRON_CHESTPLATE;
		else if(next<17)return Material.IRON_BOOTS;
		else if(next<21)return Material.IRON_HELMET;
		else if(next<25)return Material.IRON_LEGGINGS;
		else if(next<29)return Material.GOLD_CHESTPLATE;
		else if(next<36)return Material.GOLD_BOOTS;
		else if(next<43)return Material.GOLD_HELMET;
		else if(next<50)return Material.GOLD_LEGGINGS;
		else if(next<60)return Material.LEATHER_CHESTPLATE;
		else if(next<73)return Material.LEATHER_BOOTS;
		else if(next<86)return Material.LEATHER_HELMET;
		else return Material.LEATHER_LEGGINGS;
	}
	public Enchantment armorEnchantRandomizer(){
		Random chance = new Random();
		int next = chance.nextInt(4);
		switch(next){
		case 0: return Enchantment.PROTECTION_FIRE;
		case 1: return Enchantment.PROTECTION_PROJECTILE;
		case 2: return Enchantment.PROTECTION_ENVIRONMENTAL;
		case 3: return Enchantment.PROTECTION_EXPLOSIONS;
		}
		return Enchantment.PROTECTION_EXPLOSIONS;
	}
	public int armorLevelRandomizer(){
		Random chance = new Random();
		int next = chance.nextInt(100);
		if(next<1)return 10;
		else if(next<3)return 9;
		else if(next<6)return 8;
		else if(next<10)return 7;
		else if(next<15)return 6;
		else if(next<20)return 5;
		else if(next<30)return 4;
		else if(next<40)return 3;
		else if(next<50)return 2;
		else return 1;
	}
	public Material toolTypeRandomizer(){
		Random chance = new Random();
		int next = chance.nextInt(100);
		if(next<4)return Material.DIAMOND_AXE;
		else if(next<7)return Material.DIAMOND_PICKAXE;
		else if(next<10)return Material.DIAMOND_SPADE;
		else if(next<15)return Material.IRON_AXE;
		else if(next<20)return Material.IRON_PICKAXE;
		else if(next<25)return Material.IRON_SPADE;
		else if(next<29)return Material.GOLD_AXE;
		else if(next<33)return Material.GOLD_PICKAXE;
		else if(next<37)return Material.GOLD_SPADE;
		else if(next<41)return Material.STONE_AXE;
		else if(next<45)return Material.STONE_PICKAXE;
		else if(next<50)return Material.STONE_SPADE;
		else if(next<60)return Material.WOOD_PICKAXE;
		else if(next<85)return Material.WOOD_AXE;
		else return Material.WOOD_SPADE;
	}
	public Enchantment toolEnchantRandomizer(){
		Random chance = new Random();
		int next = chance.nextInt(4);
		switch(next){
		case 0: return Enchantment.DURABILITY;
		case 1: return Enchantment.LOOT_BONUS_BLOCKS;
		case 2: return Enchantment.DIG_SPEED;
		case 3: return Enchantment.SILK_TOUCH;
		}
		return Enchantment.PROTECTION_EXPLOSIONS;
	}
	public int toolLevelRandomizer(){
		Random chance = new Random();
		int next = chance.nextInt(100);
		if(next<1)return 10;
		else if(next<3)return 9;
		else if(next<6)return 8;
		else if(next<10)return 7;
		else if(next<15)return 6;
		else if(next<20)return 5;
		else if(next<30)return 4;
		else if(next<40)return 3;
		else if(next<50)return 2;
		else return 1;
	}
}