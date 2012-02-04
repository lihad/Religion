package Lihad.Religion.Util;

import java.util.TimerTask;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

import Lihad.Religion.Religion;


public class BeyondTimerTask extends TimerTask {

	@Override
	public void run(){
		try{
			System.out.println("Timer");
			for(int i=0;i<Religion.info.getTowersAll().size();i++){
				Location location = Religion.info.getTowerLocation(Religion.info.getReligion(Religion.info.getTowersAll().get(i)), Religion.info.getTowersAll().get(i));
				Chest chest = (Chest) location.getBlock().getState();
				int amount = 0;
				for(int j=0;j<27;j++){
					if(chest.getInventory().getItem(j).getType() == Material.GOLD_INGOT){
						amount = amount+chest.getInventory().getItem(j).getAmount();
					}
				}
				if(amount > Religion.info.getTowerInfluence(Religion.info.getReligion(Religion.info.getTowersAll().get(i)), Religion.info.getTowersAll().get(i))){
					Religion.info.setTowerInfluence(Religion.info.getReligion(Religion.info.getTowersAll().get(i)), Religion.info.getTowersAll().get(i), Religion.info.getTowerInfluence(Religion.info.getReligion(Religion.info.getTowersAll().get(i)), Religion.info.getTowersAll().get(i))+1);
				}else if(amount < Religion.info.getTowerInfluence(Religion.info.getReligion(Religion.info.getTowersAll().get(i)), Religion.info.getTowersAll().get(i))){
					Religion.info.setTowerInfluence(Religion.info.getReligion(Religion.info.getTowersAll().get(i)), Religion.info.getTowersAll().get(i), Religion.info.getTowerInfluence(Religion.info.getReligion(Religion.info.getTowersAll().get(i)), Religion.info.getTowersAll().get(i))-1);
				}else{
					
				}
				System.out.println("Influence = "+Religion.info.getTowerInfluence(Religion.info.getReligion(Religion.info.getTowersAll().get(i)), Religion.info.getTowersAll().get(i)));

			}
		}catch(Exception e){
			System.out.println("NO TOWER EXISTS");

		}


	}

}
