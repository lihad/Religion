package Lihad.Religion.Information;

import java.util.ArrayList;
import java.util.Arrays;
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
	public static List<String> getReligions(){
		return BeyondInfoReader.getKeyList("Religions");
	}
	/**
	 * @param towername
	 * @return Returns the religion associated with the tower.
	 */
	public static String getReligion(String towername){
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
	public static String getReligion(Player player){
		List<String> religionList = getReligions();
		for(int i = 0; i<religionList.size();i++){
			if(getTowers(religionList.get(i)).contains(getTowerName(player))) return religionList.get(i);
		}
		return null;
	}
	/**
	 * @param location
	 * @return Returns religion associated with the given location (chest location). null if location has no religion
	 */
	public static String getReligion(Location location){
		List<String> religions = getReligions();

		
		for(int i = 0;i<religions.size();i++){
			List<String> towers = getTowers(religions.get(i));
			for(int j = 0;j<towers.size();j++){
				if(getTowerLocation(towers.get(j)).equals(location)){
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
	public static List<String> getTowers(String religion){
		return BeyondInfoReader.getKeyList("Religions."+religion+".Towers");
	}
	/**
	 * @param location
	 * @return Returns the towername associated with a given location (chest location).  null if no tower associated with location
	 */
	public static String getTower(Location location){
		List<String> religions = getReligions();
		for(int i = 0;i<religions.size();i++){
			List<String> towers = getTowers(religions.get(i));
			for(int j = 0;j<towers.size();j++){
				if(getTowerLocation(towers.get(j)).equals(location)){
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
	public static List<String> getTowersAll(){
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
	//TODO:...... so... I need the name of the tower in order to get the name of the tower? ..... derp?  -- Joren
	public static String getTowerName(String religion, String towername){
		return BeyondInfoReader.getString("Religions."+religion+".Towers."+towername+".Name");
	}
	/**
	 * NOTE: using this when player has no defined religion will result in a nullpointer
	 * @param player
	 * @return Returns tower name player is associated with
	 */
	public static String getTowerName(Player player){
		return BeyondInfoReader.getString("Religions."+getPlayerPath(player)+".Name");
	}
	/**
	 * 
	 * @param towername
	 * @return Returns the tower (chest) location based on the given params
	 */
	public static Location getTowerLocation(String towername){
		String[] array;
		String string = BeyondInfoReader.getString("Religions."+getReligion(towername) + ".Towers."+towername+".Location");
		array = string.split(",");
		Location location = new Location(plugin.getServer().getWorld(array[3]), Integer.parseInt(array[0]), Integer.parseInt(array[1]), Integer.parseInt(array[2]));
		return location;
	}
	/**
	 * NOTE: using this when player has no defined religion will result in a nullpointer
	 * @param player
	 * @return Returns the given tower (chest) location of a tower given player is associated with
	 */
	public static Location getTowerLocation(Player player){
		String[] array;
		String string = BeyondInfoReader.getString("Religions."+getPlayerPath(player)+".Location");
		array = string.split(",");
		Location location = new Location(plugin.getServer().getWorld(array[3]), Integer.parseInt(array[0]), Integer.parseInt(array[1]), Integer.parseInt(array[2]));
		return location;
	}
	/**
	 * 
	 * @param towername
	 * @return Returns tower member count based on given params
	 */
	public static int getTowerMemberCount(String towername){
		return BeyondInfoReader.getInt("Religions."+getReligion(towername)+".Towers."+towername+".Members");
	}
	/**
	 * NOTE: using this when player has no defined religion will result in a nullpointer
	 * @param player
	 * @return Returns tower member count of player's associated tower
	 */
	public static int getTowerMemberCount(Player player){
		return BeyondInfoReader.getInt("Religions."+getPlayerPath(player)+".Members");
	}
	/**
	 * NOTE: This is currently not in use
	 * @param towername
	 * @return Returns tower gold as defined in the information.yml
	 */
	public static int getTowerGold(String towername){
		return BeyondInfoReader.getInt("Religions."+getReligion(towername)+".Towers."+towername+".Gold");
	}
	
	/**
	 * @param towername
	 * @return Returns the maximum amount of gold tower has ever held
	 */
	public static void getTowerMaxGold(String towername){
		BeyondInfoReader.getInt("Religions."+getReligion(towername)+".Towers."+towername+".MaxGold");
	}

	/**
	 * NOTE: using this when player has no defined religion will result in a nullpointer
	 * NOTE: This is currently not in use
	 * @param player
	 * @return Returns tower gold as defined in the information.yml for given player associated with a religion
	 */
	public static int getTowerGold(Player player){
		return BeyondInfoReader.getInt("Religions."+getPlayerPath(player)+".Gold");
	}
	/**
	 * 
	 * @param towername
	 * @return Returns tower influence rate of change based on given params
	 */
	public static double getTowerInfluenceDelta(String towername){
		return BeyondInfoReader.getDouble("Religions."+getReligion(towername)+".Towers."+towername+".Influence.Delta");
	}
	/**
	 * NOTE: using this when player has no defined religion will result in a nullpointer
	 * @param player
	 * @return Returns tower influence rate of change of a player associated with a tower
	 */
	public static double getTowerInfluenceDelta(Player player){
		return BeyondInfoReader.getDouble("Religions."+getPlayerPath(player)+".Influence.Delta");
	}
	/**
	 * 
	 * @param towername
	 * @return Returns tower influence based on given params
	 */
	public static double getTowerInfluence(String towername){
		return BeyondInfoReader.getDouble("Religions."+getReligion(towername)+".Towers."+towername+".Influence");
	}
	/**
	 * NOTE: using this when player has no defined religion will result in a nullpointer
	 * @param player
	 * @return Returns tower influence of a player associated with a tower
	 */
	public static double getTowerInfluence(Player player){
		return BeyondInfoReader.getDouble("Religions."+getPlayerPath(player)+".Influence");
	}
	/**
	 * 
	 * @param towername
	 * @return Returns the Leader of a tower.   String form.
	 */
	public static String getLeader(String towername){
		return BeyondInfoReader.getString("Religions."+getReligion(towername)+".Towers."+towername+".Leader");
	}
	/**
	 * NOTE: using this when player has no defined religion will result in a nullpointer
	 * @param player
	 * @return Returns the Leader of a tower of a player associated with the tower.  String form.
	 */
	public static String getLeader(Player player){
		return BeyondInfoReader.getString("Religions."+getPlayerPath(player)+".Leader");
	}
	/**
	 * 
	 * @param towername
	 * @return Returns the trusted list for a given tower. Null if no players exist.
	 */
	public static List<String> getTrustedList(String towername){
		String string = BeyondInfoReader.getString("Religions."+getReligion(towername)+".Towers."+towername+".Trusted");
		if(string == null) return null;
		List<String> list = Arrays.asList(string.split(","));
		return list;
	}
	/**
	 * NOTE: using this when player has no defined religion will result in a nullpointer
	 * @param player
	 * @return  Returns the trusted list for a given tower. Null if no players exist. For associated towername of a given player
	 */
	public static List<String> getTrustedList(Player player){
		String string = BeyondInfoReader.getString("Religions."+getPlayerPath(player)+".Trusted");
		if(string == null) return null;
		List<String> list = Arrays.asList(string.split(","));
		return list;
	}
	/**
	 * NOT FOR USE OUTSIDE THIS CLASS
	 */
	private static String getTrustedListRaw(String towername){
		return BeyondInfoReader.getString("Religions."+getReligion(towername)+".Towers."+towername+".Trusted");
	}
	/**
	 * NOT FOR USE OUTSIDE THIS CLASS
	 */
	private static String getTrustedListRaw(Player player){
		return BeyondInfoReader.getString("Religions."+getPlayerPath(player)+".Trusted");
	}
	/**
	 * 
	 * @param towername
	 * @return Returns the trusted list for a given tower. Null if no players exist.
	 */
	public static List<String> getInvitedList(String towername){
		String string = BeyondInfoReader.getString("Religions."+getReligion(towername)+".Towers."+towername+".Invited");
		if(string == null) return null;
		List<String> list = Arrays.asList(string.split(","));
		return list;
	}
	/**
	 * NOTE: using this when player has no defined religion will result in a nullpointer
	 * @param player
	 * @return  Returns the trusted list for a given tower. Null if no players exist. For associated towername of a given player
	 */
	public static List<String> getInvitedList(Player player){
		String string = BeyondInfoReader.getString("Religions."+getPlayerPath(player)+".Invited");
		if(string == null) return null;
		List<String> list = Arrays.asList(string.split(","));
		return list;
	}
	/**
	 * NOT FOR USE OUTSIDE THIS CLASS
	 */
	private static String getInvitedListRaw(String towername){
		return BeyondInfoReader.getString("Religions."+getReligion(towername)+".Towers."+towername+".Invited");
	}
	/**
	 * NOT FOR USE OUTSIDE THIS CLASS
	 */
	private static String getInvitedListRaw(Player player){
		return BeyondInfoReader.getString("Religions."+getPlayerPath(player)+".Invited");
	}
	/**
	 * NOTE: using this when player has no defined religion will result in a nullpointer
	 * @param player
	 * @return Returns a predefined yaml path that is given to every player.  This path defines religion and tower of a player.
	 */
	public static String getPlayerPath(Player player){
		return BeyondInfoReader.getString("Players."+player.getName());
	}
	/**
	 * 
	 * @param towername
	 * @return Returns the radius of the AoE tower effect
	 */
	public static int getTowerAoE(String towername){
		int aoe = (int) (((getTowerMemberCount(towername))*(Religion.config.getMemberBonus()))+(getTowerInfluence(towername) / 1728.0)*(double)(Religion.config.getMaximumAoE()));
		if(aoe>Religion.config.getMaximumAoE())return Religion.config.getMaximumAoE();
		else return aoe;	
	}
	/**
	 * 
	 * @param location
	 * @param towername
	 * @return Returns the vector-length distance of a Location to tower
	 */
	public static double getDistanceToTower(Location location, String towername){
		return Math.sqrt(Math.pow((location.getBlockX()-getTowerLocation(towername).getBlockX()), 2)+ Math.pow((location.getBlockZ()-getTowerLocation(towername).getBlockZ()), 2));
	}
	/**
	 * 
	 * @param location
	 * @return Returns the closest tower to the location as long as the location exists within a towers AoE.  Returns null otherwise
	 */
	public static String getClosestValidTower(Location location){
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
	 * @param location
	 * @return Returns a list of all players in a tower
	 */
	public static List<String> getTowersOfLocation(Location location) {
		List<String> alltowers = getTowersAll();
		List<String> towers = new ArrayList<String>();
		for (int i = 0; i < alltowers.size(); i++) {
			if (isTowerArea(location, alltowers.get(i)))
				towers.add(alltowers.get(i));
		}
		return towers;

	}
	/**
	 * 
	 * @return Returns all players associated with any religion
	 */
	public static List<Player> getPlayers(){
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
	public static List<Player> getTowerPlayers(String towername){
		List<Player> players = new ArrayList<Player>();
		for(int i=0;i<getPlayers().size();i++){
			if(getTowerName(getPlayers().get(i)).equals(towername)){
				players.add(getPlayers().get(i));
			}
		}
		return players;
	}
	public static List<Player> getReligionPlayers(String religion){
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
	public static boolean hasPlayer(Player player){
		try{ return getPlayers().contains(player); }
		catch(Exception e){ return false; }
	}
	public static boolean hasTower(String towername){
		try{
			List<String> religionList = getReligions();
			for(int i = 0; i<religionList.size();i++){
				if(getTowers(religionList.get(i)).contains(towername)) return true;
			}
			return false;			}
		catch(Exception e){ return false; }
	}
	//set Functions
	private static void setTowerName(String religion, String towername){
		BeyondInfoWriter.writeConfigurationString("Religions."+religion+".Towers."+towername+".Name", towername);
	}
	private static void setTowerName(Player player, String towername){
		BeyondInfoWriter.writeConfigurationString("Religions."+getPlayerPath(player)+".Name", towername);
	}
	private static void setTowerLocation(String towername, int x, int y, int z, World world){
		BeyondInfoWriter.writeConfigurationString("Religions."+getReligion(towername)+".Towers."+towername+".Location", x+","+y+","+z+","+world.getName());
	}
	private static void setTowerLocation(String towername, Location location){
		BeyondInfoWriter.writeConfigurationString("Religions."+getReligion(towername)+".Towers."+towername+".Location", location.getBlockX()+","+location.getBlockY()+","+location.getBlockZ()+","+location.getWorld().getName());
	}
	private static void setTowerLocation(Player player, int x, int y, int z, World world){
		BeyondInfoWriter.writeConfigurationString("Religions."+getPlayerPath(player)+".Location", x+","+y+","+z+","+world.getName());
	}
	private static void setTowerLocation(Player player, Location location){
		BeyondInfoWriter.writeConfigurationString("Religions."+getPlayerPath(player)+".Location", location.getBlockX()+","+location.getBlockY()+","+location.getBlockZ()+","+location.getWorld().getName());
	}
	private static void setTowerMemberCount(String towername, int arg){
		BeyondInfoWriter.writeConfigurationInt("Religions."+getReligion(towername)+".Towers."+towername+".Members", arg);
	}
	private static void setTowerMemberCount(Player player, int arg){
		BeyondInfoWriter.writeConfigurationInt("Religions."+getPlayerPath(player)+".Members", arg);
	}
	public static void setTowerGold(String towername, int arg){
		BeyondInfoWriter.writeConfigurationInt("Religions."+getReligion(towername)+".Towers."+towername+".Gold", arg);
	}
	public static void setTowerMaxGold(String towername, int arg){
		BeyondInfoWriter.writeConfigurationInt("Religions."+getReligion(towername)+".Towers."+towername+".MaxGold", arg);
	}
	public static void setTowerGold(Player player, int arg){ //what's this do?  I don't follow  --Joren
		BeyondInfoWriter.writeConfigurationInt("Religions."+getPlayerPath(player)+".Gold", arg);
	}
	public static void setTowerInfluence(String towername, double arg){
		BeyondInfoWriter.writeConfigurationDouble("Religions."+getReligion(towername)+".Towers."+towername+".Influence", arg);
	}
	public static void setTowerInfluence(Player player, double arg){
		BeyondInfoWriter.writeConfigurationDouble("Religions."+getPlayerPath(player)+".Influence", arg);
	}
	public static void setTowerInfluenceDelta(String towername, double arg){
		BeyondInfoWriter.writeConfigurationDouble("Religions."+getReligion(towername)+".Towers."+towername+".Influence.Delta", arg);
	}
	public static void setTowerInfluenceDelta(Player player, double arg){
		BeyondInfoWriter.writeConfigurationDouble("Religions."+getPlayerPath(player)+".Influence.Delta", arg);
	}
	private static void setLeader(String towername, String arg){
		BeyondInfoWriter.writeConfigurationString("Religions."+getReligion(towername)+".Towers."+towername+".Leader", arg);
	}
	private static void setLeader(Player player, String arg){
		BeyondInfoWriter.writeConfigurationString("Religions."+getPlayerPath(player)+".Leader", arg);
	}
	private static void setTrustedList(String towername, String arg){
		BeyondInfoWriter.writeConfigurationString("Religions."+getReligion(towername)+".Towers."+towername+".Trusted", arg);
	}
	private static void setTrustedList(Player player, String arg){
		BeyondInfoWriter.writeConfigurationString("Religions."+getPlayerPath(player)+".Trusted", arg);
	}
	private static void setInvitedList(String towername, String arg){
		BeyondInfoWriter.writeConfigurationString("Religions."+getReligion(towername)+".Towers."+towername+".Invited", arg);
	}
	private static void setInvitedList(Player player, String arg){
		BeyondInfoWriter.writeConfigurationString("Religions."+getPlayerPath(player)+".Invited", arg);
	}
	private static void setPlayerPath(Player player, String towername){
		BeyondInfoWriter.writeConfigurationString("Players."+player.getName(), getReligion(towername)+".Towers."+towername);
	}
	//add Functions
	public static void addPlayer(Player player, String towername){
		setPlayerPath(player,towername);
		int count = getTowerMemberCount(player); 
		setTowerMemberCount(player, count+1);
	}
	public static void addTower(Player player, String religion, String towername, Location location){
		setTowerName(religion,towername); // CALL THIS FIRST - otherwise the tower won't have a religion when anything else is called
		setTowerLocation(towername, location);
		setTowerMemberCount(towername, 0);
		setLeader(towername, player.getName());
		addPlayer(player, towername);
	}
	public static void addTrusted(String playername, String towername){
		if(getTrustedListRaw(towername) == null || getTrustedListRaw(towername) == "" || getTrustedListRaw(towername) == "''") setTrustedList(towername, playername);
		else setTrustedList(towername, getTrustedListRaw(towername).concat(","+playername));
	}
	public static void addInvited(String playername, String towername){
		if(getInvitedListRaw(towername) == null || getInvitedListRaw(towername) == ""|| getInvitedListRaw(towername) == "''") setInvitedList(towername, playername);
		else setInvitedList(towername, getInvitedListRaw(towername).concat(","+playername));
	}
	//remove Functions
	/**
	 * Removes player path, effectively removing a player from tower and religion 
	 */
	public static void removePlayer(Player player){
		if(hasPlayer(player)){
			int count = getTowerMemberCount(player);
			removeTrusted(getTowerName(player), player.getName());
			setTowerMemberCount(player, count-1);
			BeyondInfoWriter.writeConfigurationNull("Players."+player.getName());
		}else{
			System.out.println("[Religion] Player, "+player.getName()+", was attmepted to be removed.  Failed for a reason or another");
		}
	}
	/**
	 * Removes player path, effectively removing a player from tower and religion 
	 */
	public static void removePlayer(String playername){
		String towername = BeyondInfoReader.getString("Religions."+BeyondInfoReader.getString("Players."+playername)+".Name");
		if(BeyondInfoReader.getKeyList("Players").contains(playername)){
			int count = getTowerMemberCount(towername);
			removeTrusted(towername, playername);
			setTowerMemberCount(towername, count-1);
			BeyondInfoWriter.writeConfigurationNull("Players."+playername);
		}else{
			System.out.println("[Religion] Player, "+playername+", was attmepted to be removed.  Failed for a reason or another");
		}
	}
	/**
	 * Removes tower. Also removes all players associated with that tower from relgion/tower
	 * @param religion
	 * @param towername
	 */
	public static void removeTower(String religion, String towername){
		List<Player> players = getTowerPlayers(towername);
		for(int i =0; i<players.size();i++){
			removePlayer(players.get(i));
		}
		BeyondInfoWriter.writeConfigurationNull("Religions."+religion+".Towers."+towername);
	}
	public static void removeTrusted(Player playerInvoking, String playerNameToBeRemoved){
		if(getTrustedListRaw(playerInvoking) == null)return;
		else if(getTrustedListRaw(playerInvoking).contains(","+playerNameToBeRemoved)) setTrustedList(playerInvoking, getTrustedListRaw(playerInvoking).replaceAll(","+playerNameToBeRemoved, ""));
		else if(getTrustedListRaw(playerInvoking).contains(playerNameToBeRemoved+",")) setTrustedList(playerInvoking, getTrustedListRaw(playerInvoking).replaceAll(playerNameToBeRemoved+",", ""));
		else if(getTrustedListRaw(playerInvoking).contains(playerNameToBeRemoved)) setTrustedList(playerInvoking, getTrustedListRaw(playerInvoking).replaceAll(playerNameToBeRemoved, ""));
	}
	public static void removeInvited(Player playerInvoking, String playerToBeAdded){
		if(getInvitedListRaw(playerInvoking) == null)return;
		else if(getInvitedListRaw(playerInvoking).contains(","+playerToBeAdded)) setInvitedList(playerInvoking, getInvitedListRaw(playerInvoking).replaceAll(","+playerToBeAdded, ""));
		else if(getInvitedListRaw(playerInvoking).contains(playerToBeAdded+",")) setInvitedList(playerInvoking, getInvitedListRaw(playerInvoking).replaceAll(playerToBeAdded+",", ""));
		else if(getInvitedListRaw(playerInvoking).contains(playerToBeAdded)) setInvitedList(playerInvoking, getInvitedListRaw(playerInvoking).replaceAll(playerToBeAdded, ""));
	}
	public static void removeTrusted(String towername, String playerNameToBeRemoved){
		if(getTrustedListRaw(towername) == null)return;
		else if(getTrustedListRaw(towername).contains(","+playerNameToBeRemoved)) setTrustedList(towername, getTrustedListRaw(towername).replaceAll(","+playerNameToBeRemoved, ""));
		else if(getTrustedListRaw(towername).contains(playerNameToBeRemoved+",")) setTrustedList(towername, getTrustedListRaw(towername).replaceAll(playerNameToBeRemoved+",", ""));
		else if(getTrustedListRaw(towername).contains(playerNameToBeRemoved)) setTrustedList(towername, getTrustedListRaw(towername).replaceAll(playerNameToBeRemoved, ""));
	}
	public static void removeInvited(String towername, String playerToBeAdded){
		if(getInvitedListRaw(towername) == null)return;
		else if(getInvitedListRaw(towername).contains(","+playerToBeAdded)) setInvitedList(towername, getInvitedListRaw(towername).replaceAll(","+playerToBeAdded, ""));
		else if(getInvitedListRaw(towername).contains(playerToBeAdded+",")) setInvitedList(towername, getInvitedListRaw(towername).replaceAll(playerToBeAdded+",", ""));
		else if(getInvitedListRaw(towername).contains(playerToBeAdded))setInvitedList(towername, getInvitedListRaw(towername).replaceAll(playerToBeAdded, ""));
	}
	//is Functions
	public static boolean isTowerArea(Location location, String towername){
		if(Math.pow((location.getBlockX()-getTowerLocation(towername).getBlockX()), 2)+ Math.pow((location.getBlockZ()-getTowerLocation(towername).getBlockZ()), 2) < Math.pow(getTowerAoE(towername), 2)) return true;
		else return false;
	}
	public static boolean isMemberTrusted(Player player){
		if(getTrustedList(player) == null) return false;
		else if(getTrustedList(player).contains(player.getName()))return true;
		else return false;
	}
	public static boolean isMemberInvited(Player player, String towername){
		if(getInvitedList(towername) == null) return false;
		else if(getInvitedList(towername).contains(player.getName()))return true;
		else return false;
	}
	public static boolean isPlayerAMember(String playername, String towername){
		if(BeyondInfoReader.getKeyList("Players").contains(playername)){
			if(BeyondInfoReader.getString("Religions."+BeyondInfoReader.getString("Players."+playername)+".Name").equals(towername)){
				return true;
			}
		}
		return false;
	}
	public static boolean isPlayerLeader(Player player){
		if(getLeader(player).equals(player.getName()))return true;
		else return false;
	}
}

