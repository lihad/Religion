package Lihad.Religion.Listeners;


import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;

import Lihad.Religion.Religion;
import Lihad.Religion.Abilities.ReverseEngineering;

public class BeyondInventoryListener implements Listener {
	public static Religion plugin;

	public BeyondInventoryListener(Religion instance) {
		plugin = instance;
	}
	@EventHandler
	public void onPlayerCraft(PrepareItemCraftEvent event){
		
		if(ReverseEngineering.getAllowableMaterialList().contains(event.getRecipe().getResult().getType())&& isFull(event.getInventory().getMatrix())){
			ReverseEngineering.reverseEngineer(event);
		}
		if(ReverseEngineering.getConvertibleMaterialListResult().contains(event.getRecipe().getResult().getType()) && isSingle(event.getInventory().getMatrix())){
			ReverseEngineering.weaponUpgrade(event);
		}
		
	}
	private static boolean isFull(ItemStack[] inv){
		for(int i = 0;i<inv.length-1;i++){
			if(inv[i].getType() == Material.AIR) return false;
		}
		return true;
	}
	private static boolean isSingle(ItemStack[] inv){
		int count = 0;
		for(int i = 0;i<inv.length-1;i++){
			if(inv[i].getType() != Material.AIR) count ++;
			if(count >1)return false;
		}
		return true;
	}
}
