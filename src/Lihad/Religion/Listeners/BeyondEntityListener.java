package Lihad.Religion.Listeners;

import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;

import Lihad.Religion.Religion;
import Lihad.Religion.Bosses.Bosses;
import Lihad.Religion.Information.BeyondInfo;
import Lihad.Religion.Util.BeyondUtil;

public class BeyondEntityListener implements Listener {
	public static Religion plugin;
	
	
	public BeyondEntityListener(Religion instance) {
		plugin = instance;
	}
	@EventHandler
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
		}

		/**
		 * The following 'if' makes players that take damage in their own religions AoE take half.  Those who take damage in opposing religion AoE
		 * take +1 
		 */
		if(event.getEntity() instanceof Player){
			Player player = (Player)event.getEntity();
			if(BeyondInfo.getClosestValidTower(player.getLocation()) ==  null || BeyondInfo.getReligion(player) == null) return;
			else{
				if(!BeyondInfo.getReligion(BeyondInfo.getClosestValidTower(player.getLocation())).equals(BeyondInfo.getReligion(player))){
					player.damage(1);
				}
			}
		}
		
		if(event instanceof EntityDamageByEntityEvent)onEntityDamageByEntity((EntityDamageByEntityEvent)event);

		if(event.getEntity() instanceof Creature){
			if((Bosses.bossHealthMap.containsKey(event.getEntity())) && !(event instanceof EntityDamageByEntityEvent)){
				for(int i = 0;i<Bosses.bossHealthMap.size();i++){
					LivingEntity entity = (LivingEntity) Bosses.bossHealthMap.keySet().toArray()[i];
					if(entity.equals(event.getEntity())){
						if(!(((Creature)entity).getTarget() == null))Religion.bosses.healthDeplete(event);
						event.setCancelled(true);
					}
				}
			}
		}
	}
	@EventHandler
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
	@EventHandler
	public void onEntityDeath(EntityDeathEvent event){
		if(event.getEntity() instanceof Player){
			Player player = (Player)event.getEntity();
			if(BeyondInfo.getClosestValidTower(player.getLocation()) != null && BeyondInfo.getReligion(player) != null){
				if(!BeyondInfo.getReligion(BeyondInfo.getClosestValidTower(player.getLocation())).equals(BeyondInfo.getReligion(player))){
					BeyondInfo.setPlayerCooldown(player);
				}
			}
		}
	}
	@EventHandler
	public void onExplosionPrime(ExplosionPrimeEvent event){
		if(event.getEntity() instanceof LivingEntity){
			if(Bosses.bossHealthMap.containsKey((LivingEntity)event.getEntity())){
				event.setCancelled(true);


			}
		}
	}
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event){

		//
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//
		// "Bosses"
		//
		if(event.getEntity() instanceof Creature){
			for(int i = 0;i<Bosses.bossHealthMap.size();i++){
				LivingEntity entity = (LivingEntity) Bosses.bossHealthMap.keySet().toArray()[i];
				if(entity.equals(event.getEntity())){
					if(event.getCause() == DamageCause.PROJECTILE){
						if((((Creature)entity).getTarget() == null)){
							event.setCancelled(true);
							return;
						}
					}
					for(int j=0;j<Bosses.bossNameMap.size();j++){
						if(Bosses.bossNameMap.get(Bosses.bossNameMap.keySet().toArray()[j]).equals(entity)){
							Religion.bosses.damageTriggers(event, (String) Bosses.bossNameMap.keySet().toArray()[j]);
						}
					}
					Religion.bosses.healthDepleteByEntity(event);
					event.setCancelled(true);
				}
			}
		}
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////


		
		
		if(event.getDamager() instanceof Player && !Bosses.bossHealthMap.containsKey(event.getEntity())){
			Player player = (Player)event.getDamager();
			if(BeyondInfo.getReligion(player) != null){
				int dice = BeyondUtil.calculator(player);
					
				
				//
				/////////////////////////////////////////////////////////////////////////////////////////////////////////////
				//
				// "Golden Fleece"
				//
				if(event.getDamager() instanceof Player && event.getEntity() instanceof Sheep){
					if(event.getDamage() >= ((Sheep)event.getEntity()).getHealth() && BeyondUtil.calculator(player) < 20){
						event.getEntity().remove();
						event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), new ItemStack(Material.GOLD_INGOT, 3));
					}
				}
				
				//
				/////////////////////////////////////////////////////////////////////////////////////////////////////////////
				//
				// "Happy Creeper"
				//
				
				if(event.getDamager() instanceof Player && event.getEntity() instanceof Creeper){
					if(event.getDamage() >= ((Creeper)event.getEntity()).getHealth()){
						if((BeyondInfo.getReligion(player).equals("Lihazism") && dice < 5)
								|| (BeyondInfo.getReligion(player).equals("Fercism") && dice < 5)
								|| (BeyondInfo.getReligion(player).equals("Pandasidism") && dice < 5)
								|| (BeyondInfo.getReligion(player).equals("Notchitism") && dice < 20)
								|| (BeyondInfo.getReligion(player).equals("Jorism") && dice < 20)){
							event.getEntity().remove();
							for(int i =0;i<5;i++){
								((Wolf)event.getEntity().getWorld().spawnCreature(event.getEntity().getLocation(), EntityType.WOLF)).setOwner(player);
							}
						}else if((BeyondInfo.getReligion(player).equals("Lihazism") && dice < 30)
								|| (BeyondInfo.getReligion(player).equals("Fercism") && dice < 30)
								|| (BeyondInfo.getReligion(player).equals("Pandasidism") && dice < 30)
								|| (BeyondInfo.getReligion(player).equals("Notchitism") && dice < 100)
								|| (BeyondInfo.getReligion(player).equals("Jorism") && dice < 100)){
							event.getEntity().remove();
							for(int i =0;i<10;i++){
								event.getEntity().getWorld().spawnCreature(event.getEntity().getLocation(), EntityType.CHICKEN);
							}
						}
					}
				}
				
				//
				/////////////////////////////////////////////////////////////////////////////////////////////////////////////
				//
				// "Drunk Zombie"
				//
				
				if(event.getDamager() instanceof Player && event.getEntity() instanceof Zombie){
					if(event.getDamage() >= ((Zombie)event.getEntity()).getHealth()){
						if((BeyondInfo.getReligion(player).equals("Lihazism") && dice < 7)
								|| (BeyondInfo.getReligion(player).equals("Fercism") && dice < 7)
								|| (BeyondInfo.getReligion(player).equals("Pandasidism") && dice < 40)
								|| (BeyondInfo.getReligion(player).equals("Notchitism") && dice < 7)
								|| (BeyondInfo.getReligion(player).equals("Jorism") && dice < 7)){
							event.getEntity().remove();
							Potion potion = new Potion(BeyondUtil.potionTypeRandomizer(), BeyondUtil.potionTierRandomizer(), BeyondUtil.potionSplashRandomizer());
							ItemStack stack = new ItemStack(Material.POTION, 1);
							potion.apply(stack);
							event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), stack);	
						}
					}
				}
				
				//
				/////////////////////////////////////////////////////////////////////////////////////////////////////////////
				//
				// "Slayer"
				//
				
				if(event.getDamager() instanceof Player && event.getEntity() instanceof PigZombie){
					if(event.getDamage() >= ((PigZombie)event.getEntity()).getHealth()){
						if((BeyondInfo.getReligion(player).equals("Lihazism") && dice < 5)
								|| (BeyondInfo.getReligion(player).equals("Fercism") && dice < 20)
								|| (BeyondInfo.getReligion(player).equals("Pandasidism") && dice < 5)
								|| (BeyondInfo.getReligion(player).equals("Notchitism") && dice < 5)
								|| (BeyondInfo.getReligion(player).equals("Jorism") && dice < 5)){
							event.getEntity().remove();
							ItemStack stack = new ItemStack(BeyondUtil.weaponTypeRandomizer(), 1);
							while(stack.getEnchantments().isEmpty()){
								stack.addUnsafeEnchantment(BeyondUtil.weaponEnchantRandomizer(), BeyondUtil.weaponLevelRandomizer());
								Random chance = new Random();
								int next = chance.nextInt(100);
								if(next<10)stack.addUnsafeEnchantment(BeyondUtil.weaponEnchantRandomizer(),BeyondUtil.weaponLevelRandomizer());
								if(next<5)stack.addUnsafeEnchantment(BeyondUtil.weaponEnchantRandomizer(), BeyondUtil.weaponLevelRandomizer());
								if(next<1)stack.addUnsafeEnchantment(BeyondUtil.weaponEnchantRandomizer(), BeyondUtil.weaponLevelRandomizer());
							}
							event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), stack);
							((Player)event.getDamager()).sendMessage("Hooray! A "+ChatColor.BLUE.toString()+stack.getType().toString()+ChatColor.WHITE.toString()+" dropped! Rarity Index: "+BeyondUtil.getColorOfRarity(BeyondUtil.rarity(stack))+BeyondUtil.rarity(stack));
						}
					}
				}
				
				//
				/////////////////////////////////////////////////////////////////////////////////////////////////////////////
				//
				// "Defender"
				//
				
				if(event.getDamager() instanceof Player && event.getEntity() instanceof PigZombie){
					if(event.getDamage() >= ((PigZombie)event.getEntity()).getHealth()){
						if((BeyondInfo.getReligion(player).equals("Lihazism") && dice < 20)
								|| (BeyondInfo.getReligion(player).equals("Fercism") && dice < 5)
								|| (BeyondInfo.getReligion(player).equals("Pandasidism") && dice < 5)
								|| (BeyondInfo.getReligion(player).equals("Notchitism") && dice < 5)
								|| (BeyondInfo.getReligion(player).equals("Jorism") && dice < 5)){
							event.getEntity().remove();
							ItemStack stack = new ItemStack(BeyondUtil.armorTypeRandomizer(), 1);
							stack.addUnsafeEnchantment(BeyondUtil.armorEnchantRandomizer(), BeyondUtil.armorLevelRandomizer());
							Random chance = new Random();
							int next = chance.nextInt(100);
							if(next<10)stack.addUnsafeEnchantment(BeyondUtil.armorEnchantRandomizer(), BeyondUtil.armorLevelRandomizer());							
							if(next<5)stack.addUnsafeEnchantment(BeyondUtil.armorEnchantRandomizer(), BeyondUtil.armorLevelRandomizer());
							if(next<1)stack.addUnsafeEnchantment(BeyondUtil.armorEnchantRandomizer(), BeyondUtil.armorLevelRandomizer());
							event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), stack);
							((Player)event.getDamager()).sendMessage("Hooray! A "+ChatColor.BLUE.toString()+stack.getType().toString()+ChatColor.WHITE.toString()+" dropped! Rarity Index: "+BeyondUtil.getColorOfRarity(BeyondUtil.rarity(stack))+BeyondUtil.rarity(stack));
						}
					}
				}
				
				//
				/////////////////////////////////////////////////////////////////////////////////////////////////////////////
				//
				// "Tool Grinder"
				//
				
				if(event.getDamager() instanceof Player && event.getEntity() instanceof Enderman){
					if(event.getDamage() >= ((Enderman)event.getEntity()).getHealth()){
						if((BeyondInfo.getReligion(player).equals("Lihazism") && dice < 5)
								|| (BeyondInfo.getReligion(player).equals("Fercism") && dice < 5)
								|| (BeyondInfo.getReligion(player).equals("Pandasidism") && dice < 5)
								|| (BeyondInfo.getReligion(player).equals("Notchitism") && dice < 30)
								|| (BeyondInfo.getReligion(player).equals("Jorism") && dice < 5)){
							event.getEntity().remove();
							ItemStack stack = new ItemStack(BeyondUtil.toolTypeRandomizer(), 1);
							while(stack.getEnchantments().isEmpty()){
								stack.addUnsafeEnchantment(BeyondUtil.toolEnchantRandomizer(), BeyondUtil.toolLevelRandomizer());
								Random chance = new Random();
								int next = chance.nextInt(100);
								if(next<10)stack.addUnsafeEnchantment(BeyondUtil.toolEnchantRandomizer(), BeyondUtil.toolLevelRandomizer());
								if(next<5)stack.addUnsafeEnchantment(BeyondUtil.toolEnchantRandomizer(), BeyondUtil.toolLevelRandomizer());
								if(next<1)stack.addUnsafeEnchantment(BeyondUtil.toolEnchantRandomizer(), BeyondUtil.toolLevelRandomizer());
							}
							event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), stack);
							((Player)event.getDamager()).sendMessage("Hooray! A "+ChatColor.BLUE.toString()+stack.getType().toString()+ChatColor.WHITE.toString()+" dropped! Rarity Index: "+BeyondUtil.getColorOfRarity(BeyondUtil.rarity(stack))+BeyondUtil.rarity(stack));
						}
					}
				}
			}
		}
	}
	
	

}