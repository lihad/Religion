package Lihad.Religion.Information;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import Lihad.Religion.Religion;

public class BeyondInfo {
	public static Religion plugin;


	public BeyondInfo(Religion instance) {
		plugin = instance;
	}
	
	//get Functions
	
	/**
	 * @return A list of all the Religions, string form.
	 */
	public List<String> getReligions(){
		return BeyondInfoReader.getKeyList("Religions");
	}
	/**
	 * @param towername
	 * @return Returns the religion associated with the tower.
	 */
	public String getReligion(String towername){
		List<String> religionList = getReligions();
		for(int i = 0; i<religionList.size();i++){
			if(getTowers(religionList.get(i)).contains(towername)) return religionList.get(i);
		}
		return null;
	}
	/**
	 * @param player
	 * @return Returns the religion of the player. null if the player has no religion
	 */
	public String getReligion(Player player){
		List<String> religionList = getReligions();
		for(int i = 0; i<religionList.size();i++){
			if(getTowers(religionList.get(i)).contains(getTowerName(player))) return religionList.get(i);
		}
		return null;
	}
	/**
	 * @param location
	 * @return Returns religion associated with the given location. null if location has no religion
	 */
	public String getReligion(Location location){
		List<String> religions = getReligions();

		
		for(int i = 0;i<religions.size();i++){
			List<String> towers = getTowers(religions.get(i));
			for(int j = 0;j<towers.size();j++){
				System.out.println("Check 2 - "+religions.get(i)+" "+ towers.get(j));
				if(getTowerLocation(religions.get(i),towers.get(j)).equals(location)){
					System.out.println("Check 3");
					return religions.get(i);
				}
			}
		}
		return null;
	}
	/**
	 * @param religion
	 * @return Returns a list of towers from a specified religion
	 */
	public List<String> getTowers(String religion){
		return BeyondInfoReader.getKeyList("Religions."+religion+".Towers");
	}
	/**
	 * @param location
	 * @return Returns the towername associated with a given location.  null if no tower associated with location
	 */
	public String getTower(Location location){
		List<String> religions = getReligions();
		for(int i = 0;i<religions.size();i++){
			List<String> towers = getTowers(religions.get(i));
			for(int j = 0;j<towers.size();j++){
				System.out.println("Check 2 - "+religions.get(i)+" "+ towers.get(j));
				if(getTowerLocation(religions.get(i),towers.get(j)).equals(location)){
					System.out.println("Check 3");
					return towers.get(j);
				}
			}
		}
		return null;
	}
	/**
	 * 
	 * @return Returns a list of all towers
	 */
	public List<String> getTowersAll(){
		List<String> all = new ArrayList<String>();
		for(int i=0;i<getReligions().size();i++){
			all.addAll(getTowers(getReligions().get(i)));
		}
		return all;
	}
	/**
	 * 
	 * @param religion
	 * @param towername
	 * @return Returns tower name associated with the given params
	 */
	public String getTowerName(String religion, String towername){
		return BeyondInfoReader.getString("Religions."+religion+".Towers."+towername+".Name");
	}
	/**
	 * NOTE: using this when player has no defined religion will result in a nullpointer
	 * @param player
	 * @return Returns tower name player is associated with
	 */
	public String getTowerName(Player player){
		return BeyondInfoReader.getString("Religions."+getPlayerPath(player)+".Name");
	}
	/**
	 * 
	 * @param religion
	 * @param towername
	 * @return Returns the tower (chest) location based on the given params
	 */
	public Location getTowerLocation(String religion, String towername){
		String[] array;
		String string = BeyondInfoReader.getString("Religions."+religion+".Towers."+towername+".Location");
		array = string.split(",");
		Location location = new Location(plugin.getServer().getWorld(array[3]), Integer.parseInt(array[0]), Integer.parseInt(array[1]), Integer.parseInt(array[2]));
		return location;
	}
	/**
	 * NOTE: using this when player has no defined religion will result in a nullpointer
	 * @param player
	 * @return Returns the given tower (chest) location of a tower given player is associated with
	 */
	public Location getTowerLocation(Player player){
		String[] array;
		String string = BeyondInfoReader.getString("Religions."+getPlayerPath(player)+".Location");
		array = string.split(",");
		Location location = new Location(plugin.getServer().getWorld(array[3]), Integer.parseInt(array[0]), Integer.parseInt(array[1]), Integer.parseInt(array[2]));
		return location;
	}
	/**
	 * 
	 * @param religion
	 * @param towername
	 * @return Returns tower member count based on given params
	 */
	public int getTowerMemberCount(String religion, String towername){
		return BeyondInfoReader.getInt("Religions."+religion+".Towers."+towername+".Members");
	}
	/**
	 * NOTE: using this when player has no defined religion will result in a nullpointer
	 * @param player
	 * @return Returns tower member count of player's associated tower
	 */
	public int getTowerMemberCount(Player player){
		return BeyondInfoReader.getInt("Religions."+getPlayerPath(player)+".Members");
	}
	/**
	 * NOTE: This is currently not in use
	 * @param religion
	 * @param towername
	 * @return Returns tower gold as defined in the information.yml
	 */
	public int getTowerGold(String religion, String towername){
		return BeyondInfoReader.getInt("Religions."+religion+".Towers."+towername+".Gold");
	}
	/**
	 * NOTE: using this when player has no defined religion will result in a nullpointer
	 * NOTE: This is currently not in use
	 * @param player
	 * @return Returns tower gold as defined in the information.yml for given player associated with a religion
	 */
	public int getTowerGold(Player player){
		return BeyondInfoReader.getInt("Religions."+getPlayerPath(player)+".Gold");
	}
	/**
	 * 
	 * @param religion
	 * @param towername
	 * @return Returns tower influence based on given params
	 */
	public int getTowerInfluence(String religion, String towername){
		return BeyondInfoReader.getInt("Religions."+religion+".Towers."+towername+".Influence");
	}
	/**
	 * NOTE: using this when player has no defined religion will result in a nullpointer
	 * @param player
	 * @return Returns tower influence of a player associated with a tower
	 */
	public int getTowerInfluence(Player player){
		return BeyondInfoReader.getInt("Religions."+getPlayerPath(player)+".Influence");
	}
	/**
	 * NOTE: using this when player has no defined religion will result in a nullpointer
	 * @param player
	 * @return Returns a predefined yaml path that is given to every player.  This path defines religion and tower of a player.
	 */
	public String getPlayerPath(Player player){
		return BeyondInfoReader.getString("Players."+player.getName());
	}
	/**
	 * 
	 * @param towername
	 * @return Returns the radius of the AoE tower effect
	 */
	public int getTowerAoE(String towername){
		int aoe = (int) (((getTowerMemberCount(getReligion(towername), towername))*(Religion.config.getMemberBonus()))+(((double)getTowerInfluence(getReligion(towername), towername)) / 1728.0)*(double)(Religion.config.getMaximumAoE()));
		if(aoe>Religion.config.getMaximumAoE())return Religion.config.getMaximumAoE();
		else return aoe;	
	}
	/**
	 * 
	 * @param location
	 * @param towername
	 * @return Returns the vector-length distance of a Location to tower
	 */
	public double getDistanceToTower(Location location, String towername){
		return Math.sqrt(Math.pow((location.getBlockX()-getTowerLocation(getReligion(towername), towername).getBlockX()), 2)+ Math.pow((location.getBlockZ()-getTowerLocation(getReligion(towername), towername).getBlockZ()), 2));
	}
	/**
	 * 
	 * @param location
	 * @return Returns the closest tower to the location as long as the location exists within a towers AoE.  Returns null otherwise
	 */
	public String getClosestValidTower(Location location){
		List<String> alltowers = getTowersAll();
		String closest = null;
		double distance = Religion.config.getMaximumAoE();
		for(int i = 0;i<alltowers.size();i++){
			if(getDistanceToTower(location, alltowers.get(i))<distance && isTowerArea(location, alltowers.get(i))){
				distance = getDistanceToTower(location, alltowers.get(i));
				closest = alltowers.get(i);
			}
		}
		return closest;
	}
	/**
	 * 
	 * @return Returns all players associated with any religion
	 */
	public List<Player> getPlayers(){
		List<String> strings = BeyondInfoReader.getKeyList("Players");
		List<Player> players = new ArrayList<Player>();
		for(int i=0;i<strings.size();i++){
			players.add(plugin.getServer().getPlayer(strings.get(i)));
		}
		return players;
	}
	/**
	 * 
	 * @param towername
	 * @return Returns all players associated with param towername
	 */
	public List<Player> getTowerPlayers(String towername){
		List<Player> players = new ArrayList<Player>();
		for(int i=0;i<getPlayers().size();i++){
			if(getTowerName(getPlayers().get(i)).equals(towername)){
				players.add(getPlayers().get(i));
			}
		}
		return players;
	}
	public List<Player> getReligionPlayers(String religion){
		List<Player> players = new ArrayList<Player>();
		List<Player> playersall = getPlayers();
		for(int i=0;i<playersall.size();i++){
			if(getPlayerPath(playersall.get(i)).contains(religion)){
				players.add(playersall.get(i));
			}
		}
		return players;
	}
	//has Functions
	/**
	 * Simply checks to see if player exists in the information.yml
	 */
	public boolean hasPlayer(Player player){
		try{ return getPlayers().contains(player.getName()); }
		catch(Exception e){ return false; }
	}
	public boolean hasTower(String towername){
		try{
			List<String> religionList = getReligions();
			for(int i = 0; i<religionList.size();i++){
				if(getTowers(religionList.get(i)).contains(towername)) return true;
			}
			return false;			}
		catch(Exception e){ return false; }
	}
	//set Functions
	private void setTowerName(String religion, String towername){
		BeyondInfoWriter.writeConfigurationString("Religions."+religion+".Towers."+towername+".Name", towername);
	}
	private void setTowerName(Player player, String towername){
		BeyondInfoWriter.writeConfigurationString("Religions."+getPlayerPath(player)+".Name", towername);
	}
	private void setTowerLocation(String religion, String towername, int x, int y, int z, World world){
		BeyondInfoWriter.writeConfigurationString("Religions."+religion+".Towers."+towername+".Location", x+","+y+","+z+","+world.getName());
	}
	private void setTowerLocation(String religion, String towername, Location location){
		BeyondInfoWriter.writeConfigurationString("Religions."+religion+".Towers."+towername+".Location", location.getBlockX()+","+location.getBlockY()+","+location.getBlockZ()+","+location.getWorld().getName());
	}
	private void setTowerLocation(Player player, int x, int y, int z, World world){
		BeyondInfoWriter.writeConfigurationString("Religions."+getPlayerPath(player)+".Location", x+","+y+","+z+","+world.getName());
	}
	private void setTowerLocation(Player player, Location location){
		BeyondInfoWriter.writeConfigurationString("Religions."+getPlayerPath(player)+".Location", location.getBlockX()+","+location.getBlockY()+","+location.getBlockZ()+","+location.getWorld().getName());
	}
	private void setTowerMemberCount(String religion, String towername, int arg){
		BeyondInfoWriter.writeConfigurationInt("Religions."+religion+".Towers."+towername+".Members", arg);
	}
	private void setTowerMemberCount(Player player, int arg){
		BeyondInfoWriter.writeConfigurationInt("Religions."+getPlayerPath(player)+".Members", arg);
	}
	public void setTowerGold(String religion, String towername, int arg){
		BeyondInfoWriter.writeConfigurationInt("Religions."+religion+".Towers."+towername+".Gold", arg);
	}
	public void setTowerGold(Player player, int arg){
		BeyondInfoWriter.writeConfigurationInt("Religions."+getPlayerPath(player)+".Gold", arg);
	}
	public void setTowerInfluence(String religion, String towername, int arg){
		BeyondInfoWriter.writeConfigurationInt("Religions."+religion+".Towers."+towername+".Influence", arg);
	}
	public void setTowerInfluence(Player player, int arg){
		BeyondInfoWriter.writeConfigurationInt("Religions."+getPlayerPath(player)+".Influence", arg);
	}
	private void setPlayerPath(Player player, String religion, String towername){
		BeyondInfoWriter.writeConfigurationString("Players."+player.getName(), religion+".Towers."+towername);
	}
	//add Functions
	public void addPlayer(Player player, String religion, String towername){
		setPlayerPath(player,religion,towername);
		int count = getTowerMemberCount(player); 
		setTowerMemberCount(player, count+1);
	}
	public void addTower(Player player, String religion, String towername, Location location){
		setTowerName(religion,towername);
		setTowerLocation(religion, towername, location);
		setTowerMemberCount(religion, towername, 0);
		addPlayer(player, religion, towername);
	}
	//remove Functions
	/**
	 * Removes player path, effectively removing a player from tower and religion 
	 */
	public void removePlayer(Player player){
		if(hasPlayer(player)){
			int count = getTowerMemberCount(player);
			setTowerMemberCount(player, count-1);
			BeyondInfoWriter.writeConfigurationNull("Players."+player.getName());
		}else{
			System.out.println("[Religion] Player, "+player.getName()+", was attmepted to be removed.  Failed for a reason or another");
		}
	}
	/**
	 * TODO: Have this method also remove any player who is a member of this tower
	 * Removes tower.
	 * @param religion
	 * @param towername
	 */
	public void removeTower(String religion, String towername){
		BeyondInfoWriter.writeConfigurationNull("Religions."+religion+".Towers."+towername);
	}
	//is Functions
	public boolean isTowerArea(Location location, String towername){
		if(Math.pow((location.getBlockX()-getTowerLocation(getReligion(towername), towername).getBlockX()), 2)+ Math.pow((location.getBlockZ()-getTowerLocation(getReligion(towername), towername).getBlockZ()), 2) < Math.pow(getTowerAoE(towername), 2)) return true;
		else return false;
	}
}

