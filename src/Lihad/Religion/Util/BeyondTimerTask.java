package Lihad.Religion.Util;

import java.util.TimerTask;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import Lihad.Religion.Information.BeyondInfo;


public class BeyondTimerTask extends TimerTask {

	@Override
	public void run(){
		try{
			for(int i=0;i<BeyondInfo.getTowersAll().size();i++){
				Location location = BeyondInfo.getTowerLocation(BeyondInfo.getTowersAll().get(i));
				Chest chest = (Chest) location.getBlock().getState();
				int amount = 0;
				for(int j=0;j<27;j++){
					if(chest.getInventory().getItem(j).getType() == Material.GOLD_INGOT){
						amount = amount+chest.getInventory().getItem(j).getAmount();
					}
				}
				if(amount > BeyondInfo.getTowerInfluence(BeyondInfo.getTowersAll().get(i))){
					BeyondInfo.setTowerInfluence(BeyondInfo.getReligion(BeyondInfo.getTowersAll().get(i)), BeyondInfo.getTowersAll().get(i), BeyondInfo.getTowerInfluence(BeyondInfo.getTowersAll().get(i))+1);
				}else if(amount < BeyondInfo.getTowerInfluence(BeyondInfo.getTowersAll().get(i))){
					BeyondInfo.setTowerInfluence(BeyondInfo.getReligion(BeyondInfo.getTowersAll().get(i)), BeyondInfo.getTowersAll().get(i), BeyondInfo.getTowerInfluence(BeyondInfo.getTowersAll().get(i))-1);
				}else{
					
				}
			}
		}catch(Exception e){
			System.out.println("NO TOWER EXISTS");

		}


	}

}
