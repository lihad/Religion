package Lihad.Religion.Abilities;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import Lihad.Religion.Religion;
import Lihad.Religion.Util.BeyondUtil;

public class ReverseEngineering {
	public static Religion plugin;
	public ReverseEngineering(Religion instance) {
		plugin = instance;
	}
	public static void weaponUpgrade(PrepareItemCraftEvent event){
		ItemStack compare = null;
		for(int i = 0;i<event.getInventory().getMatrix().length-1;i++){
			if(event.getInventory().getMatrix()[i].getType() != Material.AIR) compare = event.getInventory().getMatrix()[i];
		}
		if(BeyondUtil.rarity(compare) - BeyondUtil.rarity(new ItemStack(compare.getType())) >= 20 && Religion.handler.has((Player)event.getViewers().get(0), "religion.ability.weaponupgrade")){
			event.getInventory().getResult().addUnsafeEnchantments(modifyConvertedEnchantments(compare.getEnchantments()));
			event.getInventory().getResult().setDurability((short) (compare.getDurability() * 1.5));
		}else{
			event.getInventory().getResult().setType(Material.STICK);
		}
	}

	public static void reverseEngineer(PrepareItemCraftEvent event){
		if(Religion.handler.has((Player)event.getViewers().get(0), "religion.ability.reverseengineer")){
			Material mattype = event.getRecipe().getResult().getType();

			short durability = -1;
			//WEAPON
			int sharpness = 0;
			int smite = 0;
			int bane = 0;
			int knockback = 0;
			int fire  = 0;
			int looting = 0;
			//ARMOR
			int protection = 0;
			int fireprotection = 0;
			int blast = 0;
			int projectile = 0;
			//TOOL
			int efficiency = 0;
			int silk = 0;
			int unbreaking = 0;
			int fortune = 0;

			for(int i = 0;i<event.getInventory().getMatrix().length-1;i++){
				if(event.getInventory().getMatrix()[i].getEnchantments() == null) continue;
				Map<Enchantment, Integer> map = event.getInventory().getMatrix()[i].getEnchantments();
				if(getAllowableMaterialSwordList().contains(mattype)){
					for(int j = 0;j<map.size();j++){
						Enchantment enchant = (Enchantment) map.keySet().toArray()[j];
						if(enchant.equals(Enchantment.KNOCKBACK) && map.get(enchant) > knockback)knockback = map.get(Enchantment.KNOCKBACK);
						if(enchant.equals(Enchantment.DAMAGE_UNDEAD) && map.get(enchant) > smite)smite = map.get(Enchantment.DAMAGE_UNDEAD);
						if(enchant.equals(Enchantment.DAMAGE_ARTHROPODS) && map.get(enchant) > bane)bane = map.get(Enchantment.DAMAGE_ARTHROPODS);
						if(enchant.equals(Enchantment.DAMAGE_ALL) && map.get(enchant) > sharpness)sharpness = map.get(Enchantment.DAMAGE_ALL);
						if(enchant.equals (Enchantment.FIRE_ASPECT) && map.get(enchant) > fire)fire = map.get(Enchantment.FIRE_ASPECT);
						if(enchant.equals(Enchantment.LOOT_BONUS_MOBS) && map.get(enchant) > looting)looting = map.get(Enchantment.LOOT_BONUS_MOBS);
						if(durability == -1) event.getInventory().getContents()[i].getDurability();
						if(durability > event.getInventory().getContents()[i].getDurability())durability = event.getInventory().getContents()[i].getDurability();
					}
				}
				if(getAllowableMaterialArmorList().contains(mattype)){
					for(int j = 0;j<map.size();j++){
						Enchantment enchant = (Enchantment) map.keySet().toArray()[j];
						if(enchant.equals(Enchantment.PROTECTION_ENVIRONMENTAL) && map.get(enchant) > protection)protection = map.get(Enchantment.PROTECTION_ENVIRONMENTAL);
						if(enchant.equals(Enchantment.PROTECTION_FIRE) && map.get(enchant) > fireprotection)fireprotection = map.get(Enchantment.PROTECTION_FIRE);
						if(enchant.equals(Enchantment.PROTECTION_PROJECTILE) && map.get(enchant) > projectile)projectile = map.get(Enchantment.PROTECTION_PROJECTILE);
						if(enchant.equals(Enchantment.PROTECTION_EXPLOSIONS) && map.get(enchant) > blast)blast = map.get(Enchantment.PROTECTION_EXPLOSIONS);
						if(durability == -1) event.getInventory().getContents()[i].getDurability();
						if(durability > event.getInventory().getContents()[i].getDurability())durability = event.getInventory().getContents()[i].getDurability();
					}
				}
				if(getAllowableMaterialToolList().contains(mattype)){
					for(int j = 0;j<map.size();j++){
						Enchantment enchant = (Enchantment) map.keySet().toArray()[j];
						if(enchant.equals(Enchantment.DIG_SPEED) && map.get(enchant) > efficiency)efficiency = map.get(Enchantment.DIG_SPEED);
						if(enchant.equals(Enchantment.SILK_TOUCH) && map.get(enchant) > silk)silk = map.get(Enchantment.SILK_TOUCH);
						if(enchant.equals(Enchantment.DURABILITY) && map.get(enchant) > unbreaking)unbreaking = map.get(Enchantment.DURABILITY);
						if(enchant.equals(Enchantment.LOOT_BONUS_BLOCKS) && map.get(enchant) > fortune)fortune = map.get(Enchantment.LOOT_BONUS_BLOCKS);
						if(durability == -1) event.getInventory().getContents()[i].getDurability();
						if(durability > event.getInventory().getContents()[i].getDurability())durability = event.getInventory().getContents()[i].getDurability();
					}
				}
			}
			ItemStack stack = new ItemStack(mattype, 1);
			if(knockback != 0)stack.addUnsafeEnchantment(Enchantment.KNOCKBACK, knockback);
			if(smite != 0)stack.addUnsafeEnchantment(Enchantment.DAMAGE_UNDEAD, smite);
			if(bane != 0)stack.addUnsafeEnchantment(Enchantment.DAMAGE_ARTHROPODS, bane);
			if(sharpness != 0)stack.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, sharpness);
			if(fire != 0)stack.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, fire);
			if(looting != 0)stack.addUnsafeEnchantment(Enchantment.LOOT_BONUS_MOBS, looting);
			if(protection != 0)stack.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, protection);
			if(fireprotection != 0)stack.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, fireprotection);
			if(projectile != 0)stack.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, projectile);
			if(blast != 0)stack.addUnsafeEnchantment(Enchantment.PROTECTION_EXPLOSIONS, blast);
			if(efficiency != 0)stack.addUnsafeEnchantment(Enchantment.DIG_SPEED, efficiency);
			if(silk != 0)stack.addUnsafeEnchantment(Enchantment.SILK_TOUCH, silk);
			if(unbreaking != 0)stack.addUnsafeEnchantment(Enchantment.DURABILITY, unbreaking);
			if(fortune != 0)stack.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, fortune);
			event.getInventory().setResult(stack);
		}else{
			event.getInventory().setResult(new ItemStack(Material.STICK));
		}
	}
	private static Map<Enchantment,Integer> modifyConvertedEnchantments(Map<Enchantment,Integer> origin){
		int removal = 20;
		while(removal != 0){
			Random rnd = new Random();
			int next = rnd.nextInt(origin.size());
			if(origin.get((Enchantment)origin.keySet().toArray()[next]) == 1){
				origin.remove((Enchantment)origin.keySet().toArray()[next]);
			}else{
				origin.put((Enchantment)origin.keySet().toArray()[next], origin.get((Enchantment)origin.keySet().toArray()[next])-1);
			}
			removal --;
		}
		return origin;	
	}
	public static List<Material> getAllowableMaterialList(){
		List<Material> materials = new LinkedList<Material>();
		materials.add(Material.IRON_SWORD);
		materials.add(Material.STONE_SWORD);
		materials.add(Material.GOLD_SWORD);
		materials.add(Material.WOOD_SWORD);
		materials.add(Material.DIAMOND_SWORD);
		materials.add(Material.DIAMOND_BOOTS);
		materials.add(Material.DIAMOND_HELMET);
		materials.add(Material.DIAMOND_LEGGINGS);
		materials.add(Material.DIAMOND_CHESTPLATE);
		materials.add(Material.IRON_BOOTS);
		materials.add(Material.IRON_HELMET);
		materials.add(Material.IRON_LEGGINGS);
		materials.add(Material.IRON_CHESTPLATE);
		materials.add(Material.LEATHER_BOOTS);
		materials.add(Material.LEATHER_HELMET);
		materials.add(Material.LEATHER_LEGGINGS);
		materials.add(Material.LEATHER_CHESTPLATE);
		materials.add(Material.GOLD_BOOTS);
		materials.add(Material.GOLD_HELMET);
		materials.add(Material.GOLD_LEGGINGS);
		materials.add(Material.GOLD_CHESTPLATE);
		materials.add(Material.DIAMOND_SPADE);
		materials.add(Material.DIAMOND_PICKAXE);
		materials.add(Material.DIAMOND_AXE);
		materials.add(Material.DIAMOND_HOE);
		materials.add(Material.IRON_SPADE);
		materials.add(Material.IRON_PICKAXE);
		materials.add(Material.IRON_AXE);
		materials.add(Material.IRON_HOE);
		materials.add(Material.WOOD_SPADE);
		materials.add(Material.WOOD_PICKAXE);
		materials.add(Material.WOOD_AXE);
		materials.add(Material.WOOD_HOE);
		materials.add(Material.GOLD_SPADE);
		materials.add(Material.GOLD_PICKAXE);
		materials.add(Material.GOLD_AXE);
		materials.add(Material.GOLD_CHESTPLATE);
		materials.add(Material.STONE_SPADE);
		materials.add(Material.STONE_PICKAXE);
		materials.add(Material.STONE_AXE);
		materials.add(Material.STONE_HOE);
		return materials;
	}
	public static List<Material> getConvertibleMaterialListResult(){
		List<Material> materials = new LinkedList<Material>();
		materials.add(Material.IRON_SWORD);
		materials.add(Material.STONE_SWORD);
		materials.add(Material.GOLD_SWORD);
		materials.add(Material.DIAMOND_SWORD);
		materials.add(Material.DIAMOND_BOOTS);
		materials.add(Material.DIAMOND_HELMET);
		materials.add(Material.DIAMOND_LEGGINGS);
		materials.add(Material.DIAMOND_CHESTPLATE);
		materials.add(Material.IRON_BOOTS);
		materials.add(Material.IRON_HELMET);
		materials.add(Material.IRON_LEGGINGS);
		materials.add(Material.IRON_CHESTPLATE);
		materials.add(Material.GOLD_BOOTS);
		materials.add(Material.GOLD_HELMET);
		materials.add(Material.GOLD_LEGGINGS);
		materials.add(Material.GOLD_CHESTPLATE);
		materials.add(Material.DIAMOND_SPADE);
		materials.add(Material.DIAMOND_PICKAXE);
		materials.add(Material.DIAMOND_AXE);
		materials.add(Material.DIAMOND_HOE);
		materials.add(Material.IRON_SPADE);
		materials.add(Material.IRON_PICKAXE);
		materials.add(Material.IRON_AXE);
		materials.add(Material.IRON_HOE);
		materials.add(Material.GOLD_SPADE);
		materials.add(Material.GOLD_PICKAXE);
		materials.add(Material.GOLD_AXE);
		materials.add(Material.GOLD_CHESTPLATE);
		materials.add(Material.STONE_SPADE);
		materials.add(Material.STONE_PICKAXE);
		materials.add(Material.STONE_AXE);
		materials.add(Material.STONE_HOE);
		return materials;
	}
	public static List<Material> getConvertibleMaterialList(){
		List<Material> materials = new LinkedList<Material>();
		materials.add(Material.IRON_SWORD);
		materials.add(Material.STONE_SWORD);
		materials.add(Material.GOLD_SWORD);
		materials.add(Material.WOOD_SWORD);
		materials.add(Material.IRON_BOOTS);
		materials.add(Material.IRON_HELMET);
		materials.add(Material.IRON_LEGGINGS);
		materials.add(Material.IRON_CHESTPLATE);
		materials.add(Material.LEATHER_BOOTS);
		materials.add(Material.LEATHER_HELMET);
		materials.add(Material.LEATHER_LEGGINGS);
		materials.add(Material.LEATHER_CHESTPLATE);
		materials.add(Material.GOLD_BOOTS);
		materials.add(Material.GOLD_HELMET);
		materials.add(Material.GOLD_LEGGINGS);
		materials.add(Material.GOLD_CHESTPLATE);
		materials.add(Material.IRON_SPADE);
		materials.add(Material.IRON_PICKAXE);
		materials.add(Material.IRON_AXE);
		materials.add(Material.IRON_HOE);
		materials.add(Material.WOOD_SPADE);
		materials.add(Material.WOOD_PICKAXE);
		materials.add(Material.WOOD_AXE);
		materials.add(Material.WOOD_HOE);
		materials.add(Material.GOLD_SPADE);
		materials.add(Material.GOLD_PICKAXE);
		materials.add(Material.GOLD_AXE);
		materials.add(Material.GOLD_CHESTPLATE);
		materials.add(Material.STONE_SPADE);
		materials.add(Material.STONE_PICKAXE);
		materials.add(Material.STONE_AXE);
		materials.add(Material.STONE_HOE);
		return materials;
	}
	private static List<Material> getAllowableMaterialSwordList(){
		List<Material> materials = new LinkedList<Material>();
		materials.add(Material.IRON_SWORD);
		materials.add(Material.STONE_SWORD);
		materials.add(Material.GOLD_SWORD);
		materials.add(Material.WOOD_SWORD);
		materials.add(Material.DIAMOND_SWORD);
		return materials;
	}
	private static List<Material> getAllowableMaterialArmorList(){
		List<Material> materials = new LinkedList<Material>();
		materials.add(Material.DIAMOND_BOOTS);
		materials.add(Material.DIAMOND_HELMET);
		materials.add(Material.DIAMOND_LEGGINGS);
		materials.add(Material.DIAMOND_CHESTPLATE);
		materials.add(Material.IRON_BOOTS);
		materials.add(Material.IRON_HELMET);
		materials.add(Material.IRON_LEGGINGS);
		materials.add(Material.IRON_CHESTPLATE);
		materials.add(Material.LEATHER_BOOTS);
		materials.add(Material.LEATHER_HELMET);
		materials.add(Material.LEATHER_LEGGINGS);
		materials.add(Material.LEATHER_CHESTPLATE);
		materials.add(Material.GOLD_BOOTS);
		materials.add(Material.GOLD_HELMET);
		materials.add(Material.GOLD_LEGGINGS);
		materials.add(Material.GOLD_CHESTPLATE);
		return materials;
	}
	private static List<Material> getAllowableMaterialToolList(){
		List<Material> materials = new LinkedList<Material>();
		materials.add(Material.DIAMOND_SPADE);
		materials.add(Material.DIAMOND_PICKAXE);
		materials.add(Material.DIAMOND_AXE);
		materials.add(Material.DIAMOND_HOE);
		materials.add(Material.IRON_SPADE);
		materials.add(Material.IRON_PICKAXE);
		materials.add(Material.IRON_AXE);
		materials.add(Material.IRON_HOE);
		materials.add(Material.WOOD_SPADE);
		materials.add(Material.WOOD_PICKAXE);
		materials.add(Material.WOOD_AXE);
		materials.add(Material.WOOD_HOE);
		materials.add(Material.GOLD_SPADE);
		materials.add(Material.GOLD_PICKAXE);
		materials.add(Material.GOLD_AXE);
		materials.add(Material.GOLD_CHESTPLATE);
		materials.add(Material.STONE_SPADE);
		materials.add(Material.STONE_PICKAXE);
		materials.add(Material.STONE_AXE);
		materials.add(Material.STONE_HOE);
		return materials;
	}
	public static Material getConvertedMaterial(Material material){
		if(material == Material.WOOD_AXE) return Material.STONE_AXE;
		if(material == Material.WOOD_PICKAXE) return Material.STONE_PICKAXE;
		if(material == Material.WOOD_SPADE) return Material.STONE_SPADE;
		if(material == Material.WOOD_HOE) return Material.STONE_HOE;
		if(material == Material.STONE_AXE) return Material.GOLD_AXE;
		if(material == Material.STONE_PICKAXE) return Material.GOLD_PICKAXE;
		if(material == Material.STONE_SPADE) return Material.GOLD_SPADE;
		if(material == Material.STONE_HOE) return Material.GOLD_HOE;
		if(material == Material.GOLD_AXE) return Material.IRON_AXE;
		if(material == Material.GOLD_PICKAXE) return Material.IRON_PICKAXE;
		if(material == Material.GOLD_SPADE) return Material.IRON_SPADE;
		if(material == Material.GOLD_HOE) return Material.IRON_HOE;
		if(material == Material.IRON_AXE) return Material.DIAMOND_AXE;
		if(material == Material.IRON_PICKAXE) return Material.DIAMOND_PICKAXE;
		if(material == Material.IRON_SPADE) return Material.DIAMOND_SPADE;
		if(material == Material.IRON_HOE) return Material.DIAMOND_HOE;
		if(material == Material.LEATHER_CHESTPLATE) return Material.GOLD_CHESTPLATE;
		if(material == Material.LEATHER_LEGGINGS) return Material.GOLD_LEGGINGS;
		if(material == Material.LEATHER_HELMET) return Material.GOLD_HELMET;
		if(material == Material.LEATHER_BOOTS) return Material.GOLD_BOOTS;
		if(material == Material.GOLD_CHESTPLATE) return Material.IRON_CHESTPLATE;
		if(material == Material.GOLD_LEGGINGS) return Material.IRON_LEGGINGS;
		if(material == Material.GOLD_HELMET) return Material.IRON_HELMET;
		if(material == Material.GOLD_BOOTS) return Material.IRON_BOOTS;
		if(material == Material.IRON_CHESTPLATE) return Material.DIAMOND_CHESTPLATE;
		if(material == Material.IRON_LEGGINGS) return Material.DIAMOND_LEGGINGS;
		if(material == Material.IRON_HELMET) return Material.DIAMOND_HELMET;
		if(material == Material.IRON_BOOTS) return Material.DIAMOND_BOOTS;
		if(material == Material.WOOD_SWORD) return Material.STONE_SWORD;
		if(material == Material.STONE_SWORD) return Material.GOLD_SWORD;
		if(material == Material.GOLD_SWORD) return Material.IRON_SWORD;
		if(material == Material.IRON_SWORD) return Material.DIAMOND_SWORD;


		return Material.STONE_AXE;
	}

	private static boolean isSame(Inventory inv){
		Material mat = inv.getContents()[1].getType();
		for(int i = 1;i<inv.getContents().length;i++){
			if(inv.getContents()[i].getType() != mat) return false;
		}
		return true;

	}
	private static boolean isEmpty(Inventory inv){
		for(int i = 1;i<inv.getContents().length;i++){
			if(inv.getContents()[i].getType() != Material.AIR) return false;
		}
		return true;
	}
}
