package Lihad.Religion.Recipe;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapelessRecipe;

import Lihad.Religion.Religion;
import Lihad.Religion.Abilities.ReverseEngineering;

public class Recipes {
	public static Religion plugin;
	public List<Recipe> REVERSE_ENGINEERING_RECIPES;
	public List<Recipe> WEAPON_UPGRADE_RECIPES;

	
	public Recipes(Religion instance) {
		plugin = instance;
		REVERSE_ENGINEERING_RECIPES = loadReverseEngineeringRecipes();
		WEAPON_UPGRADE_RECIPES = loadWeaponUpgradeRecipes();
	}
	
	public static Recipe customFull(Material input, Material output){
		ItemStack item = new ItemStack(output, 1);
		ShapelessRecipe recipe = new ShapelessRecipe(item);
		recipe.addIngredient(9, input);
		return recipe;
	}
	public static Recipe customSingle(Material input, Material output){
		ItemStack item = new ItemStack(output, 1);
		item.setDurability((short) 10000);
		ShapelessRecipe recipe = new ShapelessRecipe(item);
		recipe.addIngredient(1, input);
		return recipe;
	}
	private static List<Recipe> loadReverseEngineeringRecipes(){
		List<Recipe> recipes = new LinkedList<Recipe>();
		for(int i = 0;i<ReverseEngineering.getAllowableMaterialList().size();i++){
			recipes.add(Recipes.customFull(ReverseEngineering.getAllowableMaterialList().get(i), ReverseEngineering.getAllowableMaterialList().get(i)));
		}
		return recipes;
	}
	private static List<Recipe> loadWeaponUpgradeRecipes(){
		List<Recipe> recipes = new LinkedList<Recipe>();
		for(int i = 0;i<ReverseEngineering.getConvertibleMaterialList().size();i++){
			recipes.add(Recipes.customSingle(ReverseEngineering.getConvertibleMaterialList().get(i), ReverseEngineering.getConvertedMaterial(ReverseEngineering.getConvertibleMaterialList().get(i))));
		}
		return recipes;
	}
}
