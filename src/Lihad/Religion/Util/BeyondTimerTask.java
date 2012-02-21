package Lihad.Religion.Util;

import java.util.TimerTask;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.plugin.Plugin;

import Lihad.Religion.Religion;
import Lihad.Religion.Bosses.Bosses;
import Lihad.Religion.Config.BeyondConfig;
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

				//Influence = Influence + InfluenceDelta (this will run every time the timer does, so this needs to be low)
				//BeyondInfo.setTowerInfluence(BeyondInfo.getTowersAll().get(i), BeyondInfo.getTowerInfluence(BeyondInfo.getTowersAll().get(i)) + BeyondInfo.getTowerInfluenceDelta(BeyondInfo.getTowersAll().get(i)));
				//Right now this will get overwritten by the lines below.
				//Eventually, want gold to modify InfluenceDelta, but at a *much* lower rate.  Have to think on this.  -- Joren
				
				// Joren - instead, just adjust the timer rate to the rate you want the growth to occur at, unless you want it to be exponential

				if(amount > BeyondInfo.getTowerInfluence(BeyondInfo.getTowersAll().get(i))){
					BeyondInfo.setTowerInfluence(BeyondInfo.getTowersAll().get(i), BeyondInfo.getTowerInfluence(BeyondInfo.getTowersAll().get(i))+1);
				}else if(amount < BeyondInfo.getTowerInfluence(BeyondInfo.getTowersAll().get(i))){
					BeyondInfo.setTowerInfluence(BeyondInfo.getTowersAll().get(i), BeyondInfo.getTowerInfluence(BeyondInfo.getTowersAll().get(i))-1);
				}else{
					
				}
			}
			
			Bosses.chunk = BeyondConfig.getAhkmedLocation().getChunk();

			Religion.trades.driver();
			Religion.information.save();
		}catch(Exception e){
			System.out.println("[Religion] [ERROR] [NO TOWER] Lihad knows why you are seeing this, ask him");
			e.printStackTrace();

		}
	}
}
//1927 50 997
