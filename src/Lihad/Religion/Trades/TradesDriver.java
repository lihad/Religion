package Lihad.Religion.Trades;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
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
			for(int j = 0;j<towers.size();j++){
				List<String> trades = BeyondInfo.getTrades(towers.get(j));
				if(trades==null)continue;
				for(int k = 0; k<trades.size();k++){
					Location location = BeyondInfo.getTradeLocation(towers.get(j), trades.get(k));
					if(location.getBlock().getState() instanceof Chest){
						Chest chest = (Chest) location.getBlock().getState();
						if(trades.get(k).equals("Blacksmith"))auxBlacksmith(chest);
						else if(trades.get(k).equals("Fishery"))auxFishery(chest);
						else if(trades.get(k).equals("Fletcher"))auxFletcher(chest);
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

	public void auxBlacksmith(Chest chest){
		Inventory inventory = chest.getInventory();
		for(int i=0;i<inventory.getSize();i++){
			ItemStack item = inventory.getItem(i);
			try{
				if(item.getDurability()<=0) continue;
				item.setDurability((short) (item.getDurability()-20));
			}catch(Exception e){
				System.out.println("[Religion] Watchdog. Get Lihad. auxBlacksmith. Exception thrown. Handled");
				e.printStackTrace();
			}
		}
	}
	public void auxFishery(Chest chest){
		Inventory inventory = chest.getInventory();
		try{

			inventory.setItem(inventory.firstEmpty(), new ItemStack(Material.RAW_FISH, 1));
		}catch(Exception e){
			System.out.println("[Religion] Watchdog. Get Lihad. auxFishery. Exception thrown. Handled");
			e.printStackTrace();
		}
	}
	public void auxFletcher(Chest chest){
		Inventory inventory = chest.getInventory();
		try{
			if(inventory.contains(Material.WOOD)){
				int index = inventory.first(Material.WOOD);
				if(inventory.getItem(index).getAmount() == 1)inventory.setItem(index, null);
				else{
					inventory.getItem(index).setAmount(inventory.getItem(index).getAmount());
					inventory.setItem(index, inventory.getItem(index));
				}
				inventory.addItem(new ItemStack(Material.ARROW, 1));
			}
		}catch(Exception e){
			System.out.println("[Religion] Watchdog. Get Lihad. auxFletcher. Exception thrown. Handled");
			e.printStackTrace();
		}
	}

}
