package Lihad.Religion.Trades;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import Lihad.Religion.Religion;
import Lihad.Religion.Information.BeyondInfo;

public class TradesDriver {
	public static Religion plugin;

	public TradesDriver(Religion instance){
		plugin = instance;
	}


	public void driver(){
		checkTradeChests();
		updateTradeChests();
	}

	public void checkTradeChests(){
		List<String> religions = BeyondInfo.getReligions();
		for(int i = 0;i<religions.size();i++){
			List<String> towers = BeyondInfo.getTowers(religions.get(i));
			if(towers == null)continue;
			for(int j = 0;j<towers.size();j++){
				List<String> trades = BeyondInfo.getTrades(towers.get(j));
				if(trades==null)continue;
				for(int k = 0; k<trades.size();k++){
					Location location = BeyondInfo.getTradeLocation(towers.get(j), trades.get(k));
					if(BeyondInfo.getClosestValidTower(location) == null){
						BeyondInfo.removeTrades(towers.get(j), trades.get(k));
						location.getWorld().createExplosion(location, 20, true);
					}
				}
			}
		}
	}
	public void updateTradeChests(){
		List<String> religions = BeyondInfo.getReligions();
		for(int i = 0;i<religions.size();i++){
			List<String> towers = BeyondInfo.getTowers(religions.get(i));
			if(towers == null)continue;
			for(int j = 0;j<towers.size();j++){
				List<String> trades = BeyondInfo.getTrades(towers.get(j));
				if(trades==null)continue;
				for(int k = 0; k<trades.size();k++){
					Location location = BeyondInfo.getTradeLocation(towers.get(j), trades.get(k));
					if(location.getBlock().getState() instanceof Chest){
						Chest chest = (Chest) location.getBlock().getState();
						if(trades.get(k).equals("Blacksmith"))auxBlacksmith(chest,towers.get(j));
						else if(trades.get(k).equals("Fishery"))auxFishery(chest,towers.get(j));
						else if(trades.get(k).equals("Fletcher"))auxFletcher(chest,towers.get(j));
						else if(trades.get(k).equals("Disenchant"))auxFletcher(chest,towers.get(j));
						else{
							System.out.println("[Religion] Watchdog. Get Lihad. updateTradeChests. Invalid Trade found: "+trades.get(k));
						}
					}else{
						System.out.println("[Religion] ERROR - Get Lihad.  Error in update pattern. Location - "+location.toString()+" trade: "+trades.get(k));
					}
				}
			}
		}
	}

	public void auxBlacksmith(Chest chest, String towername){
		Inventory inventory = chest.getInventory();
		for(int i=0;i<inventory.getSize();i++){
			ItemStack item = inventory.getItem(i);
			if(item == null) continue;
			if(item.getDurability()<=0) continue;
			item.setDurability((short) (item.getDurability()-BeyondInfo.getTowerAoE(towername)));
			if(item.getDurability()<0)item.setDurability((short)0);
		}
	}
	public void auxFishery(Chest chest, String towername){
		Inventory inventory = chest.getInventory();
		inventory.addItem(new ItemStack(Material.RAW_FISH, (int) (1*BeyondInfo.getTowerAoE(towername))));
	}
	public void auxFletcher(Chest chest, String towername){
		Inventory inventory = chest.getInventory();
		inventory.addItem(new ItemStack(Material.ARROW, (int) (1*BeyondInfo.getTowerAoE(towername))));
	}
	public void auxDisenchant(Chest chest, String towername){
		Inventory inventory = chest.getInventory();
		for(int i = 0; i<inventory.getSize();i++){
			switch(i){
			case 0:
				inventory.getItem(i).removeEnchantment(Enchantment.ARROW_DAMAGE);
				break;
			case 1:
				inventory.getItem(i).removeEnchantment(Enchantment.ARROW_FIRE);
				break;
			case 2:
				inventory.getItem(i).removeEnchantment(Enchantment.ARROW_INFINITE);
				break;
			case 3:
				inventory.getItem(i).removeEnchantment(Enchantment.ARROW_KNOCKBACK);
				break;
			case 4:
				inventory.getItem(i).removeEnchantment(Enchantment.DAMAGE_ALL);
				break;
			case 5:
				inventory.getItem(i).removeEnchantment(Enchantment.DAMAGE_ARTHROPODS);
				break;
			case 6:
				inventory.getItem(i).removeEnchantment(Enchantment.DAMAGE_UNDEAD);
				break;
			case 7:
				inventory.getItem(i).removeEnchantment(Enchantment.DIG_SPEED);
				break;
			case 8:
				inventory.getItem(i).removeEnchantment(Enchantment.DURABILITY);
				break;
			case 9:
				inventory.getItem(i).removeEnchantment(Enchantment.FIRE_ASPECT);
				break;
			case 10:
				inventory.getItem(i).removeEnchantment(Enchantment.KNOCKBACK);
				break;
			case 11:
				inventory.getItem(i).removeEnchantment(Enchantment.LOOT_BONUS_BLOCKS);
				break;
			case 12:
				inventory.getItem(i).removeEnchantment(Enchantment.LOOT_BONUS_MOBS);
				break;
			case 13:
				inventory.getItem(i).removeEnchantment(Enchantment.OXYGEN);
				break;
			case 14:
				inventory.getItem(i).removeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL);
				break;
			case 15:
				inventory.getItem(i).removeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL);
				break;
			case 16:
				inventory.getItem(i).removeEnchantment(Enchantment.PROTECTION_FALL);
				break;
			case 17:
				inventory.getItem(i).removeEnchantment(Enchantment.PROTECTION_FIRE);
				break;
			case 18:
				inventory.getItem(i).removeEnchantment(Enchantment.PROTECTION_PROJECTILE);
				break;
			case 19:
				inventory.getItem(i).removeEnchantment(Enchantment.SILK_TOUCH);
				break;
			case 20:
				inventory.getItem(i).removeEnchantment(Enchantment.WATER_WORKER);
				break;
			}
		}
	}
}
