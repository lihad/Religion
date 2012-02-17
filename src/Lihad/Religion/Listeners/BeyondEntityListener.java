package Lihad.Religion.Listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Enderman;
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
import org.bukkit.potion.Potion.Tier;
import org.bukkit.potion.PotionType;

import Lihad.Religion.Religion;
import Lihad.Religion.Information.BeyondInfo;

public class BeyondEntityListener extends EntityListener {
	public static Religion plugin;
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
		if(event.getDamager() instanceof Player){
			Player player = (Player)event.getDamager();
			if(BeyondInfo.getReligion(player) != null){

				List<String> sheeppower = new ArrayList<String>();
				sheeppower.add("Lihazism");
				sheeppower.add("Fercism");
				sheeppower.add("Jorism");
				sheeppower.add("Pandasidism");
				if(event.getDamager() instanceof Player && event.getEntity() instanceof Sheep && sheeppower.contains(BeyondInfo.getReligion(player))){
					if(event.getDamage() >= ((Sheep)event.getEntity()).getHealth() && calculator(player) < 20){
						event.getEntity().remove();
						event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), new ItemStack(Material.GOLD_INGOT, 3));
					}
				}
				
				List<String> chickenpower = new ArrayList<String>();
				chickenpower.add("Jorism");
				chickenpower.add("Notchitism");
				if(event.getDamager() instanceof Player && event.getEntity() instanceof Creeper && chickenpower.contains(BeyondInfo.getReligion(player))){
					if(event.getDamage() >= ((Creeper)event.getEntity()).getHealth() && calculator(player) < 10){
						event.getEntity().remove();
						((Wolf)event.getEntity().getWorld().spawnCreature(event.getEntity().getLocation(), CreatureType.WOLF)).setOwner(player);
						((Wolf)event.getEntity().getWorld().spawnCreature(event.getEntity().getLocation(), CreatureType.WOLF)).setOwner(player);
						((Wolf)event.getEntity().getWorld().spawnCreature(event.getEntity().getLocation(), CreatureType.WOLF)).setOwner(player);
						((Wolf)event.getEntity().getWorld().spawnCreature(event.getEntity().getLocation(), CreatureType.WOLF)).setOwner(player);
					}
					else if(event.getDamage() >= ((Creeper)event.getEntity()).getHealth() && calculator(player) < 100){
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
				
				List<String> potionpower = new ArrayList<String>();
				potionpower.add("Pandasidism");
				if(event.getDamager() instanceof Player && event.getEntity() instanceof Zombie && potionpower.contains(BeyondInfo.getReligion(player))){
					if(event.getDamage() >= ((Zombie)event.getEntity()).getHealth() && calculator(player) < 20){
						event.getEntity().remove();
						Potion potion = new Potion(potionTypeRandomizer(), potionTierRandomizer(), potionSplashRandomizer());
						ItemStack stack = new ItemStack(Material.POTION, 1);
						potion.apply(stack);
						event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), stack);

					}
				}
				List<String> weaponpower = new ArrayList<String>();
				weaponpower.add("Fercism");
				if(event.getDamager() instanceof Player && event.getEntity() instanceof PigZombie && weaponpower.contains(BeyondInfo.getReligion(player))){
					if(event.getDamage() >= ((PigZombie)event.getEntity()).getHealth() && calculator(player) < 20){
						event.getEntity().remove();
						ItemStack stack = new ItemStack(weaponTypeRandomizer(), 1);
						while(stack.getEnchantments().isEmpty()){
							try{
								stack.addEnchantment(weaponEnchantRandomizer(), weaponLevelRandomizer());
							}catch(IllegalArgumentException e){
								System.out.println("[Religion] DEBUG - weaponpower");
							}
						}
						event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), stack);
					}
				}
				List<String> armorpower = new ArrayList<String>();
				armorpower.add("Lihazism");
				if(event.getDamager() instanceof Player && event.getEntity() instanceof PigZombie && armorpower.contains(BeyondInfo.getReligion(player))){
					if(event.getDamage() >= ((PigZombie)event.getEntity()).getHealth() && calculator(player) < 20){
						event.getEntity().remove();
						ItemStack stack = new ItemStack(armorTypeRandomizer(), 1);
						stack.addEnchantment(armorEnchantRandomizer(), armorLevelRandomizer());
						event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), stack);
					}
				}
				List<String> toolpower = new ArrayList<String>();
				toolpower.add("Notchitism");
				if(event.getDamager() instanceof Player && event.getEntity() instanceof Enderman && toolpower.contains(BeyondInfo.getReligion(player))){
					if(event.getDamage() >= ((Enderman)event.getEntity()).getHealth() && calculator(player) < 40){
						event.getEntity().remove();
						ItemStack stack = new ItemStack(toolTypeRandomizer(), 1);
						while(stack.getEnchantments().isEmpty()){
							try{
								stack.addEnchantment(toolEnchantRandomizer(), toolLevelRandomizer());
							}catch(IllegalArgumentException e){
								System.out.println("[Religion] DEBUG - toolpower");
							}
						}
						stack.addEnchantment(toolEnchantRandomizer(), toolLevelRandomizer());
						event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), stack);
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
		int next = chance.nextInt(4);
		switch(next){
		case 0: return Material.IRON_SWORD;
		case 1: return Material.DIAMOND_SWORD;
		case 2: return Material.WOOD_SWORD;
		case 3: return Material.GOLD_SWORD;
		}
		return Material.DIAMOND_SWORD;
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
		int next = chance.nextInt(5)+1;
		return next;
	}
	public Material armorTypeRandomizer(){
		Random chance = new Random();
		int next = chance.nextInt(16);
		switch(next){
		case 0: return Material.LEATHER_BOOTS;
		case 1: return Material.LEATHER_CHESTPLATE;
		case 2: return Material.LEATHER_HELMET;
		case 3: return Material.LEATHER_LEGGINGS;
		case 4: return Material.IRON_BOOTS;
		case 5: return Material.IRON_CHESTPLATE;
		case 6: return Material.IRON_HELMET;
		case 7: return Material.IRON_LEGGINGS;
		case 8: return Material.GOLD_BOOTS;
		case 9: return Material.GOLD_CHESTPLATE;
		case 10: return Material.GOLD_HELMET;
		case 11: return Material.GOLD_LEGGINGS;
		case 12: return Material.DIAMOND_BOOTS;
		case 13: return Material.DIAMOND_CHESTPLATE;
		case 14: return Material.DIAMOND_HELMET;
		case 15: return Material.DIAMOND_LEGGINGS;
		}
		return Material.DIAMOND_BOOTS;
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
		int next = chance.nextInt(4)+1;
		return next;
	}
	public Material toolTypeRandomizer(){
		Random chance = new Random();
		int next = chance.nextInt(16);
		switch(next){
		case 0: return Material.WOOD_AXE;
		case 2: return Material.WOOD_PICKAXE;
		case 3: return Material.WOOD_SPADE;
		case 4: return Material.STONE_AXE;
		case 6: return Material.STONE_PICKAXE;
		case 7: return Material.STONE_SPADE;
		case 9: return Material.GOLD_AXE;
		case 10: return Material.GOLD_PICKAXE;
		case 11: return Material.GOLD_SPADE;
		case 13: return Material.DIAMOND_AXE;
		case 14: return Material.DIAMOND_PICKAXE;
		case 15: return Material.DIAMOND_SPADE;
		}
		return Material.DIAMOND_BOOTS;
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
		int next = chance.nextInt(5)+1;
		return next;
	}
	
}
